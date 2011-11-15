package com.nhn.socialanalytics.miner.termvector;

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
		this(outputDir, false);
	}
	
	public DocIndexWriter(String outputDir, boolean append) throws IOException {
		
		File file = new File(outputDir);
		if (!file.exists())
			file.mkdir();
		
		indexDir = FSDirectory.open(file);	
		
		boolean newCreate = true;
		if (append) 
			newCreate = false;
		
		//Analyzer luceneAnalyzer = new StopAnalyzer(Version.LUCENE_33, MY_STOP_WORDS_SET);	// removes numbers too
		Analyzer luceneAnalyzer = new StandardAnalyzer(Version.LUCENE_33, MY_STOP_WORDS_SET);
		indexWriter = new IndexWriter(indexDir, luceneAnalyzer, newCreate, IndexWriter.MaxFieldLength.UNLIMITED);
	}
	
	public void write(DetailDoc doc) throws IOException, CorruptIndexException {
		String site = doc.getSite();
		String date = doc.getDate();
		String userId = doc.getUserId();
		String userName = doc.getUserName();
		String docId = doc.getDocId();
		String subject = doc.getSubject();
		String predicate = doc.getPredicate();
		String objects = doc.getObjects();
		String text = doc.getText();		
		
		if (subject == null || subject.trim().equals(""))
			subject = "tagempty";
		if (predicate == null || predicate.trim().equals(""))
			predicate = "tagempty";
		if (objects == null || objects.trim().equals(""))
			objects = "tagempty";
		if (text == null || text.trim().equals(""))
			text = "tagempty";
			
		Document document = new Document();
		document.add(new Field(FieldConstants.SITE, site, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
		document.add(new Field(FieldConstants.DATE, date, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
		document.add(new Field(FieldConstants.USER_ID, userId, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
		document.add(new Field(FieldConstants.USER_NAME, userName, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
		document.add(new Field(FieldConstants.DOC_ID, docId, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
		document.add(new Field(FieldConstants.SUBJECT, subject, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
		document.add(new Field(FieldConstants.PREDICATE, predicate, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
		document.add(new Field(FieldConstants.OBJECT, objects, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
		document.add(new Field(FieldConstants.TEXT, text, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));

		indexWriter.addDocument(document);
	}
	
	public void close() throws Exception {
		indexWriter.optimize();
		indexWriter.close();
	}
	
}
