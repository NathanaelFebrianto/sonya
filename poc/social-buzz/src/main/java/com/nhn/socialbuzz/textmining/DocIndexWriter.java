package com.nhn.socialbuzz.textmining;

import java.io.File;
import java.util.List;

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
	public void write(List<String> texts, String ouputDir) throws Exception {

		FSDirectory indexDir = FSDirectory.open(new File(ouputDir));
		
		TextAnalyzer analyzer = new TextAnalyzer();
		
		IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_CURRENT, null); 
		IndexWriter indexWriter = new IndexWriter(indexDir, iwConfig);
		
		// add documents to the index
		for (int i = 0; i < texts.size(); i++) {

		        String text = (String) texts.get(i);
				Document document = new Document();
				//document.add(new Field("user", userId, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
				document.add(new Field("body", text, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
				//document.add(new Field("path", textFiles[i].getPath(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
				//document.add(new Field("timeline", timeline, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
				//document.add(new Field("indexDate", "20110823", Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, Field.TermVector.NO));
				indexWriter.addDocument(document);
		}

		indexWriter.optimize();
		indexWriter.close();
	}
	
	public static void main(String[] args) {

	}

}
