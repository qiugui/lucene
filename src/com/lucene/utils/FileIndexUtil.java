 package com.lucene.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
 public class FileIndexUtil {

	 private static Directory directory = null;
	 
	 private static final String DOC_PATH="D:\\lucene\\example";
	 private static final String INDEX_PATH="D:\\lucene\\indexPath02";
	 
	 static {
		 try {
			directory = FSDirectory.open(Paths.get(INDEX_PATH, new String[0]));
		} catch (IOException e) {
			 e.printStackTrace();
		}
	 }
	 
	 public static Directory getDirectory() {
		 return directory;
	 }
	 
	 public static void index(boolean hasNew) {
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
			if (hasNew) {
				writer.deleteAll();
			}
			File file = new File(DOC_PATH);
			Document doc = null;
			Random random = new Random();
			int id = 0;
			for (File f : file.listFiles()) {
				int score = random.nextInt(600);
				id++;
				doc = new Document();
				doc.add(new SortedDocValuesField("id", new BytesRef(id)));
				doc.add(new IntField("id", id, Store.YES));
				
				doc.add(new TextField("content", new FileReader(f)));
				
				doc.add(new SortedDocValuesField("sortFileName", new BytesRef(f.getName())));
				doc.add(new StringField("fileName", f.getName(), Store.YES));
				
				doc.add(new StringField("filePath", f.getAbsolutePath(), Store.YES));
				
				doc.add(new NumericDocValuesField("date", f.lastModified()));
				doc.add(new LongField("date", f.lastModified(), Store.YES));
				
				doc.add(new NumericDocValuesField("sortSize", f.length()));
				doc.add(new IntField("size", (int)f.length(), Store.YES));
				
				doc.add(new NumericDocValuesField("score",score));
				doc.add(new IntField("score", score, Store.YES));
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
}
 