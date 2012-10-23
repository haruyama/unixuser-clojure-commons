(ns mixi.lucene.analysis)

(import org.apache.lucene.analysis.util.AbstractAnalysisFactory)
(import org.apache.lucene.analysis.util.TokenizerFactory)
(import org.apache.lucene.analysis.Analyzer)

(defmacro ^AbstractAnalysisFactory get-analysis-factory
  ([factory-name] `(get-analysis-factory ~factory-name {}))
  ([factory-name args]
    `(let [factory# (new ~factory-name)]
       (.setLuceneMatchVersion factory# org.apache.lucene.util.Version/LUCENE_40)
       (.init factory# ~args)
       factory#)))

(defmulti createTokenStream (fn  [i & other]  (class i)))
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
          (let [term (.toString termAtt)]
            (recur (conj result term)))
          (do
            (.end ts)
            result))))))

