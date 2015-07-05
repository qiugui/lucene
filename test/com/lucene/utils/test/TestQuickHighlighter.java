 package com.lucene.utils.test;

import org.junit.Test;

import com.lucene.highlighter.QuickHighlight;
 public class TestQuickHighlighter {

	@Test 
	 public void test01() {
		 QuickHighlight qhl = new QuickHighlight();
		 System.out.println(qhl.highlight());
	 }
}

 