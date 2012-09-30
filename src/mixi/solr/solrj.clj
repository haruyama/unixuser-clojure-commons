(ns mixi.solr.solrj)

(import org.apache.solr.client.solrj.SolrQuery)
(import org.apache.solr.client.solrj.SolrQuery$ORDER)

(defn ^SolrQuery make-query
  [^String q start rows sort-params & param-lists]
  (let [query (new SolrQuery (if q q "*:*"))]
    (-> query
      (.setStart (Integer. start))
      (.setRows  (Integer. rows)))
    (doseq [sort-param sort-params]
      (.addSortField query (first sort-param)
                     (if (= (second sort-param) "asc") SolrQuery$ORDER/asc SolrQuery$ORDER/desc)))
    (doseq [param-list param-lists]
      (doseq [param param-list]
        (.setParam query (first param)
         (if (coll? (second param))
           (into-array (second param))
           (into-array [(second param)])))))
    query))
