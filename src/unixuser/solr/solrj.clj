(ns unixuser.solr.solrj
  (:import [org.apache.solr.client.solrj SolrQuery SolrQuery$ORDER]))

(defn ^SolrQuery make-query
  [^String q start rows sort-params & param-lists]
  (let [query (new SolrQuery (if q q "*:*"))]
    (-> query
      (.setStart (Integer. #^int start))
      (.setRows  (Integer. #^int rows)))
    (doseq [sort-param sort-params]
      (.addSort query (first sort-param)
                     (if (= (second sort-param) "asc") SolrQuery$ORDER/asc SolrQuery$ORDER/desc)))
    (doseq [param-list param-lists]
      (doseq [param param-list]
        (.setParam query #^String (first param)
         (if (coll? (second param))
           (into-array (second param))
           (into-array [(second param)])))))
    query))
