 package com.lucene.nrt;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.activation.FileDataSource;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexReaderContext;
import org.apache.lucene.index.IndexUpgrader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TrackingIndexWriter;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.ControlledRealTimeReopenThread;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

 @SuppressWarnings("unused")
public class IndexUtil {

	 private final static String INDEX_PATH = "D://lucene//indexPath04";
	 
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
	 
	 private Map<String, Float> scores = new HashMap<String, Float>();
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	 
	 private Directory directory = null;
	 private SearcherManager mgr = null;
	 private IndexWriter writer = null;
	 private TrackingIndexWriter trkWriter = null;
	 private ControlledRealTimeReopenThread<IndexSearcher> crtThread = null;
//	 private AtomicLong gen;
	 private long gen;
	 
	 public IndexUtil(){
		try {
			setDates();
			scores.put("itat.org", 2.0f);
			scores.put("zttc.edu", 1.5f);
			
			directory = FSDirectory.open(Paths.get(INDEX_PATH, new String[0]));
			writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
			trkWriter = new TrackingIndexWriter(writer);
			//新建SearcherManager 
			//true 表示在内存中删除，false可能删可能不删，设为false性能会更好一些
			mgr = new SearcherManager(directory, new SearcherFactory());
//			gen = new AtomicLong(trkWriter.getGeneration());
			//启动 SearcherManager的reopen线程
			crtThread = new ControlledRealTimeReopenThread<IndexSearcher>(trkWriter, mgr, 5.0, 0.025);
			crtThread.setDaemon(true);
			crtThread.setName("crtThread线程");
			crtThread.start();
		} catch (IOException e) {
			 e.printStackTrace();
		}
	 }
	 
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
			Document doc = null;
			for (int i = 0; i < ids.length; i++) {
				doc = new Document();
				String et = emails[i].substring(emails[i].lastIndexOf("@")+1);
				Field idField = new StringField("id", ids[i], Field.Store.YES);
				Field emailField = new StringField("email", emails[i], Field.Store.YES);
				Field contentField = new TextField("content", contents[i], Field.Store.NO);
				Field nameField = new StringField("name",names[i],Field.Store.YES);
				Field attachField = new IntField("attach", attachs[i], Field.Store.YES);
				Field dateField = new LongField("date", dates[i].getTime(), Field.Store.YES);
				/*
				 * 老版本是通过Document.setBoost()，在建立索引时进行加权，
				 * 新版本中，是在相应的索引域Field上，使用Field.setBoost()，进行索引时加权
				 */
				if (scores.containsKey(et)) {
					contentField.setBoost(scores.get(et));
				} else {
					contentField.setBoost(0.5f);
				}
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
			if (null != writer)
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
		 try {
			/*
			 * 参数是一个选项，可以是一个Query，也可以是一个Term，Term是一个精确查找的值
			 * 此时删除的文档并不会被完全删除，而是存储在一个回收站中的，可以恢复
			 */
			gen = trkWriter.deleteDocuments(new Term("id", "2"));
		} catch (IOException e) {
			 e.printStackTrace();
		} 
	 }
	 
	 public void update() {
		try {
			Document doc = new Document();
			doc.add(new StringField("id", "11", Field.Store.YES));
			doc.add(new StringField("email", emails[0], Field.Store.YES));
			doc.add(new TextField("content", contents[0], Field.Store.NO));
			doc.add(new StringField("name",names[0],Field.Store.YES));
			gen = trkWriter.updateDocument(new Term("id", "1"), doc);
		} catch (IOException e) {
			 e.printStackTrace();
		}
	 }
	 
	 public void search() {
		 IndexSearcher searcher = null;
		 try {
//			if (mgr.maybeRefresh()) System.out.println("refresh");//判断是否需要重新打开一个searcher
			mgr.maybeRefresh();
			crtThread.waitForGeneration(gen);
			searcher = mgr.acquire();
			TermQuery query = new TermQuery(new Term("content", "like"));
			TopDocs tds = searcher.search(query, 10);
			Calendar c = Calendar.getInstance();
			for (ScoreDoc sd : tds.scoreDocs ) {
				int sdId = sd.doc;
				Document doc = searcher.doc(sdId);
				long time = Long.parseLong(doc.get("date"));
				c.setTimeInMillis(time);
				System.out.println("[id]"+doc.get("id") +"(sdId:"+sdId+")"+doc.get("name")+" ["+doc.get("email")+
						"]--->"+" [附件数]"+doc.get("attach")+" [日期]"+sdf.format(c.getTime()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			 e.printStackTrace();
			 
		} finally {
//			try {
//				mgr.release(searcher);
//			} catch (IOException e) {
//				 e.printStackTrace();
//			}
		}
		 
	 }
	 
	 public void commit() {
		 try {
			writer.commit();
		} catch (IOException e) {
			 e.printStackTrace();
		}
	 }
}

 