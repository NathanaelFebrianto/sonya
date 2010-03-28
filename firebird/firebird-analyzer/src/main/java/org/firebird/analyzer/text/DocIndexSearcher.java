/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.text;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/**
 * This class searches from the index with Lucene.
 * 
 * @author Young-Gue Bae
 */
public class DocIndexSearcher {

	/** index directory that hosts Lucene's index files */
	private FSDirectory indexDir;
	
	/**
	 * Constructor.
	 * 
	 * @param indexDir the index directory that hosts Lucene's index files
	 * @exception
	 */
	public DocIndexSearcher(String indexDir) throws Exception {
		this.indexDir = FSDirectory.open(new File(indexDir));		
	}
	
	public void search(String word) throws Exception {
		IndexSearcher searcher = new IndexSearcher(indexDir, true);
		IndexReader reader = searcher.getIndexReader();
		
		Term term = new Term("body", word);
		Query query = new TermQuery(term);
		
		System.out.println("--------------------------------------");
		System.out.println("search term == " + term.toString());
		
		//int docFreq = searcher.docFreq(term);
		//System.out.println("docFreq == " + docFreq);		
		
		TopDocs rs = searcher.search(query, null, 10);
		System.out.println("total hits(docFreq) == " + rs.totalHits);
		System.out.println("--------------------------------------\n");
		
		for (int i = 0; i < rs.totalHits; i++) {
			int docId = rs.scoreDocs[i].doc;
			Document doc = searcher.doc(docId);
			System.out.println("docId == " + docId + 
					", docFile == " + doc.get("path") + 
					", user == " + doc.getField("user").stringValue());
			
			TermFreqVector tfv = reader.getTermFreqVector(docId, "body");
			int[] tfs = tfv.getTermFrequencies();
			int termFreq = tfs[tfv.indexOf(word)];			
			
			Explanation explanation = searcher.explain(query, docId);
			System.out.println(explanation.toString());
			
			Explanation[] details = explanation.getDetails();
			System.out.println("termFreq == " + termFreq);
			System.out.println("tf == " + details[0].getValue());
			System.out.println("idf == " + details[1].getValue());
			System.out.println("fieldNorm == " + details[2].getValue());
			System.out.println("--------------------------------------\n");
		}		
	}
	
	public static void main(String[] args) throws Exception {
		DocIndexSearcher searcher = new DocIndexSearcher("D:/firebird/index/");
		searcher.search("iphone");
	}
}
