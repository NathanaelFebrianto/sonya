package com.nhn.textmining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Date;

import org.apache.lucene.analysis.kr.KoreanTokenizer;
import org.apache.lucene.analysis.kr.morph.MorphAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class DocIndexWriter {

	public DocIndexWriter() {
		
	}
	
	/**
	 * Creates an index with Lucene from the text files.
	 * 
	 * @param inputDir the file directory that contains the text files to be indexed
	 * @param ouputDir the index directory that hosts Lucene's index files
	 * @exception
	 */
	public void write(String inputDir, String ouputDir) throws Exception {
		File fileDir = new File(inputDir);
		FSDirectory indexDir = FSDirectory.open(new File(ouputDir));
		
		MorphAnalyzer analyzer = new MorphAnalyzer();
		KoreanTokenizer tokenizer = new KoreanTokenizer(new StringReader(""));
		
		IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_CURRENT, tokenizer); 
		IndexWriter indexWriter = new IndexWriter(indexDir, iwConfig);
		File[] textFiles = fileDir.listFiles();
		
		// add documents to the index
		for (int i = 0; i < textFiles.length; i++) {
			if (textFiles[i].getName().endsWith(".txt")) {
				logger.info("creating index : " + textFiles[i].getCanonicalPath());
				
				Reader textReader = new FileReader(textFiles[i]);
				
		        // first line is the user id, rest from 3rd line is the body
		        BufferedReader reader = new BufferedReader(textReader);
		        String timeline = reader.readLine();
		        String userId = reader.readLine();
		        logger.info("timeline == " + timeline);
		        logger.info("userId == " + userId);
		        reader.readLine();// skip an empty line

		        StringBuffer bodyBuffer = new StringBuffer(1024);
		        String line = null;
		        while ((line = reader.readLine()) != null) {
		        	bodyBuffer.append(line).append(' ');
		        }
		        reader.close();
		        
		        logger.info("body == " + bodyBuffer.toString()+ "\n");
		        
				Document document = new Document();
				document.add(new Field("user", userId, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
				document.add(new Field("body", bodyBuffer.toString(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
				document.add(new Field("path", textFiles[i].getPath(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
				document.add(new Field("timeline", timeline, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
				document.add(new Field("indexDate", ConvertUtil.convertDateToString("yyyyMMdd", new Date()), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
				indexWriter.addDocument(document);
			}
		}

		indexWriter.optimize();
		indexWriter.close();
	}
	
	public static void main(String[] args) {

	}

}
