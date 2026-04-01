(ns gcp.dev.toolchain.analyzer
  (:require
    [clojure.core.match :refer [match]]
    [clojure.set :as set]
    [clojure.string :as string]
    [gcp.dev.toolchain.shared :as shared :refer [categorize-type]]
    [gcp.dev.util :as u]
    [taoensso.telemere :as tel]))

(def LOADED (str (java.time.Instant/now)))

(defn get-nested [node name]
  (first (filter #(= (:className %) name) (:nested node))))

(defn get-builder [node] (get-nested node "Builder"))

(defn- public? [member]
  (not (:private? member)))

(defn- static? [member]
  (:static? member))

(defn getters [node]
  (filter (fn [m]
            (and (not (static? m))
                 (not (string/starts-with? (:name m) "has"))
                 (public? m)
                 (empty? (:parameters m))))
          (:methods node)))

(defn setters
  [{:keys [fqcn className] :as node}]
  (filter (fn [m]
            (and (not (static? m))
                 (public? m)
                 (= 1 (count (:parameters m)))
                 (let [rt (str (:returnType m))]
                   (or (= rt className)
                       (= rt fqcn)
                       (string/ends-with? rt (str "." className))))))
          (:methods node)))

#!----------------------------------------------------------------------------------------------------------------------

(declare _analyze-class-node)

(defn classify-dependencies [deps ts]
  (reduce
    (fn [{:keys [requires imports] :as acc} t]
      (let [cat (categorize-type deps t)]
        (match cat
          ;; TODO do we want to require a native binding ns?
          (:or :native [:array :native]) acc
          (:or :self
            [:map :scalar :self]
            [:list :self]) acc
          (:or :scalar :nested :sibling) acc
          [(:or :set :list :array) (:or :scalar :nested)] acc

          (:or :peer :custom :support) (update acc :requires assoc t cat)

          :peer/nested (update acc :requires assoc t :peer/nested)

          [:iterable :generic/peer] (let [[A B C] (second t)]
                                      ;; "com.google.cloud.vertexai.api.Content"
                                      ;; [:iterable :generic/peer] for type: [java.lang.Iterable [? :extends com.google.cloud.vertexai.api.Part]]
                                      (assert (= '? A))
                                      (assert (= :extends B))
                                      (update acc :requires assoc C :peer))

          [(:or :list :iterable) (:or :generic/self :generic/nested :generic/scalar)] acc

          [:iterable :nested] acc
          [:iterable :scalar] acc

          [(:or :iterable :list :array) (:or :peer :custom :support :peer/nested)] (update acc :requires assoc (second t) (second cat))

          [:map :scalar :nested] acc
          [:map :scalar :scalar] acc
          [:map :scalar (:or :peer :custom :support)] (update acc :requires assoc (nth t 2) (nth cat 2))
          [:map :scalar [:list (:or :custom :peer :support)]] (update acc :requires assoc (get-in t [2 1]) (get-in cat [2 1]))
          #!-------------------------------------------------------------
          #! enums
          :enum (update acc :imports conj (symbol (u/fqcn->java-name t)))
          [(:or :array :iterable :list) :enum] (update acc :imports conj (symbol (u/fqcn->java-name (second t))))
          [:map :scalar :enum] (update acc :imports conj (symbol (u/fqcn->java-name (nth t 2))))
          #!-------------------------------------------------------------
          #! :foreign
          :foreign (-> acc
                       (update :requires assoc t :foreign)
                       (update :imports conj t))

          [:foreign (:or :self :generic)]
          (-> acc
              (update :requires assoc (first t) :foreign)
              (update :imports conj (first t)))

          ;; ex: [com.google.api.core.ApiFuture com.google.cloud.storage.BlobInfo]
          [:foreign :peer] (-> acc
                               (update :imports conj (first t) (second t))
                               (update :requires assoc (first t) :foreign)
                               (update :requires assoc (second t) :peer))

          [(:or :iterable :list :array) :foreign]
          (-> acc
              (update :requires assoc (second t) :foreign)
              (update :imports conj (second t)))

          [(:or :iterable :list :array) :generic/foreign]
          (let [[_ [_ _ foreign]] t]
            (-> acc
                (update :requires assoc foreign :foreign)
                (update :imports conj foreign)))

          [:foreign :foreign :foreign]
          (let [[a b c] t]
            (-> acc
                (update :requires assoc a :foreign b :foreign c :foreign)
                (update :imports conj a b c)))

          [:foreign :peer :peer]
          (let [[a b c] t]
            (-> acc
                (update :requires assoc a :foreign b :peer c :peer)
                (update :imports conj a b c)))

          [:map :scalar :foreign]
          (-> acc
              (update :requires assoc (nth t 2) :foreign)
              (update :imports conj (nth t 2)))
          #!-------------------------------------------------------------

          :else (throw (ex-info (str "unmatched user type-category " cat " for type: " t)
                                {:deps          deps
                                 :type-category cat
                                 :t             t})))))
    {:requires (sorted-map) :imports #{}}
    ts))

(defn- merge-nested-deps [{:keys [requires imports]} nested-nodes]
  (reduce (fn [acc n]
            (-> acc
                (update :requires merge (:require-types n))
                (update :imports set/union (:import-types n))))
          {:requires requires :imports imports}
          nested-nodes))

(defn- basic-info
  [{:keys [category deps] :as node}]
  {:pre [(contains? shared/categories category)]}
  (let [select (select-keys node [:fqcn :className :file-git-sha :package :category :doc :resource-identifier? :deps :gcp/key :gcp/ns :discriminator :variant-mappings])
        nested (letfn [(rf [acc node]
                         (conj (reduce rf acc (:nested node))
                               (_analyze-class-node (assoc node :deps deps))))]
                 (reduce rf [] (:nested node)))
        {:keys [requires imports]} (merge-nested-deps {:requires {} :imports #{}} nested)]
    (cond-> (assoc (into (sorted-map) select)
                   :require-types requires
                   :import-types imports)
            (seq nested) (assoc :nested nested))))

(defn analyze-static-factory [{:keys [deps] :as node}]
  (let [base (basic-info node)
        static-methods (sequence (comp (remove #(:varArgs? (last (:parameters %))))
                                       (filter :static?))
                                 (:methods node))
        factory-methods (vec (sort-by #(count (:parameters %)) < static-methods))
        _ (let [param-types (mapv #(map :type (:parameters %)) factory-methods)]
            (assert (= (count param-types) (count (into #{} param-types)))
                    (str "found ambiguous static factory methods for type " (:fqcn node) "; they need to be unique by arity count and type")))
        ;; Determine required parameters (intersection of all 'of' method parameters)
        ;; WARNING: This currently only intersects based on literal parameter names.
        ;; If different factory methods use different names for parameters that map to the same
        ;; underlying field (e.g. 'of(View view)' vs 'of(Entity entity)'), the intersection will be empty.
        ;; TO FIX: Map parameter names to their underlying property-keys (via field-name metadata)
        ;; before performing the intersection to correctly identify required fields.
        required-params (if-some [param-names-seq (not-empty (map (fn [m] (set (map (comp keyword :name) (:parameters m)))) factory-methods))]
                          (apply clojure.set/intersection param-names-seq)
                          #{})
        getters-by-key (into (sorted-map)
                             (map
                               (fn [m]
                                 (let [pname (u/property-key (:name m))]
                                   [pname (-> m
                                              (dissoc :parameters :abstract? :static?)
                                              (assoc :required? (contains? required-params pname)))])))
                             (getters node))
        method-count  (count factory-methods)
        min-arity     (apply min (map (comp count :parameters) factory-methods))
        max-arity     (apply max (map (comp count :parameters) factory-methods))
        intersect?    (shared/intersecting-methods? factory-methods)
        strategy      (match [method-count min-arity max-arity intersect?]
                             [1 0 0 false] :single-method-no-param
                             [1 1 1 false] :single-method-single-param
                             [1 _ _ false] :map-keys
                             [_ 1 _ true]  :intersecting-with-sugar
                             [_ _ _ true]  :intersecting-no-sugar
                             [_ _ _ false] :cond)

        factory-method-types (into #{} (mapcat (fn [m] (map :type (:parameters m)))) factory-methods)
        getter-types (into #{} (map :returnType (vals getters-by-key)))
        {:keys [requires imports]} (classify-dependencies deps (set/union factory-method-types getter-types))
        {:keys [requires imports]} (merge-nested-deps {:requires requires :imports imports} (:nested base))]
    (cond-> (assoc base :factory-methods factory-methods
                        :strategy strategy
                        :require-types requires
                        :import-types imports)
            (seq getters-by-key) (assoc :getters-by-key getters-by-key))))

(defn analyze-union-abstract
  [{:keys [nested methods variant-mappings deps] :as node}]
  (assert (seq variant-mappings))
  (let [base                   (dissoc (basic-info node) :nested)
        methods-by-name      (into {} (map (fn [{:keys [name] :as m}] [name m])) methods)
        _                    (assert (contains? methods-by-name "getType"))
        nested-by-name       (into {} (map (fn [{:keys [className] :as c}] [className c])) nested)
        _                    (assert (contains? nested-by-name "Type"))
        discriminator-enum   (get nested-by-name "Type")
        _                    (assert (some? discriminator-enum))
        discriminator-values (into #{} (map :name)
                                       (if (= :enum (:category discriminator-enum))
                                         (get discriminator-enum :values)
                                         (filter #(and
                                                    (not (:private? %))
                                                    (= (symbol (:fqcn discriminator-enum)) (:type %)))
                                                 (get discriminator-enum :fields))))
        variant-types        (into #{} (map symbol) (vals variant-mappings))
        {:keys [requires imports]} (classify-dependencies deps variant-types)
        ;; Note: unions don't usually have nested nodes besides the Type enum which is handled above
        {:keys [requires imports]} (merge-nested-deps {:requires requires :imports imports} (:nested base))]
    (assoc base :discriminator-values discriminator-values
                :discriminator-enum discriminator-enum
                :variant-mappings variant-mappings
                :discriminator-method (get methods-by-name "getType")
                :require-types requires
                :import-types imports)))

(defn analyze-union-concrete
  [{:keys [variant-mappings methods deps] :as node}]
  (assert (= 1 (count (filter #(= "of" (:name %)) methods))))
  (let [base (basic-info node)
        methods-by-name          (into {} (map (fn [{:keys [name] :as m}] [name m])) methods)
        _                        (assert (contains? methods-by-name "getType") "missing getType discriminator method")
        discriminator-method (get methods-by-name "getType")
        _ (assert (empty? (:parameters discriminator-method)))
        _ (assert (= 'java.lang.String (:returnType discriminator-method)))
        discriminator-values (into #{}
                                   (comp
                                     (filter :static?)
                                     (map :value))
                                   (:fields node))
        peer-variant-values (into (sorted-set) (keys variant-mappings))
        self-variant-values (set/difference discriminator-values peer-variant-values)
        self-variant-mappings (loop [acc {}
                                     variants self-variant-values]
                                (if-let [t (first variants)]
                                  (let [method (reduce
                                                 (fn [_ {:keys [doc] :as method}]
                                                   (when (boolean (re-find (re-pattern (str "(?i)" t)) doc))
                                                     (reduced method)))
                                                 nil
                                                 (filter #(= (symbol (:fqcn node)) (:returnType %)) methods))]
                                    (assert (some? method))
                                    (recur (assoc acc t method) (disj variants t)))
                                  acc))
        peer-types (into #{} (map symbol) (vals variant-mappings))
        {:keys [requires imports]} (classify-dependencies deps peer-types)
        {:keys [requires imports]} (merge-nested-deps {:requires requires :imports imports} (:nested base))]
    (when-not (= (count discriminator-values) (+ (count variant-mappings) (count self-variant-mappings)))
      (throw (ex-info (str "concrete-union variants not all accounted for")
                      {:fqcn                  (:fqcn node)
                       :variant-mappings      variant-mappings
                       :self-variant-mappings self-variant-mappings
                       :discriminator-values  discriminator-values})))
    (cond->
      (assoc base :discriminator-values discriminator-values
                  :peer-variant-mappings variant-mappings
                  :self-variant-mappings self-variant-mappings
                  :discriminator-method discriminator-method
                  :require-types requires
                  :import-types imports)
      (= 'java.lang.String (get-in methods-by-name ["of" :parameters :type]))
      (assoc :sugar-factory (get methods-by-name "of")))))

(defn analyze-union-tagged
  [{:keys [methods nested tag-field payload-field deps] :as node}]
  (let [base (basic-info node)
        methods-by-name          (into {} (map (fn [{:keys [name] :as m}] [name m])) methods)
        _                        (assert (contains? methods-by-name "getType"))
        nested-by-name           (into {} (map (fn [{:keys [className] :as c}] [className c])) nested)
        _                        (assert (contains? nested-by-name "Type"))
        discriminator-enum       (get nested-by-name "Type")
        discriminator-values     (into (sorted-set) (map :name (:values discriminator-enum)))
        discriminator-method     (get methods-by-name "getType")
        _ (assert (empty? (:parameters discriminator-method)))
        _ (assert (= (symbol (:fqcn discriminator-enum)) (:returnType discriminator-method)))
        getters-by-key (into (sorted-map)
                         (map
                           (fn [m]
                             [(keyword (u/property-name (:name m))) (dissoc m :parameters :abstract? :static?)]))
                         (getters node))
        _ (assert (= #{(keyword tag-field) (keyword payload-field)} (set (keys getters-by-key))))
        factory-by-tag (into {}
                             (map
                               (fn [{:keys [variant] :as method}]
                                 [variant method]))
                             (filter :variant methods))
        getter-types (into #{} (map :returnType) (vals getters-by-key))
        factory-types (into #{} (mapcat (fn [m] (map :type (:parameters m)))) (vals factory-by-tag))
        {:keys [requires imports]} (classify-dependencies deps (set/union getter-types factory-types))
        {:keys [requires imports]} (merge-nested-deps {:requires requires :imports imports} (:nested base))]
    (assert (= (set (keys factory-by-tag)) discriminator-values))
    (assoc base
      :tag-key (keyword tag-field)
      :payload-key (keyword payload-field)
      :getters-by-key getters-by-key
      :discriminator-values discriminator-values
      :discriminator-enum discriminator-enum
      :discriminator-method discriminator-method
      :factories-by-tag factory-by-tag
      :require-types requires
      :import-types imports)))

(defn clean-getter [m] (dissoc m :parameters :abstract? :static?))
(defn clean-setter [m] (dissoc m :abstract? :static? :private? :returnType))

(defn getter-builder-setters-by-key
  [{:keys [deps] :as node}]
  (let [builder-node (get-nested node "Builder")
        fields-by-key (into (sorted-map)
                            (map
                              (fn [f]
                                [(u/property-key (:name f)) f]))
                            (:fields node))
        getters-by-key (reduce-kv
                         (fn [acc k gs]
                           (assert (some? k))
                           (cond
                             (= 1 (count gs))
                             (assoc acc k (clean-getter (first gs)))

                             ;; a common pattern is to have underlying int field have a getXValue()=>int or getX()=>enum
                             ;; ...we want the enum representation so we may use strings in edn
                             ;; TODO its probably safer to look at types rather than lexically
                             (and (= 2 (count gs)) (= 1 (count (filter #(string/ends-with? (:name %) "Value") gs))))
                             (assoc acc k (clean-getter (first (remove #(string/ends-with? (:name %) "Value") gs))))

                             true
                             (let [;; multiple getters for same field, prefer returnType that matches field
                                   ;; (typically the is where we select getListX => List<X> vs getX => X)
                                   [selected-k getter] (reduce
                                                (fn [_ getter]
                                                  (let [fk (u/property-key (or (:field-name getter) (:name getter)))
                                                        actual-type (get-in fields-by-key [fk :type])
                                                        return-type (:returnType getter)]
                                                    (if (= actual-type return-type)
                                                      (reduced [fk getter])
                                                      nil)))
                                                nil gs)]
                               (if getter
                                 (assoc acc selected-k (clean-getter getter))
                                 (do
                                   (tel/log! :warn ["WARNING getter selection failed for field " k])
                                   (assoc acc k (clean-getter (first gs))))))))
                         (sorted-map)
                         (group-by #(u/property-key (or (:field-name %) (:name %))) (getters node)))
        _ (assert (every? some? (keys getters-by-key)))
        resolve-setter-param (fn [k param-type]
                               (if (and (vector? param-type)
                                        (= (first param-type) :type-parameter))
                                 (let [actual (get-in fields-by-key [k :type])]
                                   (if-not (or (get-in node [:deps :foreign-mappings actual])
                                               (get-in node [:deps :custom-mappings actual])
                                               (get-in node [:deps :peer-mappings actual]))
                                     (do
                                       (tel/log! :warn ["unresolved generic returnType for setter for " (:className node) " with param-type " param-type])
                                       param-type)
                                     actual))
                                 param-type))
        builder-setters-by-key (reduce-kv
                                 (fn [acc k [setter :as ss]]
                                   (cond
                                     (= 1 (count ss))
                                     (let [_ (assert (= 1 (count (:parameters setter))))
                                           k (u/property-key (or (:field-name setter) (:name setter)))
                                           t (resolve-setter-param k (get-in setter [:parameters 0 :type]))]
                                       (assoc acc k (assoc-in setter [:parameters 0 :type] t)))

                                     (and (= 2 (count ss)) (= 1 (count (filter #(string/ends-with? (:name %) "Value") ss))))
                                     (assoc acc k (first (remove #(string/ends-with? (:name %) "Value") ss)))

                                     true
                                     (let [;;  in situations where there are multiple setters with same name,
                                           ;;  we choose the one where type aligns with underlying field;
                                           ;;  more likely to get bijective getter pairing
                                           ;; TODO consider synthetic [:or A B] here
                                           [k setter] (reduce
                                                        (fn [_ setter]
                                                          (let [k        (u/property-key setter)
                                                                resolved (resolve-setter-param k (get-in setter [:parameters 0 :type]))
                                                                actual   (get-in fields-by-key [k :type])]
                                                            (if (= actual resolved)
                                                              (reduced [k (assoc-in setter [:parameters 0 :type] resolved)])
                                                              nil)))
                                                        nil ss)]
                                       (when-not (some? setter)
                                         (throw (ex-info "failed to choose setter" {:fqcn (:fqcn node) :setters ss})))
                                       (assoc acc k setter))))
                                 (sorted-map)
                                 (group-by #(u/property-key (:or (:field-name %) (:name %)))
                                           (map #(dissoc % :returnType :abstract? :static?)
                                                (remove #(:varArgs? (first (:parameters %))) (setters builder-node)))))
        getters-by-key (reduce-kv
                         (fn [acc k {:keys [returnType name] :as m}]
                           (assert (some? k))
                           (if (and (vector? returnType)
                                    (= (first returnType) :type-parameter))
                             (let [setter-type (get-in builder-setters-by-key [k :parameters 0 :type])
                                   field-type (get-in fields-by-key [k :type])
                                   actual (or setter-type field-type)]
                               (if-not (or (get-in node [:deps :foreign-mappings actual])
                                           (get-in node [:deps :custom-mappings actual])
                                           (get-in node [:deps :peer-mappings actual]))
                                 (do
                                   (tel/log! :warn ["unresolved generic returnType for getter" (str (:className node) "." name "()")])
                                   (assoc acc k m))
                                 (assoc acc k (assoc m :returnType actual))))
                             (assoc acc k m)))
                         (sorted-map)
                         getters-by-key)]
    {:getters-by-key getters-by-key
     :setters-by-key builder-setters-by-key}))

(defn choose-newBuilder-method [node]
  (let [{:keys [getters-by-key]} (getter-builder-setters-by-key node)
        candidates (sequence
                     (comp
                       (remove #(:varArgs? (last (get % :parameters))))
                       (filter #(= (:name %) "newBuilder"))
                       (remove :private?)
                       (remove :protected?))
                     (:methods node))
        candidate-stats (map (fn [m]
                               (let [mappings (:parameter-mappings m)
                                     param->key (fn [p-name]
                                                  (if-let [setter (get mappings p-name)]
                                                    (keyword (u/property-name setter))
                                                    (keyword (u/property-name p-name))))
                                     params (map (comp param->key :name) (:parameters m))
                                     param-set (set params)
                                     getter-keys (set (keys getters-by-key))
                                     matches (count (set/intersection param-set getter-keys))
                                     mismatches (count (set/difference param-set getter-keys))]
                                 {:method m
                                  :matches matches
                                  :mismatches mismatches
                                  :arity (count params)}))
                             candidates)
        fully-valid (filter #(zero? (:mismatches %)) candidate-stats)
        best-candidate (if (empty? fully-valid)
                         (do
                           (tel/log! :warn (str "newBuilder params does not match getter fields for type " (:fqcn node)))
                           (:method (first (sort-by (juxt (comp - :matches) :arity) candidate-stats))))
                         (if (= 1 (count fully-valid))
                           (:method (first fully-valid))
                           (if (apply = (map :arity fully-valid))
                             ;; Disambiguation Strategy:
                             ;; If multiple newBuilders have the same arity and match all keys,
                             ;; prefer the one whose parameter types match the *getter* return types.
                             ;; This resolves cases like BigQuery Field where newBuilder accepts
                             ;; either StandardSQLTypeName or LegacySQLTypeName, but .getType()
                             ;; returns LegacySQLTypeName. We want the one that matches .getType().
                             (let [getter-return-types (into {}
                                                             (map (fn [[k m]] [k (:returnType m)]))
                                                             getters-by-key)
                                   scored-candidates
                                   (map (fn [candidate]
                                          (let [method (:method candidate)
                                                score (reduce
                                                        (fn [acc param]
                                                          (let [param-name (:name param)
                                                                ;; map param name back to property key
                                                                mappings (:parameter-mappings method)
                                                                k (if-let [setter (get mappings param-name)]
                                                                    (keyword (u/property-name setter))
                                                                    (keyword (u/property-name param-name)))
                                                                getter-type (get getter-return-types k)]
                                                            (if (= (:type param) getter-type)
                                                              (inc acc)
                                                              acc)))
                                                        0
                                                        (:parameters method))]
                                            (assoc candidate :type-matches score)))
                                        fully-valid)
                                   winner (first (sort-by (comp - :type-matches) scored-candidates))]
                               (:method winner))
                             (:method (first (sort-by :arity fully-valid))))))]
    (assert (some? best-candidate))
    best-candidate))

(defn- analyze-accessor-with-builder
  [{:keys [deps] :as node}]
  (let [base                              (basic-info node)
        builder-node                      (get-nested node "Builder")
        newBuilder                        (choose-newBuilder-method node)
        {:keys [getters-by-key setters-by-key]} (getter-builder-setters-by-key node)
        param->key                        (fn [p-name]
                                            (if-let [setter (get (:parameter-mappings newBuilder) p-name)]
                                              (keyword (u/property-name setter))
                                              (keyword (u/property-name p-name))))
        newBuilder                        (update newBuilder :parameters
                                                  (fn [params]
                                                    (mapv (fn [p]
                                                            (assoc p :key (param->key (:name p))))
                                                          params)))
        ;;; Required === must be param to builder OR must be set by builder
        ;;;   1) for autovalue they are all required unless marked nullable in getter
        ;;;   2) default: builder params are required, rest are not
        ;;;
        ;;; Optional === could be set, but not required to be set.
        ;;; Read-Only === can only be get, never set
        ;;
        [required-keys
         builder-setters-by-key
         optional-keys]                   (if (:autovalue? node)
                                            (let [newBuilder-keys (into #{} (map :key (:parameters newBuilder)))
                                                  settable-keys (set/union newBuilder-keys (set (keys setters-by-key)))
                                                  required-keys (set/intersection settable-keys (into #{} (remove #(get-in getters-by-key [% :nullable?])) (keys getters-by-key)))
                                                  builder-setters-by-key setters-by-key
                                                  optional-keys (set/intersection settable-keys (into #{} (filter #(get-in getters-by-key [% :nullable?])) (keys getters-by-key)))]
                                              [required-keys builder-setters-by-key optional-keys])
                                            (let [required-keys (mapv (comp param->key :name) (:parameters newBuilder))
                                                  builder-setters-by-key (apply dissoc setters-by-key required-keys)
                                                  optional-keys (set/intersection (set (keys getters-by-key)) (set (keys builder-setters-by-key)))]
                                              [required-keys builder-setters-by-key optional-keys]))
        setter-types                      (reduce (fn [acc {:keys [parameters]}] (into acc (map :type parameters))) #{} (vals builder-setters-by-key))
        #!--------------------------------------------------------------------------------------------------------------
        required-newBuilder-params-by-key (into {}
                                                (map (fn [{:keys [name] :as parameter}]
                                                       [(param->key name) parameter]))
                                                (:parameters newBuilder))
        read-only-keys                    (set/difference (set (keys getters-by-key))
                                                          (set required-keys)
                                                          (set optional-keys))
        new-builder-types                 (into #{} (map :type (:parameters newBuilder)))
        getter-types                      (into #{} (map :returnType (vals getters-by-key)))
        {:keys [requires imports]} (classify-dependencies deps (set/union new-builder-types getter-types setter-types))
        {:keys [requires imports]} (merge-nested-deps {:requires requires :imports imports} (:nested base))]
    (when (not= (set required-keys) (set/intersection (set required-keys) (set (keys getters-by-key))))
      (throw (ex-info "expected a getter for each required param" {:fqcn (:fqcn node)
                                                                   :required-keys required-keys
                                                                   :getters (set (keys getters-by-key))
                                                                   :missing (set/difference (set required-keys) (set (keys getters-by-key)))})))
    (when-let [intersected (not-empty (set/intersection (set required-keys) optional-keys))]
      (throw (ex-info "found intersection between required and optional keys" {:fqcn          (:fqcn node)
                                                                               :required-keys required-keys
                                                                               :optional-keys optional-keys
                                                                               :intersection  intersected})))
    (when-let [intersected (not-empty (set/intersection (set required-keys) read-only-keys))]
      (throw (ex-info "found intersection between required & read-only keys" {:fqcn          (:fqcn node)
                                                                              :required-keys required-keys
                                                                              :optional-keys optional-keys
                                                                              :read-only read-only-keys
                                                                              :intersection  intersected})))
    (when-let [intersected (not-empty (set/intersection read-only-keys optional-keys))]
      (throw (ex-info "found intersection between read-only & optional keys" {:fqcn          (:fqcn node)
                                                                              :required-keys required-keys
                                                                              :optional-keys optional-keys
                                                                              :read-only read-only-keys
                                                                              :intersection  intersected})))
    (assoc base
      :newBuilder newBuilder
      :builder builder-node
      :getters-by-key getters-by-key
      :builder-setters-by-key builder-setters-by-key
      ; :fields-by-key fields-by-key
      ; :keys/fields   field-keys
      :keys/required required-keys
      :keys/optional (into (sorted-set) optional-keys)
      :keys/read-only (into (sorted-set) read-only-keys)
      :required-newBuilder-params-by-key required-newBuilder-params-by-key
      :require-types requires
      :import-types imports
      :autovalue? (boolean (:autovalue? node)))))

(defn analyze-variant-accessor
  [{:keys [deps] :as node}]
  (let [base (basic-info node)
        newBuilder (choose-newBuilder-method node)
        param->key (fn [p-name]
                     (if-let [setter (get (:parameter-mappings newBuilder) p-name)]
                       (keyword (u/property-name setter))
                       (keyword (u/property-name p-name))))
        newBuilder (update newBuilder :parameters
                           (fn [params]
                             (mapv (fn [p]
                                     (assoc p :key (param->key (:name p))))
                                   params)))
        newBuilder-keys (map (comp param->key :name) (:parameters newBuilder))
        {:keys [getters-by-key setters-by-key]} (getter-builder-setters-by-key node)
        discriminator-type (get-in getters-by-key [:type :returnType])
        _ (assert (some? discriminator-type))
        getters-by-key (dissoc getters-by-key :type)
        setters-by-key (apply dissoc (dissoc setters-by-key :type) newBuilder-keys)
        new-builder-types (into #{} (map :type (:parameters newBuilder)))
        getter-types (into #{} (map :returnType (vals getters-by-key)))
        setter-types (reduce (fn [acc {:keys [parameters]}] (into acc (map :type parameters))) #{} (vals setters-by-key))
        {:keys [requires imports]} (classify-dependencies deps (set/union new-builder-types getter-types setter-types))
        {:keys [requires imports]} (merge-nested-deps {:requires requires :imports imports} (:nested base))
        required-keys (set newBuilder-keys)]
    (assoc base
      :keys/required required-keys
      :keys/optional (set/difference (set/intersection (set (keys setters-by-key)) (set (keys getters-by-key))) required-keys)
      :keys/read-only (set/difference (set (keys getters-by-key)) (set (keys setters-by-key)) required-keys)
      :require-types requires
      :import-types imports
      :autovalue? (boolean (:autovalue? node))
      :discriminator (:discriminator node)
      :discriminator-type discriminator-type
      :newBuilder newBuilder
      :getters-by-key getters-by-key
      :setters-by-key setters-by-key)))

(defn analyze-collection-wrapper
  [{:keys [deps] :as node}]
  (let [base          (basic-info node)
        of-iterable   (reduce
                        (fn [_ method]
                          (when (and (= "of" (:name method))
                                     (= 'java.lang.Iterable (get-in method [:parameters 0 :type 0])))
                            (reduced method)))
                        nil
                        (filter :static? (:methods node)))
        _             (assert (some? of-iterable))
        element-type  (get-in of-iterable [:parameters 0 :type 1])
        {:keys [requires imports]} (classify-dependencies deps #{element-type})
        {:keys [requires imports]} (merge-nested-deps {:requires requires :imports imports} (:nested base))]
    (cond-> (assoc base :of-iterable of-iterable
                        :element-type element-type
                        :require-types requires
                        :import-types imports))))

(defn analyze-read-only [{:keys [deps] :as node}]
  (let [base (basic-info node)
        getters-by-key (into (sorted-map)
                         (map
                           (fn [m]
                             [(u/property-key (:name m)) (dissoc m :parameters :abstract? :static?)]))
                         (getters node))
        getter-types (into #{} (map :returnType) (vals getters-by-key))
        {:keys [requires imports]} (classify-dependencies deps getter-types)
        {:keys [requires imports]} (merge-nested-deps {:requires requires :imports imports} (:nested base))]
    (assoc base :getters-by-key getters-by-key
                :require-types requires
                :import-types imports)))

(defn analyze-interface [{:keys [deps] :as node}]
  (let [base (basic-info node)
        getters-by-key (into (sorted-map)
                         (map
                           (fn [m]
                             [(u/property-key (:name m)) (dissoc m :parameters :abstract? :static?)]))
                         (getters node))
        getter-types (into #{} (map :returnType) (vals getters-by-key))
        {:keys [requires imports]} (classify-dependencies deps getter-types)
        {:keys [requires imports]} (merge-nested-deps {:requires requires :imports imports} (:nested base))]
    (assoc base :getters-by-key getters-by-key
                :require-types requires
                :import-types imports)))

(defn analyze-pojo
  [{:keys [constructors deps] :as node}]
  {:pre [(seq constructors)]}
  (let [base (basic-info node)
        constructors-by-keys (into {}
                                   (map
                                     (fn [{:keys [parameters] :as ctor}]
                                       [(set (map (comp keyword :name) parameters)) ctor]))
                                   constructors)
        getters-by-key (into (sorted-map)
                             (map
                               (fn [m]
                                 (let [key (u/property-key (:name m))]
                                   [key (dissoc m :parameters :abstract? :static?)])))
                             (getters node))        constructor-keys (apply set/union (keys constructors-by-keys))
        required-keys    (apply set/intersection (keys constructors-by-keys))
        read-only-keys   (set/difference (set (keys getters-by-key)) constructor-keys)
        constructor-types (into #{} (mapcat (fn [c] (map :type (:parameters c)))) constructors)
        getter-types (into #{} (map :returnType) (vals getters-by-key))
        {:keys [requires imports]} (classify-dependencies deps (set/union constructor-types getter-types))
        {:keys [requires imports]} (merge-nested-deps {:requires requires :imports imports} (:nested base))]
    (assert (= (count constructors) (count constructors-by-keys)))
    (cond-> (assoc base :constructors-by-keys constructors-by-keys
                        :keys/constructor constructor-keys
                        :keys/required required-keys
                        :keys/read-only read-only-keys
                        :require-types requires
                        :import-types imports)
            (seq getters-by-key) (assoc :getters-by-key getters-by-key))))

(defn analyze-enum [node]
  (let [base (basic-info node)
        values (if (contains? #{:nested/string-enum :string-enum} (:category node))
                 (reduce
                   (fn [acc {:keys [] :as f}]
                     (if (and (static? f)
                              (public? f)
                              (= (string/upper-case (:name f)) (:name f)))
                       (conj acc {:name (:name f) :doc (:doc f)})
                       acc))
                   []
                   (:fields node))
                 (:values node))
        {:keys [requires imports]} (merge-nested-deps {:requires {} :imports #{}} (:nested base))]
    (assert (seq values))
    (assert (every? #(and (map? %) (contains? % :doc)) values))
    (assoc base :values values
                :require-types requires
                :import-types imports)))

(defn by-field-key [ms]
  (into {}
        (map
          (fn [{:keys [field-name] :as m}]
            ;; NOTE: this is likely filtering out inherited getters!
            (when field-name
              [(u/property-key field-name) m])))
        ms))

(defn align-getter-setters-by-key [node]
  (let [getters (getters node)
        getters-by-field (by-field-key getters)
        setters (setters node)
        setters-by-field (by-field-key setters)]
    (reduce
      (fn [acc k]
        (assoc acc k {:getter (get getters-by-field k)
                      :setter (get setters-by-field k)}))
      (sorted-map)
      (keys setters-by-field))))

(defn analyze-mutable-pojo
  [{:keys [deps] :as node}]
  (let [base (basic-info node)
        by-key (align-getter-setters-by-key node)
        ts (reduce
             (fn [acc [_ {:keys [getter setter]}]]
               (conj acc
                     (:returnType getter)
                     (get-in setter [:parameters 0 :type])))
             #{}
             by-key)
        {:keys [requires imports]} (merge-nested-deps (classify-dependencies deps ts)
                                                      (:nested base))]
    (assoc base :getter-setters-by-key by-key
                :require-types requires
                :import-types imports)))

;; -----------------------------------------------------------------------------
#! Statics

;; (Class/a <arg>)  || (Class/b <arg>)  ||  (Class/c)
;; -> {:a <arg>}    || {:b <arg>}       ||  {:c nil}

(defn analyze-static-variants
  [{:keys [deps] :as node}]
  (let [base                 (basic-info node)
        static-methods       (sequence (comp (filter :static?)
                                             (map #(-> (dissoc % :abstract? :static? :private? :parameter-mappings)
                                                       (assoc :key (u/property-key (:name %))))))
                                       (:methods node))
        factory-methods      (vec (sort-by #(count (:parameters %)) < static-methods))
        _                    (when-some [no-args (not-empty (filter #(zero? (count (:parameters %))) factory-methods))]
                               (when (< 1 (count no-args))
                                 (throw (ex-info "ambiguous no-arg static-methods" {:no-arg-methods no-args
                                                                                    :fqcn           (:fqcn node)}))))
        instance-methods     (remove :static? (:methods node))
        variant-type         (some :variant-type factory-methods)
        discriminator-method (when variant-type
                               (first (filter (fn [m]
                                                (and (empty? (:parameters m))
                                                     (clojure.string/ends-with? (str (:returnType m)) variant-type)))
                                              instance-methods)))
        _ (assert discriminator-method (str "could not find discriminator-method for " (:fqcn node)))
        factory-methods'     (mapv (fn [fm]
                                     (if (seq (:parameters fm))
                                       (let [fname (:field-name fm)
                                             exact-match (when fname
                                                           (first (filter #(and (empty? (:parameters %))
                                                                                (= fname (:field-name %)))
                                                                          instance-methods)))
                                             fallback (first (filter #(and (empty? (:parameters %))
                                                                           (= "getValue" (:name %)))
                                                                     instance-methods))
                                             value-method (or exact-match fallback)]
                                         (if value-method
                                           (assoc fm :value-method (assoc value-method :returnType (-> fm :parameters first :type)))
                                           fm))
                                       fm))
                                   factory-methods)
        variant->key         (into (sorted-map)
                                   (keep
                                     (fn [{:keys [variant key]}]
                                       (when variant
                                         [variant key])))
                                   factory-methods')
        variant->getter      (into (sorted-map)
                                   (keep
                                     (fn [{:keys [variant value-method]}]
                                       (when variant
                                         [variant value-method])))
                                   factory-methods')
        _ (assert (= (set (keys variant->key)) (set (keys variant->getter))) "expected each variant to have an associated getter")
        key->factory (into (sorted-map) (map #(vector (:key %) %)) factory-methods')
        ts (into #{} (comp (map #(get-in % [:parameters 0 :type])) (filter some?)) factory-methods')
        {:keys [requires imports]} (merge-nested-deps (classify-dependencies deps ts)
                                                      (:nested base))]
    (cond-> (assoc base :key->factory key->factory
                        :variant->key variant->key
                        :variant->getter variant->getter
                        :require-types requires
                        :import-types imports)
      discriminator-method (assoc :discriminator-method discriminator-method))))

(defn analyze-client-options
  [{:keys [deps] :as node}]
  (let [base (basic-info node)
        static-methods (sequence (comp (filter :static?)
                                       (filter #(clojure.string/ends-with? (str (:returnType %)) (:className node)))
                                       (map #(let [k (if (= "of" (:name %))
                                                       (if-let [pname (:name (first (:parameters %)))]
                                                         (u/property-key pname)
                                                         (u/property-key (:name %)))
                                                       (u/property-key (:name %)))]
                                               (-> (dissoc % :abstract? :static? :private? :parameter-mappings)
                                                   (assoc :key k)))))
                                 (:methods node))
        key->factories (group-by :key static-methods)
        key->factory (update-vals key->factories (fn [ms] (first (sort-by #(count (:parameters %)) ms))))
        ts (into #{}
                 (comp (mapcat :parameters) (map :type) (filter some?))
                 (vals key->factory))
        {:keys [requires imports]} (merge-nested-deps (classify-dependencies deps ts)
                                                      (:nested base))]
    (assoc base :key->factory key->factory
                :require-types requires
                :import-types imports)))

;; -----------------------------------------------------------------------------

(defn extract-protobuf-behavior [doc]
  (when doc
    (cond
      (re-find #"\.google\.api\.field_behavior\) = ([^\]]+)" doc)
      (let [b (second (re-find #"\.google\.api\.field_behavior\) = ([^\]]+)" doc))]
        (cond
          (string/includes? b "REQUIRED") :required
          (string/includes? b "OUTPUT_ONLY") :read-only
          (string/includes? b "OPTIONAL") :optional
          :else :optional))
      (re-find #"(?i)^<pre>\s*(?:<b>)?\s*Required\." doc) :required
      (re-find #"(?i)^<pre>\s*(?:<b>)?\s*Output only\." doc) :read-only
      (re-find #"(?i)^<pre>\s*(?:<b>)?\s*Optional\." doc) :optional
      :else :optional)))

(defn- protobuf-value-method? [m all-method-names]
  (let [n (:name m)]
    (and (string/ends-with? n "Value")
         (not= n "getValue")
         (let [base (subs n 0 (- (count n) 5))]
           (contains? all-method-names base)))))

(defn analyze-union-protobuf-oneof
  [{:keys [deps nested] :as node}]
  (let [base (basic-info node)
        builder-node (get-builder node)
        instance-methods (:methods node)
        builder-methods (:methods builder-node)
        all-instance-method-names (into #{} (map :name) instance-methods)
        all-builder-method-names (into #{} (map :name) builder-methods)

        newBuilder (first (filter #(and (= "newBuilder" (:name %)) (empty? (:parameters %))) (:methods node)))
        _ (assert (some? newBuilder) "union protobuf missing no-arg newBuilder()")

        ;; Ensure we only work with standard POJO-like accessors for non-union resolution
        getters-list (getters node)
        setters-list (setters builder-node)
        case-enums (filter #(and (= :nested/enum (:category %))
                                 (string/ends-with? (:className %) "Case"))
                           nested)
        unions (into (sorted-map)
                     (for [enum-node case-enums
                           :let [enum-name (:className enum-node)
                                 union-key (u/property-key (subs enum-name 0 (- (count enum-name) 4)))
                                 case-getter-name (str "get" enum-name)
                                 case-values (remove #(string/ends-with? (:name %) "_NOT_SET") (:values enum-node))]]
                       [union-key
                        {:case-getter case-getter-name
                         :variants (into (sorted-map)
                                         (for [v-node case-values
                                               :let [v-name (:name v-node)
                                                     pascal (u/screaming-snake->pascal v-name)
                                                     variant-key (u/property-key pascal)
                                                     get-name (str "get" pascal)
                                                     has-name (str "has" pascal)
                                                     set-name (str "set" pascal)]]
                                           [variant-key
                                            (cond-> {}
                                              (some #(= get-name (:name %)) instance-methods) (assoc :get get-name)
                                              (some #(= has-name (:name %)) instance-methods) (assoc :has has-name)
                                              (some #(= set-name (:name %)) builder-methods) (assoc :set set-name))]))}]))

        ;; Create a lookup map for method names to union info
        method-name->union-info (into {}
                                      (for [[_union-key union-info] unions
                                            [_variant-key variant-info] (:variants union-info)
                                            [role method-name] variant-info]
                                        [method-name {:key _union-key :variant _variant-key :role role}]))

        ;; Add case-getters to the lookup map
        method-name->union-info (into method-name->union-info
                                      (for [[_union-key union-info] unions]
                                        [(:case-getter union-info) {:key _union-key :role :case-getter}]))

        label-methods (fn [methods]
                        (map (fn [m]
                               (if-let [union-info (get method-name->union-info (:name m))]
                                 (assoc m :union union-info)
                                 m))
                             methods))

        instance-methods' (label-methods instance-methods)
        builder-methods' (label-methods builder-methods)

        labeled-getters (label-methods getters-list)
        labeled-setters (label-methods setters-list)

        extract-key (fn [m]
                      (let [n (:name m)
                            base (cond
                                   (string/starts-with? n "has") (subs n 3)
                                   :else n)
                            k (u/property-key base)
                            ks (name k)]
                        (cond
                          (and (string/ends-with? ks "List") (> (count ks) 4)) (keyword (subs ks 0 (- (count ks) 4)))
                          (and (string/ends-with? ks "Map") (> (count ks) 3)) (keyword (subs ks 0 (- (count ks) 3)))
                          :else k)))

        extract-setter-key (fn [m]
                             (let [ks (name (u/property-key (:name m)))]
                               (cond
                                 (and (string/starts-with? (:name m) "set")
                                      (string/ends-with? ks "List") (> (count ks) 4)) (keyword (subs ks 0 (- (count ks) 4)))
                                 (and (string/starts-with? (:name m) "set")
                                      (string/ends-with? ks "Map") (> (count ks) 3)) (keyword (subs ks 0 (- (count ks) 3)))
                                 (string/starts-with? (:name m) "addAll")
                                 (let [base (subs (:name m) 6)]
                                   (keyword (u/property-name base)))
                                 (string/starts-with? (:name m) "putAll")
                                 (let [base (subs (:name m) 6)]
                                   (keyword (u/property-name base)))
                                 :else (keyword ks))))

        ;; Group methods by key for consistent analysis
        getters-by-key (into (sorted-map)
                             (comp
                               (filter #(and (:union %) (= :get (:role (:union %)))))
                               (remove #(protobuf-value-method? % all-instance-method-names))
                               (map (fn [m] [(:variant (:union m)) (clean-getter m)]))
                               (remove #(#{:serializedSize :defaultInstance} (first %))))
                             instance-methods')

        ;; Non-union getters
        non-union-getters-by-key (into (sorted-map)
                                       (comp
                                         (remove :union)
                                         (filter #(or (string/starts-with? (:name %) "get")
                                                      (string/starts-with? (:name %) "is")))
                                         (remove #(string/ends-with? (:name %) "Count"))
                                         (remove #(protobuf-value-method? % all-instance-method-names))
                                         (map (fn [m] [(extract-key m) (clean-getter m)]))
                                         (remove #(#{:serializedSize :defaultInstance} (first %))))
                                       labeled-getters)

        setters-by-key (into (sorted-map)
                             (comp
                               (filter #(and (:union %) (= :set (:role (:union %)))))
                               (remove #(protobuf-value-method? % all-builder-method-names))
                               (map (fn [m] [(:variant (:union m)) (clean-setter m)]))
                               (remove #(#{:serializedSize :defaultInstance} (first %))))
                             builder-methods')

        non-union-setters-by-key (into (sorted-map)
                                       (comp
                                         (remove :union)
                                         (filter #(or (string/starts-with? (:name %) "set")
                                                      (string/starts-with? (:name %) "addAll")
                                                      (string/starts-with? (:name %) "putAll")))
                                         (remove #(protobuf-value-method? % all-builder-method-names))
                                         (map (fn [m] [(extract-setter-key m) (clean-setter m)]))
                                         (remove #(#{:serializedSize :defaultInstance} (first %))))
                                       labeled-setters)

        has-methods-by-key (into (sorted-map)
                                 (comp
                                   (filter #(and (:union %) (= :has (:role (:union %)))))
                                   (remove #(protobuf-value-method? % all-instance-method-names))
                                   (map (fn [m] [(:variant (:union m)) (clean-getter m)]))
                                   (remove #(#{:serializedSize :defaultInstance} (first %))))
                                 instance-methods')

        non-union-has-methods-by-key (into (sorted-map)
                                           (comp
                                             (remove :union)
                                             (filter #(and (not (:static? %))
                                                           (empty? (:parameters %))
                                                           (string/starts-with? (:name %) "has")))
                                             (remove #(protobuf-value-method? % all-instance-method-names))
                                             (map (fn [m] [(extract-key m) (clean-getter m)]))
                                             (remove #(#{:serializedSize :defaultInstance} (first %))))
                                           instance-methods')
                                           
        merged-has-methods (merge non-union-has-methods-by-key has-methods-by-key)

        ;; Assertions
        _ (doseq [[union-key union-info] unions
                  [variant-key variant-methods] (:variants union-info)]
            (assert (contains? getters-by-key variant-key)
                    (str "Missing getter for union variant: " union-key "/" variant-key " in " (:className node)))
            (assert (contains? setters-by-key variant-key)
                    (str "Missing setter for union variant: " union-key "/" variant-key " in " (:className node))))

        ;; Dependencies
        getter-types (into #{} (map :returnType) (vals getters-by-key))
        setter-types (into #{} (mapcat #(map :type (:parameters %)) (vals setters-by-key)))
        non-union-getter-types (into #{} (map :returnType) (vals non-union-getters-by-key))
        non-union-setter-types (into #{} (mapcat #(map :type (:parameters %)) (vals non-union-setters-by-key)))
        has-method-types (into #{} (map :returnType) (vals has-methods-by-key))
        non-union-has-method-types (into #{} (map :returnType) (vals non-union-has-methods-by-key))

        all-types (set/union getter-types setter-types non-union-getter-types non-union-setter-types has-method-types non-union-has-method-types)

        {:keys [requires imports]} (merge-nested-deps (classify-dependencies deps all-types)
                                                      (:nested base))

        merged-getters (merge non-union-getters-by-key getters-by-key)

        behaviors (reduce-kv (fn [acc k m]
                               (let [behavior (or (extract-protobuf-behavior (:doc m)) :optional)]
                                 (update acc behavior (fnil conj #{}) k)))
                             {:required #{} :optional #{} :read-only #{}}
                             merged-getters)

        required-keys (:required behaviors)
        read-only-keys (:read-only behaviors)
        optional-keys (set/difference (set (keys merged-getters)) required-keys read-only-keys)

        merged-setters (merge non-union-setters-by-key setters-by-key)
        _ (assert (empty? (set/difference required-keys (set (keys merged-setters))))
                  (str "union protobuf is missing setters for required properties: " (set/difference required-keys (set (keys merged-setters)))))
        _ (assert (empty? (set/difference optional-keys (set (keys merged-setters))))
                  (str "union protobuf is missing setters for optional properties: " (set/difference optional-keys (set (keys merged-setters)))))]

    (assoc base
      :unions unions
      :newBuilder newBuilder
      :methods instance-methods'
      :builder (assoc (basic-info builder-node) :methods builder-methods')
      :getters-by-key merged-getters
      :setters-by-key merged-setters
      :has-methods-by-key merged-has-methods
      :keys/required required-keys
      :keys/optional optional-keys
      :keys/read-only read-only-keys
      :require-types requires
      :import-types imports)))

#!----------------------------------------------------------------------------------------------------------------------

(defn analyze-protobuf-message
  [{:keys [deps] :as node}]
  (let [base (basic-info node)
        builder-node (get-builder node)
        all-method-names (into #{} (map :name) (:methods node))
        all-builder-method-names (into #{} (map :name) (:methods builder-node))
        newBuilder (first (filter #(and (= "newBuilder" (:name %)) (empty? (:parameters %))) (:methods node)))
        _ (assert (some? newBuilder) "protobuf message missing no-arg newBuilder()")
        
        extract-key (fn [m]
                      (let [n (:name m)
                            base (cond
                                   (string/starts-with? n "has") (subs n 3)
                                   :else n)
                            k (u/property-key base)
                            ks (name k)]
                        (cond
                          (and (string/ends-with? ks "List") (> (count ks) 4)) (keyword (subs ks 0 (- (count ks) 4)))
                          (and (string/ends-with? ks "Map") (> (count ks) 3)) (keyword (subs ks 0 (- (count ks) 3)))
                          :else k)))
                          
        extract-setter-key (fn [m]
                             (let [ks (name (u/property-key (:name m)))]
                               (cond
                                 (and (string/starts-with? (:name m) "set")
                                      (string/ends-with? ks "List") (> (count ks) 4)) (keyword (subs ks 0 (- (count ks) 4)))
                                 (and (string/starts-with? (:name m) "set")
                                      (string/ends-with? ks "Map") (> (count ks) 3)) (keyword (subs ks 0 (- (count ks) 3)))
                                 (string/starts-with? (:name m) "addAll")
                                 (let [base (subs (:name m) 6)]
                                   (keyword (u/property-name base)))
                                 (string/starts-with? (:name m) "putAll")
                                 (let [base (subs (:name m) 6)]
                                   (keyword (u/property-name base)))
                                 :else (keyword ks))))
                          
        getters-by-key (into (sorted-map)
                             (comp
                               (filter #(or (string/starts-with? (:name %) "get")
                                            (string/starts-with? (:name %) "is")))
                               (remove #(string/ends-with? (:name %) "Count"))
                               (remove #(protobuf-value-method? % all-method-names))
                               (map (fn [m] [(extract-key m) (clean-getter m)]))
                               (remove #(#{:serializedSize :defaultInstance} (first %))))
                             (getters node))
                             
        setters-by-key (into (sorted-map)
                             (comp
                               (filter #(or (string/starts-with? (:name %) "set")
                                            (string/starts-with? (:name %) "addAll")
                                            (string/starts-with? (:name %) "putAll")))
                               (remove #(protobuf-value-method? % all-builder-method-names))
                               (map (fn [m] [(extract-setter-key m) (clean-setter m)]))
                               (remove #(#{:serializedSize :defaultInstance} (first %))))
                             (setters builder-node))

        has-methods-by-key (into (sorted-map)
                                 (comp
                                   (filter #(and (not (:static? %))
                                                 (empty? (:parameters %))
                                                 (string/starts-with? (:name %) "has")))
                                   (remove #(protobuf-value-method? % all-method-names))
                                   (map (fn [m] [(extract-key m) (clean-getter m)]))
                                   (remove #(#{:serializedSize :defaultInstance} (first %))))
                                 (:methods node))

        getter-types (into #{} (map :returnType) (vals getters-by-key))
        setter-types (into #{} (mapcat #(map :type (:parameters %)) (vals setters-by-key)))
        has-method-types (into #{} (map :returnType) (vals has-methods-by-key))
        all-types (set/union getter-types setter-types has-method-types)
        {:keys [requires imports]} (merge-nested-deps (classify-dependencies deps all-types) (:nested base))
        
        behaviors (reduce-kv (fn [acc k m]
                               (let [behavior (or (extract-protobuf-behavior (:doc m)) :optional)]
                                 (update acc behavior (fnil conj #{}) k)))
                             {:required #{} :optional #{} :read-only #{}}
                             getters-by-key)
        
        ;; Ensure required and optional are disjoint, etc.
        required-keys (:required behaviors)
        read-only-keys (:read-only behaviors)
        optional-keys (set/difference (set (keys getters-by-key)) required-keys read-only-keys)
        
        _ (assert (empty? (set/difference required-keys (set (keys setters-by-key))))
                  (str "protobuf message is missing setters for required properties: " (set/difference required-keys (set (keys setters-by-key)))))
        _ (assert (empty? (set/difference optional-keys (set (keys setters-by-key))))
                  (str "protobuf message is missing setters for optional properties: " (set/difference optional-keys (set (keys setters-by-key)))))]
        
    (assoc base
      :newBuilder newBuilder
      :builder (assoc (basic-info builder-node) :methods (:methods builder-node))
      :getters-by-key getters-by-key
      :setters-by-key setters-by-key
      :has-methods-by-key has-methods-by-key
      :keys/required required-keys
      :keys/optional optional-keys
      :keys/read-only read-only-keys
      :require-types requires
      :import-types imports)))


;; -----------------------------------------------------------------------------
;; Analyzer
;; -----------------------------------------------------------------------------

(defn _analyze-class-node
  [{:keys [category] :as node}]
  {:post [(= category (:category %)) (contains? % :className)]}
  (case category
    (:protobuf-message :nested/protobuf-message)          (analyze-protobuf-message node)
    (:union-protobuf-oneof :nested/union-protobuf-oneof)  (analyze-union-protobuf-oneof node)
    (:mutable-pojo :nested/mutable-pojo) (analyze-mutable-pojo node)
    :accessor-with-builder (analyze-accessor-with-builder node)
    :client                (basic-info node)
    :collection-wrapper    (analyze-collection-wrapper node)
    :factory               (basic-info node)
    :functional-interface  (basic-info node)
    :interface             (analyze-interface node)
    :pojo                  (analyze-pojo node)
    :read-only             (analyze-read-only node)
    :resource-extended     (basic-info node)
    :sentinel              (basic-info node)
    ; :static-utilities      (analyze-static-utilities node)
    :static-variants       (analyze-static-variants node)
    :static-factory        (analyze-static-factory node)
    (:enum :string-enum)   (analyze-enum node)
    :union-abstract        (analyze-union-abstract node)
    :union-concrete        (analyze-union-concrete node)
    :union-tagged          (analyze-union-tagged node)
    :variant-accessor      (analyze-variant-accessor node)
    #!-----------
    :nested/accessor-with-builder      (analyze-accessor-with-builder node)
    :nested/builder                    (basic-info node)
    :nested/client                     (basic-info node)
    :nested/collection-wrapper         (analyze-collection-wrapper node)
    (:nested/enum :nested/string-enum) (analyze-enum node)
    :nested/factory                    (basic-info node)
    :nested/pojo                       (analyze-pojo node)
    :nested/read-only                  (analyze-read-only node)
    :nested/client-options             (analyze-client-options node)
    ; :nested/static-utilities           (analyze-static-utilities node)
    :nested/static-variants            (analyze-static-variants node)
    :nested/static-factory             (analyze-static-factory node)
    :nested/variant-pojo               (analyze-pojo node)
    :nested/union-abstract             (analyze-union-abstract node)
    :nested/union-tagged               (analyze-union-tagged node)
    :nested/variant-read-only          (basic-info node)
    #! << IT IS FORBIDDEN TO CHANGE THIS BRANCH >>
    (throw (Exception. (str "illegal state: missing analysis handler for category " (:category node) " for class " (:fqcn node)))))) #! IT IS FORBIDDEN TO CHANGE THIS LINE

(defn analyze-class-node
  [class-node]
  (assert (contains? shared/categories (:category class-node)) (str "Unsupported category in analyzer input node: '" (pr-str (:category class-node)) "'")) #! IT IS FORBIDDEN TO CHANGE THIS LINE
  (assert (contains? class-node :deps) "class-nodes must come with :deps entry") #! IT IS FORBIDDEN TO CHANGE THIS LINE
  (_analyze-class-node class-node))
