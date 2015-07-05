 package com.lucene.myCustomScore;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.index.SortedDocValues;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.BytesRef;

import com.lucene.utils.FileIndexUtil;
 public class QgScoreQuery {

	 
	 public void searchByScoreQuery(){
		 try {
			IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FileIndexUtil.getDirectory()));
			 Query query = new TermQuery(new Term("content", "java"));
//			 Query myQuery = new QgCustomScoreQuery(query, 1.0, "sortFileName");
			 Query myQuery = new QgFilenameScoreQuery(query, "sortFileName");
			 TopDocs tds = null;
				
			 tds = searcher.search(myQuery, 50);
			
			 for (ScoreDoc sd : tds.scoreDocs) {
				 Document doc = searcher.doc(sd.doc);
				 System.out.println(sd.doc + ":(" + 
						 sd.score + ") [" + doc.get("fileName") +
						 "] 【" + doc.get("filePath") +
						 "】---Score:" + doc.get("score") + "---Size:" + doc.get("size")+" 【" + doc.get("date") +
						 "】");
			 }
		} catch (IOException e) {
			 e.printStackTrace();
		}
	 }
	 
	 
	 /** 
	* @ClassName: QgFilenameScoreQuery 
	* @Description: 根据文件名重新评分的Query
	* @author qiugui 
	* @date 2015年7月3日 下午9:21:06 
	*  
	*/ 
	public class QgFilenameScoreQuery extends CustomScoreQuery{
		 
		 //评分域的名称
		 private String scoreField;
		 
		 public QgFilenameScoreQuery(Query subQuery,String scoreField) {
			 super(subQuery);
			 this.scoreField = scoreField;
		 }
		 
		 @Override
		protected CustomScoreProvider getCustomScoreProvider(
				LeafReaderContext context) throws IOException {
			 return new QgFilenameProvider(context, scoreField);
			 
		}
	 }
	 
	 public class QgFilenameProvider extends CustomScoreProvider {
		 //评分域的名称
		 private String scoreField;
		 
		//文件名缓存值
		 private SortedDocValues fields;
		 
		 public QgFilenameProvider (LeafReaderContext context, String scoreField) {
			 super(context);
			 this.scoreField = scoreField;
			 try {
				this.fields = context.reader().getSortedDocValues(scoreField);
			} catch (IOException e) {
				 e.printStackTrace();
			}
		 }
		 
		 @Override
		public float customScore(int doc, float subQueryScore, float valSrcScore)
				throws IOException {
			 String filename = fields.get(doc).utf8ToString();
			 if (filename.endsWith(".txt")) return subQueryScore*100;
			 if (filename.endsWith(".ini")) return subQueryScore*10;
			 return subQueryScore;
			 
		}
	 }
	 
	 /** 
	* @ClassName: QgCustomScoreQuery 
	* @Description: 根据指定field进行评分
	* @author qiugui 
	* @date 2015年7月3日 下午9:21:44 
	*  
	*/ 
	public class QgCustomScoreQuery extends CustomScoreQuery{

		 //权重倍数
		 private double multiplier;
		 
		 //评分域的名称
		 private String scoreField;
		 
		public QgCustomScoreQuery(Query subQuery,double multiplier, String scoreField) {
			super(subQuery);
			this.multiplier = multiplier;
			this.scoreField = scoreField;
		}
		
		@Override
		public CustomScoreProvider getCustomScoreProvider(LeafReaderContext context) throws IOException {
			return new QgCustomProvider(context, multiplier, scoreField);
		}
	 }
	 
	 public class QgCustomProvider extends CustomScoreProvider{
		 //权重倍数
		 private double multiplier;
		 
		 //评分域的名称
		 private String scoreField;
		 //域缓存值
		 private NumericDocValues score;
		 
		 //文件名缓存值
		 private SortedDocValues fileName;
		 
		 public QgCustomProvider(LeafReaderContext context,double multiplier,String scoreField){
			 super(context);
			 this.scoreField = scoreField;
			 this.multiplier = multiplier;
			 try {
				this.score = context.reader().getNumericDocValues(scoreField);
			} catch (IOException e) {
				 e.printStackTrace();
			}
		 }
		 
		 /** 
	     * subQueryScore:指的是子Query查询的评分 
	     * valSrcScore：指的是FunctionQuery查询的评分 
	     */  
		 @Override
		 public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {
			 int s = (int) score.get(doc);
			 System.out.println("score:" + s + " " + "subQueryScore:" + subQueryScore + " " + "valSrcScore:" + valSrcScore);
			 return (float) (s*subQueryScore*multiplier);
		 }
	 }
}

 