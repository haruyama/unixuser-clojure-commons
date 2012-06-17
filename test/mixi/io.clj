(ns mixi.io
  (:use clojure.test))

(defn- temp-file
  [prefix suffix]
  (doto (java.io.File/createTempFile prefix suffix)
    (.deleteOnExit)))

(deftest test-spit-and-slurp
         (let [f (temp-file "mixi.io" "test")]
           (spit f "foobar")
           (is (= "foobar" (slurp f)))
           (spit f "foobar" :encoding "UTF-16")
           (is (= "foobar" (slurp f :encoding "UTF-16")))))
