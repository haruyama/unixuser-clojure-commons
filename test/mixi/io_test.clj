(ns mixi.io-test
  (:use clojure.test))

(require 'mixi.io)

(defn- temp-file
  [prefix suffix]
  (doto (java.io.File/createTempFile prefix suffix)
    (.deleteOnExit)))

(deftest test-spit-and-slurp
         (let [f (temp-file "mixi.io" "test")]
           (mixi.io/spit-file f "foobar")
           (is (= "foobar" (mixi.io/slurp-file f)))
           (mixi.io/spit-file f "foobar" :encoding "UTF-16")
           (is (= "foobar" (mixi.io/slurp-file f :encoding "UTF-16")))))
