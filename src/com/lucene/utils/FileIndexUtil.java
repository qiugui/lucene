 package com.lucene.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
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
			for (File f : file.listFiles()) {
				doc = new Document();
				doc.add(new TextField("content", new FileReader(f)));
				doc.add(new StringField("fileName", f.getName(), Store.YES));
				doc.add(new StringField("filePath", f.getAbsolutePath(), Store.YES));
				doc.add(new LongField("date", f.lastModified(), Store.YES));
				doc.add(new IntField("size", (int) (f.length() / 1024), Store.YES));
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

 