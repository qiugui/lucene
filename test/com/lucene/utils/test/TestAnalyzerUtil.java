 package com.lucene.utils.test;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import com.chenlb.mmseg4j.analysis.ComplexAnalyzer;
import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import com.lucene.myAnalyzer.ComplexSimilarWords;
import com.lucene.myAnalyzer.QgSimilarAnalyzer;
import com.lucene.myAnalyzer.QgStopAnalyzer;
import com.lucene.myAnalyzer.SimpleSimilarWords;
import com.lucene.utils.AnalyzerUtil;
 public class TestAnalyzerUtil {

	 @Test
	 public void test01() {
		 Analyzer a1 = new StandardAnalyzer();
		 Analyzer a2 = new StopAnalyzer();
		 Analyzer a3 = new SimpleAnalyzer();
		 Analyzer a4 = new WhitespaceAnalyzer();
		 
		 String text = "this is my house, I am from SuZhou AnHui. "+
		 "My email is qiugui0557@sina.com. My qq is 123456.";
		 
		 AnalyzerUtil.displayToken(text, a1);
		 AnalyzerUtil.displayToken(text, a2);
		 AnalyzerUtil.displayToken(text, a3);
		 AnalyzerUtil.displayToken(text, a4);
	 }
	 
	 @Test
	 public void test02() {
		 Analyzer a1 = new StandardAnalyzer();
		 Analyzer a2 = new StopAnalyzer();
		 Analyzer a3 = new SimpleAnalyzer();
		 Analyzer a4 = new WhitespaceAnalyzer();
		 Analyzer a5 = new SmartChineseAnalyzer();
		 
		 String text = "我来自中国安徽宿州埇桥区。";
		 
		 AnalyzerUtil.displayToken(text, a1);
		 AnalyzerUtil.displayToken(text, a2);
		 AnalyzerUtil.displayToken(text, a3);
		 AnalyzerUtil.displayToken(text, a4);
		 AnalyzerUtil.displayToken(text, a5);
	 }
	 
	 @Test
	 public void test03() {
		 Analyzer a1 = new StandardAnalyzer();
		 Analyzer a2 = new StopAnalyzer();
		 Analyzer a3 = new SimpleAnalyzer();
		 Analyzer a4 = new WhitespaceAnalyzer();
		 
		 String text = "how are you thank you";
		 
		 AnalyzerUtil.displayAllTokenInfo(text, a1);
		 AnalyzerUtil.displayAllTokenInfo(text, a2);
		 AnalyzerUtil.displayAllTokenInfo(text, a3);
		 AnalyzerUtil.displayAllTokenInfo(text, a4);
	 }
	 
	 @Test
	 public void test04() {
		 Analyzer a1 = new QgStopAnalyzer(new String[]{"hate"});
		 Analyzer a2 = new QgStopAnalyzer();
		 Analyzer a3 = new StandardAnalyzer();
		 String text = "how are you thank you, I hate you";
		 AnalyzerUtil.displayToken(text, a1);
		 AnalyzerUtil.displayToken(text, a2);
		 AnalyzerUtil.displayToken(text, a3);
	 }
	 
	 @Test
	 public void test05() throws IOException {
		 //同义词分词器
		 Analyzer a1 = new QgSimilarAnalyzer(new ComplexSimilarWords());
//		 Analyzer a2 = new MMSegAnalyzer("D:\\软件\\lucene-5.2.0-jar\\data");
//		 Analyzer a3 = new ComplexAnalyzer("D:\\软件\\lucene-5.2.0-jar\\data");
		 String text = "我来自中国安徽宿州埇桥区";
		 Directory directory = new RAMDirectory();
		 IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(a1));
		 Document doc = new Document();
		 doc.add(new TextField("content", text, Store.YES));
		 writer.addDocument(doc);
		 writer.close();
		 IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
		 Query query = new TermQuery(new Term("content", "皖"));
		 TopDocs tds = searcher.search(query, 10);
		 Document result = searcher.doc(tds.scoreDocs[0].doc);
		 System.out.println(result.get("content"));
//		 AnalyzerUtil.displayAllTokenInfo(text, a1);
	 }
}

 