(ns gcp.auth
  (:require [gcp.global :as global])
  (:import (com.google.auth Credentials)))

(def registry
  {::Credentials [:fn #(instance? Credentials %)]})

(global/include! registry)