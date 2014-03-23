(ns unixuser.math-test
  (:use [clojure.test :only (deftest are)])
  (:require [unixuser.math]))

(deftest log-test
         (are [x y]  (= x y)
              1.0 (unixuser.math/log2 2)
              4.0 (unixuser.math/log2 16.0)
           ))
