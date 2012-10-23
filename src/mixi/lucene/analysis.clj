(ns mixi.lucene.analysis)

(import org.apache.lucene.analysis.util.TokenizerFactory)

(defmacro ^TokenizerFactory get-tokenizer-factory
  ([factory-name] `(get-tokenizer-factory ~factory-name {}))
  ([factory-name args]
    `(let [factory# (new ~factory-name)]
       (.setLuceneMatchVersion factory# org.apache.lucene.util.Version/LUCENE_40)
       (.init factory# ~args)
       factory#)))

(defn tokenize [^TokenizerFactory factory ^String sentence]
  (with-open [reader (java.io.StringReader. sentence)
              ts      (.create factory reader)]
    (let [termAtt (.getAttribute ts org.apache.lucene.analysis.tokenattributes.CharTermAttribute)]
      (loop [result []]
        (if (.incrementToken ts)
          (let [term (.toString termAtt)]
            (recur (conj result term)))
          result)))))
