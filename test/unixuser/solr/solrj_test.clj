(ns unixuser.solr.solrj-test
  (:use [clojure.test :only (deftest is)])
  (:require [unixuser.solr.solrj]))

(deftest test-make-query
         (let [query (unixuser.solr.solrj/make-query nil 0 0 [["timestamp" "asc"] ["score" "desc"]])]
           (is (= "*:*" (. query getQuery)))
           (is (= 0 (. query getStart)))
           (is (= 0 (. query getRows)))
           (is (= "timestamp"  (. (first (. query getSorts)) getItem)))
           (is (= "score"      (. (second (. query getSorts)) getItem))))

         (let [query (unixuser.solr.solrj/make-query "hoge:lupin" 20 10 nil)]
           (is (= "hoge:lupin" (. query getQuery)))
           (is (= 20 (. query getStart)))
           (is (= 10 (. query getRows))))

         (let [query (unixuser.solr.solrj/make-query nil 30 10 nil [["facet" "true"] ["facet.field" "category_id"]])]
           (is (= "*:*" (. query getQuery)))
           (is (= 30 (. query getStart)))
           (is (= 10 (. query getRows)))
           (is (= "category_id" (first (. query getFacetFields)))))

         (let [query (unixuser.solr.solrj/make-query nil 30 10 nil [["facet" "true"] ["facet.field" ["category_id" "date"]]] [["hl" "true"] ["hl.fl" ["body" "title"]]])]
           (is (= "*:*" (. query getQuery)))
           (is (= 30 (. query getStart)))
           (is (= 10 (. query getRows)))
           (is (= "category_id" (first (. query getFacetFields))))
           (is (= "date" (second (. query getFacetFields))))
           (is (= "body" (first (. query getHighlightFields))))
           (is (= "title" (second (. query getHighlightFields))))))
