(ns unixuser.lucene.fst
  (:import [org.apache.lucene.util IntsRefBuilder]
           [org.apache.lucene.util.fst Builder FST FST$INPUT_TYPE NoOutputs Outputs PositiveIntOutputs Util]))

(defn to-utf16
  ([s] (let [scratch-ints (IntsRefBuilder.)]
         (to-utf16 s scratch-ints)))
  ([s scratch-ints]
    (Util/toUTF16 s scratch-ints)))

(defn build-fst [key-value-map input-type outputs convert-key convert-output]
  (let [builder       (Builder. input-type outputs)]
    (doseq [kv key-value-map]
      (.add builder (convert-key (key kv)) (convert-output (val kv))))
    (.finish builder)))

(defn build-byte2-positiveint-fst [key-value-map]
  (let [scratch-ints  (IntsRefBuilder.)]
    (build-fst key-value-map FST$INPUT_TYPE/BYTE2  (PositiveIntOutputs/getSingleton)
               (fn [k]  (to-utf16 k scratch-ints)) identity)))

(defn build-byte2-fsa [key-seq]
  (let [scratch-ints  (IntsRefBuilder.)
        output-object (Object.)
        key-value-map (apply sorted-map (interleave key-seq (repeat output-object))) ]
    (build-fst key-value-map FST$INPUT_TYPE/BYTE2  (NoOutputs/getSingleton)
               (fn [k]  (to-utf16 k scratch-ints)) identity)))


(defn get-output [^FST fst k]
  (Util/get fst k))

(defn get-by-output [^FST fst output]
  (Util/getByOutput fst output))

(defn to-dot [fst ^String filename same-rank label-states]
  (with-open [w (java.io.FileWriter. filename)]
    (Util/toDot fst w same-rank label-states)))
