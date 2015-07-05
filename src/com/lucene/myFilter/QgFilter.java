 package com.lucene.myFilter;

import java.io.IOException;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.SortedDocValues;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.DocValuesDocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.BitDocIdSet;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;
 public class QgFilter extends Filter {

	 private final String filterField;
	 
	 private String[] ids = new String[]{"8"};
	 
	 private SortedDocValues idDocValues;
	 
	 private TopDocs tds;
	 
	 public QgFilter(String filterField) {
		 if (null == filterField) 
			 throw new NullPointerException("过滤字段不能为空！");
		 this.filterField = filterField;
	}
	 
	@Override
	public DocIdSet getDocIdSet(LeafReaderContext context, Bits acceptDocs)
			throws IOException {
		
		idDocValues = context.reader().getSortedDocValues(filterField);
		int maxDoc = context.reader().maxDoc();
		System.out.println("idDocValues:"+idDocValues.toString());
		System.out.println("acceptDocs:"+acceptDocs.toString());
		for (String id:ids) {
			IndexSearcher searcher = new IndexSearcher(context.reader());
			tds = searcher.search(new TermQuery(new Term(filterField, id)), maxDoc);
		}
		
		return new DocIdSet() {
			
			@Override
			public long ramBytesUsed() {
				return 0;
				
			}
			
			@Override
			public DocIdSetIterator iterator() throws IOException {
				return null;
				
			}
		};
	}

	@Override
	public String toString(String field) {
		
		return "QgFilter(" + filterField + ")";
	}

}

 