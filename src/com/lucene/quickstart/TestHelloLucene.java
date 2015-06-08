 package com.lucene.quickstart;

import org.junit.Test;
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

 