(ns mixi.solr.kuromoji)

(defn ^org.apache.solr.analysis.JapaneseTokenizerFactory get-tokenizer-factory
  [& {:keys [mode userDictionary userDictionaryEncoding]
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
          ts (. factory create reader)
          termAtt (. ts getAttribute org.apache.lucene.analysis.tokenattributes.CharTermAttribute)
          ]
      (letfn [(tokenize-iter [result]
                (loop [result result]
                  (if (. ts incrementToken)
                    (let [term (. termAtt toString)]
                      (recur (conj result term)))
                    result))
                )]
        (tokenize-iter [])))))
