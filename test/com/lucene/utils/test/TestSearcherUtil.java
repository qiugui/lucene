 package com.lucene.utils.test;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.junit.Before;
import org.junit.Test;

import com.lucene.utils.SearcherUtil;

 public class TestSearcherUtil {

	 private SearcherUtil su;
	 
	 @Before
	 public void init() {
		 su = new SearcherUtil();
	 }
	 
	 @Test
	 public void searchByTerm() {
		 su.searchByTerm("content", "i", 3);
	 }
	 
	 @Test
	 public void searchByTermRange() {
		 su.searchByTermRange("id", "1", "4", 10);
	 }
	 
	 @Test
	 public void searchByNumRange() {
		 su.searchByNumRange("attach", 2, 4, 10);
	 }
	 
	 @Test
	 public void searchByPrefix() {
		 su.searchByPrefix("content", "s", 10);
	 }
	 
	 @Test
	 public void searchByWildcard() {
		 su.searchByWildcard("name", "J???", 10);
	 }
	 
	 @Test
	 public void searchByBoolean() {
		 su.searchByBoolean(10);
	 }

	 @Test
	 public void searchByPhrase() {
		 su.searchByPhrase(10);
	 }
	 
	 @Test
	 public void searchByFuzzy() {
		 su.searchByFuzzyQuery("name", "Mik", 10);
	 }
	 
	 @Test
	 public void searchByQueryParser() throws ParseException {
		 //1 创建QueryParser对象，默认搜索域是content，
		 //而且搜索域必须是已经分词过后的搜索域
		 QueryParser parser = new QueryParser("content", new StandardAnalyzer());
		 //2 创建Query
//		 Query query = parser.parse("like");
		 
//		 basketball football == basketball OR football 
//		   默认空格操作符为OR，通过以下方法，可以修改默认的分隔符
//		 parser.setDefaultOperator(Operator.AND);
//		 query = parser.parse("basketball football");
		 
//		 默认通配符不能放在首位，通过以下方法可以设置通配符允许放于首位
		 parser.setAllowLeadingWildcard(true);
//		 Query query = parser.parse("*football");
		 
//		 放置在字符串前的+表示包括，-表示不包括
//		 Query query = parser.parse("football -basketball");
		 
		 //如果其他的域是分词后的域，可以用以下方法，改变默认域
//		 Query query = parser.parse("name:Mike");
//		 Query query = parser.parse("+name:Jodan -football");
		 
		 //匹配一个区间，[]闭区间，{}开区间，其中TO必须是大写
		 Query query = parser.parse("id:[1 TO 3]");
//		 Query query = parser.parse("id:{1 TO 3}");
		 
		 //完全匹配
//		 Query query = parser.parse("\"I like football\"");
		 //完全匹配，且I 和 football中间有一个单词，类似模糊查询
//		 Query query = parser.parse("\"I football\"~1");
		 su.searchByQueryParser(query, 10);
	 }
}

 