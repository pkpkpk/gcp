(ns gcp.dev.analyzer.javaparser.ast
  "Core AST extraction logic using JavaParser.
   Responsible for traversing Java source files and extracting structural information
   into Clojure data structures."
  (:require [clojure.string :as string])
  (:import (com.github.javaparser StaticJavaParser)
           (com.github.javaparser.ast CompilationUnit ImportDeclaration PackageDeclaration)
           (com.github.javaparser.ast.body ClassOrInterfaceDeclaration EnumDeclaration FieldDeclaration MethodDeclaration ConstructorDeclaration TypeDeclaration Parameter EnumConstantDeclaration VariableDeclarator BodyDeclaration)
           (com.github.javaparser.ast.type ClassOrInterfaceType PrimitiveType ArrayType VoidType WildcardType Type TypeParameter)
           (com.github.javaparser.javadoc Javadoc)
           (java.io FileInputStream)))

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
    (if (.isPresent (.getJavadoc node))
      (.toText (.get (.getJavadoc node)))
      nil)
    (catch IllegalArgumentException _
      ;; Fallback for nodes that don't have getJavadoc (like PackageDeclaration in some versions)
      (let [comment (.getComment node)]
        (if (and (.isPresent comment)
                 (instance? com.github.javaparser.ast.comments.JavadocComment (.get comment)))
          (.toText (.parse (.asJavadocComment (.get comment))))
          nil)))))

(defn build-type-solver 
  "Creates a function that resolves simple class names to their fully qualified names
   based on the package declaration and imports."
  [package imports]
  (let [import-map (reduce (fn [acc ^String i]
                             (let [parts (string/split i #"\.")
                                   simple (last parts)]
                               (assoc acc simple i)))
                           {}
                           (map :name imports))]
    (fn [simple-name]
      (or (get import-map simple-name)
          (if (contains? #{"String" "Integer" "Boolean" "Long" "Double" "Float" "Short" "Byte" "Character" "Object" "Class" "Void" "Enum"} simple-name)
            (str "java.lang." simple-name)
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
                    scope (.getScope t)]
                (if (.isPresent scope)
                  (symbol (str (.getNameAsString (.get scope)) "." name))
                  (if-let [args (.getTypeArguments t)]
                    (if (.isPresent args)
                      (let [arg-types (mapv #(parse-type-ast % solver) (.get args))]
                        (into [(resolve-type name solver)] arg-types))
                      (resolve-type name solver))
                    (resolve-type name solver))))

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
        has-mod? (fn [kw] (some #(and (= (.getKeyword %) kw)) modifiers))
        is-private? (has-mod? com.github.javaparser.ast.Modifier$Keyword/PRIVATE)
        is-public? (has-mod? com.github.javaparser.ast.Modifier$Keyword/PUBLIC)
        is-protected? (has-mod? com.github.javaparser.ast.Modifier$Keyword/PROTECTED)
        is-package? (not (or is-private? is-public? is-protected?))]
    (cond
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
          {:name (.getNameAsString a)
           :arguments (if (instance? com.github.javaparser.ast.expr.NormalAnnotationExpr a)
                        (mapv (fn [p]
                                {:key (.getNameAsString p)
                                 :value (.toString (.getValue p))})
                              (.getPairs a))
                        (if (instance? com.github.javaparser.ast.expr.SingleMemberAnnotationExpr a)
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
                  :modifiers (mapv #(.toString %) (.getModifiers m))
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
                               :modifiers (mapv #(.toString %) (.getModifiers f))
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
                  :modifiers (mapv #(.toString %) (.getModifiers c))
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

(defn categorize-class 
  "Categorizes a class into :enum, :functional-interface, :client, :interface, :abstract, :builder, or :readonly."
  [^TypeDeclaration type-decl]
  (let [name (.getNameAsString type-decl)]
    (cond
      (instance? EnumDeclaration type-decl) :enum
      (functional-interface? type-decl) :functional-interface
      (lifecycle-client? type-decl) :client
      (and (instance? ClassOrInterfaceDeclaration type-decl)
           (.isInterface type-decl)) :interface
      (and (instance? ClassOrInterfaceDeclaration type-decl)
           (.isAbstract type-decl)) :abstract
      (or (string/ends-with? name "Builder")
          (and (instance? ClassOrInterfaceDeclaration type-decl)
               (some (fn [member]
                       (and (instance? TypeDeclaration member)
                            (string/ends-with? (.getNameAsString member) "Builder")))
                     (.getMembers type-decl)))) :builder
      :else :readonly)))

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
  (if-not (visible? type-decl options)
    nil
    (let [solver (build-type-solver package imports)
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
        :modifiers (mapv #(.toString %) (.getModifiers type-decl))
        :methods (extract-methods type-decl solver options)
        :fields (extract-fields type-decl solver options)}
       
       (when (instance? ClassOrInterfaceDeclaration type-decl)
         {:extends (extract-extends type-decl solver)
          :implements (extract-implements type-decl solver)})

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
      (println (str "Error parsing " file-path ": " (.getMessage e)))
      nil)))