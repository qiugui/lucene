 package com.lucene.utils.test;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

import com.lucene.myFilter.QgFilter;
import com.lucene.utils.FileIndexUtil;
 public class TestQgFilter {

	 @Test
	 public void index() {
		 FileIndexUtil.index(true);
	 }
	 
	 @Test
	 public void test01(){
		 searchFilter("java", null);
	 }
	 
	 @Test
	 public void test02() {
		 Filter qgFilter = new 	QgFilter("id");
		 searchFilter("java", qgFilter);
	 }
	 
	 @SuppressWarnings("deprecation")
	public void searchFilter(String qStr, Filter filter) {
		 try {
				IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FileIndexUtil.getDirectory()));
				Query query = new TermQuery(new Term("content", qStr));
				TopDocs tds = null;
				if (null == filter) {
					tds = searcher.search(query, 50);
				} else {
					tds = searcher.search(query, filter, 50);
				}
				
				for (ScoreDoc sd : tds.scoreDocs) {
					Document doc = searcher.doc(sd.doc);
					System.out.println(sd.doc + ":(" + 
							sd.score + ")" + " id:" + doc.get("id") + " [" + doc.get("fileName") +
							"] 【" + doc.get("filePath") +
							"】---Score:" + doc.get("score") + "---Size:" + doc.get("size")+" 【" + doc.get("date") +
							"】");
				}
				
			} catch (IOException e) {
				 e.printStackTrace();
			}
		 }
}

 