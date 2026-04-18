(ns gcp.dev.toolchain.parser.ast
  "Core AST extraction logic using JavaParser.
   Responsible for traversing Java source files and extracting structural information
   into Clojure data structures."
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [gcp.dev.util :as u]
   [taoensso.telemere :as tel])
  (:import
   (com.github.javaparser StaticJavaParser)
   (com.github.javaparser.ast CompilationUnit ImportDeclaration Modifier$Keyword PackageDeclaration)
   (com.github.javaparser.ast.body AnnotationDeclaration BodyDeclaration ClassOrInterfaceDeclaration ConstructorDeclaration EnumConstantDeclaration EnumDeclaration FieldDeclaration MethodDeclaration Parameter TypeDeclaration VariableDeclarator)
   (com.github.javaparser.ast.comments JavadocComment)
   (com.github.javaparser.ast.expr AssignExpr FieldAccessExpr MethodCallExpr NameExpr NormalAnnotationExpr ObjectCreationExpr SingleMemberAnnotationExpr StringLiteralExpr ThisExpr)
   (com.github.javaparser.ast.stmt ExplicitConstructorInvocationStmt ExpressionStmt ReturnStmt)
   (com.github.javaparser.ast.type ArrayType ClassOrInterfaceType PrimitiveType Type TypeParameter VoidType WildcardType)
   (com.github.javaparser.javadoc Javadoc)
   (java.io File FileInputStream)))

(defn extract-type-parameters
  "Extracts type parameter names from a type declaration."
  [^TypeDeclaration type-decl]
  (if (instance? ClassOrInterfaceDeclaration type-decl)
    (mapv #(.getNameAsString %) (.getTypeParameters ^ClassOrInterfaceDeclaration type-decl))
    []))

(defn type-parameter?
  "Checks if a given Type is a generic type parameter."
  [^Type t]
  (or (instance? TypeParameter t)
      (and (instance? ClassOrInterfaceType t)
           (or (.isTypeParameter (.asClassOrInterfaceType t))
               ;; Heuristic: Single uppercase letter is likely a type parameter
               (let [n (.getNameAsString t)]
                 (and (= 1 (count n))
                      (Character/isUpperCase (first n))))))))

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

(defn scan-nested-types
  "Scans a CompilationUnit for nested types within top-level types.
   Returns a map {TopLevelSimpleName #{NestedSimpleName ...}}."
  [^CompilationUnit cu]
  (let [types (.getTypes cu)]
    (reduce (fn [acc ^TypeDeclaration t]
              (let [top-name (.getNameAsString t)
                    nested (->> (.getMembers t)
                                (filter #(instance? TypeDeclaration %))
                                (map #(.getNameAsString ^TypeDeclaration %))
                                set)]
                (if (seq nested)
                  (assoc acc top-name nested)
                  acc)))
            {}
            types)))

(defn build-type-solver
  "Creates a function that resolves simple class names to their fully qualified names
   based on the package declaration, imports, and locally defined types."
  [package imports local-overrides type-parameters package-peers nested-peers extended-peers]
  (let [class-type-params (set type-parameters)
        import-map (reduce (fn [acc ^String i]
                             (let [parts (string/split i #"\.")
                                   simple (last parts)]
                               (assoc acc simple i)))
                           {}
                           (map :name imports))
        java-lang-types #{"String" "Integer" "Boolean" "Long" "Double" "Float" "Short" "Byte" "Character" "Object" "Class" "Void" "Enum" "AutoCloseable" "Iterable" "Runnable" "Throwable" "Error" "Exception" "Cloneable" "Comparable"}
        known-prefixes #{"java" "javax" "com" "org" "net" "io"}
        known-types (into java-lang-types known-prefixes)
        implicit-util-types {"Entry" "java.util.Map.Entry"
                             "Map.Entry" "java.util.Map.Entry"}]
    (fn solver
      ([simple-name] (solver simple-name class-type-params))
      ([simple-name all-type-params]
       (or (get local-overrides simple-name)
           (get import-map simple-name)
           (get implicit-util-types simple-name)
           (if (contains? known-types simple-name)
             (str (if (contains? java-lang-types simple-name) "java.lang." "") simple-name)
             (if (and package (not (or (contains? #{"T" "E" "K" "V" "?"} simple-name)
                                     (contains? all-type-params simple-name))))
               (cond
                 (contains? package-peers simple-name)
                 (str package "." simple-name)

                 ;; Check if it is a nested type of a peer class that we extend
                 (some (fn [extended-peer]
                         (when-let [nested-set (get nested-peers extended-peer)]
                           (contains? nested-set simple-name)))
                       extended-peers)
                 (let [owner (some (fn [extended-peer]
                                     (when (contains? (get nested-peers extended-peer) simple-name)
                                       extended-peer))
                                   extended-peers)]
                   (str package "." owner "." simple-name))

                 :else (str package "." simple-name))
               simple-name)))))))

(defn resolve-type
  "Resolves a type string (including generics and arrays) to a symbol using the solver."
  ([type-str solver] (resolve-type type-str solver #{}))
  ([type-str solver all-type-params]
   (cond
     (string/includes? type-str "<")
     (let [base-end (.indexOf type-str "<")
           base (subs type-str 0 base-end)
           generics (subs type-str (inc base-end) (dec (count type-str)))]
       (symbol (str (solver base all-type-params) "<" generics ">")))
     (string/ends-with? type-str "[]")
     (let [base (subs type-str 0 (- (count type-str) 2))]
       [(resolve-type base solver all-type-params)])
     :else
     (symbol (solver type-str all-type-params)))))

(defn parse-type-ast
  "Parses a JavaParser Type object into a Clojure-friendly representation (symbol or vector).
   Handles primitives, arrays, generics, wildcards, and void."
  [^Type t solver type-params]
  (let [res (cond
              (instance? ClassOrInterfaceType t)
              (let [name (.getNameAsString t)
                    scope (.getScope t)
                    base (if (.isPresent scope)
                           (let [scope-ast (parse-type-ast (.get scope) solver type-params)
                                 scope-sym (if (sequential? scope-ast) (first scope-ast) scope-ast)]
                             (symbol (str scope-sym "." name)))
                           (resolve-type name solver type-params))
                    ret (if-let [args (.getTypeArguments t)]
                          (if (.isPresent args)
                            (let [arg-types (mapv #(parse-type-ast % solver type-params) (.get args))]
                              (into [base] arg-types))
                            base)
                          base)]
                (when (and (vector? ret) (= 1 (count ret)))
                  (throw (Exception. (str "Ambiguous type representation:" ret ", vector representations require [type element...]"))))
                ret)

              (instance? PrimitiveType t)
              (symbol (.asString t))

              (instance? ArrayType t)
              (let [component (.getComponentType t)]
                [:array (parse-type-ast component solver type-params)])

              (instance? VoidType t)
              'void

              (instance? WildcardType t)
              (let [extended (.getExtendedType t)
                    super (.getSuperType t)]
                (cond
                  (.isPresent extended) (vector '? :extends (parse-type-ast (.get extended) solver type-params))
                  (.isPresent super) (vector '? :super (parse-type-ast (.get super) solver type-params))
                  :else '?))

              :else
              (symbol (.asString t)))]
    (if (or (type-parameter? t)
            (contains? type-params (str res)))
      [:type-parameter res]
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
  [^ClassOrInterfaceDeclaration type-decl solver type-params]
  (mapv #(parse-type-ast % solver type-params) (.getExtendedTypes type-decl)))

(defn extract-implements
  "Extracts implemented types from a class or interface declaration."
  [^ClassOrInterfaceDeclaration type-decl solver type-params]
  (mapv #(parse-type-ast % solver type-params) (.getImplementedTypes type-decl)))

(def known-annotations
  #{{:name "BetaApi", :arguments []}
    {:name "CanIgnoreReturnValue", :arguments []}
    {:name "Override", :arguments []}
    {:name "java.lang.Override", :arguments []}
    {:name "Nullable", :arguments []}
    {:name "NonNull", :arguments []}
    {:name "SuppressWarnings", :arguments [{:value "\"PatternMatchingInstanceof\""}]}
    {:name "SuppressWarnings", :arguments [{:value "\"unchecked\""}]}
    {:name "SuppressWarnings", :arguments [{:value "\"NotPresentToEmptyOptional\""}]}
    {:name "ExcludeFromGeneratedCoverageReport", :arguments []}
    {:name "VisibleForTesting", :arguments []}})

(defn annotated-beta? [annotations]
  (some #(= "BetaApi" %) (map :name annotations)))

(defn annotated-deprecated? [annotations]
  (when (seq annotations)
    (assert (every? string? (mapv :name annotations)) (str " => " annotations))
    (some #(= "java.lang.Deprecated" %) (map :name annotations))))

(defn annotated-nullable? [annotations]
  (some #(= "Nullable" %) (map :name annotations)))

(defn annotated-non-null? [annotations]
  (some #(= "NonNull" %) (map :name annotations)))

(defn annotated-obsolete? [annotations]
  (some #(= "ObsoleteApi" %) (map :name annotations)))

(defn annotated-autovalue? [annotations]
  (some #(or (= "AutoValue" %)
             (= "com.google.auto.value.AutoValue" %))
        (map :name annotations)))

(defn parameter->edn
  [solver type-params ^Parameter p]
  (let [annotations (extract-annotations p)
        nullable? (annotated-nullable? annotations)
        annotations' annotations
        non-null? (annotated-non-null? annotations)
        t (parse-type-ast (.getType p) solver type-params)
        base {:name (.getNameAsString p)
              :type (if (.isVarArgs p) [:array t] t)}]
    (cond-> base
            nullable? (assoc :nullable? nullable?)
            non-null? (assoc :non-null? non-null?)
            (.isVarArgs p) (assoc :varArgs? true)
            (not-empty annotations') (assoc :annotations annotations'))))

(defn check-args-for-variant [args]
  (some (fn [arg]
          (let [name (cond
                       (instance? FieldAccessExpr arg)
                       (.getIdentifier (.getName ^FieldAccessExpr arg))

                       (instance? NameExpr arg)
                       (.getNameAsString ^NameExpr arg)

                       :else nil)
                type (when (instance? FieldAccessExpr arg)
                       (let [scope (.getScope ^FieldAccessExpr arg)]
                         (when scope
                           (.toString scope))))]
            (when (and name (re-matches #"^[A-Z0-9_]+$" name))
              {:variant name :variant-type type})))
        args))

(defn extract-factory-variant
  "Analyzes a static factory method body to determine the variant it produces.
   Looks for 'return new Class(Variant.ENUM, ...)' patterns, checking both direct instantiation
   and inner subclass constructors."
  [^MethodDeclaration m ^TypeDeclaration parent-decl]
  (when (and (.isStatic m) (.isPresent (.getBody m)))
    (let [body (.get (.getBody m))
          stmts (.getStatements body)]
      (some (fn [stmt]
              (when (instance? ReturnStmt stmt)
                (let [expr (.getExpression ^ReturnStmt stmt)]
                  (when (.isPresent expr)
                    (let [e (.get expr)]
                      (when (instance? com.github.javaparser.ast.expr.ObjectCreationExpr e)
                        (let [oce ^com.github.javaparser.ast.expr.ObjectCreationExpr e
                              args (.getArguments oce)
                              type-name (.getNameAsString (.getType oce))]
                          ;; Strategy 1: Check arguments directly
                          (or (check-args-for-variant args)
                              ;; Strategy 2: Check inner class constructor
                              (let [inner-decl (some (fn [member]
                                                       (when (and (instance? com.github.javaparser.ast.body.TypeDeclaration member)
                                                                  (= (.getNameAsString member) type-name))
                                                         member))
                                                     (.getMembers parent-decl))]
                                (when inner-decl
                                  (some (fn [^ConstructorDeclaration ctor]
                                          (let [cbody (.getBody ctor)
                                                cstmts (.getStatements cbody)]
                                            (when (and (not (.isEmpty cstmts))
                                                       (instance? ExplicitConstructorInvocationStmt (.get cstmts 0)))
                                              (let [super-call ^ExplicitConstructorInvocationStmt (.get cstmts 0)]
                                                (when (not (.isThis super-call))
                                                  (check-args-for-variant (.getArguments super-call)))))))
                                        (.getConstructors inner-decl))))))))))))
            stmts))))

(defn extract-factory-field
  "Analyzes a static factory method body to determine if it passes its single parameter
   to a constructor, and returns the name of the corresponding constructor parameter."
  [^MethodDeclaration m ^TypeDeclaration parent-decl]
  (when (and (.isStatic m) (.isPresent (.getBody m)))
    (let [params (mapv #(.getNameAsString %) (.getParameters m))
          body (.get (.getBody m))
          stmts (vec (.getStatements body))
          last-stmt (last stmts)]
      (when (and last-stmt (instance? ReturnStmt last-stmt))
        (let [expr (.getExpression ^ReturnStmt last-stmt)]
          (when (.isPresent expr)
            (let [e (.get expr)]
              (when (instance? ObjectCreationExpr e)
                (let [oce ^ObjectCreationExpr e
                      args (.getArguments oce)
                      type-name (.getNameAsString (.getType oce))
                      ;; Find if any arg matches a parameter
                      matched-idx (first (keep-indexed (fn [idx arg]
                                                         (when (and (instance? NameExpr arg)
                                                                    ((set params) (.getNameAsString ^NameExpr arg)))
                                                           idx))
                                                       args))]
                  (when matched-idx
                    ;; Try to find the matching constructor declaration
                    (let [target-decl (if (= type-name (.getNameAsString parent-decl))
                                        parent-decl
                                        (some (fn [member]
                                                (when (and (instance? TypeDeclaration member)
                                                           (= type-name (.getNameAsString member)))
                                                  member))
                                              (.getMembers parent-decl)))]
                      (when target-decl
                        (let [ctors (filter #(instance? ConstructorDeclaration %) (.getMembers target-decl))
                              ctor (first (filter #(= (.size (.getParameters %)) (.size args)) ctors))]
                          (when ctor
                            (let [ctor-param (.get (.getParameters ctor) matched-idx)]
                              (.getNameAsString ctor-param))))))))))))))))

(defn extract-returned-field
  "Extracts the name of the field returned by a simple getter method.
   Matches 'return field;' or 'return this.field;'. Falls back to looking for a single accessed class field."
  ([^MethodDeclaration m]
   (extract-returned-field m nil))
  ([^MethodDeclaration m ^TypeDeclaration parent-decl]
   (let [body (when (.isPresent (.getBody m)) (.get (.getBody m)))
         stmts (when body (.getStatements body))]
     (or
       ;; First pass: the original, strict pattern-matching heuristic.
       ;; This is fast and reliable for standard POJO getters (e.g. `return field;` or `return this.field;`).
       ;; We preserve this because many downstream classes rely on its specific constraints.
       (when (and stmts (= 1 (.size stmts)))
         (let [stmt (.get stmts 0)]
           (when (instance? ReturnStmt stmt)
             (let [expr (when (.isPresent (.getExpression ^ReturnStmt stmt))
                          (.get (.getExpression ^ReturnStmt stmt)))]
               (when expr
                 (cond
                   (instance? FieldAccessExpr expr) (.getNameAsString ^FieldAccessExpr expr)
                   (instance? NameExpr expr) (.getNameAsString ^NameExpr expr)
                   :else nil))))))
       ;; Second pass: the generalized AST fallback.
       ;; Used for complex generated getters (like Protobuf's) where the method body might contain
       ;; intermediate variable assignments or conditional logic (e.g. checking bitfields).
       ;; This dynamically verifies if there is exactly ONE class field accessed within the method body.
       (when (and parent-decl body)
         (let [fields (set (keep (fn [member]
                                   (when (instance? FieldDeclaration member)
                                     (.getNameAsString (.getVariable ^FieldDeclaration member 0))))
                                 (.getMembers parent-decl)))
               names (map #(.getNameAsString %) (.findAll m NameExpr))
               field-accesses (map #(.getNameAsString %) (.findAll m FieldAccessExpr))
               all-accessed (concat names field-accesses)
               accessed-fields (filter fields all-accessed)
               unique-accessed-fields (set accessed-fields)]
           (when (= 1 (count unique-accessed-fields))
             (first unique-accessed-fields))))))))

(defn extract-assigned-field
  "Extracts the name of the field assigned in a simple setter method.
   Matches 'this.field = arg;' or 'field = arg;' and optionally a second 'return this;' statement for fluent setters. Falls back to looking for a single accessed class field."
  ([^MethodDeclaration m]
   (extract-assigned-field m nil))
  ([^MethodDeclaration m ^TypeDeclaration parent-decl]
   (let [body (when (.isPresent (.getBody m)) (.get (.getBody m)))
         stmts (when body (.getStatements body))]
     (or
       ;; First pass: the original, strict pattern-matching heuristic.
       ;; This is fast and highly reliable for standard POJO setters (e.g. `this.field = arg;`).
       ;; We preserve this to ensure no regressions for simple, well-behaved classes.
       (when stmts
         (let [size (.size stmts)
               valid-setter? (or (= size 1)
                                 (and (= size 2)
                                      (let [stmt1 (.get stmts 1)]
                                        (and (instance? ReturnStmt stmt1)
                                             (let [expr (.getExpression ^ReturnStmt stmt1)]
                                               (and (.isPresent expr)
                                                    (instance? ThisExpr (.get expr))))))))]
           (when valid-setter?
             (let [stmt (.get stmts 0)]
               (when (instance? ExpressionStmt stmt)
                 (let [expr (.getExpression ^ExpressionStmt stmt)]
                   (when (instance? AssignExpr expr)
                     (let [target (.getTarget ^AssignExpr expr)]
                       (cond
                         (instance? FieldAccessExpr target) (.getNameAsString ^FieldAccessExpr target)
                         (instance? NameExpr target) (.getNameAsString ^NameExpr target)
                         :else nil)))))))))

       ;; Second pass: the generalized AST fallback.
       ;; Used for complex generated setters (like Protobuf's) where the method body might contain
       ;; null checks, bitfield assignments, or derive values via method calls (e.g. `field = val.getNumber()`).
       ;; This identifies fields that are either directly assigned using a parameter,
       ;; or mutated by a method call that involves a parameter.
       (when (and parent-decl body)
         (let [;; Collect all formal parameter names of the method.
               params (set (map #(.getNameAsString %) (.getParameters m)))
               ;; Collect all field names defined in the parent class to ensure we only return valid fields.
               fields (set (mapcat (fn [member]
                                     (when (instance? FieldDeclaration member)
                                       (map #(.getNameAsString %) (.getVariables ^FieldDeclaration member))))
                                   (.getMembers parent-decl)))
               assignments (.findAll m AssignExpr)
               method-calls (.findAll m MethodCallExpr)

               ;; Identify fields assigned a value that involves a method parameter.
               ;; Handles cases like: `this.language_ = value.getNumber();`
               assigned-fields
               (keep (fn [^AssignExpr a]
                       (let [target (.getTarget a)
                             value (.getValue a)
                             target-name (cond
                                           (instance? NameExpr target) (.getNameAsString ^NameExpr target)
                                           (instance? FieldAccessExpr target) (.getNameAsString ^FieldAccessExpr target)
                                           :else nil)
                             ;; Find all symbols used in the value (RHS) of the assignment.
                             involved-in-value (set (concat (map #(.getNameAsString %) (.findAll value NameExpr))
                                                            (map #(.getNameAsString %) (.findAll value FieldAccessExpr))))]
                         ;; If the target is a known field and any parameter is involved in the assignment...
                         (when (and target-name (fields target-name)
                                    (some params involved-in-value))
                           target-name)))
                     assignments)

               ;; Identify fields involved in method calls that also use a method parameter.
               ;; Handles cases like: `onChanged(field, param);` or `field.update(param);`
               mutated-fields
               (mapcat (fn [^MethodCallExpr call]
                         ;; Find all symbols involved in the entire method call (scope and arguments).
                         (let [involved-names (set (concat (map #(.getNameAsString %) (.findAll call NameExpr))
                                                           (map #(.getNameAsString %) (.findAll call FieldAccessExpr))))
                               ;; If any method parameter is used in this call...
                               has-param? (some params involved-names)]
                           (if has-param?
                             ;; ...then all class fields mentioned in the same call are potentially being mutated.
                             (filter fields involved-names)
                             [])))
                       method-calls)

               ;; Merge all candidate fields identified via assignment or mutation.
               all-involved-fields (set (concat assigned-fields mutated-fields))]
           (when (seq all-involved-fields)
             ;; Tie-breaking: If multiple fields are involved (e.g. Protobuf's `parts_` vs `partsBuilder_`),
             ;; we prefer the shortest name, which typically represents the underlying storage field.
             (first (sort-by count all-involved-fields)))))))))

(defn extract-parameter-mappings
  "Analyzes a builder factory method body to map parameters to their corresponding setter methods.
   Looks for 'return new Builder().setX(param).setY(param2);' patterns."
  [^MethodDeclaration m]
  (let [params (set (map #(.getNameAsString %) (.getParameters m)))
        body (when (.isPresent (.getBody m)) (.get (.getBody m)))
        stmts (when body (.getStatements body))]
    (when (and stmts (= 1 (.size stmts)))
      (let [stmt (.get stmts 0)]
        (when (instance? ReturnStmt stmt)
          (let [expr (when (.isPresent (.getExpression ^ReturnStmt stmt))
                       (.get (.getExpression ^ReturnStmt stmt)))]
            (when expr
              (loop [e expr mappings {}]
                (if (instance? MethodCallExpr e)
                  (let [mce ^MethodCallExpr e
                        name (.getNameAsString mce)
                        args (.getArguments mce)
                        new-mappings (if (and (string/starts-with? name "set")
                                              (= 1 (.size args)))
                                       (let [arg (.get args 0)]
                                         (if (and (instance? NameExpr arg)
                                                  (contains? params (.getNameAsString ^NameExpr arg)))
                                           (assoc mappings (.getNameAsString ^NameExpr arg) name)
                                           mappings))
                                       mappings)]
                    (if (.isPresent (.getScope mce))
                      (recur (.get (.getScope mce)) new-mappings)
                      new-mappings))
                  mappings)))))))))

(defn- extract-regex-from-expr [expr parent-decl]
  (cond
    (instance? StringLiteralExpr expr)
    (.getValue ^StringLiteralExpr expr)

    (instance? MethodCallExpr expr)
    (let [mce ^MethodCallExpr expr]
      (when (and (= "compile" (.getNameAsString mce))
                 (let [s (if (.isPresent (.getScope mce))
                           (let [scope (.get (.getScope mce))]
                             (if (instance? NameExpr scope)
                               (.getNameAsString ^NameExpr scope)
                               (.toString scope)))
                           "Pattern")]
                   (or (= "Pattern" s) (= "java.util.regex.Pattern" s))))
        (let [args (.getArguments mce)]
          (when (and (seq args))
            (extract-regex-from-expr (first args) parent-decl)))))

    (instance? NameExpr expr)
    (let [name (.getNameAsString ^NameExpr expr)]
      (some (fn [member]
              (when (and (instance? FieldDeclaration member)
                         (some #(= name (.getNameAsString %)) (.getVariables ^FieldDeclaration member)))
                (let [v (first (filter #(= name (.getNameAsString %)) (.getVariables ^FieldDeclaration member)))]
                  (when (.isPresent (.getInitializer v))
                    (extract-regex-from-expr (.get (.getInitializer v)) parent-decl)))))
            (.getMembers parent-decl)))

    (instance? FieldAccessExpr expr)
    (let [fae ^FieldAccessExpr expr]
      (let [name (.getIdentifier (.getName fae))]
        (some (fn [member]
                (when (and (instance? FieldDeclaration member)
                           (some #(= name (.getNameAsString %)) (.getVariables ^FieldDeclaration member)))
                  (let [v (first (filter #(= name (.getNameAsString %)) (.getVariables ^FieldDeclaration member)))]
                    (when (.isPresent (.getInitializer v))
                      (extract-regex-from-expr (.get (.getInitializer v)) parent-decl)))))
              (.getMembers parent-decl))))

    :else nil))

(defn- extract-parameter-regexes [^MethodDeclaration m parent-decl]
  (let [body (when (.isPresent (.getBody m)) (.get (.getBody m)))
        mce-calls (when body (.findAll body MethodCallExpr))
        params (mapv #(.getNameAsString %) (.getParameters m))]
    (reduce
      (fn [acc mce]
        (if (and (= "matcher" (.getNameAsString mce))
                 (.isPresent (.getScope mce)))
          (let [args (.getArguments mce)]
            (if (and (= 1 (count args))
                     (instance? NameExpr (first args))
                     (some #(= (.getNameAsString ^NameExpr (first args)) %) params))
              (let [param-name (.getNameAsString ^NameExpr (first args))
                    scope (.get (.getScope mce))
                    regex (extract-regex-from-expr scope parent-decl)]
                (if regex
                  (assoc acc param-name regex)
                  acc))
              acc))
          acc))
      {}
      mce-calls)))

(defn method->edn
  [solver type-params ^TypeDeclaration parent-decl ^MethodDeclaration m]
  (let [method-type-params (set (map #(.getNameAsString %) (.getTypeParameters m)))
        all-type-params (into (set type-params) method-type-params)
        annotations (extract-annotations m)
        nullable? (annotated-nullable? annotations)
        non-null? (annotated-non-null? annotations)
        beta? (annotated-beta? annotations)
        factory-variant (extract-factory-variant m parent-decl)
        parameter-mappings (extract-parameter-mappings m)
        parameter-regexes (extract-parameter-regexes m parent-decl)
        annotations' (remove (fn [{:keys [name] :as annotation}]
                               (or (known-annotations annotation)
                                   (string/starts-with?  name "Json")
                                   (= "ObsoleteApi" name)
                                   (= "BetaApi" name)
                                   (= "TransportCompatibility" name)))
                             annotations)
        doc (extract-javadoc m)
        field-name (if (.isStatic m)
                     (when (= 1 (.size (.getParameters m)))
                       (extract-factory-field m parent-decl))
                     (if (empty? (.getParameters m))
                       (extract-returned-field m parent-decl)
                       (when (= 1 (.size (.getParameters m)))
                         (extract-assigned-field m parent-decl))))
        base {:name (.getNameAsString m)
              ; :modifiers (extract-modifiers m)
              :returnType (parse-type-ast (.getType m) solver all-type-params)
              :parameters (mapv (fn [p]
                                  (let [p-edn (parameter->edn solver all-type-params p)]
                                    (if-let [re (get parameter-regexes (:name p-edn))]
                                      (assoc p-edn :regex re)
                                      p-edn)))
                                (.getParameters m))
              :static? (.isStatic m)
              :private? (not (.isPublic m))
              :abstract? (.isAbstract m)}
        throws (mapv #(parse-type-ast % solver all-type-params) (.getThrownExceptions m))]
    (when (not-empty annotations')
      (println "WARN novel annotations for "(.getNameAsString m) ":" annotations'))
    (cond-> base
            doc (assoc :doc doc)
            (:variant factory-variant) (assoc :variant (:variant factory-variant))
            (:variant-type factory-variant) (assoc :variant-type (:variant-type factory-variant))
            field-name (assoc :field-name field-name)
            parameter-mappings (assoc :parameter-mappings parameter-mappings)
            (not-empty annotations') (assoc :annotations annotations')
            (seq throws) (assoc :throws throws)
            nullable? (assoc :nullable? nullable?)
            non-null? (assoc :non-null? non-null?)
            beta? (assoc :beta? beta?))))

(def protobuf-types
  '#{com.google.protobuf.Descriptors.FieldDescriptor
     com.google.protobuf.FieldMask})

(defn extract-methods
  "Extracts method details (name, modifiers, return type, parameters, doc, annotations) from a type declaration."
  [^TypeDeclaration type-decl solver options type-params]
  (let [methods (.getMethods type-decl)]
    (->> methods
         (filter #(visible? % options))
         (remove (fn [^MethodDeclaration m]
                   (or
                     (.isProtected m)
                     (.isPrivate m)
                     (let [methodName  (.getNameAsString m)
                           annotations (extract-annotations m)]
                       (or (annotated-deprecated? annotations)
                           (annotated-obsolete? annotations)
                           (#{"toString" "equals" "hashCode" "toBuilder"
                              "toPb" "fromPb" "clone" "setField"
                              "mergeFrom" "buildPartial"
                              "getClass" "wait" "notify" "notifyAll"
                              "mergeReadMask" "getReadMask" "setReadMask" "hasReadMask"
                              "mergeUpdateMask" "getUpdateMask" "setUpdateMask" "hasUpdateMask"
                              "getDescriptorForType" "getDescriptor" "setRepeatedField" "getDefaultInstanceForType"
                              "addRepeatedField" "mergeUnknownFields" "setUnknownFields"
                              "getParserForType" "parser" "parseDelimitedFrom" "writeTo"
                              "isInitialized"} methodName)
                           (string/starts-with? methodName "clear")
                           (string/starts-with? methodName "remove")
                           (string/starts-with? methodName "merge")
                           (string/ends-with? methodName "Callable")
                           (and (string/starts-with? methodName "add")
                                (not (string/starts-with? methodName "addAll")))
                           (string/includes? methodName "$")
                           (and (not (.isStatic m))
                                (string/includes? methodName "Builder")))))))
         (mapv (partial method->edn solver type-params type-decl))
         (remove (fn [{parameters :parameters :as m}]
                   (or
                     (some #(or (protobuf-types %)
                                (= % "builderForValue")
                                (string/includes? % "Callable")
                                (string/includes? % "ReadMask"))
                           (map :name parameters))
                     (and (or (string/starts-with? (:name m) "add")
                              (string/starts-with? (:name m) "set")
                              (string/starts-with? (:name m) "get"))
                          (= "index" (get-in parameters [0 :name])))
                     (protobuf-types (:returnType m))))))))

(defn extract-fields
  "Extracts field details (name, type, modifiers, doc, annotations) from a type declaration."
  [^TypeDeclaration type-decl solver options type-params]
  (let [fields (.getFields type-decl)]
    (->> fields
         (mapcat (fn [^FieldDeclaration f]
                   (let [modifiers   (into #{} (remove #{"private" "static" "final"}) (extract-modifiers f))
                         annotations (extract-annotations f)
                         common (cond-> {:doc (extract-javadoc f)
                                         :static? (.isStatic f)
                                         :private? (not (.isPublic f))
                                         :final? (.isFinal f)}
                                        (seq annotations) (assoc :annotations  annotations)
                                        (seq modifiers) (assoc :modifiers modifiers))]
                     (into []
                           (comp
                             (map (fn [^VariableDeclarator v]
                                    (let [base (merge common
                                                      {:name (.getNameAsString v)
                                                       :type (parse-type-ast (.getType v) solver type-params)})]
                                      (if (and (.isPresent (.getInitializer v))
                                               (.isStringLiteralExpr (.get (.getInitializer v))))
                                        (assoc base :value (.getValue (.asStringLiteralExpr (.get (.getInitializer v)))))
                                        base))))
                             (remove #(contains? #{"serialVersionUID" "TO_PB_FUNCTION" "FROM_PB_FUNCTION"
                                                   "INSTANCE"}
                                                 (:name %))))
                           (.getVariables f)))))
         vec)))

(defn type-contains-hidden? [type-ast hidden-fqcns]
  (cond
    (symbol? type-ast) (contains? hidden-fqcns (str type-ast))
    (vector? type-ast) (some #(type-contains-hidden? % hidden-fqcns) type-ast)
    :else false))

(defn extract-constructors
  "Extracts constructor details (name, modifiers, parameters, doc, annotations) from a class declaration."
  [^ClassOrInterfaceDeclaration type-decl solver options type-params hidden-types]
  (let [constructors (.getConstructors type-decl)]
    (->> constructors
         (filter #(visible? % options))
         ;; Note: This only filters constructors using hidden *nested* types.
         ;; Constructors using package-private types from *peer* classes (same package)
         ;; are not currently detected because we don't have visibility into peer classes here.
         (remove (fn [^ConstructorDeclaration c]
                   (let [ctor-type-params (set (map #(.getNameAsString %) (.getTypeParameters c)))
                         all-type-params (into (set type-params) ctor-type-params)]
                     (some (fn [p]
                             (let [t (parse-type-ast (.getType p) solver all-type-params)]
                               (type-contains-hidden? t hidden-types)))
                           (.getParameters c)))))
         (mapv (fn [^ConstructorDeclaration c]
                 (let [ctor-type-params (set (map #(.getNameAsString %) (.getTypeParameters c)))
                       all-type-params (into (set type-params) ctor-type-params)
                       annotations (extract-annotations c)
                       doc         (extract-javadoc c)]
                   (cond->
                     {:name        (.getNameAsString c)
                      :modifiers   (extract-modifiers c)
                      :parameters  (mapv (fn [^Parameter p]
                                           (let [annotations (extract-annotations p)]
                                             (cond->
                                               {:name (.getNameAsString p)
                                                :type (parse-type-ast (.getType p) solver all-type-params)}
                                               (seq annotations) (assoc :annotations annotations))))
                                         (.getParameters c))}
                     doc (assoc :doc doc)
                     (seq (.getThrownExceptions c)) (assoc :throws (mapv #(parse-type-ast % solver all-type-params) (.getThrownExceptions c)))
                     (seq annotations) (assoc :annotations annotations)))))
         (remove
           (fn [{:keys [parameters] :as ctor}]
             (and (= 1 (count parameters))
                  (= "builder" (get-in parameters [0 :name]))))))))

(defn extract-enum-constants
  "Extracts enum constants (name, doc, arguments, annotations) from an enum declaration."
  [^EnumDeclaration type-decl]
  (into []
        (comp
          (remove
            (fn [^EnumConstantDeclaration c]
              (let [annotations (extract-annotations c)]
                (annotated-deprecated? annotations))))
          (map (fn [^EnumConstantDeclaration c]
                 (let [annotations (extract-annotations c)
                       #_ #_ arguments   (mapv #(.toString %) (.getArguments c))]
                   (cond-> {:name  (.getNameAsString c)
                            :doc (extract-javadoc c)}
                     (seq annotations) (assoc :annotations annotations))))))
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

(defn extract-discriminator
  "Scans for a discriminator setting.
   Strategy 1: Check 'newBuilder' for .setType(Type.X)
   Strategy 2: Check constructors for super(Type.X)
   Strategy 3: Check nested 'Builder' class constructors for super(Type.X)"
  [^TypeDeclaration type-decl]
  (let [methods (if (instance? ClassOrInterfaceDeclaration type-decl) (.getMethods type-decl) [])
        constructors (if (instance? ClassOrInterfaceDeclaration type-decl) (.getConstructors type-decl) [])
        valid-discriminator? (fn [s]
                               (and (string? s)
                                    (not (string/blank? s))
                                    ;; Heuristic: Discriminators are usually enum constants (UPPER_CASE)
                                    (re-matches #"^[A-Z0-9_]+$" s)))

        ;; Strategy 1: newBuilder().setType(...) or builder().setType(...)
        from-builder-method
        (some (fn [m]
                (let [n (.getNameAsString m)]
                  (when (and (or (= n "newBuilder") (= n "builder"))
                             (.isStatic m)
                             (empty? (.getParameters m)))
                    (let [calls (.findAll m MethodCallExpr)]
                      (some (fn [^MethodCallExpr call]
                              (when (= (.getNameAsString call) "setType")
                                (let [args (.getArguments call)]
                                  (when (= 1 (.size args))
                                    (let [arg (.get args 0)]
                                      (when (instance? FieldAccessExpr arg)
                                        (let [res (.getIdentifier (.getName ^FieldAccessExpr arg))]
                                          (when (valid-discriminator? res) res))))))))
                            calls)))))
              methods)

        check-constructor
        (fn [ctors]
          (some (fn [^ConstructorDeclaration c]
                  (let [body (.getBody c)
                        stmts (.getStatements body)]
                    (if (and (not (.isEmpty stmts))
                             (instance? ExplicitConstructorInvocationStmt (.get stmts 0)))
                      (let [super-call ^ExplicitConstructorInvocationStmt (.get stmts 0)]
                        (if (not (.isThis super-call)) ;; it is super(...)
                          (let [args (.getArguments super-call)]
                            (some (fn [arg]
                                    (let [res (cond
                                                (instance? FieldAccessExpr arg)
                                                (.getIdentifier (.getName ^FieldAccessExpr arg))

                                                (instance? NameExpr arg)
                                                (let [n (.getNameAsString ^NameExpr arg)]
                                                  (last (string/split n #"\.")))

                                                :else nil)]
                                      (when (valid-discriminator? res) res)))
                                  args))
                          nil))
                      nil)))
                ctors))

        ;; Strategy 2: Constructor super(...) call
        from-constructor (check-constructor constructors)

        ;; Strategy 3: Nested Builder constructor super(...) call
        from-nested-builder
        (when (instance? ClassOrInterfaceDeclaration type-decl)
          (let [members (.getMembers type-decl)
                builder (some (fn [m]
                                (when (and (instance? ClassOrInterfaceDeclaration m)
                                           (= (.getNameAsString m) "Builder"))
                                  m))
                              members)]
            (when builder
              (check-constructor (.getConstructors builder)))))]
    (or from-builder-method from-constructor from-nested-builder)))

(defn public-constructors?
  "Checks if a class has any public constructors."
  [^TypeDeclaration type-decl]
  (if (instance? ClassOrInterfaceDeclaration type-decl)
    (not-empty (filter (fn [^ConstructorDeclaration c]
                         (.isPublic c))
                       (.getConstructors type-decl)))
    false))

(defn has-static-factories? [type-decl]
  (let [self-name (.getNameAsString type-decl)]
    (some (fn [^MethodDeclaration m]
            (and (.isStatic m)
                 (.isPublic m)
                 (let [ret (.asString (.getType m))]
                   (or (= ret self-name)
                       (string/ends-with? ret (str "." self-name))))))
          (.getMethods type-decl))))

(defn has-nested-builder? [type-decl]
  (some (fn [member]
          (and (instance? TypeDeclaration member)
               (string/ends-with? (.getNameAsString member) "Builder")))
        (.getMembers type-decl)))

(defn union-type?
  "Detects if a class is a union type (has a getType() method)."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (let [methods (.getMethods type-decl)]
         (some (fn [^MethodDeclaration m]
                 (and (= (.getNameAsString m) "getType")
                      (= 0 (.size (.getParameters m)))
                      (let [ret (.getType m)]
                        (and (instance? ClassOrInterfaceType ret)
                             (or (= (.getNameAsString ret) "Type")
                                 (= (.getNameAsString ret) "String")
                                 (string/ends-with? (.getNameAsString ret) ".Type"))))))
               methods))))

(defn abstract-union?
  [^TypeDeclaration type-decl]
  (and (union-type? type-decl)
       (.isAbstract type-decl)
       (not (has-static-factories? type-decl))))

(defn concrete-union?
  [^TypeDeclaration type-decl]
  (and (union-type? type-decl)
       (not (.isAbstract type-decl))))

(defn collection-wrapper?
  "Detects if a class is a collection wrapper (constructed via static 'of' taking Iterable/Array/Varargs)."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (not (.isInterface type-decl))
       (not (public-constructors? type-decl))
       (some (fn [t]
               (let [n (.getNameAsString t)]
                 (or (string/includes? n "List")
                     (string/includes? n "Iterable")
                     (string/includes? n "Collection")
                     (string/includes? n "Set"))))
             (concat (.getImplementedTypes ^ClassOrInterfaceDeclaration type-decl)
                     (.getExtendedTypes ^ClassOrInterfaceDeclaration type-decl)))
       (let [methods (.getMethods type-decl)
             of-methods (filter #(and (.isStatic %)
                                      (.isPublic %)
                                      (= "of" (.getNameAsString %)))
                                methods)]
         (and (seq of-methods)
              (every? (fn [^MethodDeclaration m]
                        (let [params (.getParameters m)]
                          (and (= 1 (.size params))
                               (let [p (.get params 0)
                                     t (.getType p)]
                                 (or (.isArrayType t)
                                     (.isVarArgs p)
                                     (and (.isClassOrInterfaceType t)
                                          (let [n (.getNameAsString t)]
                                            (or (= n "Iterable")
                                                (= n "List")
                                                (string/ends-with? n ".Iterable")
                                                (string/ends-with? n ".List")))))))))
                      of-methods)))))

(defn union-tagged?
  "Detects a tagged union type:
   - Must be a union type (has getType() discriminator).
   - Not a builder owner, has static factories.
   - No public constructors.
   - Exactly two instance fields (discriminator + payload)."
  [^TypeDeclaration type-decl]
  (and (union-type? type-decl)
       (not (has-nested-builder? type-decl))
       (has-static-factories? type-decl)
       (not (public-constructors? type-decl))
       (let [fields (.getFields type-decl)
             vars (->> fields
                       (remove #(.isStatic %))
                       (mapcat #(.getVariables %)))]
         (= 2 (count vars)))))

(defn client-options?
  "Detects if a class acts as a client option factory (name ends with Option/Options, or extends Option, has static factories returning itself)."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (not (.isInterface type-decl))
       (not (.isAbstract type-decl))
       (not (public-constructors? type-decl))
       (let [name (.getNameAsString type-decl)
             types (concat (.getExtendedTypes type-decl)
                           (.getImplementedTypes type-decl))]
         (or (clojure.string/ends-with? name "Option")
             (clojure.string/ends-with? name "Options")
             (some #(clojure.string/includes? (.getNameAsString %) "Option") types)))
       (some (fn [^MethodDeclaration m]
               (and (.isStatic m)
                    (let [rt (.getType m)
                          rt-name (cond
                                    (instance? com.github.javaparser.ast.type.ArrayType rt)
                                    (.getNameAsString (.getComponentType ^com.github.javaparser.ast.type.ArrayType rt))
                                    (instance? com.github.javaparser.ast.type.ClassOrInterfaceType rt)
                                    (.getNameAsString ^com.github.javaparser.ast.type.ClassOrInterfaceType rt)
                                    :else "")]
                      (= rt-name (.getNameAsString type-decl)))))
             (.getMethods type-decl))))

(defn static-factory?
  "Detects if a class is a static factory (no public constructors, has static factories returning self)."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (not (union-type? type-decl))
       (not (.isInterface type-decl))
       (not (has-nested-builder? type-decl))
       (not (collection-wrapper? type-decl))
       (not (client-options? type-decl))
       ;; No public constructors
       (empty? (filter (fn [^ConstructorDeclaration c]
                         (.isPublic c))
                       (.getConstructors type-decl)))
       (has-static-factories? type-decl)))

(defn resource-identifier?
  "Detects if a class is a resource identifier.
   1. Handwritten: Static factory ending in 'Id' (e.g. TableId).
   2. GAPIC: Implements ResourceName (e.g. TopicName)."
  [^TypeDeclaration type-decl]
  (or (and (static-factory? type-decl)
           (string/ends-with? (.getNameAsString type-decl) "Id"))
      (and (instance? ClassOrInterfaceDeclaration type-decl)
           (not (.isInterface type-decl))
           (some (fn [t]
                   (let [n (.getNameAsString t)]
                     (or (= n "ResourceName")
                         (= n "com.google.api.resourcenames.ResourceName"))))
                 (.getImplementedTypes type-decl)))))

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

(defn union-protobuf-oneof?
  "Detects a Protobuf oneof union type (has a [Something]Case enum and get[Something]Case() method)."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (let [members (.getMembers type-decl)
             enums (filter #(instance? EnumDeclaration %) members)
             methods (.getMethods type-decl)]
         (some (fn [^EnumDeclaration e]
                 (let [enum-name (.getNameAsString e)]
                   (and (string/ends-with? enum-name "Case")
                        (some (fn [^MethodDeclaration m]
                                (let [method-name (.getNameAsString m)]
                                  (and (= method-name (str "get" enum-name))
                                       (= 0 (.size (.getParameters m))))))
                              methods))))
               enums))))

(defn protobuf-message?
  "Detects if a class is a generated Protobuf message."
  [^TypeDeclaration type-decl]
  (and (not (union-protobuf-oneof? type-decl))
       (if (instance? ClassOrInterfaceDeclaration type-decl)
         (let [extended (.getExtendedTypes type-decl)
               implements (.getImplementedTypes type-decl)]
           (or (some (fn [^ClassOrInterfaceType t]
                       (or (= (.getNameAsString t) "GeneratedMessageV3")
                           (= (.getNameAsString t) "GeneratedMessage")
                           (= (.getNameAsString t) "MessageOrBuilder")
                           (= (.getNameAsString t) "GeneratedMessage.Builder")
                           (= (.getNameAsString t) "GeneratedMessageV3.Builder")))
                     extended)
               (some (fn [^ClassOrInterfaceType t]
                       (or (= (.getNameAsString t) "MessageOrBuilder")
                           (= (.getNameAsString t) "Message.Builder")))
                     implements)))
         false)))

(defn has-accessors? [^TypeDeclaration type-decl]
  (or (some (fn [^FieldDeclaration f] (and (.isPublic f) (not (.isStatic f)))) (.getFields type-decl))
      (some (fn [^MethodDeclaration m]
              (and (.isPublic m)
                   (not (.isStatic m))
                   (empty? (.getParameters m))
                   (let [n (.getNameAsString m)]
                     (or (string/starts-with? n "get")
                         (string/starts-with? n "is")
                         (string/starts-with? n "has")))))
            (.getMethods type-decl))))

(defn exception?
  "Detects if a class is an exception (extends Throwable via reflection)."
  [^TypeDeclaration type-decl]
  (try
    (if (.isPresent (.getFullyQualifiedName type-decl))
      (let [fqcn (.get (.getFullyQualifiedName type-decl))
            c (u/as-class fqcn)]
        (.isAssignableFrom Throwable c))
      false)
    (catch Throwable _ false)))

(defn public-no-arg-constructor?
  "Checks if a class has a public no-arg constructor (either explicit or implicit)."
  [^TypeDeclaration type-decl]
  (if (instance? ClassOrInterfaceDeclaration type-decl)
    (let [ctors (.getConstructors type-decl)]
      (if (empty? ctors)
        (.isPublic type-decl)
        (some (fn [^ConstructorDeclaration c]
                (and (.isPublic c) (empty? (.getParameters c))))
              ctors)))
    false))

(defn has-mutable-setters?
  "Checks if a class has at least one mutable setter (public instance method starting with 'set' returning itself)."
  [^TypeDeclaration type-decl]
  (let [self-name (.getNameAsString type-decl)]
    (some (fn [^MethodDeclaration m]
            (and (.isPublic m)
                 (not (.isStatic m))
                 (string/starts-with? (.getNameAsString m) "set")
                 (= 1 (.size (.getParameters m)))
                 (= self-name (.asString (.getType m)))))
          (.getMethods type-decl))))

(declare has-builder?)

(defn mutable-pojo?
  "Detects if a class is a mutable POJO (has public no-arg constructor, no builder, and mutable setters)."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (not (.isInterface type-decl))
       (not (.isAbstract type-decl))
       (not (exception? type-decl))
       (nil? (extract-discriminator type-decl))
       (not (has-builder? type-decl))
       (public-no-arg-constructor? type-decl)
       (has-mutable-setters? type-decl)))

(defn pojo?
  "Detects if a class is a POJO (has public constructors, no builder).
   Relaxed to include classes without explicit accessors (e.g. exceptions),
   but explicitly excludes Exceptions to enforce :read-only semantics."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (not (.isInterface type-decl))
       (not (.isAbstract type-decl))
       (public-constructors? type-decl)
       (not (exception? type-decl))
       (nil? (extract-discriminator type-decl))
       (not (mutable-pojo? type-decl))))

(defn variant-pojo?
  "Detects if a class is a variant POJO (a POJO that also has a discriminator)."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (not (.isInterface type-decl))
       (not (.isAbstract type-decl))
       (public-constructors? type-decl)
       (not (exception? type-decl))
       (some? (extract-discriminator type-decl))))

(defn sentinel?
  "Detects if a class is a sentinel (no fields, no methods, no public constructors, not abstract)."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (not (.isInterface type-decl))
       (not (.isAbstract type-decl))
       (not (public-constructors? type-decl))
       (empty? (.getFields type-decl))
       (empty? (.getMethods type-decl))))

(defn static-variants?
  "Detects if a class is a collection of static variants (all static members, has static factories that produce variants)."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (not (.isInterface type-decl))
       (not (.isAbstract type-decl))
       (not (public-constructors? type-decl))
       (not (client-options? type-decl))
       (nil? (extract-discriminator type-decl))
       (or (seq (.getMethods type-decl))
           (seq (.getFields type-decl)))
       (every? #(.isStatic %) (.getMethods type-decl))
       (every? #(.isStatic %) (.getFields type-decl))
       (boolean (some (fn [m] (and (.isStatic m) (extract-factory-variant m type-decl))) (.getMethods type-decl)))))

(defn static-utilities?
  "Detects if a class is a collection of static utility members (no public constructors, all static members, not abstract, no variants).
   Must have at least one method or field to distinguish from sentinel."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (not (.isInterface type-decl))
       (not (.isAbstract type-decl))
       (not (public-constructors? type-decl))
       (not (client-options? type-decl))
       (nil? (extract-discriminator type-decl))
       (or (seq (.getMethods type-decl))
           (seq (.getFields type-decl)))
       (every? #(.isStatic %) (.getMethods type-decl))
       (every? #(.isStatic %) (.getFields type-decl))
       (not (some (fn [m] (and (.isStatic m) (extract-factory-variant m type-decl))) (.getMethods type-decl)))))

(defn interface? [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl) (.isInterface type-decl)))

(defn has-builder?
  "Detects if a class has a public static 'newBuilder' (or 'builder') method."
  [^TypeDeclaration type-decl]
  (let [methods (.getMethods type-decl)]
    (some (fn [^MethodDeclaration m]
            (and (.isStatic m)
                 (.isPublic m)
                 (let [n (.getNameAsString m)]
                   (or (= n "newBuilder")
                       (= n "builder")))))
          methods)))

(defn accessor?
  "Must have nested useful Builder AND (be concrete OR have a builder static factory)"
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (has-builder? type-decl)
       (nil? (extract-discriminator type-decl))
       (not (union-protobuf-oneof? type-decl))
       (not (protobuf-message? type-decl))))

(defn read-only?
  "Detects if a class is read-only (no public constructors, has instance methods)."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (not (.isInterface type-decl))
       (not (has-builder? type-decl))
       (not  (instance? EnumDeclaration type-decl))
       (not (string-enum? type-decl))
       (nil? (extract-discriminator type-decl))
       (not (public-constructors? type-decl))
       (not (client-options? type-decl))
       (not (static-factory? type-decl))
       (not (collection-wrapper? type-decl))
       (not (union-type? type-decl))
       (not (union-protobuf-oneof? type-decl))
       (not (resource-extended? type-decl))
       (not (mutable-pojo? type-decl))
       (some (fn [^MethodDeclaration m]
               (not (.isStatic m)))
             (.getMethods type-decl))))

(defn variant-read-only?
  "Detects if a class is a read-only variant (discriminated, no builder, no public constructors)."
  [^TypeDeclaration type-decl]
  (and (instance? ClassOrInterfaceDeclaration type-decl)
       (some? (extract-discriminator type-decl))
       (not (has-builder? type-decl))
       (not (has-nested-builder? type-decl))
       (not (public-constructors? type-decl))
       (not (union-protobuf-oneof? type-decl))))

(defn variant-accessor? [type-decl]
  (and (has-nested-builder? type-decl)
       (if (instance? ClassOrInterfaceDeclaration type-decl)
         (let [extended (.getExtendedTypes type-decl)]
           (some (fn [^ClassOrInterfaceType t]
                   (let [n (.getNameAsString t)]
                     (and (not (= n "ServiceOptions"))
                          (or (string/ends-with? n "Options")
                              (string/ends-with? n "Definition")
                              (string/ends-with? n "Configuration")))))
                 extended))
         false)
       (some? (extract-discriminator type-decl))
       (not (union-protobuf-oneof? type-decl))))

(def ^:dynamic *throw-on-uncategorized?* false)

(defn categorize-class
  [^TypeDeclaration type-decl]
  (let [fqcn (if (.isPresent (.getFullyQualifiedName type-decl))
               (.get (.getFullyQualifiedName type-decl))
               (.getNameAsString type-decl))]
    (when (and (not (.isNestedType type-decl))
               (not (variant-accessor? type-decl))
               (extract-discriminator type-decl))
      (tel/log! :warn ["found potential non-accessor variant " fqcn]))
    (if (.isNestedType type-decl)
      (cond
        (accessor? type-decl)                 :nested/accessor-with-builder
        (union-protobuf-oneof? type-decl)     :nested/union-protobuf-oneof
        (protobuf-message? type-decl)         :nested/protobuf-message
        (string/ends-with? fqcn "Builder")    :nested/builder
        (lifecycle-client? type-decl)         :nested/client
        (collection-wrapper? type-decl)       :nested/collection-wrapper
        (instance? EnumDeclaration type-decl) :nested/enum
        (factory? type-decl)                  :nested/factory
        (functional-interface? type-decl)      :nested/functional-interface
        (string-enum? type-decl)              :nested/string-enum
        (abstract-union? type-decl)           :nested/union-abstract
        (union-tagged? type-decl)             :nested/union-tagged
        (mutable-pojo? type-decl)             :nested/mutable-pojo
        (static-factory? type-decl)           :nested/static-factory
        (variant-read-only? type-decl)        :nested/variant-read-only
        (variant-pojo? type-decl)             :nested/variant-pojo
        (read-only? type-decl)                :nested/read-only
        (exception? type-decl)                :nested/read-only
        (pojo? type-decl)                     :nested/pojo
        (client-options? type-decl)           :nested/client-options
        (static-variants? type-decl)          :nested/static-variants
        (static-utilities? type-decl)         :nested/static-utilities
        :else  #! << REMOVING THIS BRANCH IS FORBIDDEN>>
        (let [msg (str "unknown category for nested-type: '" fqcn "'")]
          (if *throw-on-uncategorized?*
            (throw (ex-info msg {:type-decl type-decl}))
            (do
              (tel/log! :warn msg)
              :nested/other))))
      (cond
        (accessor? type-decl)                  :accessor-with-builder
        (union-protobuf-oneof? type-decl)      :union-protobuf-oneof
        (protobuf-message? type-decl)          :protobuf-message
        (lifecycle-client? type-decl)          :client
        (collection-wrapper? type-decl)        :collection-wrapper
        (mutable-pojo? type-decl)              :mutable-pojo
        (instance? EnumDeclaration type-decl)  :enum
        (factory? type-decl)                   :factory
        (functional-interface? type-decl)      :functional-interface
        (interface? type-decl)                 :interface
        (exception? type-decl)                 :read-only
        (pojo? type-decl)                      :pojo
        (read-only? type-decl)                 :read-only
        (resource-extended? type-decl)         :resource-extended
        (sentinel? type-decl)                  :sentinel
        (static-variants? type-decl)           :static-variants
        (static-utilities? type-decl)          :static-utilities
        (static-factory? type-decl)            :static-factory
        (string-enum? type-decl)               :string-enum
        (abstract-union? type-decl)            :union-abstract
        (concrete-union? type-decl)            :union-concrete
        (union-tagged? type-decl)              :union-tagged
        (variant-accessor? type-decl)          :variant-accessor
        (variant-read-only? type-decl)         :variant-read-only
        ; (stub? type-decl) :stub
        :else  #! << REMOVING THIS BRANCH IS FORBIDDEN>>
        (let [msg (str "unknown category for package-type: '" fqcn "'")]
          (if *throw-on-uncategorized?*
            (throw (ex-info msg {:type-decl type-decl}))
            (do
              (tel/log! :warn msg)
              :other)))))))

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

(defn extract-payload-field [^TypeDeclaration type-decl]
  (let [fields (.getFields type-decl)
        vars (->> fields
                  (remove #(.isStatic %))
                  (mapcat #(.getVariables %)))]
    (when (= 2 (count vars))
      (let [discriminator (some #(when (contains? #{"type" "kind" "mode" "code"} (.getNameAsString %)) %) vars)
            payload (if discriminator
                      (first (remove #(= % discriminator) vars))
                      nil)]
        (when payload
          (.getNameAsString payload))))))

(defn extract-tag-field [^TypeDeclaration type-decl]
  (let [fields (.getFields type-decl)
        vars (->> fields
                  (remove #(.isStatic %))
                  (mapcat #(.getVariables %)))]
    (when (= 2 (count vars))
      (some (fn [v]
              (let [n (.getNameAsString v)]
                (when (contains? #{"type" "kind" "mode" "code"} n)
                  n)))
            vars))))

(defn process-type
  "Processes a single TypeDeclaration into a map containing its structure, members, and metadata."
  ([type-decl package imports options file-git-sha]
   (process-type type-decl package imports options file-git-sha {} #{} #{}))
  ([^TypeDeclaration type-decl package imports options file-git-sha parent-local-types parent-type-params parent-extended-peers]
   {:post [(or (sorted? %) (nil? %))]}
   (if (or (not (visible? type-decl options))
           (instance? AnnotationDeclaration type-decl)
           (and package (string/includes? package ".spi."))
           (u/excluded-type-name? (.getNameAsString type-decl))
           (and (:exempt-types options)
                (try
                  (if (.isPresent (.getFullyQualifiedName type-decl))
                    (contains? (:exempt-types options) (.get (.getFullyQualifiedName type-decl)))
                    false)
                  (catch Exception _ false))))
     nil
     (let [fqcn                (.get (.getFullyQualifiedName type-decl))
           current-type-params (extract-type-parameters type-decl)
           current-local-types (extract-local-types type-decl)
           local-types         (merge parent-local-types current-local-types)
           all-type-params     (into (set parent-type-params) current-type-params)
           package-peers       (if-let [peers-map (:dir-peers options)]
                                 (get peers-map (:current-file-dir options))
                                 #{})
           nested-peers        (if-let [nested-map (:nested-peers options)]
                                 (get nested-map (:current-file-dir options))
                                 {})
           raw-extends         (if (instance? ClassOrInterfaceDeclaration type-decl)
                                 (set (map #(.getNameAsString %) (.getExtendedTypes type-decl)))
                                 #{})
           ;; Current class extends these peers
           current-extended-peers (into #{} (filter #(contains? package-peers %)) raw-extends)
           ;; Merged extended peers (including parent's) for resolution scope
           all-extended-peers  (into parent-extended-peers current-extended-peers)

           solver              (build-type-solver package imports local-types all-type-params package-peers nested-peers all-extended-peers)
           className           (.getNameAsString type-decl)
           kind                (cond
                                 (instance? ClassOrInterfaceDeclaration type-decl)
                                 (if (.isInterface type-decl) :interface :class)
                                 (instance? EnumDeclaration type-decl) :enum
                                 :else :unknown)
           category            (categorize-class type-decl)
           payload-field       (when (or (= category :union-tagged)
                                         (= category :nested/union-tagged))
                                 (extract-payload-field type-decl))
           tag-field           (when (or (= category :union-tagged)
                                         (= category :nested/union-tagged))
                                 (extract-tag-field type-decl))
           discriminator       (when (or (= category :variant-accessor)
                                         (= category :variant-read-only)
                                         (= category :nested/variant-read-only)
                                         (= category :nested/variant-pojo))
                                 (extract-discriminator type-decl))
           resource-id?        (resource-identifier? type-decl)           nested              (->> (.getMembers type-decl)
                                                                                                (filter #(and (instance? TypeDeclaration %) (visible? % options)))
                                                                                                (mapv #(process-type % package imports options file-git-sha local-types all-type-params all-extended-peers))
                                                                                                (remove nil?)
                                                                                                vec)

           ;; Identify nested types that were hidden (e.g. package-private) so we can filter constructors using them
           nested-decls (filter #(instance? TypeDeclaration %) (.getMembers type-decl))
           hidden-nested (into #{}
                               (comp (remove #(visible? % options))
                                     (keep (fn [t]
                                             (get local-types (.getNameAsString t)))))
                               nested-decls)

           [extends implements] (if (instance? ClassOrInterfaceDeclaration type-decl)
                                  [(filterv #(not (u/excluded-type-name? (str %))) (extract-extends type-decl solver all-type-params))
                                   (filterv #(not (u/excluded-type-name? (str %))) (extract-implements type-decl solver all-type-params))]
                                  [[] []])
           annotations (extract-annotations type-decl)
           constructors (when (= kind :class)
                          (extract-constructors type-decl solver options all-type-params hidden-nested))
           methods (extract-methods type-decl solver options all-type-params)
           fields (extract-fields type-decl solver options all-type-params)
           modifiers (extract-modifiers type-decl)
           autovalue? (annotated-autovalue? annotations)]
       (when (and (= category :variant-accessor) (nil? discriminator))
         (throw (ex-info (str "Missing discriminator for variant-accessor: " className)
                         {:class className :package package})))
       (cond->
         (sorted-map
           :className className
           :package package
           :gcp/key (u/fqcn->gcp-key fqcn)
           :gcp/ns (u/fqcn->gcp-ns fqcn)
           :fqcn fqcn
           :category category
           :file-git-sha file-git-sha
           :doc (extract-javadoc type-decl)
           :abstract? (when (instance? ClassOrInterfaceDeclaration type-decl) (.isAbstract type-decl))
           :private? (not (or (.isPublic type-decl) (.isProtected type-decl) (.isPrivate type-decl))))         autovalue? (assoc :autovalue? true)
         (seq modifiers) (assoc :modifiers modifiers)
         (seq fields) (assoc :fields fields)
         (seq current-type-params) (assoc :type-parameters current-type-params)
         (seq annotations) (assoc :annotations annotations)
         (seq methods) (assoc :methods methods)
         (seq nested) (assoc :nested nested)
         (seq extends) (assoc :extends extends)
         (seq implements) (assoc :implements implements)
         (seq constructors) (assoc :constructors constructors)
         resource-id? (assoc :resource-identifier? true)
         payload-field (assoc :payload-field payload-field)
         tag-field (assoc :tag-field tag-field)
         discriminator (assoc :discriminator discriminator)
         (= kind :enum) (assoc :values (extract-enum-constants type-decl)))))))

(defn parse
  "Parses a Java file at the given path and returns a vector of processed type maps.
   Returns nil if parsing fails."
  [file-path options file-git-sha]
  (try
    (let [cu (StaticJavaParser/parse (FileInputStream. file-path))
          package (get-package cu)
          imports (get-imports cu)
          types (.getTypes cu)
          current-dir (.getAbsolutePath (.getParentFile (io/file file-path)))
          options' (assoc options :current-file-dir current-dir)]
      (->> types
           (map #(process-type % package imports options' file-git-sha))
           (remove nil?)
           vec))
    (catch Exception e
      (tel/log! :error ["Error parsing" file-path ":" (.getMessage e)])
      nil)))
