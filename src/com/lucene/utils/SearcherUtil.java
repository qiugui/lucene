 package com.lucene.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class SearcherUtil {
	 private Directory directory;
	 private IndexReader reader;
	 
	 private String[] ids = {"1","2","3","4","5","6"};
	 private String[] emails = {
		"aa@itat.org","bb@itat.org","cc@itat.org",
		"dd@zttc.edu","ee@126.com","ff@sina.com"
	 };
	 private String[] contents = {
			 "welcome to visit the space, I like java","my name is cc, I like PHP",
			 "I like football","I like football and I like basketball too",
			 "I like movie and swim","my school is JUST, I like it"
	 };
	 private Date[] dates = null;
	 private int[] attachs = {2,2,3,5,4,1};
	 private String[] names = {"John","Mike","Jodan","Lucas","Tommy","Joy"};
	 
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	 
	 private void setDates() {
		 dates = new Date[ids.length];
		 try {
			dates[0] = sdf.parse("2010-11-15");
			dates[1] = sdf.parse("2012-10-03");
			dates[2] = sdf.parse("2013-01-01");
			dates[3] = sdf.parse("2014-09-24");
			dates[4] = sdf.parse("2015-06-18");
			dates[5] = sdf.parse("2009-10-01");
		} catch (java.text.ParseException e) {
			 e.printStackTrace();
		}
	}
	 
	 public void index() {
		 IndexWriter writer = null;
		 try {
			writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
			writer.deleteAll();
			Document doc = null;
			for (int i = 0; i < ids.length; i++) {
				doc = new Document();
				Field idField = new StringField("id", ids[i], Field.Store.YES);
				Field emailField = new StringField("email", emails[i], Field.Store.YES);
				Field contentField = new TextField("content", contents[i], Field.Store.NO);
				Field nameField = new StringField("name",names[i],Field.Store.YES);
				Field attachField = new IntField("attach", attachs[i], Field.Store.YES);
				Field dateField = new LongField("date", dates[i].getTime(), Field.Store.YES);
				doc.add(idField);
				doc.add(emailField);
				doc.add(contentField);
				doc.add(nameField);
				doc.add(attachField);
				doc.add(dateField);
				writer.addDocument(doc);
			}
		} catch (IOException e) {
			 e.printStackTrace();
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	 }
	 
	 public SearcherUtil() {
		 setDates();
		 directory = new RAMDirectory();
		 index();
	 }
	 
	 public IndexSearcher getSearcher() {
		 try {
			 if (reader == null) {
				 reader = DirectoryReader.open(directory);
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
	 
	 public IndexSearcher getSearcher(Directory directory) {
		 try {
			 if (reader == null) {
				 reader = DirectoryReader.open(directory);
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
	 
	 //精确查询
	 public void searchByTerm(String field, String value, int num) {
		Query query = new TermQuery(new Term(field, value));
		queryResult(query, num);
	}
	 
	 //范围查询，只能查字符串
	 public void searchByTermRange(String field, String start, String end, int num) {
		 Query query = TermRangeQuery.newStringRange(field, start, end, true, true);
		 queryResult(query, num);
	 }
	 
	 //范围查询，查数字
	 public void searchByNumRange(String field, int min, int max, int num) {
		 Query query = NumericRangeQuery.newIntRange(field, min, max, true, true);
		 queryResult(query, 10);
	 }
	 
	 //前缀搜索
	 public void searchByPrefix(String field, String prefix, int num) {
		 Query query = new PrefixQuery(new Term(field, prefix));
		 queryResult(query, num);
	 }

	 //通配符搜索，?表示一个字符，*表示多个字符
	 public void searchByWildcard(String field, String value, int num) {
		 Query query = new WildcardQuery(new Term(field, value));
		 queryResult(query, 10);
	 }
	 
	 //可以连接多个条件
	 public void searchByBoolean(int num) {
		 BooleanQuery query = new BooleanQuery();
		 /*
		  * Occur.MUST 必须出现
		  * Occur.SHOULD 可以出现
		  * Occur.MUST_NOT 不能出现
		  */
		 query.add(new TermQuery(new Term("name", "John")), Occur.MUST_NOT);
		 query.add(new TermQuery(new Term("content", "like")), Occur.SHOULD);
		 queryResult(query, num);
	 }
	 
	 //短语搜索
	 public void searchByPhrase(int num) {
		 PhraseQuery query = new PhraseQuery();
		 query.setSlop(1);
		 query.add(new Term("content", "i"));
		 query.add(new Term("content", "football"));
		 queryResult(query, num);
	 }
	 
	 //模糊查询
	 public void searchByFuzzyQuery(String field, String value, int num) {
		 //FuzzyQuery query = new FuzzyQuery(new Term(field, value));
		 FuzzyQuery query = new FuzzyQuery(new Term(field, value), 1);
		 System.out.println(query.getMaxEdits()+" "+query.getPrefixLength());
		 queryResult(query, num);
	 }
	 
	 //几乎覆盖所有的Query
	 public void searchByQueryParser(Query query, int num) {
		 queryResult(query, num);
	 }
	 
	private void queryResult(Query query, int num) {
		Calendar c = Calendar.getInstance();
		try {
			IndexSearcher searcher = getSearcher();
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了："+tds.totalHits+"条");
			for (ScoreDoc sd : tds.scoreDocs) {
				int docId = sd.doc;
				Document doc = searcher.doc(docId);
				long time = Long.parseLong(doc.get("date"));
				c.setTimeInMillis(time);
				System.out.println("[id:"+doc.get("id") +"] [docId:"+docId+"] "+doc.get("name")+" ["+doc.get("email")+
						"] --->"+" [附件数："+doc.get("attach")+"] [日期："+sdf.format(c.getTime()) + "]");
			}
		} catch (IOException e) {
			 e.printStackTrace();
		}
	}

	 public void searchByPage(String queryStr, int pageIndex, int pageSize) {
		 Directory directory = FileIndexUtil.getDirectory();
		 IndexSearcher searcher = getSearcher(directory);
		 QueryParser parser = new QueryParser("content", new StandardAnalyzer());
		 try {
			Query query = parser.parse(queryStr);
			TopDocs tds = searcher.search(query, 200);
			System.out.println("一共查询了："+tds.totalHits+"条");
			ScoreDoc[] sds = tds.scoreDocs;
			int start = (pageIndex - 1) * pageSize;
			int end = pageIndex * pageSize;
			for (int i = start; i < end; i++) {
				Document doc = searcher.doc(sds[i].doc);
				System.out.println("["+sds[i].doc+"]"+doc.get("filePath") + "--->" + doc.get("fileName"));
			}
		} catch (ParseException e) {
			 e.printStackTrace();
		} catch (IOException e) {
			 e.printStackTrace();
		}
	 }
	 
	 public void searchNoPage(String queryStr) {
		 Directory directory = FileIndexUtil.getDirectory();
		 IndexSearcher searcher = getSearcher(directory);
		 QueryParser parser = new QueryParser("content", new StandardAnalyzer());
		 try {
			Query query = parser.parse(queryStr);
			TopDocs tds = searcher.search(query, 200);
			System.out.println("一共查询了："+tds.totalHits+"条");
			ScoreDoc[] sds = tds.scoreDocs;
			
			for (int i = 0; i < sds.length; i++) {
				Document doc = searcher.doc(sds[i].doc);
				System.out.println("["+sds[i].doc+"]"+doc.get("filePath") + "--->" + doc.get("fileName"));
			}
		} catch (ParseException e) {
			 e.printStackTrace();
		} catch (IOException e) {
			 e.printStackTrace();
		}
	 }
	 
	 /*
	  * 根据页码和分页大小获取上一页的最后一个ScoreDoc
	  */
	 public ScoreDoc lastScoreDoc(int pageIndex, int pageSize, Query query, IndexSearcher searcher) {
		 try {
			if (pageIndex == 1) return null;	//如果是第一页，返回null
			int queryNum = (pageIndex - 1) * pageSize;	//获取上一页的数量
			TopDocs tds = searcher.search(query, queryNum);
			System.out.println("一共查询了："+tds.totalHits+"条（"+ queryNum +"）");
			ScoreDoc[] sds = tds.scoreDocs;
			return sds[queryNum -1];
		} catch (IOException e) {
			 e.printStackTrace();
		}
		 return null;
	 }
	 
	 public void searchBySearchAfter(String queryStr, int pageIndex, int pageSize) {
		 Directory directory = FileIndexUtil.getDirectory();
		 IndexSearcher searcher = getSearcher(directory);
		 QueryParser parser = new QueryParser("content", new StandardAnalyzer());
		 try {
			Query query = parser.parse(queryStr);
			//先获取上一页的最后一个
			ScoreDoc lastSd = lastScoreDoc(pageIndex, pageSize, query, searcher);
			//再根据上一页的最后一个，使用searchAfter
			TopDocs tds = searcher.searchAfter(lastSd, query, pageSize);
			for (ScoreDoc sd : tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println("["+sd.doc+"]"+doc.get("filePath") + "--->" + doc.get("fileName"));
			}
		} catch (ParseException e) {
			 e.printStackTrace();
		} catch (IOException e) {
			 e.printStackTrace();
		}
	 }
}
 