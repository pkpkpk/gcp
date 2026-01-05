(ns gcp.foreign.com.google.api.gax.batching
  {:gcp.dev/certification
   {:BatchingSettings
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767570434548
     :timestamp "2026-01-04T23:47:14.564142115Z"
     :passed-stages {:smoke 1767570434548
                     :standard 1767570434549
                     :stress 1767570434550}
     :source-hash "2db39e85bf19b1e0d025bc24f9ec3a5959c307a94a33d0c2bc912600d78c50e5"}
    :FlowControlSettings
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767570434564
     :timestamp "2026-01-04T23:47:14.569571772Z"
     :passed-stages {:smoke 1767570434564
                     :standard 1767570434565
                     :stress 1767570434566}
     :source-hash "2db39e85bf19b1e0d025bc24f9ec3a5959c307a94a33d0c2bc912600d78c50e5"}}}
  (:require [gcp.global :as global]
            [gcp.foreign.org.threeten.bp :as bp])
  (:import (com.google.api.gax.batching BatchingSettings BatchingSettings$Builder FlowControlSettings FlowControlSettings$Builder FlowController$LimitExceededBehavior)))

(def registry
  (let [pos-duration [:map
                      [:seconds [:int {:min 0 :max 100}]]
                      [:nanos [:int {:min 1 :max 999999999}]]]]
    (with-meta
      {:gcp.foreign.com.google.api.gax.batching/LimitExceededBehavior [:enum :ThrowException :Block :Ignore]
       :gcp.foreign.com.google.api.gax.batching/FlowControlSettings [:map
                                                                     [:maxOutstandingElementCount {:optional true} [:int {:min 1}]]
                                                                     [:maxOutstandingRequestBytes {:optional true} [:int {:min 1}]]
                                                                     [:limitExceededBehavior {:optional true} [:ref :gcp.foreign.com.google.api.gax.batching/LimitExceededBehavior]]]
       :gcp.foreign.com.google.api.gax.batching/BatchingSettings [:map
                                                                  [:elementCountThreshold {:optional true} [:int {:min 1}]]
                                                                  [:requestByteThreshold {:optional true} [:int {:min 1}]]
                                                                  [:delayThreshold {:optional true} pos-duration]
                                                                  [:flowControlSettings {:optional true} [:ref :gcp.foreign.com.google.api.gax.batching/FlowControlSettings]]
                                                                  [:isEnabled {:optional true} :boolean]]}
      {:gcp.global/name :gcp.foreign.com.google.api.gax.batching/registry})))

(global/include-schema-registry! registry)

#!-----------------------------------------------------------------------------

(defn FlowControlSettings-to-edn [^FlowControlSettings arg]
  (cond-> {}
    (.getMaxOutstandingElementCount arg) (assoc :maxOutstandingElementCount (.getMaxOutstandingElementCount arg))
    (.getMaxOutstandingRequestBytes arg) (assoc :maxOutstandingRequestBytes (.getMaxOutstandingRequestBytes arg))
    (.getLimitExceededBehavior arg) (assoc :limitExceededBehavior (keyword (.name (.getLimitExceededBehavior arg))))))

(defn ^FlowControlSettings FlowControlSettings-from-edn [arg]
  (let [builder (FlowControlSettings/newBuilder)]
    (when-some [v (:maxOutstandingElementCount arg)] (.setMaxOutstandingElementCount builder v))
    (when-some [v (:maxOutstandingRequestBytes arg)] (.setMaxOutstandingRequestBytes builder v))
    (when-some [v (:limitExceededBehavior arg)]
      (.setLimitExceededBehavior builder (FlowController$LimitExceededBehavior/valueOf (name v))))
    (.build builder)))

#!-----------------------------------------------------------------------------

(defn BatchingSettings-to-edn [^BatchingSettings arg]
  (cond-> {}
    (.getElementCountThreshold arg) (assoc :elementCountThreshold (.getElementCountThreshold arg))
    (.getRequestByteThreshold arg) (assoc :requestByteThreshold (.getRequestByteThreshold arg))
    (.getDelayThreshold arg) (assoc :delayThreshold (bp/Duration-to-edn (.getDelayThreshold arg)))
    (.getFlowControlSettings arg) (assoc :flowControlSettings (FlowControlSettings-to-edn (.getFlowControlSettings arg)))
    (some? (.getIsEnabled arg)) (assoc :isEnabled (.getIsEnabled arg))))

(defn ^BatchingSettings BatchingSettings-from-edn [arg]
  (let [builder (BatchingSettings/newBuilder)]
    (when-some [v (:elementCountThreshold arg)] (.setElementCountThreshold builder v))
    (when-some [v (:requestByteThreshold arg)] (.setRequestByteThreshold builder v))
    (when-some [v (:delayThreshold arg)] (.setDelayThreshold builder (bp/Duration-from-edn v)))
    (when-some [v (:flowControlSettings arg)] (.setFlowControlSettings builder (FlowControlSettings-from-edn v)))
    (when-some [v (:isEnabled arg)] (.setIsEnabled builder v))
    (.build builder)))
