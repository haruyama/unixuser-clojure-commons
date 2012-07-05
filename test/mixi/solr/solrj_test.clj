(ns mixi.solr.solrj-test
  (:use clojure.test))

(require 'mixi.solr.solrj)

;(defn- extract-field [results field]
;  (map (fn [r]
;         (try
;           (. r getFieldValue field)
;           (catch Exception e "")))
;       results))

;(deftest solrj
;         (let
;           [
;            solr-query (-> (org.apache.solr.client.solrj.SolrQuery. "*:*")
;                         (.setRows (int 2)))
;            server (org.apache.solr.client.solrj.impl.HttpSolrServer. "http://localhost:8983/solr/")
;            ]
;           (try
;             (print (extract-field (mixi.solr.solrj/get-results server solr-query) "content"))
;             (finally (. server shutdown))
;           )))

