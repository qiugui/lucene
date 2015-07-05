 package com.lucene.myQueryParser;

import com.lucene.utils.test.TestSearcherUtil;
 

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.NumericRangeQuery;
 public class QgQueryParser extends QueryParser {

	public QgQueryParser(String field, Analyzer analyzer) {
		super(field,analyzer);
	}

	@Override
	protected org.apache.lucene.search.Query getFuzzyQuery(String field,
			String termStr, float minSimilarity) throws ParseException {
		throw new ParseException("为了性能，禁用模糊查询！");
		 
	}
	
	@Override
	protected org.apache.lucene.search.Query getWildcardQuery(String field,
			String termStr) throws ParseException {
		throw new ParseException("为了性能，禁用通配符查询");
	}
	/**
	 * 其测试 可见{@link TestSearcherUtil}的{@link searchByQueryParser}方法
	 */
	@Override
	protected org.apache.lucene.search.Query getRangeQuery(String field,
			String part1, String part2, boolean startInclusive,
			boolean endInclusive) throws ParseException {
		if ("attach".equals(field)) {
			System.out.println("进入自定义QueryParser");
			org.apache.lucene.search.Query query = 
					NumericRangeQuery.newIntRange(
							field, 
							Integer.parseInt(part1), 
							Integer.parseInt(part2), 
							startInclusive, endInclusive);
			return query;
		}
		System.out.println("进入超类");
		 return super.getRangeQuery(field, part1, part2, startInclusive, endInclusive);
	}
}

 