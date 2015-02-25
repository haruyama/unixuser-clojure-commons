(ns unixuser.lucene.analysis-test
  (:use [clojure.test :only (deftest is)])
  (:require [unixuser.lucene.analysis])
  (:import org.apache.lucene.analysis.util.AbstractAnalysisFactory))

(deftest tokenize-test
         (let [resource-loader (org.apache.solr.core.SolrResourceLoader. "." (.getClassLoader org.apache.lucene.analysis.ja.JapaneseTokenizerFactory))]
           (let [factory (org.apache.lucene.analysis.ja.JapaneseTokenizerFactory. (java.util.HashMap.))]
             (.inform factory resource-loader)
             (is (= ["私" "の" "名前" "は" "中野" "です"] (unixuser.lucene.analysis/tokenize factory "私の名前は中野です")))
             (is (= ["関西" "関西国際空港" "国際" "空港"] (unixuser.lucene.analysis/tokenize factory "関西国際空港"))))

           (let [factory (org.apache.lucene.analysis.ja.JapaneseTokenizerFactory. (doto (java.util.HashMap.) (.put "mode" "NORMAL")))]
             (.inform factory resource-loader)
             (is (= ["私" "の" "名前" "は" "中野" "です"] (unixuser.lucene.analysis/tokenize factory "私の名前は中野です")))
             (is (= ["関西国際空港"] (unixuser.lucene.analysis/tokenize factory "関西国際空港")))))

         (let [factory (org.apache.lucene.analysis.standard.StandardTokenizerFactory. (doto (java.util.HashMap.) (.put AbstractAnalysisFactory/LUCENE_MATCH_VERSION_PARAM "LUCENE_50")))]
           (is (= ["私" "の" "名" "前" "は" "中" "野" "で" "す"] (unixuser.lucene.analysis/tokenize factory "私の名前は中野です")))
           (is (= ["関" "西" "国" "際" "空" "港"] (unixuser.lucene.analysis/tokenize factory "関西国際空港")))))

(defmulti  get-value class)
(defmethod get-value org.apache.lucene.analysis.tokenattributes.CharTermAttribute        [attribute] (str attribute))
(defmethod get-value org.apache.lucene.analysis.ja.tokenattributes.PartOfSpeechAttribute [attribute] (.getPartOfSpeech attribute))

(deftest tokenize-test-2
         (let [resource-loader (org.apache.solr.core.SolrResourceLoader. "." (.getClassLoader org.apache.lucene.analysis.ja.JapaneseTokenizerFactory))]
           (let [factory (org.apache.lucene.analysis.ja.JapaneseTokenizerFactory. (java.util.HashMap.))]
             (.inform factory resource-loader)
             (is (= [["私" "名詞-代名詞-一般"]  ["の" "助詞-連体化"]  ["名前" "名詞-一般"]  ["は" "助詞-係助詞"]  ["中野" "名詞-固有名詞-地域-一般"]  ["です" "助動詞"]]
                    (unixuser.lucene.analysis/tokenize factory "私の名前は中野です" [org.apache.lucene.analysis.tokenattributes.CharTermAttribute org.apache.lucene.analysis.ja.tokenattributes.PartOfSpeechAttribute] get-value))))))

(deftest tokenizer-chain-test
         (let [stf (org.apache.lucene.analysis.standard.StandardTokenizerFactory. (doto (java.util.HashMap.) (.put AbstractAnalysisFactory/LUCENE_MATCH_VERSION_PARAM "LUCENE_46")))
               cwff (org.apache.lucene.analysis.cjk.CJKWidthFilterFactory. (java.util.HashMap.))
               lcff (org.apache.lucene.analysis.core.LowerCaseFilterFactory. (doto (java.util.HashMap.) (.put AbstractAnalysisFactory/LUCENE_MATCH_VERSION_PARAM "LUCENE_46")))
               cbff (org.apache.lucene.analysis.cjk.CJKBigramFilterFactory. (java.util.HashMap.))]
           (with-open [tokenizer-chain (org.apache.solr.analysis.TokenizerChain. stf (into-array org.apache.lucene.analysis.util.TokenFilterFactory [cwff lcff cbff]))]
             (is (= ["lupin" "the" "third"] (unixuser.lucene.analysis/tokenize tokenizer-chain "Lupin The Third")))
             (is (= ["佐" "々" "木未" "未来" "来オ" "オフ" "フィ" "ィシ" "シャ" "ャル" "ルブ" "ブロ" "ログ"] (unixuser.lucene.analysis/tokenize tokenizer-chain "佐々木未来ｵﾌｨｼｬﾙﾌﾞﾛｸﾞ"))))))
