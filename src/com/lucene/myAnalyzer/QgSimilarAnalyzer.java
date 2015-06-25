 package com.lucene.myAnalyzer;

import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.cn.smart.HMMChineseTokenizer;
import org.apache.lucene.analysis.cn.smart.SentenceTokenizer;
import org.apache.lucene.analysis.cn.smart.WordTokenFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

import com.chenlb.mmseg4j.Chunk;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Sentence;
import com.chenlb.mmseg4j.SimpleSeg;
import com.chenlb.mmseg4j.analysis.MMSegTokenizer;
 /** 
* @ClassName: QgSimilarAnalyzer 
* @Description: 自定义同义词分词器 
* @author qiugui 
* @date 2015年6月25日 上午10:31:43 
*  
*/ 
@SuppressWarnings("deprecation")
public class QgSimilarAnalyzer extends Analyzer {

	@SuppressWarnings("rawtypes")
	private Set stopWords;
	private SimilarWordsEngine similarWordsEngine;
	
	
	/**   
	 * @Title:  QgSimilarAnalyzer   
	 * @Description: 使用默认stop_words_set的构造函数   
	 * @param similarWordsEngine   
	 */  
	public QgSimilarAnalyzer(SimilarWordsEngine similarWordsEngine) {
		this.stopWords = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
		this.similarWordsEngine = similarWordsEngine;
	}
	 
	/**   
	 * @Title:  QgSimilarAnalyzer   
	 * @Description: 使用自定义stop_words_set的构造函数   
	 * @param stopwords
	 * @param similarWordsEngine   
	 */  
	@SuppressWarnings("unchecked")
	public QgSimilarAnalyzer(String[] stopwords, SimilarWordsEngine similarWordsEngine) {
		this.stopWords = StopFilter.makeStopSet(stopwords, true);
		this.stopWords.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
		this.similarWordsEngine = similarWordsEngine;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		final Tokenizer tokenizer;
		TokenStream result;
//		if (getVersion().onOrAfter(Version.LUCENE_4_8_0)){
//			tokenizer = new HMMChineseTokenizer();
//			result = tokenizer;
//		} else {
//			tokenizer = new SentenceTokenizer();
//			result = new WordTokenFilter(tokenizer);
//		}
		tokenizer = new MMSegTokenizer(new SimpleSeg(Dictionary.getInstance("D:\\软件\\lucene-5.2.0-jar\\data")));
		result = tokenizer;
		if (!stopWords.isEmpty()) result = new StopFilter(result, (CharArraySet) stopWords);
		result = new QgSimilarFilter(result,similarWordsEngine);
		return new TokenStreamComponents(tokenizer, result);

	}

}

 