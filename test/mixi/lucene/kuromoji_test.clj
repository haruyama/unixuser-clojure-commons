(ns mixi.lucene.kuromoji-test
  (:use clojure.test))

(require 'mixi.lucene.kuromoji)

(deftest tokenize
         (let [factory (mixi.lucene.kuromoji/get-tokenizer-factory)]
           (is (= ["私" "の" "名前" "は" "中野" "です"] (mixi.lucene.kuromoji/tokenize factory "私の名前は中野です")))
           (is (= ["関西国際空港"] (mixi.lucene.kuromoji/tokenize factory "関西国際空港")))
           )
         (let [factory (mixi.lucene.kuromoji/get-tokenizer-factory :mode "SEARCH")]
           (is (= ["私" "の" "名前" "は" "中野" "です"] (mixi.lucene.kuromoji/tokenize factory "私の名前は中野です")))
           (is (= ["関西" "関西国際空港" "国際" "空港"] (mixi.lucene.kuromoji/tokenize factory "関西国際空港")))
           ))
