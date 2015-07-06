 package com.lucene.nrt.test;

import org.junit.Before;
import org.junit.Test;

import com.lucene.nrt.IndexUtil;


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
	 public void testSearch() {
		for (int i=0;i<5;i++){
			iu.search();
			System.out.println();
			iu.delete();
//			if (i == 2) {
//				iu.update();
//			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				 e.printStackTrace();
			}
		}
	 }
}

 