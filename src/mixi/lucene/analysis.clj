(ns mixi.lucene.analysis
  (:import org.apache.lucene.analysis.util.TokenizerFactory
           org.apache.lucene.analysis.Analyzer))

(defmulti createTokenStream (fn  [i & _]  (class i)))
(defmethod createTokenStream TokenizerFactory [factory reader]
  (.create factory reader))
(defmethod createTokenStream Analyzer [analyzer reader]
  (.tokenStream analyzer "dummy" reader))

(defn tokenize [analyzer-or-factory ^String sentence]
  (with-open [reader (java.io.StringReader. sentence)
              ts      (createTokenStream analyzer-or-factory reader)]
    (let [termAtt (.getAttribute ts org.apache.lucene.analysis.tokenattributes.CharTermAttribute)]
      (.reset ts)
      (loop [result []]
        (if (.incrementToken ts)
          (let [term (str termAtt)]
            (recur (conj result term)))
          (do
            (.end ts)
            result))))))

