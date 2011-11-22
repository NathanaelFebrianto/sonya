package com.nhn.socialanalytics.miner.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

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

import com.nhn.socialanalytics.miner.termvector.ChildTerm;
import com.nhn.socialanalytics.miner.termvector.DetailDoc;
import com.nhn.socialanalytics.miner.termvector.FieldConstants;
import com.nhn.socialanalytics.miner.termvector.TargetTerm;

public class DocIndexSearcher {

	private IndexSearcher searcher;
	private Set<String> stopwordSet = new HashSet<String>();
	
	public DocIndexSearcher(String[] indexDirs) throws IOException, CorruptIndexException {
		List<IndexReader> indexReaders = getIndexReaders(indexDirs);
		MultiReader multiReader = new MultiReader(indexReaders.toArray(new IndexReader[0]));  
		this.searcher = new IndexSearcher(multiReader);	
	} 
	
	private List<IndexReader> getIndexReaders(String[] indexDirs) 
			throws IOException, CorruptIndexException {
		List<IndexReader> readers = new ArrayList<IndexReader>();
		
		for (int i = 0; i < indexDirs.length; i++) {
			try {
				IndexReader reader = IndexReader.open(FSDirectory.open(new File(indexDirs[i])));
				readers.add(reader);
			} catch (IndexNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
		return readers;
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
	
	public Map<String, Integer> getTerms(String object, String field, int minTF) 
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
			
			int tf = this.getTF(object, field, text);
			if (tf >= minTF) {
				terms.put(text, new Integer(tf));
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
							result.addChildTerm(childTerm);
							
							System.out.println("\n" + outField + " == " + outText + " (tf: " + tf + ")");
						}
					}
				}
			} else if (outField.equals(FieldConstants.ATTRIBUTE)) {
				Map<String, Integer> mapAttribute = this.tokenizeAttributes(object, outText);

				for (Map.Entry<String, Integer> entry : mapAttribute.entrySet()) {
					String attributeTerm = entry.getKey();
					Integer attributeTF = (Integer) entry.getValue();

					if (!stopwordSet.contains(outText)) {
						if (attributeTF >= minTF) {
							ChildTerm exist = result.getChildTerm(attributeTerm);
							if (exist != null) {
								exist.addDoc(detailDoc);
							} else {
								ChildTerm childTerm = new ChildTerm();
								childTerm.setTerm(attributeTerm);
								childTerm.setTF(attributeTF);
								childTerm.addDoc(detailDoc);
								result.addChildTerm(childTerm);
								
								System.out.println("\n" + outField + " == " + attributeTerm + " (tf: " + attributeTF + ")");
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
				if (!attribute.trim().equals("")) {
					int tf = this.getTF(object, FieldConstants.ATTRIBUTE, attribute);
					map.put(attribute, new Integer(tf));
				}
			}			
		}
	
		return map;
	}
	
	public static void main(String[] args) {		
		try {	
			String[] indexDirs = { "./bin/twitter/index" };
			String object = "kakaotalk";
			DocIndexSearcher searcher = new DocIndexSearcher(indexDirs);
			
			Map<String, Integer> terms = searcher.getTerms(object, FieldConstants.PREDICATE, 2);
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
