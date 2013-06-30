(ns mixi.math-test
  (:use clojure.test)
  (:require [mixi.math]))

(deftest log-test
         (is (= 1.0 (mixi.math/log2 2)))
         (is (= 4.0 (mixi.math/log2 16.0))))
