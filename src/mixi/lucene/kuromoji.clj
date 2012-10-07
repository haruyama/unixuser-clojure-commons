(ns mixi.lucene.kuromoji)

(defn ^org.apache.lucene.analysis.ja.JapaneseTokenizerFactory get-tokenizer-factory
  [& {:keys [^String mode ^String userDictionary ^String userDictionaryEncoding]
      :or
      {mode "NORMAL"
       userDictionary nil
       userDictionaryEncoding "UTF-8"}}]
  (let [args (java.util.HashMap.)]
    (.put args "mode" mode)
    (if userDictionary
      (.put args "userDictionary" userDictionary)
      (.put args "userDictionaryEncoding" userDictionaryEncoding))
    (let [factory (org.apache.lucene.analysis.ja.JapaneseTokenizerFactory.)
          loader  (org.apache.solr.core.SolrResourceLoader. "." (.getClassLoader org.apache.lucene.analysis.ja.JapaneseTokenizerFactory))]
      (.init factory args)
      (.inform factory loader)
      factory)))

(defn tokenize [^org.apache.lucene.analysis.ja.JapaneseTokenizerFactory factory ^String sentence]
  (with-open [reader (java.io.StringReader. sentence)]
    (let [ts      (.create factory reader)
          termAtt (.getAttribute ts org.apache.lucene.analysis.tokenattributes.CharTermAttribute)]
      (loop [result []]
        (if (.incrementToken ts)
          (let [term (.toString termAtt)]
            (recur (conj result term)))
          result)))))
