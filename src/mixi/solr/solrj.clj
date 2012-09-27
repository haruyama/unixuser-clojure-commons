(ns mixi.solr.solrj)

(import org.apache.solr.client.solrj.SolrQuery)
(import org.apache.solr.client.solrj.SolrQuery$ORDER)

(defn ^SolrQuery make-query
  [^String q start rows sort-params & param-lists]
  (let [query (if q
                (new SolrQuery q)
                (new SolrQuery "*:*")
                )
        ]
    (.setStart query (Integer. start))
    (.setRows query (Integer. rows))
    (doseq [sort-param sort-params]
      (if (= (second sort-param) "asc")
        (.addSortField query (first sort-param) SolrQuery$ORDER/asc)
        (.addSortField query (first sort-param) SolrQuery$ORDER/desc)))
    (doseq [param-list param-lists]
      ( doseq [param param-list]
        (if (coll? (second param))
          (.setParam query (first param) (into-array (second param)))
          (.setParam query (first param) (into-array [(second param)])))))
    query
    ))
