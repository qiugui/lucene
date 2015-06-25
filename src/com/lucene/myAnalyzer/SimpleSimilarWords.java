 package com.lucene.myAnalyzer;

import java.util.HashMap;
import java.util.Map;
 /** 
* @ClassName: SimpleSimilarWords 
* @Description: 简单同义词构造器 
* @author qiugui 
* @date 2015年6月25日 上午10:30:57 
*  
*/ 
public class SimpleSimilarWords implements SimilarWordsEngine {

	 Map<String, String[]> maps = new HashMap<String, String[]>();
		
	@Override
	public String[] getSimilarWords(String str) {
		maps.put("中国", new String[]{"天朝","大陆"});
		maps.put("埇桥区", new String[]{"埇桥"});
		maps.put("我", new String[]{"俺","咱"});
		String[] sWords = maps.get(str);
		return sWords;
	}

}

 