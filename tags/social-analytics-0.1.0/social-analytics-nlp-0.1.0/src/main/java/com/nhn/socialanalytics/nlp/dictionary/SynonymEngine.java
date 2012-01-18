package com.nhn.socialanalytics.nlp.dictionary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class SynonymEngine {

	private static String synonymDictionary = "";
	private static RAMDirectory directory;
	private static IndexSearcher searcher;
	private static SynonymEngine instance;
	private List<String> synonyms = new ArrayList<String>();
	
	public static SynonymEngine getInstance(String dictionaryFile) {
		if (instance == null || !synonymDictionary.equals(dictionaryFile)) {
			synonymDictionary = dictionaryFile;
			instance = new SynonymEngine();			
		}
		return instance;
	}
	
	private SynonymEngine() {
		loadDictionary();		
		createSynonymIndex();
		
		try {
			searcher = new IndexSearcher(directory);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private void loadDictionary() {
		synonyms = DictionaryFactory.loadDictionary(synonymDictionary);	
	}

	private void createSynonymIndex() {
		directory = new RAMDirectory();

		try {
			Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_33);
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_33, analyzer);

			IndexWriter ramWriter = new IndexWriter(directory, iwc);
			
			for(String syn : synonyms) {
				String[] synonymWords = syn.split(",");
				Document doc = new Document();
				
				if (synonymWords != null && synonymWords.length > 0) {
					String fieldValue = synonymWords[0].toLowerCase();
					Field fieldStandard = new Field("standard", fieldValue, Store.YES, Index.NOT_ANALYZED_NO_NORMS, TermVector.NO);
					doc.add(fieldStandard);
				}
				
				for(int i = 0; i < synonymWords.length ; i++) {
					String fieldValue = synonymWords[i].toLowerCase();
					Field fieldSynonym = new Field("synonym", fieldValue, Store.YES, Index.NOT_ANALYZED_NO_NORMS, TermVector.NO);
					doc.add(fieldSynonym);					
				}
				ramWriter.addDocument(doc);
			}
			
			ramWriter.optimize();
			ramWriter.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getSynonyms(String word) throws Exception {
		List<String> synonyms = new ArrayList<String>();
		
		if (word != null)
			word = word.toLowerCase();			
		
		Query query = new TermQuery(new Term("synonym", word));		
		
		TopScoreDocCollector collector = TopScoreDocCollector.create(5 * 5, false);
		searcher.search(query, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		for(int i = 0; i < hits.length; i++) {
			Document doc = searcher.doc(hits[i].doc);

			//String standard = doc.get("standard");
			String[] values = doc.getValues("synonym");			

			for(int j = 0; j < values.length; j++) {
				if(!word.equals(values[j])) {
					synonyms.add(values[j]);
				}
			}
		}
		return synonyms;
	}
	
	public String getStandardWord(String word) {
		try {
			if (word != null)
				word = word.toLowerCase();
			
			Query query = new TermQuery(new Term("synonym", word));		
			
			TopScoreDocCollector collector = TopScoreDocCollector.create(5 * 5, false);
			searcher.search(query, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			for(int i = 0; i < hits.length; i++) {
				Document doc = searcher.doc(hits[i].doc);

				String standard = doc.get("standard");
				if (standard != null)
					return standard;
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return word;
	}
	
	public static void main(String[] args) {
		try {
			//SynonymEngine synonymEngine = SynonymEngine.getInstance("com/nhn/socialanalytics/nlp/lang/ko/dic/synonym_ko.dic");
			SynonymEngine synonymEngine = SynonymEngine.getInstance("com/nhn/socialanalytics/nlp/lang/ja/dic/synonym_ja.dic");
			List<String> synonyms = synonymEngine.getSynonyms("アプリ");			
			System.out.println("synonyms == " + synonyms);
			
			String standard = synonymEngine.getStandardWord("アプリ");			
			System.out.println("standard == " + standard);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
