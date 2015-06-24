 package com.lucene.myAnalyzer;

import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.util.CharArraySet;
 public class QgStopAnalyzer extends Analyzer {

	@SuppressWarnings("rawtypes")
	private Set stopWords;
	 
	@SuppressWarnings("unchecked")
	public QgStopAnalyzer(String[] stopWords) {
		this.stopWords = StopFilter.makeStopSet(stopWords, true);
		this.stopWords.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
	}
	
	public QgStopAnalyzer() {
		this.stopWords = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
	}
	 
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		//为该分词器设定 分词器 和 过滤链
		final Tokenizer tokenizer = new LetterTokenizer();
		TokenStream result;
		result = new LowerCaseFilter(tokenizer);
		result = new StopFilter(result, (CharArraySet) stopWords);
		return new TokenStreamComponents(tokenizer, result);
	}

}

 