 package com.lucene.utils.test;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import com.lucene.highlighter.FirstTika;
 public class TestFirstTika {

	 @Test
	 public void test01() {
		 FirstTika ft = new FirstTika();
		 ft.index();
	 }
	 
	 @Test
	 public void testQuery() {
		 query("环境变量", 100);
	 }
	 
	 @Test
	 public void test02() {
		 FirstTika ft = new FirstTika();
		 System.out.println(ft.file2Txt());
	 }
	 
	 @Test
	 public void test03() {
		 FirstTika ft = new FirstTika();
		 System.out.println(ft.tikaTool());
	 }
	 private void query(String queryStr, int num) {
		 IndexReader reader = null;
		 try {
			reader = DirectoryReader.open(FSDirectory.open(Paths.get("D:\\lucene\\indexPath03", new String[0])));
			Query query = new TermQuery(new Term("content", queryStr));
			IndexSearcher searcher = new IndexSearcher(reader);
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了："+tds.totalHits+"条");
			for (ScoreDoc sd : tds.scoreDocs) {
				int docId = sd.doc;
				Document doc = searcher.doc(docId);
				System.out.println("[docId:"+docId+"] "+doc.get("content")+"--->"+doc.get("fileName"));
			}
		} catch (IOException e) {
			 e.printStackTrace();
		} finally {
			if (null != reader)
				try {
					reader.close();
				} catch (IOException e) {
					 e.printStackTrace();
				}
		}
	}
}

 