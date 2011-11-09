package com.nhn.socialanalytics.nlp.kr.termvector;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class DocIndexWriter {
	
	public static final Set<?> MY_STOP_WORDS_SET;

	private FSDirectory indexDir;
	private IndexWriter indexWriter;
	
	static {
		final List<String> stopWords = Arrays.asList(
//				 "tagquestion"
//				, "tagsmile"
//				, "tagcry"
//				, "taglove"
//				, "tagexclamation"
//				, "http"
//				, "gt"
//				, "lt"
//				, "brgt"
//				, "brgt"
		);
		final CharArraySet stopSet = new CharArraySet(stopWords.size(), false);
		stopSet.addAll(stopWords);
		MY_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
	}
	
	public DocIndexWriter(String outputDir) throws IOException {
		
		File file = new File(outputDir);
		if (!file.exists())
			file.mkdir();
		
		indexDir = FSDirectory.open(file);	

		boolean isAppend = false;
		
		if (indexDir.listAll().length == 0)
			isAppend = true;
				
		//Analyzer luceneAnalyzer = new StopAnalyzer(Version.LUCENE_CURRENT, MY_STOP_WORDS_SET);	// removes numbers too
		Analyzer luceneAnalyzer = new StandardAnalyzer(Version.LUCENE_CURRENT, MY_STOP_WORDS_SET);
		indexWriter = new IndexWriter(indexDir, luceneAnalyzer, isAppend, IndexWriter.MaxFieldLength.UNLIMITED);
	}
	
	public void write(String site, String date, String user, String docId, 
			String subject, String predicate, String objects, String text) 
			throws IOException, CorruptIndexException {
		
		if (subject == null || subject.trim().equals(""))
			subject = "tagempty";
		if (predicate == null || predicate.trim().equals(""))
			predicate = "tagempty";
		if (objects == null || objects.trim().equals(""))
			objects = "tagempty";
		if (text == null || text.trim().equals(""))
			text = "tagempty";
			
		Document document = new Document();
		document.add(new Field("site", site, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
		document.add(new Field("date", date, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
		document.add(new Field("user", user, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
		document.add(new Field("docId", docId, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
		document.add(new Field("subject", subject, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
		document.add(new Field("predicate", predicate, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
		document.add(new Field("objects", objects, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
		document.add(new Field("text", text, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));

		indexWriter.addDocument(document);
	}
	
	public void close() throws Exception {
		indexWriter.optimize();
		indexWriter.close();
	}
	
}
