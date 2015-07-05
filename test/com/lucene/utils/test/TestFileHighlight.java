 package com.lucene.utils.test;

import org.junit.Test;

import com.lucene.highlighter.FileHighlight;
import com.lucene.highlighter.FileIndexUtil;
 public class TestFileHighlight {

	 @Test
	 public void index() {
		 FileIndexUtil.index(true);
	 }
	 
	 @Test
	 public void test01() {
		 FileHighlight fh = new FileHighlight();
		 fh.queryByHighlight("考研", 50);
	 }
}

 