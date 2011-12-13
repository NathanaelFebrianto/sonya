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

import com.nhn.socialanalytics.miner.opinion.OpinionTerm;
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
	
	public void setStopwords(Set<String> stopwords) {
		this.stopwordSet = stopwords;
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
	
	public List<OpinionTerm> searchTerms(String object, String language, String field, int minTF, boolean excludeStopwords) 
			throws IOException, CorruptIndexException {	
		
		List<OpinionTerm> opinionTerms = new ArrayList<OpinionTerm>();
		
		Term objTerm = new Term(FieldConstants.OBJECT, object);	
		TermsFilter filter = new TermsFilter();
		filter.addTerm(objTerm);
		
		Term term = new Term(FieldConstants.LANGUAGE, language);
		Query query = new TermQuery(term);

		TopDocs rs = searcher.search(query, filter, 1000000);
		System.out.println("docFreq == " + rs.totalHits);
		
		// get sentiment analyzer suitable for the language
		SentimentAnalyzer sentimentAnalyzer = sentimentAnalyzers.get(language);

		for (int i = 0; i < rs.totalHits; i++) {
			int docId = rs.scoreDocs[i].doc;
			Document doc = searcher.doc(docId);
			String text = doc.get(field);			
			
			if (excludeStopwords && !stopwordSet.contains(text)) {
				int tf = this.getTF(object, field, text);
				if (tf >= minTF) {
					OpinionTerm opinionTerm = new OpinionTerm();
					opinionTerm.setType(field);
					opinionTerm.setObject(object);
					opinionTerm.setTerm(text);
					opinionTerm.setTF(tf);					

					if (sentimentAnalyzer != null) {
						double polarity = sentimentAnalyzer.analyzePolarity(text);
						opinionTerm.setPolarity(polarity);			
					}

					opinionTerms.add(opinionTerm);
				}			
			} else if (!excludeStopwords) {
				int tf = this.getTF(object, field, text);
				if (tf >= minTF) {
					OpinionTerm opinionTerm = new OpinionTerm();
					opinionTerm.setType(field);
					opinionTerm.setObject(object);
					opinionTerm.setTerm(text);
					opinionTerm.setTF(tf);

					if (sentimentAnalyzer != null) {
						double polarity = sentimentAnalyzer.analyzePolarity(text);
						opinionTerm.setPolarity(polarity);			
					}

					opinionTerms.add(opinionTerm);
				}				
			}
		}

		return opinionTerms;
	}
		
	public List<OpinionTerm> searchLinkedTerms(String object, String language, String baseField, String baseText, String linkField,  
			int minTF, int minTFwithBaseTerm) throws Exception {
		
		List<OpinionTerm> linkTerms = new ArrayList<OpinionTerm>();
		
		Term objTerm = new Term(FieldConstants.OBJECT, object);		
		TermsFilter filter = new TermsFilter();
		filter.addTerm(objTerm);
		
		Term term1 = new Term(FieldConstants.LANGUAGE, language);
		Query query1 = new TermQuery(term1);
		
		Term term2 = new Term(baseField, baseText);
		Query query2 = new TermQuery(term2);
		
		BooleanQuery bQuery = new BooleanQuery();
		bQuery.add(query1, BooleanClause.Occur.MUST);
		bQuery.add(query2, BooleanClause.Occur.MUST);

		TopDocs rs = searcher.search(bQuery, filter, 1000000);
		System.out.println("base field == " + baseField);
		System.out.println("base text == " + baseText);
		System.out.println("docFreq == " + rs.totalHits);		

		// get sentiment analyzer suitable for the language
		SentimentAnalyzer sentimentAnalyzer = sentimentAnalyzers.get(language);

		for (int i = 0; i < rs.totalHits; i++) {
			int docId = rs.scoreDocs[i].doc;
			Document doc = searcher.doc(docId);
			String linkText = doc.get(linkField);

			DetailDoc detailDoc = makeDetailDoc(doc);			

			if (linkField.equals(FieldConstants.SUBJECT) || linkField.equals(FieldConstants.PREDICATE)) {
				if (!stopwordSet.contains(linkText)) {
					int tf = this.getTF(object, linkField, linkText);
					int tfWithBaseTerm = this.getTF(object, baseField, baseText, linkField, linkText);
					if (tf >= minTF && tfWithBaseTerm >= minTFwithBaseTerm) {
						OpinionTerm linkTerm = new OpinionTerm();
						linkTerm.setType(linkField);
						linkTerm.setObject(object);
						linkTerm.setTerm(linkText);
						linkTerm.setTF(tf);
						linkTerm.setTFWithBaseTerm(tfWithBaseTerm);
						linkTerm.addDoc(detailDoc);
						
						double polarity = 0.0;
						if (sentimentAnalyzer != null) {
							polarity = sentimentAnalyzer.analyzePolarity(linkText);
							linkTerm.setPolarity(polarity);
						}
						
						linkTerms.add(linkTerm);
						
						if (sentimentAnalyzer != null)
							System.out.println("\n" + linkField + " == " + linkText + " (tf: " + tf + " polarity: " + polarity + ")");
						else
							System.out.println("\n" + linkField + " == " + linkText + " (tf: " + tf + " polarity: " + "n/a" + ")");
					}
				}
			} else if (linkField.equals(FieldConstants.ATTRIBUTE)) {
				Map<String, Integer[]> mapAttribute = this.tokenizeAttributes(object, baseField, baseText, linkText);

				for (Map.Entry<String, Integer[]> entry : mapAttribute.entrySet()) {
					String attributeText = entry.getKey();
					Integer[] attributeTFs = (Integer[]) entry.getValue();
					int attributeTF = attributeTFs[0];
					int attributeTFwithBaseTerm = attributeTFs[1];

					if (!stopwordSet.contains(attributeText)) {
						if (attributeTF >= minTF && attributeTFwithBaseTerm >= minTFwithBaseTerm) { 
							OpinionTerm linkTerm = new OpinionTerm();
							linkTerm.setType(linkField);
							linkTerm.setObject(object);
							linkTerm.setTerm(attributeText);
							linkTerm.setTF(attributeTF);
							linkTerm.setTFWithBaseTerm(attributeTFwithBaseTerm);
							linkTerm.addDoc(detailDoc);					
							
							double polarity = 0.0;
							if (sentimentAnalyzer != null) {
								polarity = sentimentAnalyzer.analyzePolarity(attributeText);
								linkTerm.setPolarity(polarity);
							}
							
							linkTerms.add(linkTerm);	
							
							if (sentimentAnalyzer != null)
								System.out.println("\n" + linkField + " == " + attributeText + " (tf: " + attributeTF + " polarity: " + polarity + ")");
							else
								System.out.println("\n" + linkField + " == " + attributeText + " (tf: " + attributeTF + " polarity: " + "n/a" + ")");
						}
					}
				}
			}
		}

		return linkTerms;
	}
	 
	private DetailDoc makeDetailDoc(Document doc) {
		DetailDoc detailDoc = new DetailDoc();
		detailDoc.setSite(doc.get(FieldConstants.SITE));
		detailDoc.setObject(doc.get(FieldConstants.OBJECT));
		detailDoc.setLanguage(doc.get(FieldConstants.LANGUAGE));
		detailDoc.setCollectDate(doc.get(FieldConstants.COLLECT_DATE));
		detailDoc.setDocId(doc.get(FieldConstants.DOC_ID));
		detailDoc.setDate(doc.get(FieldConstants.DATE));
		detailDoc.setUserId(doc.get(FieldConstants.USER_ID));
		detailDoc.setUserName(doc.get(FieldConstants.USER_NAME));
		detailDoc.setFeature(doc.get(FieldConstants.FEATURE));
		detailDoc.setMainFeature(doc.get(FieldConstants.MAIN_FEATURE));
		detailDoc.setClauseFeature(doc.get(FieldConstants.CLAUSE_FEATURE));
		detailDoc.setClauseMainFeature(doc.get(FieldConstants.CLAUSE_MAIN_FEATURE));
		detailDoc.setSubject(doc.get(FieldConstants.SUBJECT));
		detailDoc.setPredicate(doc.get(FieldConstants.PREDICATE));
		detailDoc.setAttribute(doc.get(FieldConstants.ATTRIBUTE));
		detailDoc.setText(doc.get(FieldConstants.TEXT));
		detailDoc.setPolarity(Double.valueOf(doc.get(FieldConstants.POLARITY)));
		detailDoc.setPolarityStrength(Double.valueOf(doc.get(FieldConstants.POLARITY_STRENGTH)));
		detailDoc.setClausePolarity(Double.valueOf(doc.get(FieldConstants.CLAUSE_POLARITY)));
		detailDoc.setClausePolarityStrength(Double.valueOf(doc.get(FieldConstants.CLAUSE_POLARITY_STRENGTH)));
		
		return detailDoc;
	}
	
	private Map<String, Integer[]> tokenizeAttributes(String object, String searchField, String searchText, String attributes) throws Exception {
		Map<String, Integer[]> map = new HashMap<String, Integer[]>();
		
		if (attributes != null) {
			StringTokenizer st = new StringTokenizer(attributes.trim(), " ");
			while (st.hasMoreTokens()) {
				String attribute = st.nextToken();
				if (!stopwordSet.contains(attribute.trim()) && !attribute.trim().equals("")) {
					int tf = this.getTF(object, FieldConstants.ATTRIBUTE, attribute);
					int tfWithSearchText = this.getTF(object, searchField, searchText, FieldConstants.ATTRIBUTE, attribute);
					Integer[] tfs = { tf, tfWithSearchText };
					map.put(attribute, tfs);
				}
			}			
		}
	
		return map;
	}
	
	public static void main(String[] args) {		
		try {	
			File[] indexDirs = new File[1];
			indexDirs[0] = new File("./bin/data/appstore/index/20111213");
			String object = "naverline";
			File liwcFile = new File("./bin/liwc/LIWC_ja.txt");
			DocIndexSearcher searcher = new DocIndexSearcher(indexDirs);
			searcher.putSentimentAnalyzer(FieldConstants.LANG_JAPANESE, SentimentAnalyzer.getInstance(liwcFile));
			
			//searcher.getTF(object, FieldConstants.PREDICATE, "楽しい");
			//searcher.getTF(object, FieldConstants.SUBJECT, "チャット", FieldConstants.PREDICATE, "楽しい");
			
			List<OpinionTerm> baseTerms = searcher.searchTerms(object, FieldConstants.LANG_JAPANESE, FieldConstants.SUBJECT, 20, true);
						
			for (OpinionTerm term : baseTerms) {
				System.out.println("--------------------------------------\n");
				searcher.searchLinkedTerms(object, FieldConstants.LANG_JAPANESE, term.getType(), term.getTerm(), FieldConstants.PREDICATE, 20, 20);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
