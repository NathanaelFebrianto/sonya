/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.text;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * This class for running a LDA algorithm in Apache Mahout.
 * 
 * @author Young-Gue Bae
 */
public class LDAAnalyzer {

	/**
	 * Constructor.
	 * 
	 */
	public LDAAnalyzer() {

	}

	/**
	 * Analyzes documents by running LDA.
	 * 
	 */
	public void analyze() {

	}

	/**
	 * Creates vectors from index.
	 * 
	 */
	private void createVectors() throws Exception {
		try {
			System.out.println("createVectors..........");
			Process proc = Runtime.getRuntime().exec("mvn -e exec:java -Dexec.mainClass=\"org.apache.mahout.utils.vectors.lucene.Driver\" -Dexec.args=\"--dir D:/firebird/index/ --field content --dictOut D:/firebird/dict/dict.txt --output D:/firebird/vectors --minDF 100 --maxDFPercent 97\" || exit");
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

			// read the output from the command			
			String s = null;
			/*
			System.out.println("Here is the standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}
			*/

			// read any errors from the attempted command
			System.out.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Runs LDA.
	 * 
	 */
	private void runLDA() {

	}

	/**
	 * Writes top words for each topic.
	 * 
	 */
	private void writeTopics() {

	}

	public static void main(String[] args) throws Exception {
		LDAAnalyzer lda = new LDAAnalyzer();
		lda.createVectors();
	}

}