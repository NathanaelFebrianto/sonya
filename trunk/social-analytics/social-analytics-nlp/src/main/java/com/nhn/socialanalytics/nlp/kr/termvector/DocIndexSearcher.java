package com.nhn.socialanalytics.nlp.kr.termvector;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

public class DocIndexSearcher {

	private IndexSearcher searcher;
	private IndexReader reader;
	
	public DocIndexSearcher(String indexDir) throws IOException, CorruptIndexException {
		FSDirectory fsIndexDir = FSDirectory.open(new File(indexDir));
		this.searcher = new IndexSearcher(fsIndexDir, true);
		this.reader = searcher.getIndexReader();
	}
	
}
