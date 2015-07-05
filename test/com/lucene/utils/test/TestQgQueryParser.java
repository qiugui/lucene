 package com.lucene.utils.test;

import org.junit.Test;

import com.lucene.myQueryParser.QgQueryParserUtil;
 public class TestQgQueryParser {

	 @Test
	 public void test01(){
		 QgQueryParserUtil parserUtil = new QgQueryParserUtil();
		 parserUtil.search("java like~1");
	 }
	 
	 @Test
	 public void test02(){
		 QgQueryParserUtil parserUtil = new QgQueryParserUtil();
		 parserUtil.search("");
	 }
}

 