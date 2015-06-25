 package com.lucene.myAnalyzer;
 /** 
* @ClassName: SimilarWordsEngine 
* @Description: 同义词构造引擎 
* @author qiugui 
* @date 2015年6月25日 上午10:30:14 
*  
*/ 
public interface SimilarWordsEngine {

	 public String[] getSimilarWords(String str);
}

 