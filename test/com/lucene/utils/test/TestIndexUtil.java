 package com.lucene.utils.test;

import org.junit.Before;
import org.junit.Test;

import com.lucene.utils.IndexUtil;
 public class TestIndexUtil {

	 IndexUtil iu = null;
	 
	 @Before
	 public void beforeTest() {
		 if (iu == null) {
			 iu = new IndexUtil();
		 }
	 }
	 
	 @Test
	 public void testIndex() {
		 iu.index();
	 }
	 
	 @Test
	 public void testQuery() {
		 iu.query();
	 }
	 
	 @Test
	 public void testDelete() {
		 iu.delete();
	 }
	 
	 @Test
	 public void testUnDelete() {
		iu.undelete();
	}
	 
	 @Test
	 public void testForceMergeDelete() {
		iu.forceMergeDeletes();
	}
	 
	 @Test
	 public void testMerge() {
		iu.merge();
	}
	 
	 @Test
	 public void testUpdate() {
		iu.update();
	}
	 
	 @Test
	 public void testSearch() {
		for (int i=0;i<5;i++){
			iu.search();
			System.out.println();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				 e.printStackTrace();
			}
		}
	 }
}

 