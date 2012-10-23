(ns mixi.lucene.analysis-test
  (:use clojure.test))

(require 'mixi.lucene.analysis)

(deftest tokenize
         (let [resource-loader (org.apache.solr.core.SolrResourceLoader. "." (.getClassLoader org.apache.lucene.analysis.ja.JapaneseTokenizerFactory))]
           (let [factory (mixi.lucene.analysis/get-analysis-factory org.apache.lucene.analysis.ja.JapaneseTokenizerFactory)]
             (.inform factory resource-loader)
             (is (= ["私" "の" "名前" "は" "中野" "です"] (mixi.lucene.analysis/tokenize factory "私の名前は中野です")))
             (is (= ["関西" "関西国際空港" "国際" "空港"] (mixi.lucene.analysis/tokenize factory "関西国際空港")))
             )
           (let [factory (mixi.lucene.analysis/get-analysis-factory org.apache.lucene.analysis.ja.JapaneseTokenizerFactory {"mode" "NORMAL"})]
             (.inform factory resource-loader)
             (is (= ["私" "の" "名前" "は" "中野" "です"] (mixi.lucene.analysis/tokenize factory "私の名前は中野です")))
             (is (= ["関西国際空港"] (mixi.lucene.analysis/tokenize factory "関西国際空港")))
             )
           )
         (let [factory (mixi.lucene.analysis/get-analysis-factory org.apache.lucene.analysis.standard.StandardTokenizerFactory)]
           (is (= ["私" "の" "名" "前" "は" "中" "野" "で" "す"] (mixi.lucene.analysis/tokenize factory "私の名前は中野です")))
           (is (= ["関" "西" "国" "際" "空" "港"] (mixi.lucene.analysis/tokenize factory "関西国際空港")))
           )
         )

(deftest tokenizer-chain
         (let [stf (mixi.lucene.analysis/get-analysis-factory org.apache.lucene.analysis.standard.StandardTokenizerFactory)
               cwff (mixi.lucene.analysis/get-analysis-factory org.apache.lucene.analysis.cjk.CJKWidthFilterFactory)
               lcff (mixi.lucene.analysis/get-analysis-factory org.apache.lucene.analysis.core.LowerCaseFilterFactory)
               cbff (mixi.lucene.analysis/get-analysis-factory org.apache.lucene.analysis.cjk.CJKBigramFilterFactory)]
           (with-open [tokenizer-chain (org.apache.solr.analysis.TokenizerChain. stf (into-array org.apache.lucene.analysis.util.TokenFilterFactory [cwff lcff cbff]))]
             (is (= ["lupin" "the" "third"] (mixi.lucene.analysis/tokenize tokenizer-chain "Lupin The Third")))
             (is (= ["佐" "々" "木未" "未来" "来オ" "オフ" "フィ" "ィシ" "シャ" "ャル" "ルブ" "ブロ" "ログ"] (mixi.lucene.analysis/tokenize tokenizer-chain "佐々木未来ｵﾌｨｼｬﾙﾌﾞﾛｸﾞ")))
             )
           ))
