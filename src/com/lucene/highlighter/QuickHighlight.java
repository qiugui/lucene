 package com.lucene.highlighter;

import java.io.IOException;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;

import com.chenlb.mmseg4j.analysis.ComplexAnalyzer;
 public class QuickHighlight {

	 public String highlight() {
		 try {
			String txt = "我爱北京天安门，天安门上彩旗飘，伟大领袖毛主席，带领我们向前进，向前进！";
			Query query = new TermQuery(new Term("f", "毛主席"));
			query = new QueryParser("f", new ComplexAnalyzer()).parse("毛主席 北京");
			QueryScorer scorer = new QueryScorer(query);
			Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
			Formatter formatter = new SimpleHTMLFormatter("<span style='color:red;'>", "</span>");
			Highlighter highlighter = new Highlighter(formatter, scorer);
			highlighter.setTextFragmenter(fragmenter);
			String string = highlighter.getBestFragment(new ComplexAnalyzer(), "f", txt);
			return string;
		} catch (IOException e) {
			 e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			 e.printStackTrace();
		} catch (ParseException e) {
			 e.printStackTrace();
		} 
		 return null;
	 }
}

 