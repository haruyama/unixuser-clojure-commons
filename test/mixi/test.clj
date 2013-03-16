(ns mixi.test
  (:require [clojure.test]
            [clojure.tools.namespace]))

(defn run-all-tests []
  (doseq [n (clojure.tools.namespace/find-namespaces-in-dir (java.io.File. "test"))]
    (require n :reload))
  (clojure.test/run-all-tests #"mixi\..*"))
