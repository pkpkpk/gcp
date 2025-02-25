(ns gcp.dev.asm
  (:import [org.objectweb.asm ClassReader ClassVisitor]))

(defn class-bytes [^Class cls]
  (with-open [is (.getResourceAsStream cls (str (.getSimpleName cls) ".class"))]
    (let [os (java.io.ByteArrayOutputStream.)]
      (loop []
        (let [buf (byte-array 4096)
              len (.read is buf)]
          (when (pos? len)
            (.write os buf 0 len)
            (recur))))
      (.toByteArray os))))

(defn all-method-info [cls]
  (let [acc (atom [])]
    (doto (ClassReader. (class-bytes cls))
      (.accept
        (proxy [ClassVisitor] [org.objectweb.asm.Opcodes/ASM9]
          (visitMethod [access name desc signature exceptions]
            (swap! acc conj
                   {:name       name
                    :desc       desc
                    :signature  signature
                    :access     access
                    :exceptions exceptions})
            (proxy-super visitMethod access name desc signature exceptions)))
        0))
    @acc))
