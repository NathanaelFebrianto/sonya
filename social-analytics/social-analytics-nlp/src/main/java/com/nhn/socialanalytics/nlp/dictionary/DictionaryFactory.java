package com.nhn.socialanalytics.nlp.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DictionaryFactory {

	public static List<String> loadDictionary(String dictionaryFile) {
		BufferedReader in = null;
		InputStream inputStream = DictionaryFactory.class.getClassLoader().getResourceAsStream(dictionaryFile);

		List<String> words = new ArrayList<String>();

		try {
			String line = "";
			in = new BufferedReader( new InputStreamReader(inputStream ,"utf-8"));
			
			while( (line = in.readLine()) != null ) {
				if (line.startsWith("#")) {
					continue;
				}
				words.add(line);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return words;
	}
}
