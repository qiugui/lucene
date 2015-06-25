 package com.lucene.myAnalyzer;

import java.io.IOException;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;

 /** 
* @ClassName: QgSimilarFilter 
* @Description: 同义词过滤器
* @author qiugui 
* @date 2015年6月25日 上午10:34:50 
*  
*/ 
public class QgSimilarFilter extends TokenFilter {

	 private final CharTermAttribute cta = this.addAttribute(CharTermAttribute.class);
	 private final PositionIncrementAttribute pia = this.addAttribute(PositionIncrementAttribute.class);
	 private AttributeSource.State current;
	 
	 private SimilarWordsEngine similarWordsEngine;
	 private Stack<String> similarWords = new Stack<String>();
	 
	protected QgSimilarFilter(TokenStream input,SimilarWordsEngine similarWordsEngine) {
		super(input);
		this.similarWordsEngine = similarWordsEngine;
	}

	@Override
	public boolean incrementToken() throws IOException {
		//进行同义词的处理
		if(hasSimilarWords(cta.toString())) current = captureState();
		
		while(similarWords.size() > 0){
			String word = similarWords.pop();
			restoreState(current);
			cta.setEmpty();
			cta.append(word);
			pia.setPositionIncrement(0);
			return true;
		}
		
		if (!input.incrementToken()) 
			return false;
		
		return true;
	}

	private boolean hasSimilarWords(String name) {
		
		String[] sWords = similarWordsEngine.getSimilarWords(name);
		if (null != sWords) {
			for(String str:sWords){
				similarWords.push(str);
			}
			return true;
		}
		return false;
	}
}

 