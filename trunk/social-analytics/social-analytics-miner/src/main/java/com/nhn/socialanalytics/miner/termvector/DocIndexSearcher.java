package com.nhn.socialanalytics.miner.termvector;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class DocIndexSearcher {

	private IndexSearcher searcher;
	private IndexReader reader;
	private Map<String, Map> dictionaryMap = new HashMap<String, Map>();
	
	public DocIndexSearcher(String indexDir) throws IOException, CorruptIndexException {
		FSDirectory fsIndexDir = FSDirectory.open(new File(indexDir));
		this.searcher = new IndexSearcher(fsIndexDir, true);
		this.reader = searcher.getIndexReader();
	}
	
	public void loadDictionaries() throws IOException {
		DocTermVectorReader dtvr = new DocTermVectorReader();
		
		Map<String, Integer> mapPredicates = dtvr.loadTermDictionary("./bin/dic_predicate.txt", false);
		Map<String, Integer> mapSubjects = dtvr.loadTermDictionary("./bin/dic_subject.txt", false);
		Map<String, Integer> mapObjects = dtvr.loadTermDictionary("./bin/dic_object.txt", false);
		
		dictionaryMap.put("predicate", mapPredicates);
		dictionaryMap.put("subject", mapSubjects);
		dictionaryMap.put("objects", mapObjects);		
	}
	
	public int getTF(String dicType, String term) throws Exception {
		if (dictionaryMap.isEmpty())
			throw new Exception("Dictionaies are empty. Load dictionaries first!");
		
		Map<String, Integer> map = dictionaryMap.get(dicType);		
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if(entry.getKey().equals(term)) {
				int tf = (Integer) entry.getValue();
				return tf;
			}
		}
		
		return 0;		
	}
	
	public TargetTerm searchTerms(String searchfield, String outfield, String searchword, int minTF) throws Exception {
		TargetTerm result = new TargetTerm();
		result.setTerm(searchword);

		Term term = new Term(searchfield, searchword);
		Query query = new TermQuery(term);

		TopDocs rs = searcher.search(query, null, 1000);
		System.out.println("search field == " + searchfield);
		System.out.println("search word == " + searchword);
		System.out.println("docFreq == " + rs.totalHits);

		result.setTF(rs.totalHits);

		for (int i = 0; i < rs.totalHits; i++) {
			int docId = rs.scoreDocs[i].doc;
			Document doc = searcher.doc(docId);
			String outword = doc.get(outfield);

			//System.out.println("\nsource text == " + doc.get("text"));

			DetailDoc detailDoc = new DetailDoc();
			detailDoc.setSite(doc.get("site"));
			detailDoc.setDate(doc.get("date"));
			detailDoc.setUser(doc.get("user"));
			detailDoc.setDocId(doc.get("docId"));
			detailDoc.setText(doc.get("text"));

			if (outfield.equals("subject")) {
				if (!DocTermVectorReader.MY_STOP_WORDS_SET.contains(outword)) {
					int tf = this.getTF(outfield, outword);
					if (tf >= minTF) {
						ChildTerm exist = result.getChildTerm(outword);
						if (exist != null) {
							exist.addDoc(detailDoc);
						} else {
							ChildTerm childTerm = new ChildTerm();
							childTerm.setTerm(outword);
							childTerm.setTF(tf);
							childTerm.addDoc(detailDoc);
							result.addChildTerm(childTerm);
							
							System.out.println("\n" + outfield + " == " + outword
									+ " (tf: " + tf + ")");
						}
					}
				}
			} else if (outfield.equals("objects")) {
				Map<String, Integer> mapObjects = this.tokenizeObjects(outword);

				for (Map.Entry<String, Integer> entry : mapObjects.entrySet()) {
					String objTerm = entry.getKey();
					Integer objTF = (Integer) entry.getValue();

					if (!DocTermVectorReader.MY_STOP_WORDS_SET
							.contains(outword)) {
						if (objTF >= minTF) {
							ChildTerm exist = result.getChildTerm(objTerm);
							if (exist != null) {
								exist.addDoc(detailDoc);
							} else {
								ChildTerm childTerm = new ChildTerm();
								childTerm.setTerm(objTerm);
								childTerm.setTF(objTF);
								childTerm.addDoc(detailDoc);
								result.addChildTerm(childTerm);
								
								System.out.println("\n" + outfield + " == "
										+ objTerm + " (tf: " + objTF + ")");
							}
						}
					}
				}
			}
		}

		return result;
	}
	
	private Map<String, Integer> tokenizeObjects(String objects) throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		if (objects != null) {
			StringTokenizer st = new StringTokenizer(objects.trim(), " ");
			while (st.hasMoreTokens()) {
				String object = st.nextToken();
				if (!object.trim().equals("")) {
					int tf = this.getTF("objects", object);
					map.put(object, new Integer(tf));
				}
			}			
		}
	
		return map;
	}
	
	public static void main(String[] args) {		
		try {	
			DocIndexSearcher searcher = new DocIndexSearcher("./bin/index/");
			searcher.loadDictionaries();
			
			DocTermVectorReader reader = new DocTermVectorReader();
			Map<String, Integer> predicates = reader.getTerms("./bin/dic_predicate.txt", 3);
			
			for (Map.Entry<String, Integer> entry : predicates.entrySet()) {
				String term = entry.getKey();
				int tf = (Integer) entry.getValue();
				System.out.println("--------------------------------------\n");
				searcher.searchTerms("predicate", "subject", term, 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
