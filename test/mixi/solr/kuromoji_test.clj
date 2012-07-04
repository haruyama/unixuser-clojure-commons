(ns mixi.solr.kuromoji-test
  (:use clojure.test))

(require 'mixi.solr.kuromoji)

(deftest tokenize
         (let [factory (mixi.solr.kuromoji/get-tokenizer-factory)]
           (is (= ["私" "の" "名前" "は" "中野" "です"] (mixi.solr.kuromoji/tokenize factory "私の名前は中野です")))
           (is (= ["関西国際空港"] (mixi.solr.kuromoji/tokenize factory "関西国際空港")))
           )
         (let [factory (mixi.solr.kuromoji/get-tokenizer-factory :mode "SEARCH")]
           (is (= ["私" "の" "名前" "は" "中野" "です"] (mixi.solr.kuromoji/tokenize factory "私の名前は中野です")))
           (is (= ["関西" "関西国際空港" "国際" "空港"] (mixi.solr.kuromoji/tokenize factory "関西国際空港")))
           ))
