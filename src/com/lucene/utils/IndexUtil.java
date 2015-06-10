 package com.lucene.utils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexUpgrader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

 public class IndexUtil {

	 private final static String INDEX_PATH = "E://lucene//indexPath02";
	 
	 private String[] ids = {"1","2","3","4","5","6"};
	 private String[] emails = {
		"aa@itat.org","bb@itat.org","cc@cc.org",
		"dd@itat.edu","ee@itat.edu","ff@sds.com"
	 };
	 private String[] contents = {
			 "welcome to visit the space","my name is cc",
			 "I like football","I like football and I like basketball too",
			 "I like movie and swim","my school is JUST"
	 };
	 private int[] attachs = {2,2,3,5,4,1};
	 private String[] names = {"John","Mike","Jodan","Lucas","Tommy","Joy"};
	 
	 private Directory directory = null;
	 
	 public IndexUtil(){
		 try {
			directory = FSDirectory.open(Paths.get(INDEX_PATH, new String[0]));
		} catch (IOException e) {
			 e.printStackTrace();
		}
	 }
	 
	 @SuppressWarnings("deprecation")
	public void index() {
		 IndexWriter writer = null;
		 try {
			writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
			Document doc = null;
			for (int i = 0; i < ids.length; i++) {
				doc = new Document();
				doc.add(new StringField("id", ids[i], Field.Store.YES));
				doc.add(new StringField("email", emails[i], Field.Store.YES));
				doc.add(new TextField("content", contents[i], Field.Store.NO));
				doc.add(new Field("name",names[i],Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
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
	 
	 public void query() {
		 IndexReader reader = null;
		 try {
			reader = DirectoryReader.open(directory);
			System.out.println("numDocs:"+reader.numDocs());
			System.out.println("maxDocs:"+reader.maxDoc());
			System.out.println("deleteDocs:"+reader.numDeletedDocs());
//			IndexSearcher searcher = new IndexSearcher(reader);
//			QueryParser parser = new QueryParser("content", new StandardAnalyzer());
//			Query query = parser.parse("football");
//			TopDocs tds = searcher.search(query, 10);
//			ScoreDoc[] sds = tds.scoreDocs;
//			for (ScoreDoc sd : sds) {
//				int docId = sd.doc;
//				Document document = searcher.doc(docId);
//				System.out.println("["+document.get("id")+"]"+document.get("name")+"--->"+
//				document.get("email")+"---"+document.get("content"));
//			}
		} catch (IOException e) {
			 e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					 e.printStackTrace();
				}
		}
		 
	 }
	 
	 public void delete() {
		 IndexWriter writer = null;
		 try {
			writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
			/*
			 * 参数是一个选项，可以是一个Query，也可以是一个Term，Term是一个精确查找的值
			 * 此时删除的文档并不会被完全删除，而是存储在一个回收站中的，可以恢复
			 */
			writer.deleteDocuments(new Term("id", "1"));
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
	 
	 public void undelete() {
		 //使用Reader进行恢复
		 IndexReader reader = null;
		 try {
				reader = DirectoryReader.open(directory);
				
			} catch (IOException e) {
				 e.printStackTrace();
			} finally {
				if (reader != null)
					try {
						reader.close();
					} catch (IOException e) {
						 e.printStackTrace();
					}
			}
			 
	 }
	 
}

 