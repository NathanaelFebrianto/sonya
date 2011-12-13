package com.nhn.socialanalytics.nlp.feature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeatureDictionary {
	
	/** Mapping associating features to regular expression patterns. */
	private Map<String, Pattern> map;

	public FeatureDictionary(File dicFile) {
		try {
			map = loadFeatureDictionary(dicFile);
			System.err.println("LIWC dictionary loaded (" + map.size() + " lexical categories)");

		} catch (IOException e) {
			System.err.println("Error: file " + dicFile + " doesn't exist");
			e.printStackTrace();
			System.exit(1);
		} catch (NullPointerException e) {
			System.err.println("Error: feature dicitonary file " + dicFile + " doesn't have the right format");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private Map<String, Pattern> loadFeatureDictionary(File dicFile) throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(dicFile));
		String line;

		Map<String, Pattern> wordLists = new LinkedHashMap<String, Pattern>();
		String currentVariable = "";
		String catRegex = "";
		int word_count = 0;

		while ((line = reader.readLine()) != null) {
			
			//System.out.println("line == " + line);

			// if encounter new variable
			if (line.matches("\\t[\\w ]+")) {
				// add full regex to database
				if (!catRegex.equals("")) {
					catRegex = catRegex.substring(0, catRegex.length() - 1);
					catRegex = "(" + catRegex + ")";
					catRegex = catRegex.replaceAll("\\*", "[\\\\w']*");
					
					//System.out.println("catRegx1 == " + catRegex);
					
					wordLists.put(currentVariable, Pattern.compile(catRegex));
				}
				// update variable
				currentVariable = line.split("\t")[1];
				catRegex = "";

			} else if (line.matches("\t\t.+ \\(\\d+\\)")) {
				word_count++;
				String newPattern = line.split("\\s+")[1].toLowerCase();
				catRegex += "\\b" + newPattern + "\\b|";
				//catRegex += "\\" + newPattern + "|";
				
				//System.out.println("catRegx2 == " + catRegex);
			}
		}
		//  add last regex to database
		if (!catRegex.equals("")) {
			catRegex = catRegex.substring(0, catRegex.length() - 1);
			catRegex = "(" + catRegex + ")";
			catRegex = catRegex.replaceAll("\\*", "[\\\\w']*");
			
			//System.out.println("catRegx3 == " + catRegex);
			
			wordLists.put(currentVariable, Pattern.compile(catRegex));
		}

		reader.close();

		System.err.println(word_count + " words and " + wordLists.size() +"categories loaded in feature dictionary");
		return wordLists;
	}
	
	public Map<String, Double> getCounts(String text, boolean absoluteCounts) {

		Map<String,Double> counts = new LinkedHashMap<String, Double>(map.size());
		String[] words = tokenize(text);
		String[] sentences = splitSentences(text);
		
		System.err.println("Input text splitted into " + words.length
				+ " words and " + sentences.length + " sentences");
		
		// word count (NOT A PROPER FEATURE)
		if (absoluteCounts) { counts.put("WC", new Double(words.length)); }
		counts.put("WPS", new Double(1.0 * words.length / sentences.length));		

		// PATTERN MATCHING
		// store word in dic
		boolean[] indic = new boolean[words.length];
		for (int i = 0; i < indic.length; i++) {
			indic[i] = false;
		}

		// first get all lexical counts
		for (String cat: map.keySet()) {

			// add entry to output hash
			Pattern catRegex = map.get(cat);
			int catCount = 0;

			for (int i = 0; i < words.length; i++) {

				String word = words[i].toLowerCase();
				Matcher m = catRegex.matcher(word);
				while (m.find()) {
					catCount++;
					indic[i] = true;
				}
			}
			if (absoluteCounts)
				counts.put(cat, new Double(catCount));
			else
				counts.put(cat, new Double(100.0 * catCount / words.length));
		}

		// put ratio of words matched
		int wordsMatched = 0;
		for (int i = 0; i < indic.length; i++) {
			if (indic[i]) {
				wordsMatched++;
			}
		}
		if (absoluteCounts)
			counts.put("DIC", new Double(wordsMatched));			
		else
			counts.put("DIC", new Double(100.0 * wordsMatched / words.length));

		return counts;
	}
	
	public static String[] tokenize(String text) {
		
		//String words_only = text.replaceAll("\\W+\\s*", " ").replaceAll("\\s+$", "").replaceAll("^\\s+", "");
		//String[] words = words_only.split("\\s+");
		String[] words = text.split("\\s+");	// for Korean
		return words;
	}	
	
	public static String[] splitSentences(String text) {
	
		return text.split("\\s*[\\.!\\?]+\\s+");
	}
	
	public static void main(String[] args) {
		FeatureDictionary featureDic = new FeatureDictionary(new File("./feature/feature_ko.txt"));
		String text = "이 제품은 기능 좋다 디자인 예쁘다";
		Map<String, Double> map = featureDic.getCounts(text, true);
		
		System.out.println("result == " + map);
	}
}
