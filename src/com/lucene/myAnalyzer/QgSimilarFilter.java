 package com.lucene.myAnalyzer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

 public class QgSimilarFilter extends TokenFilter {

	 private final CharTermAttribute cta = this.addAttribute(CharTermAttribute.class);
	 private final PositionIncrementAttribute pia = this.addAttribute(PositionIncrementAttribute.class);
	 
	protected QgSimilarFilter(TokenStream input) {
		super(input);
	}

	@Override
	public boolean incrementToken() throws IOException {
		if (!input.incrementToken()) 
			return false;
		//进行同义词的处理
		String[] sWords = getSimilarWords(cta.toString());
		if (null != sWords) {
			for(String str:sWords){
				cta.setEmpty();
				cta.append(str);
			}
		}
		return true;
	}

	private String[] getSimilarWords(String name) {
		Map<String, String[]> maps = new HashMap<String, String[]>();
		maps.put("中国", new String[]{"天朝","大陆"});
		maps.put("我", new String[]{"俺","咱"});
		return maps.get(name);
	}
}

 