(ns gcp.dev.toolchain.parser.ast
  "Core AST extraction logic using JavaParser.
   Responsible for traversing Java source files and extracting structural information
   into Clojure data structures."
  (:require
   [clojure.string :as string]
   [gcp.dev.util :as u]
   [taoensso.telemere :as tel])
  (:import
   (com.github.javaparser StaticJavaParser)
   (com.github.javaparser.ast CompilationUnit ImportDeclaration Modifier$Keyword PackageDeclaration)
   (com.github.javaparser.ast.body AnnotationDeclaration BodyDeclaration ClassOrInterfaceDeclaration ConstructorDeclaration EnumConstantDeclaration EnumDeclaration FieldDeclaration MethodDeclaration Parameter TypeDeclaration VariableDeclarator)
   (com.github.javaparser.ast.comments JavadocComment)
   (com.github.javaparser.ast.expr NormalAnnotationExpr SingleMemberAnnotationExpr)
   (com.github.javaparser.ast.type ArrayType ClassOrInterfaceType PrimitiveType Type TypeParameter VoidType WildcardType)
   (com.github.javaparser.javadoc Javadoc)
   (java.io File FileInputStream)))

(defn type-parameter?
  "Checks if a given Type is a generic type parameter."
  [^Type t]
  (or (instance? TypeParameter t)
      (and (instance? ClassOrInterfaceType t)
           (.isTypeParameter (.asClassOrInterfaceType t)))))

(defn extract-javadoc
  "Extracts the Javadoc text from a node, if present.
   Handles both structured Javadoc and Javadoc comments."
  [node]
  (try
    (let [text (if (.isPresent (.getJavadoc node))
                 (.toText (.get (.getJavadoc node)))
                 (let [comment (.getComment node)]
                   (if (and (.isPresent comment)
                            (instance? JavadocComment (.get comment)))
                     (.toText (.parse (.asJavadocComment ^JavadocComment (.get comment))))
                     nil)))]
      (when text (string/trim text)))
    (catch IllegalArgumentException _ nil)))

(defn extract-modifiers
  "Extracts modifiers from a node as a vector of trimmed strings."
  [node]
  (mapv #(string/trim (.toString %)) (.getModifiers node)))

(defn safe-fqcn [^TypeDeclaration t]
  (try
    (if (.isPresent (.getFullyQualifiedName t))
      (.get (.getFullyQualifiedName t))
      nil)
    (catch Exception _ nil)))

(defn extract-local-types
  "Extracts locally defined types (self and nested) to shadow imports."
  [^TypeDeclaration type-decl]
  (let [self-name (.getNameAsString type-decl)
        self-fqcn (safe-fqcn type-decl)
        nested (->> (.getMembers type-decl)
                    (filter #(instance? TypeDeclaration %)))]
    (cond-> {}
      self-fqcn (assoc self-name self-fqcn)
      :always (into (keep (fn [t]
                            (let [n (.getNameAsString t)
                                  f (safe-fqcn t)]
                              (when f [n f])))
                          nested)))))

(defn build-type-solver
  "Creates a function that resolves simple class names to their fully qualified names
   based on the package declaration, imports, and locally defined types."
  [package imports local-overrides]
  (let [import-map (reduce (fn [acc ^String i]
                             (let [parts (string/split i #"\.")
                                   simple (last parts)]
                               (assoc acc simple i)))
                           {}
                           (map :name imports))]
    (fn [simple-name]
      (or (get local-overrides simple-name)
          (get import-map simple-name)
          (if (contains? #{"String" "Integer" "Boolean" "Long" "Double" "Float" "Short" "Byte" "Character" "Object" "Class" "Void" "Enum" "java" "javax" "com" "org" "net" "io"} simple-name)
            (str (if (contains? #{"String" "Integer" "Boolean" "Long" "Double" "Float" "Short" "Byte" "Character" "Object" "Class" "Void" "Enum"} simple-name) "java.lang." "") simple-name)
            (if package
              (str package "." simple-name)
              simple-name))))))

(defn resolve-type
  "Resolves a type string (including generics and arrays) to a symbol using the solver."
  [type-str solver]
  (cond
    (string/includes? type-str "<")
    (let [base-end (.indexOf type-str "<")
          base (subs type-str 0 base-end)
          generics (subs type-str (inc base-end) (dec (count type-str)))]
      (symbol (str (solver base) "<" generics ">")))
    (string/ends-with? type-str "[]")
    (let [base (subs type-str 0 (- (count type-str) 2))]
      [(resolve-type base solver)])
    :else
    (symbol (solver type-str))))

(defn parse-type-ast
  "Parses a JavaParser Type object into a Clojure-friendly representation (symbol or vector).
   Handles primitives, arrays, generics, wildcards, and void."
  [^Type t solver]
  (let [res (cond
              (instance? ClassOrInterfaceType t)
              (let [name (.getNameAsString t)
                    scope (.getScope t)
                    base (if (.isPresent scope)
                           (let [scope-ast (parse-type-ast (.get scope) solver)
                                 scope-sym (if (sequential? scope-ast) (first scope-ast) scope-ast)]
                             (symbol (str scope-sym "." name)))
                           (resolve-type name solver))]
                (if-let [args (.getTypeArguments t)]
                  (if (.isPresent args)
                    (let [arg-types (mapv #(parse-type-ast % solver) (.get args))]
                      (into [base] arg-types))
                    base)
                  base))

              (instance? PrimitiveType t)
              (symbol (.asString t))

              (instance? ArrayType t)
              (let [component (.getComponentType t)]
                [(parse-type-ast component solver)])

              (instance? VoidType t)
              'void

              (instance? WildcardType t)
              (let [extended (.getExtendedType t)
                    super (.getSuperType t)]
                (cond
                  (.isPresent extended) (list '? :extends (parse-type-ast (.get extended) solver))
                  (.isPresent super) (list '? :super (parse-type-ast (.get super) solver))
                  :else '?))

              :else
              (symbol (.asString t)))]
    (if (type-parameter? t)
      (with-meta res {:type-parameter? true})
      res)))

(defn visible?
  "Determines if a node should be included in the analysis based on its visibility modifiers
   and the provided options (:include-private?, :include-package-private?)."
  [node options]
  (let [include-private? (:include-private? options)
        include-package? (:include-package-private? options)
        modifiers (.getModifiers node)
        annotations (.getAnnotations node)
        has-mod? (fn [kw] (some #(and (= (.getKeyword %) kw)) modifiers))
        internal? (some (fn [a]
                          (let [n (.getNameAsString a)]
                            (= n "InternalApi")))
                        annotations)
        deprecated? (some (fn [a]
                            (= (.getNameAsString a) "Deprecated"))
                          annotations)
        is-impl-type? (and (instance? TypeDeclaration node)
                           (u/excluded-type-name? (.getNameAsString node)))
        is-private? (has-mod? Modifier$Keyword/PRIVATE)
        is-public? (has-mod? Modifier$Keyword/PUBLIC)
        is-protected? (has-mod? Modifier$Keyword/PROTECTED)
        is-package? (not (or is-private? is-public? is-protected?))]
    (cond
      is-impl-type? false
      internal? false
      deprecated? false
      is-private? include-private?
      is-public? true
      is-protected? true
      is-package? (if (and (instance? BodyDeclaration node)
                           (.isPresent (.getParentNode node))
                           (let [parent (.get (.getParentNode node))]
                             (and (instance? ClassOrInterfaceDeclaration parent)
                                  (.isInterface parent))))
                    true
                    include-package?)
      :else true)))

(defn extract-annotations
  "Extracts annotations from a node."
  [node]
  (mapv (fn [a]
          {:name      (.getNameAsString a)
           :arguments (if (instance? NormalAnnotationExpr a)
                        (mapv (fn [p]
                                {:key   (.getNameAsString p)
                                 :value (.toString (.getValue p))})
                              (.getPairs a))
                        (if (instance? SingleMemberAnnotationExpr a)
                          [{:value (.toString (.getMemberValue a))}]
                          []))})
        (.getAnnotations node)))

(defn extract-extends
  "Extracts extended types from a class or interface declaration."
  [^ClassOrInterfaceDeclaration type-decl solver]
  (mapv #(parse-type-ast % solver) (.getExtendedTypes type-decl)))

(defn extract-implements
  "Extracts implemented types from a class or interface declaration."
  [^ClassOrInterfaceDeclaration type-decl solver]
  (mapv #(parse-type-ast % solver) (.getImplementedTypes type-decl)))

(defn extract-methods
  "Extracts method details (name, modifiers, return type, parameters, doc, annotations) from a type declaration."
  [^TypeDeclaration type-decl solver options]
  (let [methods (.getMethods type-decl)]
    (->> methods
         (filter #(visible? % options))
         (mapv (fn [^MethodDeclaration m]
                 {:name (.getNameAsString m)
                  :modifiers (extract-modifiers m)
                  :returnType (parse-type-ast (.getType m) solver)
                  :parameters (mapv (fn [^Parameter p]
                                      {:name (.getNameAsString p)
                                       :type (parse-type-ast (.getType p) solver)
                                       :varArgs? (.isVarArgs p)
                                       :annotations (extract-annotations p)})
                                    (.getParameters m))
                  :doc (extract-javadoc m)
                  :annotations (extract-annotations m)
                  :static? (.isStatic m)
                  :abstract? (.isAbstract m)})))))

(defn extract-fields
  "Extracts field details (name, type, modifiers, doc, annotations) from a type declaration."
  [^TypeDeclaration type-decl solver options]
  (let [fields (.getFields type-decl)]
    (->> fields
         (filter #(visible? % options))
         (mapcat (fn [^FieldDeclaration f]
                   (let [common {:doc (extract-javadoc f)
                                 :modifiers (extract-modifiers f)
                                 :annotations (extract-annotations f)
                                 :static? (.isStatic f)
                                 :final? (.isFinal f)}]
                     (mapv (fn [^VariableDeclarator v]
                             (merge common
                                    {:name (.getNameAsString v)
                                     :type (parse-type-ast (.getType v) solver)}))
                           (.getVariables f)))))
         vec)))

(defn extract-constructors
  "Extracts constructor details (name, modifiers, parameters, doc, annotations) from a class declaration."
  [^ClassOrInterfaceDeclaration type-decl solver options]
  (let [constructors (.getConstructors type-decl)]
    (->> constructors
         (filter #(visible? % options))
         (mapv (fn [^ConstructorDeclaration c]
                 {:name (.getNameAsString c)
                  :modifiers (extract-modifiers c)
                  :parameters (mapv (fn [^Parameter p]
                                      {:name (.getNameAsString p)
                                       :type (parse-type-ast (.getType p) solver)
                                       :annotations (extract-annotations p)})
                                    (.getParameters c))
                  :doc (extract-javadoc c)
                  :annotations (extract-annotations c)})))))

(defn extract-enum-constants
  "Extracts enum constants (name, doc, arguments, annotations) from an enum declaration."
  [^EnumDeclaration type-decl]
  (mapv (fn [^EnumConstantDeclaration c]
          {:name (.getNameAsString c)
           :doc (extract-javadoc c)
           :annotations (extract-annotations c)
           :arguments (mapv #(.toString %) (.getArguments c))})
        (.getEntries type-decl)))

(defn functional-interface?
  "Detects if a type declaration represents a functional interface (single abstract method)."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (.isInterface type-decl)
       (= 1 (count (filter (fn [^MethodDeclaration m]
                             (and (.isAbstract m)
                                  (not (contains? #{"toString" "equals" "hashCode"} (.getNameAsString m)))))
                           (.getMethods type-decl))))))

(defn stub?
  "Detects if a class is a stub (name ends with Stub)."
  [^TypeDeclaration type-decl]
  (string/ends-with? (.getNameAsString type-decl) "Stub"))

(defn lifecycle-client?
  "Heuristic to determine if a class is a service client (e.g., has shutdown methods, extends Service)."
  [^TypeDeclaration type-decl]
  (let [methods (if (instance? ClassOrInterfaceDeclaration type-decl)
                  (.getMethods type-decl)
                  [])
        name (.getNameAsString type-decl)
        is-auxiliary? (or (string/ends-with? name "Options")
                          (string/ends-with? name "Exception")
                          (string/ends-with? name "Factory")
                          (string/ends-with? name "Provider")
                          (string/ends-with? name "Settings"))
        has-lifecycle-methods? (some #(let [n (.getNameAsString %)]
                                        (or (= n "close")
                                            (= n "shutdown")
                                            (= n "shutdownNow")
                                            (= n "startAsync")
                                            (= n "stopAsync")))
                                     methods)
        extends-service? (and (instance? ClassOrInterfaceDeclaration type-decl)
                              (some #(let [n (.asString %)]
                                       (or (string/starts-with? n "ApiService")
                                           (string/starts-with? n "Service")
                                           (string/includes? n "AutoCloseable")
                                           (string/includes? n "Closeable")))
                                    (concat (.getImplementedTypes type-decl)
                                            (.getExtendedTypes type-decl))))]
    (and (not is-auxiliary?)
         (or has-lifecycle-methods? extends-service?))))

(defn union-type?
  "Detects if a class is a union type (has a getType() method returning a Type enum)."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (let [methods (.getMethods type-decl)]
         (some (fn [^MethodDeclaration m]
                 (and (= (.getNameAsString m) "getType")
                      (= 0 (.size (.getParameters m)))
                      (let [ret (.getType m)]
                        (and (instance? ClassOrInterfaceType ret)
                             (or (= (.getNameAsString ret) "Type")
                                 (string/ends-with? (.getNameAsString ret) ".Type"))))))
               methods))))

(defn static-factory?
  "Detects if a class is a static factory (no public constructors, has static 'of' method returning self)."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (not (.isInterface type-decl))
       ;; No public constructors
       (empty? (filter (fn [^ConstructorDeclaration c]
                         (.isPublic c))
                       (.getConstructors type-decl)))
       ;; Has static 'of' method returning self
       (let [self-name (.getNameAsString type-decl)]
         (some (fn [^MethodDeclaration m]
                 (and (.isStatic m)
                      (= (.getNameAsString m) "of")
                      (= (.asString (.getType m)) self-name)))
               (.getMethods type-decl)))))

(defn public-constructors?
  "Checks if a class has any public constructors."
  [^TypeDeclaration type-decl]
  (if (instance? ClassOrInterfaceDeclaration type-decl)
    (not-empty (filter (fn [^ConstructorDeclaration c]
                         (.isPublic c))
                       (.getConstructors type-decl)))
    false))

(defn resource-extended?
  "Detects if a class extends a '...Info' class, indicating it is a resource with behavior."
  [^TypeDeclaration type-decl]
  (if (instance? ClassOrInterfaceDeclaration type-decl)
    (let [extended (.getExtendedTypes type-decl)]
      (some (fn [^ClassOrInterfaceType t]
              (string/ends-with? (.getNameAsString t) "Info"))
            extended))
    false))

(defn string-enum?
  "Detects if a class extends StringEnumValue."
  [^TypeDeclaration type-decl]
  (if (instance? ClassOrInterfaceDeclaration type-decl)
    (let [extended (.getExtendedTypes type-decl)]
      (some (fn [^ClassOrInterfaceType t]
              (= (.getNameAsString t) "StringEnumValue"))
            extended))
    false))

(defn union-variant?
  "Detects if a class is a variant of a union (specifically extends 'Entity')."
  [^TypeDeclaration type-decl]
  (if (instance? ClassOrInterfaceDeclaration type-decl)
    (let [extended (.getExtendedTypes type-decl)]
      (some (fn [^ClassOrInterfaceType t]
              (= (.getNameAsString t) "Entity"))
            extended))
    false))

(defn read-only?
  "Detects if a class is read-only (no public constructors, has instance methods)."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (not (.isInterface type-decl))
       (not (.isAbstract type-decl))
       (not (public-constructors? type-decl))
       (some (fn [^MethodDeclaration m]
               (not (.isStatic m)))
             (.getMethods type-decl))))

(defn pojo?
  "Detects if a class is a POJO (has public constructors, no builder)."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (not (.isInterface type-decl))
       (not (.isAbstract type-decl))
       (public-constructors? type-decl)))

(defn sentinel?
  "Detects if a class is a sentinel (no fields, no methods, no public constructors, not abstract)."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (not (.isInterface type-decl))
       (not (.isAbstract type-decl))
       (not (public-constructors? type-decl))
       (empty? (.getFields type-decl))
       (empty? (.getMethods type-decl))))

(defn factory?
  "Detects if a class is a factory/namespace (no public constructors, specific naming patterns)."
  [^TypeDeclaration type-decl]
  (let [name (.getNameAsString type-decl)]
    (and (instance? ClassOrInterfaceDeclaration type-decl)
         (not (.isInterface type-decl))
         (not (public-constructors? type-decl))
         (or (string/ends-with? name "Configs")
             (string/ends-with? name "Roles")
             (string/ends-with? name "Factory")
             (string/ends-with? name "Factories")))))

(defn statics?
  "Detects if a class is a collection of static members (no public constructors, all static members, not abstract).
   Must have at least one method or field to distinguish from sentinel."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (not (.isInterface type-decl))
       (not (.isAbstract type-decl))
       (not (public-constructors? type-decl))
       (or (seq (.getMethods type-decl))
           (seq (.getFields type-decl)))
       (every? #(.isStatic %) (.getMethods type-decl))
       (every? #(.isStatic %) (.getFields type-decl))))

(defn categorize-class
  "Categorizes a class into :enum, :string-enum, :functional-interface, :client, :interface, :abstract-union, :concrete-union, :static-factory, :exception, :error, :resource-extended-class, :accessor-with-builder, :builder, :abstract, :union-variant, :read-only, :sentinel, :factory, :statics, or :pojo."
  [^TypeDeclaration type-decl]
  (let [name (.getNameAsString type-decl)]
    (cond
      (instance? EnumDeclaration type-decl) :enum
      (string-enum? type-decl) :string-enum
      (functional-interface? type-decl) :functional-interface
      (stub? type-decl) :stub
      (lifecycle-client? type-decl) :client
      (and (instance? ClassOrInterfaceDeclaration type-decl)
           (.isInterface type-decl)) :interface
      (string/ends-with? name "Builder") :builder
      (union-type? type-decl)
      (if (.isAbstract type-decl) :abstract-union :concrete-union)
      (string/ends-with? name "Exception") :exception
      (string/ends-with? name "Error") :error

      (resource-extended? type-decl) :resource-extended-class

      ;; A class is an :accessor-with-builder if it has a nested Builder class
      (and (instance? ClassOrInterfaceDeclaration type-decl)
           (some (fn [member]
                   (and (instance? TypeDeclaration member)
                        (string/ends-with? (.getNameAsString member) "Builder")))
                 (.getMembers type-decl))) :accessor-with-builder
      (sentinel? type-decl) :sentinel
      (static-factory? type-decl) :static-factory
      (factory? type-decl) :factory
      (statics? type-decl) :statics
      (and (instance? ClassOrInterfaceDeclaration type-decl)
           (.isAbstract type-decl)) :abstract

      (union-variant? type-decl) :union-variant
      (pojo? type-decl) :pojo
      (read-only? type-decl) :read-only
      :else #! << REMOVING THIS BRANCH IS FORBIDDEN>>
      (do
        (let [fqcn (try
                     (if (.isPresent (.getFullyQualifiedName type-decl))
                       (.get (.getFullyQualifiedName type-decl))
                       name)
                     (catch Exception _ name))]
          (tel/log! :warn ["Warning: Uncategorized class found:" fqcn]))
        :other))))

(defn get-package
  "Extracts the package name from a CompilationUnit."
  [^CompilationUnit cu]
  (if-let [pkg (.getPackageDeclaration cu)]
    (if (.isPresent pkg)
      (.getNameAsString (.get pkg))
      nil)
    nil))

(defn get-imports
  "Extracts imports from a CompilationUnit."
  [^CompilationUnit cu]
  (mapv (fn [^ImportDeclaration i]
          {:name (.getNameAsString i)
           :static? (.isStatic i)
           :asterisk? (.isAsterisk i)})
        (.getImports cu)))

(defn process-type
  "Processes a single TypeDeclaration into a map containing its structure, members, and metadata."
  [^TypeDeclaration type-decl package imports options file-git-sha]
  (if (or (not (visible? type-decl options))
          (instance? AnnotationDeclaration type-decl)
          (and package (string/includes? package ".spi."))
          (u/excluded-type-name? (.getNameAsString type-decl)))
    nil
    (let [local-types (extract-local-types type-decl)
          solver (build-type-solver package imports local-types)
          name (.getNameAsString type-decl)
          kind (cond
                 (instance? ClassOrInterfaceDeclaration type-decl)
                 (if (.isInterface type-decl) :interface :class)
                 (instance? EnumDeclaration type-decl) :enum
                 :else :unknown)]
      (merge
        {:name name
         :package package
         :kind kind
         :category (categorize-class type-decl)
         :file-git-sha file-git-sha
         :doc (extract-javadoc type-decl)
         :annotations (extract-annotations type-decl)
         :modifiers (extract-modifiers type-decl)
         :methods (extract-methods type-decl solver options)
         :fields (extract-fields type-decl solver options)}
        (when (instance? ClassOrInterfaceDeclaration type-decl)
          {:extends (filterv #(not (u/excluded-type-name? (str %))) (extract-extends type-decl solver))
           :implements (filterv #(not (u/excluded-type-name? (str %))) (extract-implements type-decl solver))})
        (when (= kind :class)
          {:constructors (extract-constructors type-decl solver options)})
        (when (= kind :enum)
          {:values (extract-enum-constants type-decl)})
        {:nested (->> (.getMembers type-decl)
                      (filter #(and (instance? TypeDeclaration %) (visible? % options)))
                      (mapv #(process-type % package imports options file-git-sha))
                      (remove nil?)
                      vec)}))))

(defn parse
  "Parses a Java file at the given path and returns a vector of processed type maps.
   Returns nil if parsing fails."
  [file-path options file-git-sha]
  (try
    (let [cu (StaticJavaParser/parse (FileInputStream. file-path))
          package (get-package cu)
          imports (get-imports cu)
          types (.getTypes cu)]
      (->> types
           (map #(process-type % package imports options file-git-sha))
           (remove nil?)
           vec))
    (catch Exception e
      (tel/log! :error ["Error parsing" file-path ":" (.getMessage e)])
      nil)))
