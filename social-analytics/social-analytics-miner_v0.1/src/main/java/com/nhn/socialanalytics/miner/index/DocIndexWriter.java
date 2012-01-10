package com.nhn.socialanalytics.miner.index;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopAnalyzer;
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
		);
		final CharArraySet stopSet = new CharArraySet(stopWords.size(), false);
		stopSet.addAll(stopWords);
		MY_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
	}
	
	public DocIndexWriter(File outputDir) throws IOException {		
		this(outputDir, null);
	}
	
	public DocIndexWriter(File outputDir, Analyzer luceneAnalyzer) throws IOException {
		
		if (!outputDir.exists())
			outputDir.mkdir();
		
		indexDir = FSDirectory.open(outputDir);	
		
		if (luceneAnalyzer == null) {
			luceneAnalyzer = new StopAnalyzer(Version.LUCENE_33, MY_STOP_WORDS_SET);	// removes numbers
			//luceneAnalyzer = new StandardAnalyzer(Version.LUCENE_33, MY_STOP_WORDS_SET);	// removes all hiraganas if japanese
		}

		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_33, luceneAnalyzer);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		indexWriter = new IndexWriter(indexDir, config);
	}
	
	public void write(DetailDoc doc) throws IOException, CorruptIndexException {
		String site = doc.getSite();
		String object = doc.getObject();
		String language = doc.getLanguage();
		String collectDate = doc.getCollectDate();
		String docId = doc.getDocId();
		String date = doc.getDate();
		String userId = doc.getUserId();
		String userName = doc.getUserName();
		String feature = doc.getFeature();
		String mainFeature = doc.getMainFeature();
		String clauseFeature = doc.getClauseFeature();
		String clauseMainFeature = doc.getClauseMainFeature();
		String subject = doc.getSubject();
		String predicate = doc.getPredicate();
		String attribute = doc.getAttribute();
		String text = doc.getText();
		double polarity = doc.getPolarity();
		double polarityStrength = doc.getPolarityStrength();
		double clausePolarity = doc.getClausePolarity();
		double clausePolarityStrength = doc.getClausePolarityStrength();
		
		if (subject == null || subject.trim().equals(""))
			subject = "tagempty";
		if (predicate == null || predicate.trim().equals(""))
			predicate = "tagempty";
		if (attribute == null || attribute.trim().equals(""))
			attribute = "tagempty";
		if (text == null || text.trim().equals(""))
			text = "tagempty";
			
		Document document = new Document();
		document.add(new Field(FieldConstants.SITE, site, Field.Store.YES, Field.Index.NOT_ANALYZED , Field.TermVector.NO));
		document.add(new Field(FieldConstants.OBJECT, object, Field.Store.YES, Field.Index.NOT_ANALYZED , Field.TermVector.NO));
		document.add(new Field(FieldConstants.LANGUAGE, language, Field.Store.YES, Field.Index.NOT_ANALYZED , Field.TermVector.NO));
		document.add(new Field(FieldConstants.COLLECT_DATE, collectDate, Field.Store.YES, Field.Index.NOT_ANALYZED , Field.TermVector.NO));
		document.add(new Field(FieldConstants.DOC_ID, docId, Field.Store.YES, Field.Index.NOT_ANALYZED , Field.TermVector.NO));
		document.add(new Field(FieldConstants.DATE, date, Field.Store.YES, Field.Index.NOT_ANALYZED , Field.TermVector.NO));
		document.add(new Field(FieldConstants.USER_ID, userId, Field.Store.YES, Field.Index.NOT_ANALYZED , Field.TermVector.NO));
		document.add(new Field(FieldConstants.USER_NAME, userName, Field.Store.YES, Field.Index.NOT_ANALYZED , Field.TermVector.NO));		
		document.add(new Field(FieldConstants.FEATURE, feature, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
		document.add(new Field(FieldConstants.MAIN_FEATURE, mainFeature, Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.YES));
		document.add(new Field(FieldConstants.CLAUSE_FEATURE, clauseFeature, Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.YES));
		document.add(new Field(FieldConstants.CLAUSE_MAIN_FEATURE, clauseMainFeature, Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.YES));		
		document.add(new Field(FieldConstants.SUBJECT, subject, Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.YES));
		document.add(new Field(FieldConstants.PREDICATE, predicate, Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.YES));
		document.add(new Field(FieldConstants.ATTRIBUTE, attribute, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
		document.add(new Field(FieldConstants.TEXT, text, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
		document.add(new Field(FieldConstants.POLARITY, String.valueOf(polarity), Field.Store.YES, Field.Index.NOT_ANALYZED , Field.TermVector.NO));
		document.add(new Field(FieldConstants.POLARITY_STRENGTH, String.valueOf(polarityStrength), Field.Store.YES, Field.Index.NOT_ANALYZED , Field.TermVector.NO));
		document.add(new Field(FieldConstants.CLAUSE_POLARITY, String.valueOf(clausePolarity), Field.Store.YES, Field.Index.NOT_ANALYZED , Field.TermVector.NO));
		document.add(new Field(FieldConstants.CLAUSE_POLARITY_STRENGTH, String.valueOf(clausePolarityStrength), Field.Store.YES, Field.Index.NOT_ANALYZED , Field.TermVector.NO));

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
