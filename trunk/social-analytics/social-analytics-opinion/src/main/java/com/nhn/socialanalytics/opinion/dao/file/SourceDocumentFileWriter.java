package com.nhn.socialanalytics.opinion.dao.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.nhn.socialanalytics.opinion.model.SourceDocument;

public class SourceDocumentFileWriter {

	private BufferedWriter writer;

	public SourceDocumentFileWriter(File file) throws IOException {
		writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file.getPath(), true), "UTF-8"));

		BufferedReader reader = new BufferedReader(new FileReader(file));
		String header = reader.readLine();
		if (header == null || header.equals(""))
			writeHeader();
	}

	private void writeHeader() throws IOException {
		writer.write(SourceDocument.getHeaderString());
		writer.newLine();
	}

	public void write(SourceDocument doc) {
		try {
			writer.write(doc.toString());
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() throws IOException {
		writer.close();
	}

}
