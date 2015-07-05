 package com.lucene.highlighter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;

import com.chenlb.mmseg4j.analysis.ComplexAnalyzer;
 public class FileIndexUtil {

	 private static Directory directory = null;
	 
	 private static final String DOC_PATH="D:\\lucene\\example2";
	 private static final String INDEX_PATH="D:\\lucene\\indexPath03";
	 
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
			writer = new IndexWriter(directory, new IndexWriterConfig(new ComplexAnalyzer()));
			if (hasNew) {
				writer.deleteAll();
			}
			File file = new File(DOC_PATH);
			Document doc = null;
			for (File f : file.listFiles()) {
				doc = generateDoc(f);
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
	 
	 @SuppressWarnings("deprecation")
	public static Document generateDoc(File file) {
		 InputStream is = null;
		 try {
			is = new FileInputStream(file);
			 Document document = new Document();
			 Metadata metadata = new Metadata();
			 document.add(new TextField("content", new Tika().parseToString(is, metadata), Store.NO));
			 document.add(new TextField("fileName",file.getName(), Store.YES));
			 document.add(new StringField("filePath", file.getPath(), Store.YES));
			 String author = metadata.get(Metadata.AUTHOR);
			 if (null == author) author = "null";
			 document.add(new StringField("author", author , Store.YES));
			 return document;
		} catch (FileNotFoundException e) {
			 e.printStackTrace();
		} catch (IOException e) {
			 e.printStackTrace();
		} catch (TikaException e) {
			 e.printStackTrace();
		} finally {
			if (null !=is)
				try {
					is.close();
				} catch (IOException e) {
					 e.printStackTrace();
				}
		}
		 return null;
	 }
}
 