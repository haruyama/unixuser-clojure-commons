(ns unixuser.lucene.fst-test
  (:use clojure.test)
  (:require [unixuser.lucene.fst])
  (:import [org.apache.lucene.util BytesRef CharsRef IntsRef]
           [org.apache.lucene.util.fst Builder CharSequenceOutputs FST FST$INPUT_TYPE Outputs PositiveIntOutputs Util]))

(deftest build-fst1
         (let [scratch-ints  (IntsRef.)
               scratch-bytes (BytesRef.)
               fst (unixuser.lucene.fst/build-fst (sorted-map "cat" 5 "dogs" 12 "dog" 7) FST$INPUT_TYPE/BYTE2 (PositiveIntOutputs/getSingleton)
                                              (fn [k] (unixuser.lucene.fst/to-utf16 k scratch-ints)) identity)]
           (is (= 7 (unixuser.lucene.fst/get-output fst (unixuser.lucene.fst/to-utf16 "dog" scratch-ints))))
           (is (= "cat" (.utf8ToString (Util/toBytesRef (unixuser.lucene.fst/get-by-output fst 5) scratch-bytes))))
           ))

(deftest build-fst2
         (let [scratch-ints  (IntsRef.)
               scratch-bytes (BytesRef.)
               fst (unixuser.lucene.fst/build-fst (sorted-map "cat" "dance" "dogs" "bow" "dog" "bowbow") FST$INPUT_TYPE/BYTE2 (CharSequenceOutputs/getSingleton)
                                              (fn [k] (unixuser.lucene.fst/to-utf16 k scratch-ints))
                                              (fn [v] (CharsRef. v)))]
           (is (= (CharsRef. "bowbow") (unixuser.lucene.fst/get-output fst (unixuser.lucene.fst/to-utf16 "dog" scratch-ints))))))

(deftest build-byte2-positiveint-fst
         (let [scratch-ints  (IntsRef.)
               scratch-bytes (BytesRef.)
               fst (unixuser.lucene.fst/build-byte2-positiveint-fst (sorted-map "cat" 5 "dogs" 12 "dog" 7))]
           (is (= 7 (unixuser.lucene.fst/get-output fst (unixuser.lucene.fst/to-utf16 "dog" scratch-ints))))
           (is (= "dogs" (.utf8ToString (Util/toBytesRef (unixuser.lucene.fst/get-by-output fst 12) scratch-bytes))))
           ))

(deftest build-byte2-fsa
         (let [scratch-ints  (IntsRef.)
               fsa (unixuser.lucene.fst/build-byte2-fsa ["東方不敗" "ルパン"] )]
           (is ((complement nil?) (unixuser.lucene.fst/get-output fsa (unixuser.lucene.fst/to-utf16 "東方不敗" scratch-ints))))
           (is (nil? (unixuser.lucene.fst/get-output fsa (unixuser.lucene.fst/to-utf16 "東方" scratch-ints))))
           ))
