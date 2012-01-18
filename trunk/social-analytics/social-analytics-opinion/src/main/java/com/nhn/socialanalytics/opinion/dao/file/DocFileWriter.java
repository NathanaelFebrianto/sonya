package com.nhn.socialanalytics.opinion.dao.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.nhn.socialanalytics.opinion.common.FieldConstants;
import com.nhn.socialanalytics.opinion.common.OpinionDocument;

public class DocFileWriter {
	
	private static final String DELIMITER = "\t";
	private BufferedWriter writer;
	
	public DocFileWriter(File file) throws IOException {
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getPath(), true), "UTF-8"));
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String header = reader.readLine();
		if (header == null || header.equals(""))
			writeHeader();
	}
	
	private void writeHeader() throws IOException {
		writer.write(FieldConstants.SITE + DELIMITER +
				FieldConstants.OBJECT + DELIMITER +
				FieldConstants.LANGUAGE + DELIMITER + 
				FieldConstants.COLLECT_DATE + DELIMITER +
				FieldConstants.DOC_ID + DELIMITER +	
				FieldConstants.DATE + DELIMITER +		
				FieldConstants.AUTHOR_ID + DELIMITER +						
				FieldConstants.AUTHOR_NAME + DELIMITER +
				FieldConstants.DOC_FEATURE + DELIMITER +	
				FieldConstants.DOC_MAIN_FEATURE + DELIMITER +
				FieldConstants.CLAUSE_FEATURE + DELIMITER +	
				FieldConstants.CLAUSE_MAIN_FEATURE + DELIMITER +	
				FieldConstants.SUBJECT + DELIMITER + 					
				FieldConstants.PREDICATE + DELIMITER +	
				FieldConstants.ATTRIBUTE + DELIMITER +		
				FieldConstants.MODIFIER + DELIMITER +		
				FieldConstants.DOC_POLARITY + DELIMITER +
				FieldConstants.DOC_POLARITY_STRENGTH+ DELIMITER +
				FieldConstants.CLAUSE_POLARITY + DELIMITER +
				FieldConstants.CLAUSE_POLARITY_STRENGTH	
				);
		writer.newLine();	
	}
	
	public void close() throws IOException {
		writer.close();
	}
	
	public void write(OpinionDocument doc) {
		try {
			writer.write(doc.getSite() + DELIMITER +
					doc.getObject() + DELIMITER +
					doc.getLanguage() + DELIMITER +
					doc.getCollectDate() + DELIMITER +
					doc.getDocId() + DELIMITER +
					doc.getDate() + DELIMITER +
					doc.getAuthorId() + DELIMITER +
					doc.getAuthorName() + DELIMITER +
					doc.getDocFeature() + DELIMITER +
					doc.getDocMainFeature() + DELIMITER +
					doc.getClauseFeature() + DELIMITER +
					doc.getClauseMainFeature() + DELIMITER +
					doc.getSubject() + DELIMITER +
					doc.getPredicate() + DELIMITER +
					doc.getAttribute() + DELIMITER +
					doc.getModifier() + DELIMITER +
					doc.getDocPolarity() + DELIMITER +
					doc.getDocPolarityStrength() + DELIMITER +
					doc.getClausePolarity() + DELIMITER +
					doc.getClausePolarityStrength()
					);
			
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
