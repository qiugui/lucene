 package com.lucene.highlighter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

import com.chenlb.mmseg4j.analysis.ComplexAnalyzer;

 public class FirstTika {

	 public void index() {
		 IndexWriter writer = null;
		 try {
			 File file = new File("D:\\lucene\\example2\\pig-0.14.0.docx");
			 Directory directory = FSDirectory.open(Paths.get("D:\\lucene\\indexPath03", new String[0]));
			 writer = new IndexWriter(directory,new IndexWriterConfig(new ComplexAnalyzer()));
			 writer.deleteAll();
			 Document doc = new Document();
			 doc.add(new TextField("content", file2String(file), Store.NO));
			 doc.add(new StringField("fileName", file.getName(), Store.YES));
			 writer.addDocument(doc);
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
	 
	 
	 /**   
	 * @Title: tikaTool   
	 * @Description: 使用Tika直接读取文件  
	 * @return        
	 */
	 
	public String tikaTool() {
		 InputStream is = null;
		 File file = new File("D:\\lucene\\example2\\pig-0.14.0.docx");
		 Tika tika = new Tika();
		 try {
			Metadata metadata = new Metadata();
			metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
			is = new FileInputStream(file);
			String str = tika.parseToString(is, metadata);
			for (String name:metadata.names()) {
				 System.out.println("["+name+"]:"+metadata.get(name));
			 }
			return tika.parseToString(file);
		} catch (IOException e) {
			 e.printStackTrace();
		} catch (TikaException e) {
			 e.printStackTrace();
		} finally {
			if (null != is)
				try {
					is.close();
				} catch (IOException e) {
					 e.printStackTrace();
				}
		}
		 return null;
	 }
	 
	 /**   
	 * @Title: file2Txt   
	 * @Description: 使用Parser读取文件  
	 * @return        
	 */
	 
	public String file2Txt() {
		 InputStream is = null;
		 File file = new File("D:\\lucene\\example2\\pig-0.14.0.docx");
		 try {
			is = new FileInputStream(file);
			 Parser parser = new AutoDetectParser();
			 ContentHandler handler = new BodyContentHandler();
			 Metadata metadata = new Metadata();
			 metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
			 ParseContext context = new ParseContext();
			 context.set(Parser.class, parser);
			 parser.parse(is, handler, metadata, context);
			 for (String name:metadata.names()) {
				 System.out.println("["+name+"]:"+metadata.get(name));
			 }
			 String str = handler.toString();
			 return str;
		} catch (FileNotFoundException e) {
			 e.printStackTrace();
		} catch (IOException e) {
			 e.printStackTrace();
		} catch (SAXException e) {
			 e.printStackTrace();
		} catch (TikaException e) {
			 e.printStackTrace();
		} finally {
			if (null != is)
				try {
					is.close();
				} catch (IOException e) {
					 e.printStackTrace();
				}
		}
		 return null;
	 }
	 
	 public String file2String(File file) {
		 InputStream is = null;
		 try {
			is = new FileInputStream(file);
			 Parser parser = new AutoDetectParser();
			 ContentHandler handler = new BodyContentHandler();
			 Metadata metadata = new Metadata();
			 ParseContext context = new ParseContext();
			 context.set(Parser.class, parser);
			 parser.parse(is, handler, metadata, context);
			 String str = handler.toString();
			 return str;
		} catch (FileNotFoundException e) {
			 e.printStackTrace();
		} catch (IOException e) {
			 e.printStackTrace();
		} catch (SAXException e) {
			 e.printStackTrace();
		} catch (TikaException e) {
			 e.printStackTrace();
		} finally {
			if (null != is)
				try {
					is.close();
				} catch (IOException e) {
					 e.printStackTrace();
				}
		}
		 return null;
	 }
}

 