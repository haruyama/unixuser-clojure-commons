(ns mixi.lucene.kuromoji)


(defn ^org.apache.lucene.analysis.ja.JapaneseTokenizerFactory get-tokenizer-factory
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
          factory (org.apache.lucene.analysis.ja.JapaneseTokenizerFactory.)
          loader  (org.apache.solr.core.SolrResourceLoader. "." (.getClassLoader org.apache.lucene.analysis.ja.JapaneseTokenizerFactory))
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
