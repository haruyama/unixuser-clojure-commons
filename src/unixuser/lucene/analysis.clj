(ns unixuser.lucene.analysis
  (:import org.apache.lucene.analysis.util.TokenizerFactory
           org.apache.lucene.analysis.Analyzer))

(defmulti  create-token-stream (fn  [i & _]  (class i)))
(defmethod create-token-stream TokenizerFactory [factory reader]
  (.create factory reader))
(defmethod create-token-stream Analyzer [analyzer reader]
  (.tokenStream analyzer "dummy" reader))

(defn tokenize

  ([analyzer-or-factory ^String sentence]
                 (tokenize analyzer-or-factory sentence [org.apache.lucene.analysis.tokenattributes.CharTermAttribute] str))

  ([analyzer-or-factory ^String sentence attribute-classes get-value]
    (with-open [reader (java.io.StringReader. sentence)
                ts      (create-token-stream analyzer-or-factory reader)]
      (let [attributes (mapv #(.getAttribute ts %) attribute-classes)]
        (.reset ts)
        (loop [result []]
          (if (.incrementToken ts)
            (let [values (mapv get-value attributes)]
              (recur (conj result values)))
            (do
              (.end ts)
              (if (= (count attribute-classes) 1)
                (flatten result)
                result))))))))
