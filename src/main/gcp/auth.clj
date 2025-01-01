(ns gcp.auth
  (:import (com.google.auth Credentials)))

(def credentials-schema
  [:fn #(instance? Credentials %)])
