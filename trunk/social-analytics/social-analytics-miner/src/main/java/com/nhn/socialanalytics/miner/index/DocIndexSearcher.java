package com.nhn.socialanalytics.miner.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermsFilter;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.mahout.common.iterator.FileLineIterator;

import com.nhn.socialanalytics.nlp.sentiment.SentimentAnalyzer;

public class DocIndexSearcher {

	private IndexSearcher searcher;
	private Map<String, SentimentAnalyzer> sentimentAnalyzers = new HashMap<String, SentimentAnalyzer>();
	
	private Set<String> stopwordSet = new HashSet<String>();
	private static final Set<?> DEFAULT_STOPWORDS;
	static {
		final List<?> stopWords = Arrays.asList(
				 "tagquestion"
				, "tagsmile"
				, "tagcry"
				, "taglove"
				, "tagexclamation"
				, "tagempty"
				, "http"
				, "gt"
				, "lt"
				, "brgt"
				, "brgt"
		);
		final CharArraySet stopSet = new CharArraySet(stopWords.size(), false);
		stopSet.addAll(stopWords);
		DEFAULT_STOPWORDS = CharArraySet.unmodifiableSet(stopSet);
	}
	
	public DocIndexSearcher(File[] indexDirs) throws IOException, CorruptIndexException {
		List<IndexReader> indexReaders = getIndexReaders(indexDirs);
		MultiReader multiReader = new MultiReader(indexReaders.toArray(new IndexReader[0]));  
		this.searcher = new IndexSearcher(multiReader);	
		
		stopwordSet.addAll((Set<String>)DEFAULT_STOPWORDS);	
	} 
	
	public void putStopwordFile(File stopwordFile) throws IOException {
		Set<String> stopwords = loadStopwords(stopwordFile);
		stopwordSet.addAll(stopwords);
	}
	
	public void putCustomStopwords(Set<String> customStopwords) {
		if (customStopwords != null)
			stopwordSet.addAll(customStopwords);
	}

	public void putSentimentAnalyzer(String language, SentimentAnalyzer analyzer) {
		if (analyzer != null) {
			sentimentAnalyzers.put(language, analyzer);
		}			
	} 
	
	private List<IndexReader> getIndexReaders(File[] indexDirs) 
			throws IOException, CorruptIndexException {
		List<IndexReader> readers = new ArrayList<IndexReader>();
		
		for (int i = 0; i < indexDirs.length; i++) {
			try {
				IndexReader reader = IndexReader.open(FSDirectory.open(indexDirs[i]));
				readers.add(reader);
			} catch (IndexNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
		return readers;
	}
	
	private Set<String> loadStopwords(File stopwordFile) throws IOException {
		Set<String> stopSet = new HashSet<String>();
		
		if (stopwordFile == null)
			return stopSet;
		
		FileLineIterator it = new FileLineIterator(new FileInputStream(stopwordFile));

		while (it.hasNext()) {
			String line = it.next();
			line = line.trim();
			if (line.startsWith("#")) {
				continue;
			}
			
			if (!line.equals("")) {
				stopSet.add(line);
			}
		}
		return stopSet;
	}
	
	public Set<String> getStopwords() {
		return this.stopwordSet;
	}
	
	public int getTF(String object, String queryField, String queryText) throws IOException, CorruptIndexException {		
		Term objTerm = new Term(FieldConstants.OBJECT, object);		
		TermsFilter filter = new TermsFilter();
		filter.addTerm(objTerm);
		
		Term term = new Term(queryField, queryText);
		Query query = new TermQuery(term);		

		TopDocs rs = searcher.search(query, filter, 1000000);
		//System.out.println("query field == " + queryField + ", query text == " + queryText + ", docFreq == " + rs.totalHits);
		
		return rs.totalHits;
	}
	
	public int getTF(String object, String queryField1, String queryText1, String queryField2, String queryText2) 
			throws IOException, CorruptIndexException {
		Term objTerm = new Term(FieldConstants.OBJECT, object);	
		TermsFilter filter = new TermsFilter();
		filter.addTerm(objTerm);
		
		Term term1 = new Term(queryField1, queryText1);
		Query query1 = new TermQuery(term1);
		
		Term term2 = new Term(queryField2, queryText2);
		Query query2 = new TermQuery(term2);
		
		BooleanQuery bQuery = new BooleanQuery();
		bQuery.add(query1, BooleanClause.Occur.MUST);
		bQuery.add(query2, BooleanClause.Occur.MUST);

		TopDocs rs = searcher.search(bQuery, filter, 1000000);
		
		return rs.totalHits;
	}
	
	public Map<String, Integer> getTerms(String object, String language, String field, int minTF, boolean excludeStopwords) 
			throws IOException, CorruptIndexException {		
		Map<String, Integer> terms = new HashMap<String, Integer>();
		
		Term objTerm = new Term(FieldConstants.OBJECT, object);	
		TermsFilter filter = new TermsFilter();
		filter.addTerm(objTerm);
		
		Term term = new Term(FieldConstants.LANGUAGE, language);
		Query query = new TermQuery(term);

		TopDocs rs = searcher.search(query, filter, 1000000);
		System.out.println("docFreq == " + rs.totalHits);

		for (int i = 0; i < rs.totalHits; i++) {
			int docId = rs.scoreDocs[i].doc;
			Document doc = searcher.doc(docId);
			String text = doc.get(field);
			
			if (excludeStopwords && !stopwordSet.contains(text)) {
				int tf = this.getTF(object, field, text);
				if (tf >= minTF) {
					terms.put(text, new Integer(tf));
				}			
			} else if (!excludeStopwords) {
				int tf = this.getTF(object, field, text);
				if (tf >= minTF) {
					terms.put(text, new Integer(tf));
				}				
			}
		}

		return terms;
	}
		
	public Set<Document> searchDocuments(String field, String text) 
			throws IOException, CorruptIndexException {		
		Set<Document> docs = new HashSet<Document>();
		
		Term term = new Term(field, text);
		Query query = new TermQuery(term);		

		TopDocs rs = searcher.search(query, null, 1000);
		System.out.println("docFreq == " + rs.totalHits);

		for (int i = 0; i < rs.totalHits; i++) {
			int docId = rs.scoreDocs[i].doc;
			Document doc = searcher.doc(docId);
			docs.add(doc);
		}

		return docs;
	}
	
	public TargetTerm search(String object, String language, String searchField, String outField, String searchText, 
			int minTF, int minTFwithinTarget) throws Exception {
		
		TargetTerm result = new TargetTerm();
		result.setObject(object);
		result.setTerm(searchText);
		
		Term objTerm = new Term(FieldConstants.OBJECT, object);		
		TermsFilter filter = new TermsFilter();
		filter.addTerm(objTerm);
		
		Term term = new Term(searchField, searchText);
		Query query = new TermQuery(term);

		TopDocs rs = searcher.search(query, filter, 1000000);
		System.out.println("search field == " + searchField);
		System.out.println("search word == " + searchText);
		System.out.println("docFreq == " + rs.totalHits);		

		result.setTF(rs.totalHits);
		
		// get sentiment analyzer suitable for the language
		SentimentAnalyzer sentimentAnalyzer = sentimentAnalyzers.get(language);
		if (sentimentAnalyzer != null) {
			double searchTextPolarity = sentimentAnalyzer.analyzePolarity(searchText);
			result.setPolarity(searchTextPolarity);			
		}

		for (int i = 0; i < rs.totalHits; i++) {
			int docId = rs.scoreDocs[i].doc;
			Document doc = searcher.doc(docId);
			String outText = doc.get(outField);

			DetailDoc detailDoc = new DetailDoc();
			detailDoc.setSite(doc.get(FieldConstants.SITE));
			detailDoc.setObject(doc.get(FieldConstants.OBJECT));
			detailDoc.setLanguage(doc.get(FieldConstants.LANGUAGE));
			detailDoc.setCollectDate(doc.get(FieldConstants.COLLECT_DATE));
			detailDoc.setDocId(doc.get(FieldConstants.DOC_ID));
			detailDoc.setDate(doc.get(FieldConstants.DATE));
			detailDoc.setUserId(doc.get(FieldConstants.USER_ID));
			detailDoc.setUserName(doc.get(FieldConstants.USER_NAME));
			detailDoc.setSubject(doc.get(FieldConstants.SUBJECT));
			detailDoc.setPredicate(doc.get(FieldConstants.PREDICATE));
			detailDoc.setAttribute(doc.get(FieldConstants.ATTRIBUTE));
			detailDoc.setText(doc.get(FieldConstants.TEXT));
			detailDoc.setPolarity(Double.valueOf(doc.get(FieldConstants.POLARITY)));
			detailDoc.setPolarityStrength(Double.valueOf(doc.get(FieldConstants.POLARITY_STRENGTH)));
			detailDoc.setClausePolarity(Double.valueOf(doc.get(FieldConstants.CLAUSE_POLARITY)));
			detailDoc.setClausePolarityStrength(Double.valueOf(doc.get(FieldConstants.CLAUSE_POLARITY_STRENGTH)));
			
			result.addDoc(detailDoc);

			if (outField.equals(FieldConstants.SUBJECT) || outField.equals(FieldConstants.PREDICATE)) {
				if (!stopwordSet.contains(outText)) {
					int tf = this.getTF(object, outField, outText);
					int tfWithinTarget = this.getTF(object, searchField, searchText, outField, outText);
					if (tf >= minTF && tfWithinTarget >= minTFwithinTarget) {
						ChildTerm exist = result.getChildTerm(outText);
						if (exist != null) {
							exist.addDoc(detailDoc);
						} else {
							ChildTerm childTerm = new ChildTerm();
							childTerm.setTerm(outText);
							childTerm.setTF(tf);
							childTerm.setTFWithinTarget(tfWithinTarget);
							childTerm.addDoc(detailDoc);
							
							double polarity = 0.0;
							if (sentimentAnalyzer != null) {
								polarity = sentimentAnalyzer.analyzePolarity(outText);
								childTerm.setPolarity(polarity);
							}
							
							result.addChildTerm(childTerm);	
							
							if (sentimentAnalyzer != null)
								System.out.println("\n" + outField + " == " + outText + " (tf: " + tf + " polarity: " + polarity + ")");
							else
								System.out.println("\n" + outField + " == " + outText + " (tf: " + tf + " polarity: " + "n/a" + ")");
						}
					}
				}
			} else if (outField.equals(FieldConstants.ATTRIBUTE)) {
				Map<String, Integer[]> mapAttribute = this.tokenizeAttributes(object, searchField, searchText, outText);

				for (Map.Entry<String, Integer[]> entry : mapAttribute.entrySet()) {
					String attributeText = entry.getKey();
					Integer[] attributeTFs = (Integer[]) entry.getValue();
					int attributeTF = attributeTFs[0];
					int attributeTFWithinTarget = attributeTFs[1];

					if (!stopwordSet.contains(attributeText)) {
						if (attributeTF >= minTF && attributeTFWithinTarget >= minTFwithinTarget) { 
							ChildTerm exist = result.getChildTerm(attributeText);
							if (exist != null) {
								exist.addDoc(detailDoc);
							} else {
								ChildTerm childTerm = new ChildTerm();
								childTerm.setTerm(attributeText);
								childTerm.setTF(attributeTF);
								childTerm.setTFWithinTarget(attributeTFWithinTarget);
								childTerm.addDoc(detailDoc);					
								
								double polarity = 0.0;
								if (sentimentAnalyzer != null) {
									polarity = sentimentAnalyzer.analyzePolarity(attributeText);
									childTerm.setPolarity(polarity);
								}
								
								result.addChildTerm(childTerm);		
								
								if (sentimentAnalyzer != null)
									System.out.println("\n" + outField + " == " + attributeText + " (tf: " + attributeTF + " polarity: " + polarity + ")");
								else
									System.out.println("\n" + outField + " == " + attributeText + " (tf: " + attributeTF + " polarity: " + "n/a" + ")");
							}
						}
					}
				}
			}
		}

		return result;
	}
	
	private Map<String, Integer[]> tokenizeAttributes(String object, String searchField, String searchText, String attributes) throws Exception {
		Map<String, Integer[]> map = new HashMap<String, Integer[]>();
		
		if (attributes != null) {
			StringTokenizer st = new StringTokenizer(attributes.trim(), " ");
			while (st.hasMoreTokens()) {
				String attribute = st.nextToken();
				if (!stopwordSet.contains(attribute.trim()) && !attribute.trim().equals("")) {
					int tf = this.getTF(object, FieldConstants.ATTRIBUTE, attribute);
					int tfWithinTarget = this.getTF(object, searchField, searchText, FieldConstants.ATTRIBUTE, attribute);
					Integer[] tfs = { tf, tfWithinTarget };
					map.put(attribute, tfs);
				}
			}			
		}
	
		return map;
	}
	
	public static void main(String[] args) {		
		try {	
			File[] indexDirs = new File[1];
			indexDirs[0] = new File("./bin/data/appstore/index/20111212");
			String object = "naverline";
			File liwcFile = new File("./bin/liwc/LIWC_ja.txt");
			DocIndexSearcher searcher = new DocIndexSearcher(indexDirs);
			searcher.putSentimentAnalyzer(FieldConstants.LANG_JAPANESE, SentimentAnalyzer.getInstance(liwcFile));
			
			//searcher.getTF(object, FieldConstants.PREDICATE, "楽しい");
			//searcher.getTF(object, FieldConstants.SUBJECT, "チャット", FieldConstants.PREDICATE, "楽しい");
			
			Map<String, Integer> terms = searcher.getTerms(object, FieldConstants.LANG_JAPANESE, FieldConstants.PREDICATE, 2, true);
			System.out.println(terms);
			
			for (Map.Entry<String, Integer> entry : terms.entrySet()) {
				String term = entry.getKey();
				System.out.println("--------------------------------------\n");
				searcher.search(object, FieldConstants.LANG_JAPANESE, FieldConstants.PREDICATE, FieldConstants.SUBJECT, term, 10, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
