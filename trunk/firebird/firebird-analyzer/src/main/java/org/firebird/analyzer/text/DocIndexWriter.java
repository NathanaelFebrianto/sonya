/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * This class creates an index with Lucene from the text files.
 * 
 * @author Young-Gue Bae
 */
public class DocIndexWriter {
	
	/** file directory that contains the text files to be indexed */
	private File fileDir;
	/** index directory that hosts Lucene's index files */
	private FSDirectory indexDir;
	
	/**
	 * Constructor.
	 * 
	 * @param fileDir the file directory that contains the text files to be indexed
	 * @param indexDir the index directory that hosts Lucene's index files
	 * @exception
	 */
	public DocIndexWriter(String fileDir, String indexDir) throws Exception {
		this.fileDir = new File(fileDir);
		this.indexDir = FSDirectory.open(new File(indexDir));
	}

	/**
	 * Creates an index with Lucene from the text files.
	 * 
	 * @exception
	 */
	public void write() throws Exception {
		Analyzer luceneAnalyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
		IndexWriter indexWriter = new IndexWriter(indexDir, luceneAnalyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
		File[] textFiles = fileDir.listFiles();
		long startTime = new Date().getTime();

		// add documents to the index
		for (int i = 0; i < textFiles.length; i++) {
			if (textFiles[i].getName().endsWith(".txt")) {
				System.out.println("creating index : " + textFiles[i].getCanonicalPath());
				Reader textReader = new FileReader(textFiles[i]);
				
		        /*
				ArrayList<String> result = new ArrayList<String>();   

		        TokenStream stream = luceneAnalyzer.tokenStream("", textReader);   
		        TermAttribute termAttr = (TermAttribute)stream.getAttribute(TermAttribute.class);   
		        //OffsetAttribute offSetAttr = (OffsetAttribute)stream.getAttribute(OffsetAttribute.class);   
		  
		        //System.out.println(stream.hasAttributes());   
		        while(stream.incrementToken()) {   
		            result.add(termAttr.term());   
		            //System.out.println(offSetAttr.startOffset() + "," + offSetAttr.endOffset());   
		        }   
		  
		        System.out.println("terms : " + result + "\n");
		        */

		        // first line is the user id, rest from 3rd line is the body
		        BufferedReader reader = new BufferedReader(textReader);
		        String userId = reader.readLine();
		        System.out.println("userId == " + userId);
		        reader.readLine();// skip an empty line

		        StringBuffer bodyBuffer = new StringBuffer(1024);
		        String line = null;
		        while ((line = reader.readLine()) != null) {
		        	bodyBuffer.append(line).append(' ');
		        }
		        reader.close();
		        
		        System.out.println("content == " + bodyBuffer.toString() + "\n");
		        
				Document document = new Document();
				document.add(new Field("user", userId, Field.Store.NO, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
				document.add(new Field("content", bodyBuffer.toString(), Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.YES));
				document.add(new Field("path", textFiles[i].getPath(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
				indexWriter.addDocument(document);
			}
		}

		indexWriter.optimize();
		indexWriter.close();
		long endTime = new Date().getTime();

		System.out.println("It took "
						+ (endTime - startTime)
						+ " milliseconds to create an index from the files in the directory "
						+ fileDir.getPath());		
	}
	
	public static void main(String[] args) throws Exception {
		
	}
}
