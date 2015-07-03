 package com.lucene.utils.test;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermRangeFilter;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.BytesRef;
import org.junit.Before;
import org.junit.Test;

import com.lucene.utils.FileIndexUtil;
 @SuppressWarnings("deprecation")
public class TestSearch {

	 private static IndexReader reader = null;
	 
	 static {
		 try {
			reader = DirectoryReader.open(FileIndexUtil.getDirectory());
		} catch (IOException e) {
			 e.printStackTrace();
		}
	 }
	 
	 public IndexSearcher getSearcher() {
		 try {
			 if (reader == null) {
				reader = DirectoryReader.open(FileIndexUtil.getDirectory());
			 } else {
				 IndexReader tr = DirectoryReader.openIfChanged((DirectoryReader) reader);
				 if (tr != null) {
					 reader.close();
					 reader = tr;
				 }
			 }
			 return new IndexSearcher(reader);
		 } catch (IOException e) {
				 e.printStackTrace();
		 }
		 return null;
	 }
	 
	public void search(String qStr, Sort sort) {
		try {
			IndexSearcher searcher = getSearcher();
			QueryParser parser = new QueryParser("content", new StandardAnalyzer());
			Query query = parser.parse(qStr);
			TopDocs tds = null;
			if (null != sort) {
				tds = searcher.search(query, 50, sort);
			} else {
				tds = searcher.search(query, 50);
			}
			
			for (ScoreDoc sd : tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println(sd.doc + ":(" + 
						sd.score + ") [" + doc.get("fileName") +
						"] 【" + doc.get("filePath") +
						"】---Score:" + doc.get("score") + "---Size:" + doc.get("size")+" 【" + doc.get("date") +
						"】");
			}
			
		} catch (ParseException e) {
			 e.printStackTrace();
		} catch (IOException e) {
			 e.printStackTrace();
		}
	 }
	 
	 TestSearch ts;
	 
	 @Test
	 public void test01(){
//		 ts.search("java", Sort.INDEXORDER);
		 
//		 ts.search("java", Sort.RELEVANCE);
		 /**
		  * 这前使用的lucene4.7版本，程序正常，现在升级到5.1时，索引文件正常，但在搜索的时候，出现：
		  * IllegalStateException: unexpected docvalues type NONE" on fields  
		  * Use UninvertingReader or index with docvalues.
		  * 后才知道，是因为Sort排序对索引字段有了新的要求，即使用DocValuesField的字段才能进行排序。
		  *	查询doc文档，最终解决方法如下：
		  *	原来的代码：dfDocument.add(new LongField("id", Long.parseLong(id),Field.Store.YES));
		  *	现在的代码：dfDocument.add(new NumericDocValuesField("id",Long.parseLong(id)));  
		  *	替换后一切OK，如果你想在查询中获取ID的字段值，那就多加一条排序的字段，那保留原代码，然后加一条：
		  *	dfDocument.add(new NumericDocValuesField("sortid",Long.parseLong(id)));  
		  *	查询的时候，使用new Sort(new SortField("sortid", SortField.Type.LONG, true));作为排序。
		  */
//		 ts.search("java", new Sort(new SortField("sortFileName", SortField.Type.STRING)));
//		 ts.search("java", new Sort(new SortField("sortSize", SortField.Type.LONG)));
//		 ts.search("java", new Sort(new SortField("date", SortField.Type.LONG)));
//		 ts.search("java", new Sort(new SortField("sortSize", SortField.Type.LONG)));
		 ts.search("java", new Sort(new SortField("score", SortField.Type.INT)));
	 }
	 
	 @Before
	 public void init() {
		 ts = new TestSearch();
	 }
	 
	 @Test
	 public void index() {
		 FileIndexUtil.index(true);
	 }
	 
	 public void searchFilter(String qStr, Filter filter) {
		 try {
			IndexSearcher searcher = getSearcher();
			QueryParser parser = new QueryParser("content", new StandardAnalyzer());
			Query query = parser.parse(qStr);
			TopDocs tds = null;
			if (null != filter) {
				tds = searcher.search(query, filter, 50);
			} else {
				tds = searcher.search(query, 50);
			}
			
			for (ScoreDoc sd : tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println(sd.doc + ":(" + 
						sd.score + ") [" + doc.get("fileName") +
						"] 【" + doc.get("filePath") +
						"】Size--->" + doc.get("size")+" 【" + doc.get("date") +
						"】");
			}
				
		} catch (ParseException e) {
			 e.printStackTrace();
		} catch (IOException e) {
			 e.printStackTrace();
		}
	 }
	 
	@Test
	 public void testByFilter(){
		Filter ft = null;
		ft = new TermRangeFilter("fileName", new BytesRef("apiError.hi"), new BytesRef("apiError.ps"), true, true);
		ft = NumericRangeFilter.newIntRange("size", 1, 3700, true, true);
		ft = new QueryWrapperFilter(new WildcardQuery(new Term("fileName", "*.ini")));
		searchFilter("java", ft);
	 }
}

 