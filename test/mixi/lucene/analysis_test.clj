(ns mixi.lucene.analysis-test
  (:use clojure.test)
  (:require [mixi.lucene.analysis])
  (:import org.apache.lucene.analysis.util.AbstractAnalysisFactory))

(deftest tokenize-test
         (let [resource-loader (org.apache.solr.core.SolrResourceLoader. "." (.getClassLoader org.apache.lucene.analysis.ja.JapaneseTokenizerFactory))]
           (let [factory (org.apache.lucene.analysis.ja.JapaneseTokenizerFactory. (java.util.HashMap.))]
             (.inform factory resource-loader)
             (is (= ["私" "の" "名前" "は" "中野" "です"] (mixi.lucene.analysis/tokenize factory "私の名前は中野です")))
             (is (= ["関西" "関西国際空港" "国際" "空港"] (mixi.lucene.analysis/tokenize factory "関西国際空港"))))

           (let [factory (org.apache.lucene.analysis.ja.JapaneseTokenizerFactory. (doto (java.util.HashMap.) (.put "mode" "NORMAL")))]
             (.inform factory resource-loader)
             (is (= ["私" "の" "名前" "は" "中野" "です"] (mixi.lucene.analysis/tokenize factory "私の名前は中野です")))
             (is (= ["関西国際空港"] (mixi.lucene.analysis/tokenize factory "関西国際空港")))))

         (let [factory (org.apache.lucene.analysis.standard.StandardTokenizerFactory. (doto (java.util.HashMap.) (.put AbstractAnalysisFactory/LUCENE_MATCH_VERSION_PARAM "LUCENE_44")))]
           (is (= ["私" "の" "名" "前" "は" "中" "野" "で" "す"] (mixi.lucene.analysis/tokenize factory "私の名前は中野です")))
           (is (= ["関" "西" "国" "際" "空" "港"] (mixi.lucene.analysis/tokenize factory "関西国際空港")))))

(deftest tokenizer-chain-test
         (let [stf (org.apache.lucene.analysis.standard.StandardTokenizerFactory. (doto (java.util.HashMap.) (.put AbstractAnalysisFactory/LUCENE_MATCH_VERSION_PARAM "LUCENE_44")))
               cwff (org.apache.lucene.analysis.cjk.CJKWidthFilterFactory. (java.util.HashMap.))
               lcff (org.apache.lucene.analysis.core.LowerCaseFilterFactory. (doto (java.util.HashMap.) (.put AbstractAnalysisFactory/LUCENE_MATCH_VERSION_PARAM "LUCENE_44")))
               cbff (org.apache.lucene.analysis.cjk.CJKBigramFilterFactory. (java.util.HashMap.))]
           (with-open [tokenizer-chain (org.apache.solr.analysis.TokenizerChain. stf (into-array org.apache.lucene.analysis.util.TokenFilterFactory [cwff lcff cbff]))]
             (is (= ["lupin" "the" "third"] (mixi.lucene.analysis/tokenize tokenizer-chain "Lupin The Third")))
             (is (= ["佐" "々" "木未" "未来" "来オ" "オフ" "フィ" "ィシ" "シャ" "ャル" "ルブ" "ブロ" "ログ"] (mixi.lucene.analysis/tokenize tokenizer-chain "佐々木未来ｵﾌｨｼｬﾙﾌﾞﾛｸﾞ"))))))
