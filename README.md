
 
 lib | release |  notes
-----|---------|---------|
`gcp.bigquery` | [![Clojars Project](https://img.shields.io/clojars/v/com.github.pkpkpk/gcp.bigquery.svg?include_prereleases)](https://clojars.org/com.github.pkpkpk/gcp.bigquery) | complete :white_check_mark:
`gcp.storage`  | [![Clojars Project](https://img.shields.io/clojars/v/com.github.pkpkpk/gcp.storage.svg?include_prereleases)](https://clojars.org/com.github.pkpkpk/gcp.storage) | (buckets, blobs, ACL+auth, Notifications) ✅, (the rest) WIP :construction:
`gcp.pubsub`   | [![Clojars Project](https://img.shields.io/clojars/v/com.github.pkpkpk/gcp.pubsub.svg?include_prereleases)](https://clojars.org/com.github.pkpkpk/gcp.pubsub) | WIP :construction:
~gcp.vertexai~ | :dead: | :dead:

### usage

```clojure
(require '[gcp.bigquery :as bq]
         '[gcp.global :as g])

;;constructing all classes via their map representation
; recovering edn from returned instances

```

### schema keys, discovery, strict-mode 

### argument parse errors

### DWIM & structural polymorphism

`pkpkpk/gcp` tries to facilitate interactive development by being as tolerant as possible when accepting arguments. The goal is a DWIM[^1] interface, where a repl user can allow themselves to be a little hand wavy and only argue shorthand. You get heavily sugared interpretations without losing the ability to be specific.

If you've used the google cloud java SDKs, you already know the general norm `(.action client object Options[])` where object is usually an id/info instance, and occasionally polymorphic. Even with sugared methods, this can still be very verbose even with sparse data. 
Here we try we try to ease some of this friction by:

+ using default client when omitted, or building one when given its config
+ using the empty opts array when omitted, or building an opts array when given map representation
+ tolerating many arities where appropriate
+ accept even more sugared forms mediated by malli transforms and structural polymorphism[^0]. If a complex schema requires only a minimal nested schema, we accept the minimal value and construct the complex parent for you.

Each operation has a canonical form, for example we can represent creating a BQ dataset in malli as:

```clojure
   > (g/get-schema :gcp.bigquery.core/DatasetCreate) 
   ;=>
   [:map {:closed true 
          :doc "call record for bq.create(datasetInfo)"}
    [:bigquery {:optional true} [:ref :gcp.bigquery.core/clientable]]
    [:datasetInfo :gcp.bigquery/DatasetInfo]
    [:opts {:optional true} :gcp.bigquery/BigQuery.DatasetOption]]
```

If you inspect the DatasetInfo class, `(g/get-schema :gcp.bigquery/DatasetInfo)` you will see that only `:gcp.bigquery/DatasetId` is required,
If you continue and inspect the DatasetId class, `(g/get-schema :gcp.bigquery/DatasetId)` you'll only a string dataset name is required; this is the minimal amount of information we need to provide to `bq/create-dataset`


```clojure
;=> [:string]
(bq/create-dataset "dataset-name") 

;=> [:gcp.bigquery/DatasetId]
(bq/create-dataset {:dataset "foo"}) 

;=> [:gcp.bigquery/DatasetInfo]
(bq/create-dataset {:datasetId {:dataset "foo"} :location "us-central1"})

;=> [:string, :gcp.bigquery/BigQuery.DatasetOption]
(bq/create-dataset "dataset-name" {:accessPolicyVersion 42}) 

;=> [:gcp.bigquery/DatasetId :gcp.bigquery/BigQuery.DatasetOption]
(bq/create-dataset {:dataset "foo" :location "us-central1"} {:accessPolicyVersion 42}) 

;=> [:gcp.bigquery/DatasetInfo :gcp.bigquery/BigQuery.DatasetOption]
(bq/create-dataset {:datasetId {:dataset "foo" :location "us-central1"}} {:accessPolicyVersion 42})
```

The above combinations can again be repeated by satisfying `:gcp.bigquery.core/clientable` in the first argument

```clojure
;=> [:gcp.bigquery/BigQueryOptions :string]
(bq/create-dataset {:projectId "foo"} "dataset-name")

;=> [:gcp.bigquery/BigQueryOptions :string]
(bq/create-dataset {:projectId "foo"} "dataset-name" {:accessPolicyVersion 42})

...
```

### clients

The general pattern of google cloud client objects is a service specific ServiceSettings/ServiceOptions map that has inherited fields for transitive google sdks. All the same, we simply represent these as edn and cache instances via that identity

```clojure
;; example of client options parameter
(g/get-schema :gcp.bigquery/BigQueryOptions)

;; omitting options gives default client. clients are cached by their arguments, if any
(assert (identical? (bq/client) (bq/client)))

```

Each package has a core namespace with a *client* dynamic var, this will take precedence over any clientable given to the function call.


```clojure
(binding [gcp.bigquery.core/*client* (bq/client {:projectId "bar"})]
  (bq/q {:projectId "foo"} "..."))
```

### aux


<hr>

### links

malli
gcp docs
sdks docs
sdks maven
sdks reps

### refs

[^0]: https://en.wikipedia.org/wiki/DWIM
[^1]: https://caml.inria.fr/pub/papers/garrigue-structural_poly-fool02.pdf
