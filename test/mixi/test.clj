(ns mixi.test
  (:require [clojure.test]
            [clojure.tools.namespace]
            [kibit.driver]))

(defn run-all-tests []
  (doseq [n (clojure.tools.namespace/find-namespaces-in-dir (java.io.File. "test"))]
    (require n :reload))
  (clojure.test/run-all-tests #"mixi\..*"))

(defn run-kibit []
  (kibit.driver/run {:source-path "src"}))


