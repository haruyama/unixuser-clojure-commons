(defproject haruyama/mixi-clojure-commons "0.2.0-SNAPSHOT"
            :description "a library for Clojure, used in mixi."
            :url "https://github.com/haruyama/mixi-clojure-commons"
            :license {:name "Eclipse Public License"
                      :url "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[org.clojure/clojure "1.5.1"]
                           [org.apache.solr/solr-core "4.4.0"]
                           [org.apache.solr/solr-solrj "4.4.0"]
                           [org.apache.lucene/lucene-analyzers-kuromoji "4.4.0"]]
            :profiles {:dev { :dependencies
                             [[org.clojure/tools.namespace "0.2.4"]
                              [jonase/kibit "0.0.8"]]}})
