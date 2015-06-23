 package com.lucene.utils;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
 public class AnalyzerUtil {

	 public static void displayToken(String str, Analyzer analyzer) {
		 TokenStream tokenStream = null;
		 try {
			tokenStream = analyzer.tokenStream("content", new StringReader(str));
			tokenStream.reset();
			//创建一个属性，这个属性会添加到流中，会随着tokenStream增加
			CharTermAttribute cta = tokenStream.addAttribute(CharTermAttribute.class);
			while(tokenStream.incrementToken()) {
				System.out.print("["+cta+"]");
			}
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (tokenStream != null) {
				try {
					tokenStream.close();
				} catch (IOException e) {
					 e.printStackTrace();
				}
			}
		}
	 }
	 
	 public static void displayAllTokenInfo(String str, Analyzer analyzer) {
		 TokenStream tokenStream = null;
		 try {
			tokenStream = analyzer.tokenStream("content", new StringReader(str));
			tokenStream.reset();
			PositionIncrementAttribute pia = tokenStream.addAttribute(PositionIncrementAttribute.class);
			OffsetAttribute osa = tokenStream.addAttribute(OffsetAttribute.class);
			CharTermAttribute cta = tokenStream.addAttribute(CharTermAttribute.class);
			TypeAttribute ta = tokenStream.addAttribute(TypeAttribute.class);
			while (tokenStream.incrementToken()) {
				System.out.print(pia.getPositionIncrement()+":");
				System.out.print(cta+"["+osa.startOffset()+"-"+osa.endOffset()+"]"+"--->"+ta.type()+"\n");
			}
			System.out.println();
		} catch (IOException e) {
			 e.printStackTrace();
		}
	 }
}

 