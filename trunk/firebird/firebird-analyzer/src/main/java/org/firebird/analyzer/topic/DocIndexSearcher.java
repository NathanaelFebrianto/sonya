/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.topic;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.search.DefaultSimilarity;
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

	private IndexSearcher searcher;
	private IndexReader reader;
	
	/**
	 * Constructor.
	 * 
	 * @param indexDir the index directory that hosts Lucene's index files
	 * @exception
	 */
	public DocIndexSearcher(String indexDir) throws Exception {
		FSDirectory fsIndexDir = FSDirectory.open(new File(indexDir));
		this.searcher = new IndexSearcher(fsIndexDir, true);
		this.reader = searcher.getIndexReader();
	}
	
	/**
	 * Writes the users' topic words.
	 * 
	 * @param topicWords the list of unique topic words
	 * @exception
	 */
	public void write(String outputFile, List<String> topicWords) throws Exception {
		Map<String, List<UserWord>> searchedUsers = searchUsers(topicWords);

		File out = new File(outputFile);
		PrintWriter writer = new PrintWriter(new FileWriter(out));

		writer.println("#user_id	doc_id	word	term_freq	tf	idf	timeline");		
	
		Iterator<String> it = searchedUsers.keySet().iterator();
		while (it.hasNext()) {
			Object topicId = it.next();
			List<UserWord> users = (List<UserWord>) searchedUsers.get(topicId);	
			
			// sort by TF
			Collections.sort(users);
			
			for (UserWord userWord : users) {
				writer.println(userWord.getUserId() + "\t" + 
							   userWord.getDocId() + "\t" + 
							   userWord.getWord() + "\t" + 
							   userWord.getTermFreq() + "\t" + 
							   userWord.getTF() + "\t" + 
							   userWord.getIDF() + "\t" + 
							   userWord.getTimeline());
			}
		}
		writer.close();
	}

	/**
	 * Searches the users' topic words.
	 * 
	 * @param topicWords the list of unique topic words
	 * @return Map<String, List<UserWord>> the user word list by each user
	 * @exception
	 */
	public Map<String, List<UserWord>> searchUsers(List<String> topicWords) throws Exception {
		Map<String, List<UserWord>> result = new HashMap<String, List<UserWord>>();

		for (String word : topicWords) {
			List<UserWord> users = searchUsers(word);
			
			for (UserWord user : users) {
				String userId = user.getUserId();
				if (result.containsKey(userId)) {
					List<UserWord> sameUsers = (List<UserWord>)result.get(userId);
					sameUsers.add(user);
				}
				else {
					List<UserWord> sameUsers = new ArrayList<UserWord>();
					sameUsers.add(user);
					result.put(userId, sameUsers);
				}
			}
		}
		
		return result;
	}
	
	private List<UserWord> searchUsers(String word) throws Exception {		
		List<UserWord> result = new ArrayList<UserWord>();
		
		Term term = new Term("body", word);
		Query query = new TermQuery(term);
		
		System.out.println("--------------------------------------");
		System.out.println("search term == " + term.toString());
		
		//int docFreq = searcher.docFreq(term);
		//System.out.println("docFreq == " + docFreq);		
		
		TopDocs rs = searcher.search(query, null, 1000);
		System.out.println("total hits(docFreq) == " + rs.totalHits);
		System.out.println("--------------------------------------\n");
		
		DefaultSimilarity similarity = new DefaultSimilarity();		
		
		for (int i = 0; i < rs.totalHits; i++) {
			int docId = rs.scoreDocs[i].doc;
			Document doc = searcher.doc(docId);
			String docFile = doc.get("path");
			String userId = doc.getField("user").stringValue();
			String timeline = doc.getField("timeline").stringValue();
			System.out.println("docId == " + docId + 
					", docFile == " + docFile + 
					", user == " + userId +
					", timeline == " + timeline);
			
			TermFreqVector tfv = reader.getTermFreqVector(docId, "body");
			int[] tfs = tfv.getTermFrequencies();
			int termFreq = tfs[tfv.indexOf(word)];
			
			Explanation explanation = searcher.explain(query, docId);
			System.out.println(explanation.toString());
			
			float tf = similarity.tf(termFreq);
			float idf = similarity.idf(rs.totalHits, searcher.maxDoc());
			
			//System.out.println("TF == " + tf);
			//System.out.println("IDF == " + idf);
			
			UserWord userWord = new UserWord();
			userWord.setDocId(docId);
			userWord.setUserId(userId);
			userWord.setWord(word);
			userWord.setTermFreq(termFreq);
			userWord.setTF(tf);
			userWord.setIDF(idf);
			userWord.setTimeline(timeline);
			
			result.add(userWord);
		}
		
		return result;
	}

}
