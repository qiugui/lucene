 package com.lucene.utils.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.junit.Test;

import com.lucene.myAnalyzer.QgSimilarAnalyzer;
import com.lucene.myAnalyzer.QgStopAnalyzer;
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
	 public void test05() {
		 Analyzer a1 = new QgSimilarAnalyzer();
		 
		 String text = "我来自中国安徽宿州埇桥区";
		 
		 AnalyzerUtil.displayAllTokenInfo(text, a1);
	 }
}

 