(ns gcp.foreign.com.google.api.gax.batching
  (:require [gcp.global :as g])
  (:import (com.google.api.gax.batching BatchingSettings BatchingSettings$Builder FlowControlSettings FlowControlSettings$Builder FlowController$LimitExceededBehavior)
           (java.time Duration)))

(def registry
  (with-meta
    {::LimitExceededBehavior [:enum :ThrowException :Block :Ignore]
     ::FlowControlSettings   [:map
                              [:maxOutstandingElementCount {:optional true} [:int {:min 1}]]
                              [:maxOutstandingRequestBytes {:optional true} [:int {:min 1}]]
                              [:limitExceededBehavior {:optional true} [:ref :gcp.foreign.com.google.api.gax.batching/LimitExceededBehavior]]]
     ::BatchingSettings      [:map
                              [:elementCountThreshold {:optional true} [:int {:min 1}]]
                              [:requestByteThreshold {:optional true} [:int {:min 1}]]
                              [:delayThreshold {:optional true} 'number?]
                              [:flowControlSettings {:optional true} [:ref :gcp.foreign.com.google.api.gax.batching/FlowControlSettings]]
                              [:isEnabled {:optional true} :boolean]]}
    {::g/name ::registry}))

(g/include-schema-registry! registry)

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
    (.getDelayThreshold arg) (assoc :delayThreshold (.toSeconds (.getDelayThresholdDuration arg)))
    (.getFlowControlSettings arg) (assoc :flowControlSettings (FlowControlSettings-to-edn (.getFlowControlSettings arg)))
    (some? (.getIsEnabled arg)) (assoc :isEnabled (.getIsEnabled arg))))

(defn ^BatchingSettings BatchingSettings-from-edn [arg]
  (let [builder (BatchingSettings/newBuilder)]
    (when-some [v (:elementCountThreshold arg)] (.setElementCountThreshold builder v))
    (when-some [v (:requestByteThreshold arg)] (.setRequestByteThreshold builder v))
    (when-some [v (:delayThreshold arg)] (.setDelayThresholdDuration builder (Duration/ofSeconds v)))
    (when-some [v (:flowControlSettings arg)] (.setFlowControlSettings builder (FlowControlSettings-from-edn v)))
    (when-some [v (:isEnabled arg)] (.setIsEnabled builder v))
    (.build builder)))
