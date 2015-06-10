 package com.lucene.utils.test;

import org.junit.Test;

import com.lucene.utils.IndexUtil;
 public class TestIndexUtil {

	 @Test
	 public void testIndex() {
		 IndexUtil iu = new IndexUtil();
		 iu.index();
	 }
	 
	 @Test
	 public void testQuery() {
		 IndexUtil iu = new IndexUtil();
		 iu.query();
	 }
	 
	 @Test
	 public void testDelete() {
		 IndexUtil iu = new IndexUtil();
		 iu.delete();
	 }
}

 