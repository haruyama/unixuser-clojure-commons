(ns mixi.solr.solrj)


(defn get-results [server solr-query]
  (. (. server query solr-query) getResults))

(defn extract-field [results field]
  (map (fn [r]
         (try
           (. r getFieldValue field)
           ; if field does not exist
           (catch Exception e "")))
       results))
