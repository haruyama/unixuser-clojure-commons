(ns unixuser.io-test
  (:use clojure.test)
  (:require [unixuser.io]))

(defn- temp-file
  [prefix suffix]
  (doto (java.io.File/createTempFile prefix suffix)
    (.deleteOnExit)))

(deftest test-spit-and-slurp
         (let [f (temp-file "unixuser.io" "test")]
           (unixuser.io/spit-file f "foobar")
           (is (= "foobar" (unixuser.io/slurp-file f)))
           (unixuser.io/spit-file f "foobar" :encoding "UTF-16")
           (is (= "foobar" (unixuser.io/slurp-file f :encoding "UTF-16")))))
