(ns mixi.solr.solrj)


(defn get-results [server solr-query]
  (. (. server query solr-query) getResults))

