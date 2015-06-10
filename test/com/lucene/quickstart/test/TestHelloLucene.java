 package com.lucene.quickstart.test;

import org.junit.Test;

import com.lucene.quickstart.HelloLucene;
 public class TestHelloLucene {

	 @Test
	 public void testIndex() {
		 HelloLucene hl = new HelloLucene();
		 hl.index();
	 }
	 
	 @Test
	 public void testSearch() {
		HelloLucene hl = new HelloLucene();
		hl.search();
	}
}

 