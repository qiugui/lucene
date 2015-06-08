 package com.lucene.quickstart;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class HelloLucene {

	 private final static String DOC_PATH = "E://lucene//docPath";
	 private final static String INDEX_PATH = "E://lucene//indexPath";
	 
	 public void index () {
		IndexWriter writer = null;
		try {
			//1 创建Directory
//	 		Directory directory = new RAMDirectory();
			Directory directory = FSDirectory.open(Paths.get(INDEX_PATH, new String[0]));
			//2 创建IndexWriter
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			writer = new IndexWriter(directory, iwc);
			//3 创建Document对象
			Document doc = null;
			//4 为Document对象添加Field
			File f = new File(DOC_PATH);
			for (File file : f.listFiles()) {
				doc = new Document();
				doc.add(new StringField("path", file.getAbsolutePath(), Field.Store.YES));
				doc.add(new StringField("filename", file.getName(), Field.Store.YES));
				doc.add(new TextField("content", new FileReader(file)));
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
	 
	 public void search() {
		 IndexReader reader = null;
		 try {
			 //1 创建Directory
			Directory directory = FSDirectory.open(Paths.get(INDEX_PATH, new String[0]));
			//2 创建IndexReader
			reader = DirectoryReader.open(directory);
			//3 根据IndexReader创建IndexSearcher
			IndexSearcher searcher = new IndexSearcher(reader);
			//4 创建搜索的Query
			QueryParser parser = new QueryParser("content", new StandardAnalyzer());
			Query query = parser.parse("test*");
			//5 根据Searcher搜索并且返回TopDocs
			TopDocs tds = searcher.search(query, 10);
			//6 根据TopDocs获取ScoreDoc对象
			ScoreDoc[] sds = tds.scoreDocs;
			for (ScoreDoc sd : sds) {
				//7 根据ScoreDoc和Searcher对象获取具体的Document对象
				int docId = sd.doc;
				//8 根据Document获取相应的值
				Document document = searcher.doc(docId);
				System.out.println(document.get("filename")+" ["+document.get("path")+"]");
			}
		} catch (IOException e) {
			 e.printStackTrace();
		} catch (ParseException e) {
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

 