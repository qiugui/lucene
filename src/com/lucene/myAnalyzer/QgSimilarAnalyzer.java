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
 @SuppressWarnings("deprecation")
public class QgSimilarAnalyzer extends Analyzer {

	@SuppressWarnings("rawtypes")
	private Set stopWords; 
	 
	public QgSimilarAnalyzer() {
		stopWords = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
	}
	 
	@SuppressWarnings("unchecked")
	public QgSimilarAnalyzer(String[] stopwords) {
		this.stopWords = StopFilter.makeStopSet(stopwords, true);
		this.stopWords.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		final Tokenizer tokenizer;
		TokenStream result;
		if (getVersion().onOrAfter(Version.LUCENE_4_8_0)){
			tokenizer = new HMMChineseTokenizer();
			result = tokenizer;
		} else {
			tokenizer = new SentenceTokenizer();
			result = new WordTokenFilter(tokenizer);
		}
		if (!stopWords.isEmpty()) result = new StopFilter(result, (CharArraySet) stopWords);
		result = new QgSimilarFilter(result);
		return new TokenStreamComponents(tokenizer, result);

	}

}

 