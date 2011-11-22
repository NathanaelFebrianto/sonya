package com.nhn.socialanalytics.miner.index;

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
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
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
		
		//Analyzer luceneAnalyzer = new StopAnalyzer(Version.LUCENE_33, MY_STOP_WORDS_SET);	// removes numbers too
		Analyzer luceneAnalyzer = new StandardAnalyzer(Version.LUCENE_33, MY_STOP_WORDS_SET);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_33, luceneAnalyzer);
		
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		indexWriter = new IndexWriter(indexDir, config);
	}
	
	public void write(DetailDoc doc) throws IOException, CorruptIndexException {
		String site = doc.getSite();
		String object = doc.getObject();
		String collectDate = doc.getCollectDate();
		String docId = doc.getDocId();
		String date = doc.getDate();
		String userId = doc.getUserId();
		String userName = doc.getUserName();
		String subject = doc.getSubject();
		String predicate = doc.getPredicate();
		String attribute = doc.getAttribute();
		String text = doc.getText();		
		
		if (subject == null || subject.trim().equals(""))
			subject = "tagempty";
		if (predicate == null || predicate.trim().equals(""))
			predicate = "tagempty";
		if (attribute == null || attribute.trim().equals(""))
			attribute = "tagempty";
		if (text == null || text.trim().equals(""))
			text = "tagempty";
			
		Document document = new Document();
		document.add(new Field(FieldConstants.SITE, site, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
		document.add(new Field(FieldConstants.OBJECT, object, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
		document.add(new Field(FieldConstants.COLLECT_DATE, collectDate, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
		document.add(new Field(FieldConstants.DOC_ID, docId, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.NO));
		document.add(new Field(FieldConstants.DATE, date, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
		document.add(new Field(FieldConstants.USER_ID, userId, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
		document.add(new Field(FieldConstants.USER_NAME, userName, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
		document.add(new Field(FieldConstants.SUBJECT, subject, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
		document.add(new Field(FieldConstants.PREDICATE, predicate, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
		document.add(new Field(FieldConstants.ATTRIBUTE, attribute, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
		document.add(new Field(FieldConstants.TEXT, text, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));

		indexWriter.addDocument(document);
	}
	
	public void update(String field, String text, Document doc) throws IOException, CorruptIndexException {
		Term term = new Term(field, text);
		indexWriter.updateDocument(term, doc);
	}
	
	public void close() throws Exception {
		indexWriter.optimize();
		indexWriter.close();
	}
	
}