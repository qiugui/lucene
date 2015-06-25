 package com.lucene.myAnalyzer;

import java.util.HashMap;
import java.util.Map;
 public class ComplexSimilarWords implements SimilarWordsEngine {

	 Map<String, String[]> maps = new HashMap<String, String[]>();
		
		@Override
		public String[] getSimilarWords(String str) {
			maps.put("安徽", new String[]{"皖"});
			String[] sWords = maps.get(str);
			return sWords;
		}

}

 