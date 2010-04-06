/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.topic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.mahout.common.FileLineIterator;

/**
 * This class reads output data from output files.
 * dict.txt
 * topics.txt
 * users.txt
 * topic_users.txt
 * 
 * @author Young-Gue Bae
 */
public class OutputFileReader {
	
	private static final Pattern TAB_PATTERN = Pattern.compile("\t");
	
	/**
	 * Constructor.
	 * 
	 */
	public OutputFileReader() { }
	
	/**
	 * Reads in a dictionary file. Format is: First line is the number of entries.
	 * 
	 * @param dictFile the dictionary file
	 * @return List<Dictionary> the list of dictionary
	 * @exception
	 */
	public static List<Dictionary> loadTermDictionary(File dictFile) throws IOException {
		List<Dictionary> result = new ArrayList<Dictionary>();
		
		InputStream is = new FileInputStream(dictFile);
		FileLineIterator it = new FileLineIterator(is);

		int numEntries = Integer.parseInt(it.next());
		System.out.println(numEntries);

		while (it.hasNext()) {
			String line = it.next();
			if (line.startsWith("#")) {
				continue;
			}
			String[] tokens = OutputFileReader.TAB_PATTERN.split(line);
			if (tokens.length < 3) {
				continue;
			}		
			
			String term = tokens[0];
			int docFreq = Integer.parseInt(tokens[1]);
			int seq = Integer.parseInt(tokens[2]);
			
			Dictionary dict = new Dictionary();
			dict.setSeq(seq);
			dict.setWord(term);
			dict.setDocFreq(docFreq);
			
			result.add(dict);
		}
		return result;
	}

}
