 package com.lucene.myQueryParser;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import com.lucene.utils.FileIndexUtil;
 public class QgQueryParserUtil {

	 public void search(String qStr) {
			try {
				IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FileIndexUtil.getDirectory()));
				QueryParser parser = new QgQueryParser("content", new StandardAnalyzer());
				Query query = parser.parse(qStr);
				TopDocs tds = null;
					tds = searcher.search(query, 50);
				
				for (ScoreDoc sd : tds.scoreDocs) {
					Document doc = searcher.doc(sd.doc);
					System.out.println(sd.doc + ":(" + 
							sd.score + ") [" + doc.get("fileName") +
							"] 【" + doc.get("filePath") +
							"】---Score:" + doc.get("score") + "---Size:" + doc.get("size")+" 【" + doc.get("date") +
							"】");
				}
				
			} catch (ParseException e) {
				 System.out.println(e.getMessage());
			} catch (IOException e) {
				 e.printStackTrace();
			}
		 }
}

 