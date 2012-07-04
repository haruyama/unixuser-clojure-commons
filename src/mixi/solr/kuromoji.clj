(ns mixi.solr.kuromoji)

(defn ^org.apache.solr.analysis.JapaneseTokenizerFactory get-tokenizer-factory [& {:keys [mode userDictionary userDictionaryEncoding]
                                                                                   :or
                                                                                   {
                                                                                    mode "NORMAL"
                                                                                    userDictionary nil
                                                                                    userDictionaryEncoding "UTF-8"
                                                                                    }
                                                                                   }]
  (let [
        args (new java.util.HashMap)
        ]
    (.put args "mode" mode)
    (if userDictionary
      (.put args "userDictionary" userDictionary)
      (.put args "userDictionaryEncoding" userDictionaryEncoding)
      )
    (let [
          factory (org.apache.solr.analysis.JapaneseTokenizerFactory.)
          loader  (org.apache.solr.core.SolrResourceLoader. "." (.getClassLoader org.apache.solr.analysis.JapaneseTokenizerFactory))
          ]
      (. factory init args)
      (. factory inform loader)
      factory
      )))

(defn tokenize [factory sentence]
  (with-open [reader (new java.io.StringReader sentence)]
    (let [
          ts (.create factory reader)
          termAtt (.getAttribute ts org.apache.lucene.analysis.tokenattributes.CharTermAttribute)
          ]
      (letfn [(tokenize-iter [result]
                (loop [result result]
                  (if (.incrementToken ts)
                    (let [term (.toString termAtt)]
                      (recur (conj result term)))
                    result))
                )]
        (tokenize-iter [])))))
