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
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermsFilter;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.mahout.common.iterator.FileLineIterator;

import com.nhn.socialanalytics.nlp.kr.sentiment.SentimentAnalyzer;

public class DocIndexSearcher {

	private IndexSearcher searcher;
	private SentimentAnalyzer sentiment;
	private static final String LIWC_CAT_FILE = "./bin/liwc/LIWC_ko.txt";
	
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
		this(indexDirs, null, null);
	} 
	
	public DocIndexSearcher(File[] indexDirs, File stopwordFile, Set<String> customStopwords) throws IOException, CorruptIndexException {
		List<IndexReader> indexReaders = getIndexReaders(indexDirs);
		MultiReader multiReader = new MultiReader(indexReaders.toArray(new IndexReader[0]));  
		this.searcher = new IndexSearcher(multiReader);	
		
		Set<String> stopwords = loadStopwords(stopwordFile);
		stopwordSet.addAll((Set<String>)DEFAULT_STOPWORDS);
		stopwordSet.addAll(stopwords);
		
		if (customStopwords != null)
			stopwordSet.addAll(customStopwords);
		
		sentiment = SentimentAnalyzer.getInstance(LIWC_CAT_FILE);
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
	
	public int getTF(String object, String field, String text) 
			throws IOException, CorruptIndexException {		
		Term objTerm = new Term(FieldConstants.OBJECT, object);		
		TermsFilter filter = new TermsFilter();
		filter.addTerm(objTerm);
		
		Term term = new Term(field, text);
		Query query = new TermQuery(term);		

		TopDocs rs = searcher.search(query, filter, 1000000);
		//System.out.println("docFreq == " + rs.totalHits);

		return rs.totalHits;
	}
	
	public Map<String, Integer> getTerms(String object, String field, int minTF, boolean excludeStopwords) 
			throws IOException, CorruptIndexException {		
		Map<String, Integer> terms = new HashMap<String, Integer>();
		
		Term term = new Term(FieldConstants.OBJECT, object);
		Query query = new TermQuery(term);		

		TopDocs rs = searcher.search(query, 1000000);
		//System.out.println("docFreq == " + rs.totalHits);

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
	
	public TargetTerm search(String object, String searchField, String outField, String searchText, int minTF) throws Exception {
		
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
		
		double searchTextPolarity = sentiment.analyzePolarity(searchText);
		result.setPolarity(searchTextPolarity);

		for (int i = 0; i < rs.totalHits; i++) {
			int docId = rs.scoreDocs[i].doc;
			Document doc = searcher.doc(docId);
			String outText = doc.get(outField);

			DetailDoc detailDoc = new DetailDoc();
			detailDoc.setSite(doc.get(FieldConstants.SITE));
			detailDoc.setObject(doc.get(FieldConstants.OBJECT));
			detailDoc.setCollectDate(doc.get(FieldConstants.COLLECT_DATE));
			detailDoc.setDocId(doc.get(FieldConstants.DOC_ID));
			detailDoc.setDate(doc.get(FieldConstants.DATE));
			detailDoc.setUserId(doc.get(FieldConstants.USER_ID));
			detailDoc.setUserName(doc.get(FieldConstants.USER_NAME));
			detailDoc.setLanguage(doc.get(FieldConstants.LANGUAGE));
			detailDoc.setSubject(doc.get(FieldConstants.SUBJECT));
			detailDoc.setPredicate(doc.get(FieldConstants.PREDICATE));
			detailDoc.setAttribute(doc.get(FieldConstants.ATTRIBUTE));
			detailDoc.setText(doc.get(FieldConstants.TEXT));

			if (outField.equals(FieldConstants.SUBJECT) || outField.equals(FieldConstants.PREDICATE)) {
				if (!stopwordSet.contains(outText)) {
					int tf = this.getTF(object, outField, outText);
					if (tf >= minTF) {
						ChildTerm exist = result.getChildTerm(outText);
						if (exist != null) {
							exist.addDoc(detailDoc);
						} else {
							ChildTerm childTerm = new ChildTerm();
							childTerm.setTerm(outText);
							childTerm.setTF(tf);
							childTerm.addDoc(detailDoc);
							double polarity = sentiment.analyzePolarity(outText);
							childTerm.setPolarity(polarity);
							
							result.addChildTerm(childTerm);							
							System.out.println("\n" + outField + " == " + outText + " (tf: " + tf + " polarity: " + polarity + ")");
						}
					}
				}
			} else if (outField.equals(FieldConstants.ATTRIBUTE)) {
				Map<String, Integer> mapAttribute = this.tokenizeAttributes(object, outText);

				for (Map.Entry<String, Integer> entry : mapAttribute.entrySet()) {
					String attributeText = entry.getKey();
					Integer attributeTF = (Integer) entry.getValue();

					if (!stopwordSet.contains(attributeText)) {
						if (attributeTF >= minTF) {
							ChildTerm exist = result.getChildTerm(attributeText);
							if (exist != null) {
								exist.addDoc(detailDoc);
							} else {
								ChildTerm childTerm = new ChildTerm();
								childTerm.setTerm(attributeText);
								childTerm.setTF(attributeTF);								
								childTerm.addDoc(detailDoc);								
								double polarity = sentiment.analyzePolarity(attributeText);
								childTerm.setPolarity(polarity);
								
								result.addChildTerm(childTerm);								
								System.out.println("\n" + outField + " == " + attributeText + " (tf: " + attributeTF + " polarity: " + polarity + ")");
							}
						}
					}
				}
			}
		}

		return result;
	}
	
	private Map<String, Integer> tokenizeAttributes(String object, String attributes) throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		if (attributes != null) {
			StringTokenizer st = new StringTokenizer(attributes.trim(), " ");
			while (st.hasMoreTokens()) {
				String attribute = st.nextToken();
				if (!stopwordSet.contains(attribute.trim()) && !attribute.trim().equals("")) {
					int tf = this.getTF(object, FieldConstants.ATTRIBUTE, attribute);
					map.put(attribute, new Integer(tf));
				}
			}			
		}
	
		return map;
	}
	
	public static void main(String[] args) {		
		try {	
			File[] indexDirs = new File[1];
			indexDirs[0] = new File("./bin/data/twitter/index/20111123");
			String object = "fta";
			DocIndexSearcher searcher = new DocIndexSearcher(indexDirs);
			
			Map<String, Integer> terms = searcher.getTerms(object, FieldConstants.PREDICATE, 2, true);
			System.out.println(terms);
			
			for (Map.Entry<String, Integer> entry : terms.entrySet()) {
				String term = entry.getKey();
				System.out.println("--------------------------------------\n");
				searcher.search(object, FieldConstants.PREDICATE, FieldConstants.SUBJECT, term, 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
