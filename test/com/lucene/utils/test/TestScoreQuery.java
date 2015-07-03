 package com.lucene.utils.test;

import org.junit.Test;

import com.lucene.myCustomScore.QgScoreQuery;
 public class TestScoreQuery {

	 @Test
	 public void test01(){
		 QgScoreQuery query = new QgScoreQuery();
		 query.searchByScoreQuery();
	 }
}

 