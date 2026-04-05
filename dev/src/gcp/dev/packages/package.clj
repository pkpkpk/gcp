(ns gcp.dev.packages.package
  (:require
   [clojure.java.io :as io]
   [clojure.reflect :as r]
   [clojure.set :as s]
   [clojure.string :as string]
   [gcp.dev.packages.layout :as layout]
   [gcp.dev.toolchain.parser :as parser]
   [gcp.dev.util :as u]
   [malli.core :as m])
  (:import
   (java.io File)))

(def pkg-node-schema :map)
(def class-node-schema :map)

#!----------------------------------------------------------------------------------------------------------------------

(defn named? [class-like] (or (string? class-like) (ident? class-like)))

(defn fqcn? [{:keys [package-prefixes] :as pkg} class-like]
  (and (named? class-like)
       (reduce
         (fn [_ prefix]
           (when (string/starts-with? (name class-like) prefix)
             (reduced true)))
         nil package-prefixes)))

(defn reflect-parent-methods [fqcn]
  (try
    (let [c       (Class/forName (str fqcn))
          members (:members (r/reflect c :ancestors true))]
      (->> members
           (filter #(contains? (:flags %) :public))
           (filter :return-type) ;; Only methods have return-type
           (remove #(or (:static (:flags %))
                        (contains? #{'getClass 'wait 'notify 'notifyAll 'hashCode 'toString 'equals} (:name %))))
           (map (fn [m]
                  {:name       (str (:name m))
                   :returnType (u/fish->ast-array (:return-type m))
                   :parameters (mapv (fn [t] {:type (u/fish->ast-array t)}) (:parameter-types m))
                   :inherited? true}))))
    (catch Throwable _ [])))

(defn reflect-parent-fields [fqcn]
  (try
    (let [c       (Class/forName (str fqcn))
          members (:members (r/reflect c :ancestors true))]
      (->> members
           (remove :return-type)
           (filter :type)
           (remove #(contains? (:flags %) :synthetic))
           (map (fn [m]
                  {:name       (str (:name m))
                   :type       (u/fish->ast-array (:type m))
                   :static?    (boolean (:static (:flags m)))
                   :public?    (boolean (:public (:flags m)))
                   :private?   (boolean (:private (:flags m)))
                   :final?     (boolean (:final (:flags m)))
                   :inherited? true}))))
    (catch Throwable _ [])))

(defn lookup-class-raw [pkg class-like]
  (assert (m/validate pkg-node-schema pkg))
  (if (named? class-like)
    (if (fqcn? pkg class-like)
      (or (get-in pkg [:class/by-fqcn (name class-like)])
          (let [{:keys [nested parent-fqcn]} (u/split-fqcn (name class-like))]
            (when (and nested parent-fqcn)
              (when-let [parent-node (lookup-class-raw pkg parent-fqcn)]
                (reduce (fn [node child-name]
                          (some #(when (= (:className %) child-name) %) (:nested node)))
                        parent-node
                        nested)))))
      (when-let [fqcn (get-in pkg [:class/name->fqcn (name class-like)])]
        (get-in pkg [:class/by-fqcn fqcn])))
    (if (and (map? class-like) (:fqcn class-like))
      class-like ;; already a node
      (throw (Exception. "bad class-like")))))

(defn property-name [getter]
  (cond
    (clojure.string/starts-with? getter "is")
    (let [s (subs getter 2)]
      (if (seq s) (str (clojure.string/lower-case (subs s 0 1)) (subs s 1)) ""))
    (or (clojure.string/starts-with? getter "get")
        (clojure.string/starts-with? getter "has"))
    (let [s (subs getter 3)]
      (if (seq s) (str (clojure.string/lower-case (subs s 0 1)) (subs s 1)) ""))
    :else getter))

(defn inject-synthetic-getters [node]
  (let [node (if (seq (:nested node))
               (update node :nested (partial mapv inject-synthetic-getters))
               node)]
    (if (and (contains? #{:pojo :nested/pojo} (:category node))
             (seq (:fields node)))
      (let [getters           (set (filter (fn [m] (and (not (:static? m))
                                                        (or (clojure.string/starts-with? (:name m) "get")
                                                            (clojure.string/starts-with? (:name m) "is")
                                                            (clojure.string/starts-with? (:name m) "has"))))
                                           (:methods node)))
            ;; Filter out static fields
            fields            (into {} (comp (remove :static?) (map (juxt :name identity))) (:fields node))
            covered-props     (into #{} (map #(property-name (:name %))) getters)
            missing-fields    (remove #(contains? covered-props %) (keys fields))
            synthetic-methods (map (fn [field-name]
                                     (let [field  (get fields field-name)
                                           type   (:type field)
                                           prefix (if (and (symbol? type) (= "boolean" (name type))) "is" "get")]
                                       {:name       (str prefix (string/capitalize field-name))
                                        :returnType type
                                        :parameters []
                                        :static?    false
                                        :abstract?  false
                                        :doc        (str "Synthetic getter for " field-name)
                                        :synthetic? true
                                        :field-name field-name}))
                                   missing-fields)]
        (update node :methods into synthetic-methods))
      node)))

(def ignore-inheritance-prefixes
  #{"com.google.protobuf."})

(defn lookup-class
  ([pkg class-like]
   (lookup-class pkg class-like true))
  ([pkg class-like resolve-nested?]
   (when-let [node (lookup-class-raw pkg class-like)]
     (let [node-with-synth (inject-synthetic-getters node)
           node-with-inheritance (if-let [extends (:extends node-with-synth)]
                                   (let [parent-fqcns                  (->> (map str extends)
                                                                            (remove (fn [fqcn]
                                                                                      (some #(string/starts-with? fqcn %) ignore-inheritance-prefixes))))
                                         ;; Methods
                                         parent-methods                (mapcat (fn [parent-fqcn]
                                                                                 (if (fqcn? pkg parent-fqcn)
                                                                                   (when-let [parent-node (lookup-class pkg parent-fqcn false)]
                                                                                     (:methods parent-node))
                                                                                   (reflect-parent-methods parent-fqcn)))
                                                                               parent-fqcns)
                                         inherited-methods             (filter #(and (not (:static? %)))
                                                                               parent-methods)

                                         ;; Fields
                                         parent-fields                 (mapcat (fn [parent-fqcn]
                                                                                 (if (fqcn? pkg parent-fqcn)
                                                                                   (when-let [parent-node (lookup-class pkg parent-fqcn false)]
                                                                                     (:fields parent-node))
                                                                                   (reflect-parent-fields parent-fqcn)))
                                                                               parent-fqcns)

                                         field-map                     (into {} (map (juxt :name :name)) parent-fields)
                                         inherited-methods-with-fields (map (fn [m]
                                                                              (if (:field-name m)
                                                                                m
                                                                                (let [prop (property-name (:name m))]
                                                                                  (if-let [f-name (get field-map prop)]
                                                                                    (assoc m :field-name f-name)
                                                                                    m))))
                                                                            inherited-methods)

                                         existing-method-names         (set (map :name (:methods node-with-synth)))
                                         unique-inherited-methods      (remove #(contains? existing-method-names (:name %)) inherited-methods-with-fields)

                                         inherited-fields              (remove #(or (:static? %)) parent-fields)
                                         existing-field-names          (set (map :name (:fields node-with-synth)))
                                         unique-inherited-fields       (remove #(contains? existing-field-names (:name %)) inherited-fields)]
                                     (-> node-with-synth
                                         (update :methods into unique-inherited-methods)
                                         (update :fields into unique-inherited-fields)))
                                   node-with-synth)]
       (if (and resolve-nested? (seq (:nested node-with-inheritance)))
         (update node-with-inheritance :nested (partial mapv #(lookup-class pkg %)))
         node-with-inheritance)))))
#!------------------------------------------------------------------------------
#! repo interop

(defn- resolve-package-files
  [{:keys [include exclude] :as pkg}]
  (let [root      (layout/package-source-root pkg)
        excludes  (map #(io/file root (subs % 1)) exclude)
        includes  (map #(io/file root (subs % 1)) include)
        all-files (mapcat (fn [f]
                            (let [exists? (.exists f)]
                              (if (.isDirectory f)
                                (filter #(string/ends-with? (.getName %) ".java") (file-seq f))
                                (if (and (.isFile f) (string/ends-with? (.getName f) ".java"))
                                  [f]
                                  []))))
                          includes)]
    (filter (fn [f]
              (let [abs-path (.getAbsolutePath f)]
                (not-any? (fn [ex]
                            (string/starts-with? abs-path (.getAbsolutePath ex)))
                          excludes)))
            all-files)))

(defn parse
  [pkg]
  (assert (map? pkg))
  (if (= (:type pkg) :parsed)
    pkg
    (let [files   (resolve-package-files pkg)
          options (assoc (:parser-options pkg)
                         :include-package-private? true
                         :exempt-types (:exempt-types pkg))
          pkg-ast (parser/analyze-package (layout/package-source-root pkg) files options)
          forwarded-keys [:name
                          :package-root
                          :bindings-target-root
                          :package-prefixes
                          :custom-namespace-mappings
                          :support-packages
                          :exempt-types
                          :opaque-types
                          :parser-options]]
      (merge (assoc pkg-ast :type :parsed)
             (select-keys pkg forwarded-keys)))))

(defn fqcn->target-ns
  "Determines the target Clojure namespace for a given FQCN.
   Accounts for handwritten custom overrides (which may reside in a different namespace structure)
   before falling back to the standard generated namespace layout."
  [pkg fqcn]
  (let [fqcn (str fqcn)]
    (if-let [custom-ns (get (:custom-namespace-mappings pkg) (symbol fqcn))]
      (if (string/ends-with? (str custom-ns) (str "." (:class (u/split-fqcn fqcn))))
        custom-ns
        (symbol (str (name custom-ns) "." (:class (u/split-fqcn fqcn)))))
      (u/fqcn->gcp-ns fqcn))))

(defn ^File fqcn->target-file
  "Determines the output file path for a given FQCN."
  [pkg node-or-fqcn]
  (assert (map? pkg))
  (if (map? node-or-fqcn)
    (fqcn->target-file pkg (:fqcn node-or-fqcn))
    (let [target-ns (fqcn->target-ns pkg node-or-fqcn)
          rel-path (str (string/replace (str target-ns) "." "/") ".clj")
          bindings-root (layout/package-bindings-root pkg)]
      (io/file bindings-root rel-path))))

(defn target-file->fqcn
  "Finds the FQCN that maps to the given target file.
   Requires pkg to be parsed (containing :class/by-fqcn).
   Returns nil if not found."
  [pkg file]
  (let [target-path (.getAbsolutePath (io/file file))]
    (reduce-kv
      (fn [_ fqcn _]
        (let [tf (fqcn->target-file pkg fqcn)]
          (when (= target-path (.getAbsolutePath tf))
            (reduced fqcn))))
      nil
      (:class/by-fqcn pkg))))

(defn package-ns->fqcn
  "Builds a reverse-lookup map from target Clojure namespace symbols to FQCN strings.
   Crucial for resolving the dependencies declared in the :require blocks of handwritten custom files."
  [pkg]
  (reduce-kv
    (fn [acc fqcn _]
      (assoc acc (fqcn->target-ns pkg fqcn) fqcn))
    {}
    (:class/by-fqcn pkg)))

;; -------------------------------------------------------------------------

(defn- extract-type-symbols
  [type-ast]
  (cond
    (symbol? type-ast)
    (sorted-set type-ast)
    (sequential? type-ast) (if (= :type-parameter (first type-ast))
                             (sorted-set)
                             (reduce into (sorted-set) (map extract-type-symbols type-ast)))
    :else (sorted-set)))

(defn class-user-types
  ([pkg class-like]
   (if-some [node (lookup-class pkg class-like)]
     (class-user-types node)
     (throw (ex-info "class not found" {:pkg pkg :class-like class-like}))))
  ([class-node]
   (let [members      (concat (:constructors class-node) (:methods class-node))
         nested       (:nested class-node)
         param-types  (mapcat :parameters members)
         return-types (keep :returnType members)
         all-types    (concat (map :type param-types) return-types)]
     (into (reduce into (sorted-set) (map extract-type-symbols all-types))
           (mapcat class-user-types nested)))))

(defn- native-type?
  [pkg type-sym]
  (let [t-str (str type-sym)
        pkg-name (:package-name pkg)
        _ (assert (some? pkg-name))
        package-prefixes (or (:package-prefixes pkg) #{pkg-name})]
    (some #(string/starts-with? t-str %) package-prefixes)))

(defn class-package-user-types
  [pkg class-like]
  (let [class-node (if (map? class-like)
                     class-like
                     (lookup-class pkg class-like))]
    (if class-node
      (let [all-types (class-user-types class-node)]
        (into (sorted-set)
              (filter (fn [t] (native-type? pkg t)))
              all-types))
      (throw (ex-info "class not found" {:pkg pkg :class-like class-like})))))

(def ignore-extends-categories
  #{:enum :string-enum :union-abstract :union-concrete :variant-accessor :union-tagged
    :accessor-with-builder :builder :static-factory :factory :pojo :read-only
    :client})

(defn dependency-seq
  "Returns a sequence of FQCNs representing the dependency closure of `class-like`
   in depth-first order. Restricted to package-local (native) user types.
   Handles cycles by tracking visited nodes."
  [pkg class-like]
  (let [{:keys [fqcn] :as root-node} (if (map? class-like)
                                       class-like
                                       (lookup-class pkg class-like))]
    (if root-node
      (loop [stack   (list fqcn)
             visited #{}
             result  []]
        (if (empty? stack)
          result
          (let [current (peek stack)
                stack   (pop stack)]
            (if (contains? visited current)
              (recur stack visited result)
              (let [deps        (try
                                  (class-package-user-types pkg current)
                                  (catch Exception _ #{}))
                    ;; Sort deps to ensure deterministic order
                    sorted-deps (sort (map str deps))
                    ;; Add deps to stack (reverse to process in order)
                    new-stack   (into stack (reverse sorted-deps))]
                (recur new-stack (conj visited current) (conj result current)))))))
      (throw (ex-info "class not found" {:pkg pkg :class-like class-like})))))

;; TODO some of these may need foreign bindings
;; Need better more robust way of determining user-types
(def ignored-deps '#{com.fasterxml.jackson.databind.JsonNode
                     com.google.api.Distribution
                     com.google.api.Distribution.BucketOptions
                     com.google.api.LaunchStage
                     com.google.api.Metric
                     com.google.api.MetricDescriptor
                     com.google.api.MetricDescriptor.MetricKind
                     com.google.api.MetricDescriptor.ValueType
                     com.google.api.MonitoredResourceMetadata
                     com.google.api.core.ApiClock
                     com.google.api.core.ApiFunction
                     com.google.api.core.SettableApiFuture
                     com.google.api.gax.batching.BatchingCallSettings
                     com.google.api.gax.batching.FlowController
                     com.google.api.gax.core.CredentialsProvider
                     com.google.api.gax.core.Distribution
                     com.google.api.gax.core.ExecutorProvider
                     com.google.api.gax.core.GoogleCredentialsProvider
                     com.google.api.gax.core.InstantiatingExecutorProvider
                     com.google.api.gax.grpc.GrpcCallContext
                     com.google.api.gax.grpc.GrpcInterceptorProvider
                     com.google.api.gax.grpc.InstantiatingGrpcChannelProvider
                     com.google.api.gax.httpjson.InstantiatingHttpJsonChannelProvider
                     com.google.api.gax.paging.AbstractFixedSizeCollection
                     com.google.api.gax.paging.AbstractPage
                     com.google.api.gax.paging.AbstractPagedListResponse
                     com.google.api.gax.retrying.RetryingContext
                     com.google.api.gax.retrying.ResultRetryAlgorithmWithContext
                     com.google.api.gax.retrying.TimedAttemptSettings
                     com.google.api.gax.retrying.TimedRetryAlgorithm
                     com.google.api.gax.retrying.TimedRetryAlgorithmWithContext
                     com.google.api.gax.tracing.ApiTracerFactory
                     com.google.api.gax.rpc.StatusCode
                     com.google.api.gax.rpc.ErrorDetails
                     com.google.api.pathtemplate.PathTemplate
                     com.google.auth.oauth2.GoogleCredentials
                     com.google.cloud.ServiceOptions.Builder
                     com.google.cloud.ServiceDefaults
                     com.google.cloud.grpc.BaseGrpcServiceException
                     com.google.cloud.grpc.GrpcTransportOptions
                     com.google.cloud.http.BaseHttpServiceException
                     com.google.cloud.http.HttpTransportOptions
                     com.google.common.base.Function
                     com.google.common.base.MoreObjects.ToStringHelper
                     com.google.common.base.Optional
                     com.google.common.base.Supplier
                     com.google.common.collect.ImmutableMap
                     com.google.common.collect.ImmutableMultimap
                     com.google.common.collect.ImmutableSet
                     com.google.common.hash.HashFunction
                     com.google.longrunning.OperationsClient
                     com.google.api.gax.rpc.PagedCallSettings.Builder
                     com.google.api.gax.httpjson.InstantiatingHttpJsonChannelProvider.Builder
                     com.google.api.gax.rpc.ClientSettings.Builder
                     com.google.api.gax.grpc.InstantiatingGrpcChannelProvider.Builder
                     com.google.api.gax.rpc.ApiClientHeaderProvider.Builder
                     com.google.api.gax.core.GoogleCredentialsProvider.Builder
                     com.google.api.gax.core.InstantiatingExecutorProvider.Builder
                     com.google.api.gax.rpc.UnaryCallSettings.Builder
                     com.google.api.gax.rpc.ServerStreamingCallSettings.Builder
                     com.google.api.gax.rpc.StreamingCallSettings.Builder
                     com.google.api.HttpBody.Builder
                     com.google.rpc.Status.Builder
                     #!---------------------------------------
                     com.google.type.CalendarPeriod
                     com.google.type.Expr
                     com.google.type.LatLng.Builder
                     com.google.type.TimeOfDay
                     com.google.type.Date.Builder
                     #!---------------------------------------\
                     io.opentelemetry.api.trace.Span
                     #!---------------------------------------
                     com.google.protobuf.Struct.Builder
                     com.google.protobuf.Duration.Builder
                     com.google.protobuf.Timestamp.Builder
                     com.google.protobuf.CodedInputStream
                     com.google.protobuf.CodedOutputStream
                     com.google.protobuf.Descriptors
                     com.google.protobuf.Descriptors$Descriptor
                     com.google.protobuf.Descriptors$FieldDescriptor
                     com.google.protobuf.Descriptors$OneofDescriptor
                     com.google.protobuf.Descriptors.EnumValueDescriptor
                     com.google.protobuf.Extension
                     com.google.protobuf.ExtensionLite
                     com.google.protobuf.ExtensionRegistryLite
                     com.google.protobuf.FieldMask
                     com.google.protobuf.FieldMask.Builder
                     com.google.protobuf.FieldSet
                     com.google.protobuf.GeneratedMessageV3
                     com.google.protobuf.GeneratedMessage
                     com.google.protobuf.GeneratedMessage.Builder
                     com.google.protobuf.GeneratedMessage$GeneratedExtension
                     com.google.protobuf.ListValue.Builder
                     com.google.protobuf.MapField
                     com.google.protobuf.MapFieldBuilder
                     com.google.protobuf.Message
                     com.google.protobuf.MessageLite$Builder
                     com.google.protobuf.Message$Builder
                     com.google.protobuf.Parser
                     com.google.protobuf.RepeatedFieldBuilderV3
                     com.google.protobuf.SingleFieldBuilderV3
                     com.google.protobuf.UnknownFieldSet
                     com.google.protobuf.Value.Builder
                     com.google.protobuf.RepeatedFieldBuilder
                     com.google.protobuf.SingleFieldBuilder
                     })

(def scalars
  '#{void java.lang.Void
     boolean java.lang.Boolean
     byte java.lang.Byte
     int java.lang.Integer
     long java.lang.Long
     char java.lang.Char
     float java.lang.Float
     double java.lang.Double

     java.lang.String
     java.util.UUID
     java.util.regex.Pattern
     java.time.Instant
     java.time.LocalTime
     java.time.LocalDate
     java.time.LocalDateTime
     java.time.OffsetDateTime
     java.time.Duration
     java.lang.Object
     java.math.BigInteger
     java.math.BigDecimal})

(def collections
  '#{java.util.Map
     java.util.List
     java.util.Set
     java.util.Collection
     java.lang.Iterable
     java.util.Iterator
     java.util.Optional
     java.util.ArrayList
     java.util.ArrayDeque
     java.util.AbstractList
     java.util.AbstractMap
     java.util.Map.Entry})

(def native-types
  '#{
     #!----------------------------------------
     #! time
     java.time.format.DateTimeFormatter
     #!----------------------------------------
     java.io.BufferedReader
     java.io.Closeable
     java.io.File
     java.io.IOException
     java.io.InputStream
     java.io.ObjectInputStream
     java.io.OutputStream
     java.io.PrintStream
     java.io.PrintWriter
     java.io.Serializable
     java.lang.AutoCloseable
     java.lang.Class
     java.lang.Error
     java.lang.Exception
     java.lang.StackTraceElement
     java.lang.Throwable
     java.lang.reflect.Method
     java.net.URI
     java.net.URL
     java.nio.ByteBuffer
     java.nio.channels.ReadableByteChannel
     java.nio.channels.ScatteringByteChannel
     java.nio.channels.SeekableByteChannel
     java.nio.channels.WritableByteChannel
     java.nio.charset.Charset
     java.nio.file.Path
     java.security.Key
     java.security.SecureRandom
     java.sql.ResultSet
     java.sql.SQLException
     java.text.SimpleDateFormat
     java.util.Base64.Encoder
     java.util.Comparator
     java.util.OptionalLong
     java.util.Spliterator
     java.util.concurrent.CompletableFuture
     java.util.concurrent.Executor
     java.util.concurrent.ScheduledExecutorService
     java.util.concurrent.ScheduledFuture
     java.util.concurrent.TimeUnit
     java.util.concurrent.atomic.AtomicBoolean
     java.util.concurrent.atomic.AtomicInteger
     java.util.concurrent.locks.Lock
     java.util.function.BiConsumer
     java.util.function.BiFunction
     java.util.function.Consumer
     java.util.function.Function
     java.util.function.Predicate
     java.util.function.Supplier
     java.util.logging.Handler
     java.util.logging.Level
     java.util.logging.LogRecord
     java.util.logging.Logger})

(defn- extract-all-dependencies [node]
  (let [fields          (:fields node)
        methods         (:methods node)
        ctors           (:constructors node)
        extends         (:extends node)
        implements      (:implements node)
        nested          (:nested node)
        subclasses      (map symbol (:subclasses node))
        field-deps      (into (sorted-set) (mapcat (fn [f] (when-not (:private f) (extract-type-symbols (:type f)))) fields))
        method-deps     (into (sorted-set)
                              (mapcat (fn [m]
                                        (concat (extract-type-symbols (:returnType m))
                                                (mapcat #(extract-type-symbols (:type %)) (:parameters m))))
                                      methods))
        ctor-deps       (into (sorted-set) (mapcat (fn [c] (mapcat #(extract-type-symbols (:type %)) (:parameters c))) ctors))
        extends-deps    (if (contains? ignore-extends-categories (:category node))
                          []
                          (into (sorted-set) (mapcat extract-type-symbols extends)))
        implements-deps (mapcat extract-type-symbols implements)
        nested-deps     (into (sorted-set) (mapcat extract-all-dependencies nested))
        candidates      (reduce into #{} (list field-deps method-deps ctor-deps extends-deps implements-deps nested-deps subclasses))]
    (into #{}
          (comp
            (map
              (fn [sym]
                (let [s (str sym)]
                  (if (string/ends-with? s "<>")
                    (symbol (subs s 0 (- (count s) 2)))
                    sym))))
            (remove #(u/excluded-type-name? (str %)))
            ; (remove #(string/ends-with? (str %) "Builder"))
            (remove #(string/ends-with? (str %) ".Annotations")) ; com.google.cloud.bigquery.Annotations etc in private fields for resource types
            (remove #(string/starts-with? (str %) "com.google.protobuf.Internal"))
            (remove #(string/starts-with? (str %) "io.opencensus"))
            (remove #(string/starts-with? (str %) "com.google.protobuf.GeneratedMessageV3"))
            (remove #(string/starts-with? (str %) "com.google.api.client"))
            (remove ignored-deps)
            (remove #{'?}))
          candidates)))

(defn class-deps
  "This produces all *possible* types used in a class-binding
   The *actual* type dependencies are a subset determined during analysis

   scalars == clojure types that do not require binding
   native  == jdk provided class
   foreign == class in different package
   peer    == class in same package
   nested  == a class belonging to a parent
   sibling == a fellow nested class from the same parent (relative relationship to node being parsed)
   custom  == a class that has a handwritten binding in an override namespace"
  [{:keys [package-prefixes
           exempt-types
           opaque-types
           prune-dependencies
           custom-namespace-mappings] :as pkg
    :or {exempt-types #{}
         opaque-types #{}}} class-like foreign-mappings support-pkgs recursive?]
  (assert (not-empty package-prefixes))
  (let [{:keys [fqcn] :as node} (lookup-class pkg class-like)
        _                 (assert (some? node))
        prune-pred        (into #{(symbol fqcn)} (map symbol) (keys prune-dependencies))
        deps              (into (sorted-set) (remove prune-pred) (extract-all-dependencies node))
        sibling?          (fn [dep]
                            (let [dep-str    (str dep)
                                  {:keys [parent-fqcn]} (u/split-fqcn dep-str)
                                  self-split (u/split-fqcn fqcn)]
                              (and (:parent-fqcn self-split) ;; we must be nested
                                   (= (:parent-fqcn self-split) parent-fqcn) ;; share same parent
                                   (not= fqcn dep-str))))
        nested?           (fn [dep]
                            (let [dep-str (str dep)
                                  {:keys [nested parent-fqcn]} (u/split-fqcn dep-str)]
                              (and (= fqcn parent-fqcn)
                                   (seq nested))))
        peer?             (fn [dep]
                            (let [dep-str (str dep)
                                  {:keys [package parent-fqcn]} (u/split-fqcn dep-str)]
                              (and (not= fqcn parent-fqcn)
                                   (not (sibling? dep))
                                   (some #(string/starts-with? dep-str %) package-prefixes))))
        foreign?          (fn [dep]
                            (contains? foreign-mappings dep))
        custom            (fn [dep]
                            (let [dep (if (u/nested-fqcn? dep)
                                        (symbol (:parent-fqcn (u/split-fqcn dep)))
                                        dep)]
                              (get custom-namespace-mappings dep)))
        support           (fn [dep]
                            (let [dep-str (str dep)]
                              (reduce
                                (fn [_ spkg]
                                  (when (some #(string/starts-with? dep-str %) (:package-prefixes spkg))
                                    (reduced spkg)))
                                nil support-pkgs)))
        unresolved        (into #{}
                                (filter (fn [dep]
                                          (and (not (native-types dep))
                                               (not (scalars dep))
                                               (not (collections dep))
                                               (not (exempt-types dep))
                                               (not (opaque-types dep))
                                               (not (peer? dep))
                                               (not (nested? dep))
                                               (not (sibling? dep))
                                               (not (foreign? dep))
                                               (not (custom dep))
                                               (not (support dep)))))
                                deps)
        _                 (when (not (empty? unresolved))
                            (throw (ex-info (str "unresolved deps for " (:fqcn node)) {:unresolved-deps unresolved})))
        calc-deps         (fn [node-or-name]
                            (let [node (if (map? node-or-name)
                                         node-or-name
                                         (lookup-class pkg node-or-name))]
                              (if node
                                (reduce (fn [acc dep]
                                          (assert (symbol? dep))
                                          (cond
                                            (custom dep) (update acc :custom conj dep)
                                            (support dep) (update acc :support conj dep)
                                            (foreign? dep) (update acc :foreign conj dep)
                                            (peer? dep) (if (= fqcn (str dep)) acc (update acc :peer conj dep))
                                            (nested? dep) (update acc :nested conj dep)
                                            (sibling? dep) (update acc :sibling conj dep)
                                            (scalars dep) (update acc :scalars conj dep)
                                            (collections dep) (update acc :collections conj dep)
                                            (native-types dep) (update acc :native conj dep)
                                            :else (throw (ex-info (str "unknown dep " dep) {:dep dep :fqcn fqcn}))))
                                        {:peer        (sorted-set)
                                         :custom      (sorted-set)
                                         :support     (sorted-set)
                                         :foreign     (sorted-set)
                                         :scalars     (sorted-set)
                                         :collections (sorted-set)
                                         :native      (sorted-set)
                                         :nested      (sorted-set)
                                         :sibling     (sorted-set)}
                                        deps)
                                {:peer        (sorted-set)
                                 :custom      (sorted-set)
                                 :support     (sorted-set)
                                 :foreign     (sorted-set)
                                 :scalars     (sorted-set)
                                 :collections (sorted-set)
                                 :native      (sorted-set)
                                 :nested      (sorted-set)
                                 :sibling     (sorted-set)})))
        res               (if-not recursive?
                            (calc-deps class-like)
                            (let [all-internal (dependency-seq pkg class-like)]
                              (reduce (fn [acc fqcn]
                                        (let [deps (calc-deps fqcn)]
                                          (-> acc
                                              (update :scalars into (:scalars deps))
                                              (update :collections into (:collections deps))
                                              (update :native into (:native deps))
                                              (update :peer into (:peer deps))
                                              (update :support into (:support deps))
                                              (update :foreign into (:foreign deps))
                                              (update :nested into (map symbol) (:nested deps))
                                              (update :sibling into (map symbol) (:sibling deps)))))
                                      {:peer        (sorted-set)
                                       :foreign     (sorted-set)
                                       :support     (sorted-set)
                                       :scalars     (sorted-set)
                                       :collections (sorted-set)
                                       :native      (sorted-set)
                                       :nested      (sorted-set)
                                       :sibling     (sorted-set)}
                                      all-internal)))
        foreign-mappings' (into (sorted-map) (select-keys foreign-mappings (get res :foreign)))
        custom-mappings'  (into (sorted-map)
                                (map
                                  (fn [dep]
                                    [dep (custom dep)]))
                                (get res :custom))
        support-mappings' (into (sorted-map)
                                (map
                                  (fn [dep]
                                    (let [spkg (support dep)]
                                      [dep (or (let [parent-dep (if (u/nested-fqcn? dep)
                                                                  (symbol (:parent-fqcn (u/split-fqcn dep)))
                                                                  dep)]
                                                 (get (:custom-namespace-mappings spkg) parent-dep))
                                               (if (u/nested-fqcn? dep)
                                                 (let [{:keys [parent-fqcn]} (u/split-fqcn dep)
                                                       parent (symbol parent-fqcn)]
                                                   (u/fqcn->gcp-ns parent))
                                                 (u/fqcn->gcp-ns dep)))])))
                                (get res :support))
        _                 (when-not (= (count foreign-mappings') (count (:foreign res)))
                            (throw (ex-info (str "missing expected foreign mappings for class " fqcn)
                                            (let [#_#_known (set (keys foreign-mappings))
                                                  expected (:foreign res)
                                                  found    (into (sorted-set) (keys foreign-mappings'))
                                                  missing  (clojure.set/difference expected found)]
                                              {:missing missing}))))
        peer-mappings     (reduce
                            (fn [acc peer]
                              (if (or (exempt-types (str peer)) (opaque-types (str peer)))
                                acc
                                (if-let [custom (get custom-namespace-mappings (str peer))]
                                  (assoc acc peer custom)
                                  (if (u/nested-fqcn? peer)
                                    (let [{:keys [parent-fqcn]} (u/split-fqcn peer)
                                          parent         (symbol parent-fqcn)
                                          parent-binding (u/fqcn->gcp-ns parent)]
                                      (assoc acc peer parent-binding
                                                 parent parent-binding))
                                    (assoc acc peer (u/fqcn->gcp-ns peer))))))
                            (sorted-map)
                            (:peer res))]
    (sorted-map
      :self              (symbol fqcn)
      :native            (get res :native)
      :scalars           (get res :scalars)
      :collections       (get res :collections)
      :nested            (get res :nested)
      :sibling           (get res :sibling)
      :exempt            (or exempt-types #{})
      :custom-mappings   custom-mappings'
      :support-mappings  support-mappings'
      :foreign-mappings  foreign-mappings'
      :peer-mappings     peer-mappings)))
