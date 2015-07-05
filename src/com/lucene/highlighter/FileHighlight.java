 package com.lucene.highlighter;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import com.chenlb.mmseg4j.analysis.ComplexAnalyzer;
 public class FileHighlight {

	 public void queryByHighlight(String queryStr, int num) {
		 IndexReader reader = null;
		 try {
			reader = DirectoryReader.open(FileIndexUtil.getDirectory());
			 IndexSearcher searcher = new IndexSearcher(reader);
			 Analyzer analyzer = new ComplexAnalyzer();
			 QueryParser parser = new MultiFieldQueryParser(new String[]{"fileName", "content"}, analyzer);
			 Query query = parser.parse(queryStr);
			 TopDocs tds = searcher.search(query, num);
			 for (ScoreDoc sd : tds.scoreDocs) {
				 int docId = sd.doc;
				 Document doc = searcher.doc(docId);
				 String title = doc.get("fileName");
				 title = highlightTxt(analyzer, query, title, "fileName");
				 String content = new Tika().parseToString(new File(doc.get("filePath")));
				 content = highlightTxt(analyzer, query, content, "content");
				 System.out.println("************************************************************");
				 System.out.println(title);
				 System.out.println("============================================================");
				 System.out.println(content);
			 }
		} catch (IOException e) {
			 e.printStackTrace();
		} catch (ParseException e) {
			 e.printStackTrace();
		} catch (TikaException e) {
			 e.printStackTrace();
		} finally {
			if (null != reader)
				try {
					reader.close();
				} catch (IOException e) {
					 e.printStackTrace();
				}
		}
	 }
	 
	 public String highlightTxt(Analyzer analyzer, Query query, String highlightTxt,String fieldName) {
		 try {
			QueryScorer scorer = new QueryScorer(query);
			 Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
			 Formatter formatter = new SimpleHTMLFormatter("<span>", "</span>");
			 Highlighter highlighter = new Highlighter(formatter, scorer);
			 highlighter.setTextFragmenter(fragmenter);
			 String hightTxt = highlighter.getBestFragment(analyzer, fieldName, highlightTxt);
			 if (null == hightTxt) return highlightTxt;
			 return hightTxt;
		} catch (IOException e) {
			 e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			 e.printStackTrace();
		}
		 return null;
	 }
}

 