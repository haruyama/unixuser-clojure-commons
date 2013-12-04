(ns unixuser.math-test
  (:use clojure.test)
  (:require [unixuser.math]))

(deftest log-test
         (is (= 1.0 (unixuser.math/log2 2)))
         (is (= 4.0 (unixuser.math/log2 16.0))))
