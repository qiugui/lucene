 package com.lucene.myAnalyzer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;

 public class QgSimilarFilter extends TokenFilter {

	 private final CharTermAttribute cta = this.addAttribute(CharTermAttribute.class);
	 private final PositionIncrementAttribute pia = this.addAttribute(PositionIncrementAttribute.class);
	 private AttributeSource.State current;
	 
	 private Stack<String> similarWords = new Stack<String>();
	 
	protected QgSimilarFilter(TokenStream input) {
		super(input);
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
		Map<String, String[]> maps = new HashMap<String, String[]>();
		maps.put("中国", new String[]{"天朝","大陆"});
		maps.put("我", new String[]{"俺","咱"});
		String[] sWords = maps.get(name);
		if (null != sWords) {
			for(String str:sWords){
				similarWords.push(str);
			}
			return true;
		}
		return false;
	}
}

 