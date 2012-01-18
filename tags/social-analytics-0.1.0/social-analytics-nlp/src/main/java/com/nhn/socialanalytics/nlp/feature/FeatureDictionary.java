package com.nhn.socialanalytics.nlp.feature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nhn.socialanalytics.nlp.morpheme.Token;

public class FeatureDictionary {
	
	/** Mapping associating features to regular expression patterns. */
	private Map<String, Pattern> map;

	public FeatureDictionary(File catFile) {
		try {
			map = loadFeatureDictionary(catFile);
			System.err.println("LIWC dictionary loaded (" + map.size() + " lexical categories)");

		} catch (IOException e) {
			System.err.println("Error: file " + catFile + " doesn't exist");
			e.printStackTrace();
			System.exit(1);
		} catch (NullPointerException e) {
			System.err.println("Error: feature dicitonary file " + catFile + " doesn't have the right format");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private Map<String, Pattern> loadFeatureDictionary(File catFile) throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(catFile));
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
					catRegex = catRegex.replaceAll("\\*", "[\\\\w\\\\W']*");	// W: is for Korean or Japanase, w: is for English
					wordLists.put(currentVariable, Pattern.compile(catRegex));
					
					//System.out.println("catRegx1 == " + catRegex);
				}
				// update variable
				currentVariable = line.split("\t")[1];
				catRegex = "";

			} else if (line.matches("\t\t.+ \\(\\d+\\)")) {
				word_count++;
				String newPattern = line.split("\\s+")[1].toLowerCase();
				catRegex += "\\b" + newPattern + "\\b|";
				
				//System.out.println("catRegx2 == " + catRegex);
			}
		}
		
		//  add last regex to database
		if (!catRegex.equals("")) {
			catRegex = catRegex.substring(0, catRegex.length() - 1);
			catRegex = "(" + catRegex + ")";
			catRegex = catRegex.replaceAll("\\*", "[\\\\w\\\\W']*");
			wordLists.put(currentVariable, Pattern.compile(catRegex));
			
			//System.out.println("final catRegx == " + catRegex);
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
		//if (absoluteCounts) { counts.put("WC", new Double(words.length)); }
		counts.put("WC", new Double(words.length));
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
	
	public Map<String, Double> sort(Map<String, Double> map, boolean ascending) {
		List<String> mapKeys = new ArrayList<String>(map.keySet());
	    List<Double> mapValues = new ArrayList<Double>(map.values());
	    
    	Collections.sort(mapValues, new CountComparator(ascending));
    	Collections.sort(mapKeys);

	    LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
	    
	    Iterator<Double> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	        Object val = valueIt.next();
	        Iterator<String> keyIt = mapKeys.iterator();
	        
	        while (keyIt.hasNext()) {
	            Object key = keyIt.next();
	            String comp1 = map.get(key).toString();
	            String comp2 = val.toString();
	            
	            if (comp1.equals(comp2)){
	                map.remove(key);
	                mapKeys.remove(key);
	                sortedMap.put((String)key, (Double)val);
	                break;
	            }
	        }

	    }
	    return sortedMap;
	}
	
	class CountComparator implements Comparator<Double> {
		private boolean ascending = true;
		
		public CountComparator(boolean ascending) {
			this.ascending = ascending;				
		}
		
		public int compare(Double o1, Double o2) {		
			if (ascending)
				return o1.compareTo(o2);
			else
				return o2.compareTo(o1);
		}		
	}
	

	/*
	 * These codes are wrong, because it removes the duplicated values when sorting!!!
	 * 
	public TreeMap<String, Double> sort(Map<String, Double> counts, boolean ascending) {
		FeatureCountComparator comparator = new FeatureCountComparator(counts, ascending);
		TreeMap<String, Double> sortedCounts = new TreeMap<String, Double>(comparator);          
		sortedCounts.putAll(counts);
		
		System.out.println("sort == " + sortedCounts);
		
		return sortedCounts;
	}
	
	class FeatureCountComparator implements Comparator<Object> {
		Map<String, Double> map;
		boolean ascending = true;

		public FeatureCountComparator(Map<String, Double> map, boolean ascending) {
			this.map = map;
			this.ascending = ascending;
		}

		public int compare(Object o1, Object o2) {
			Double d1 = (Double) map.get(o1);
			Double d2 = (Double) map.get(o2);

			if (ascending)
				return d1.compareTo(d2);
			else
				return d2.compareTo(d1);	
		}
	}
	*/
	
	public static void main(String[] args) {
		FeatureDictionary featureDic = new FeatureDictionary(new File("./feature/feature_default_ko.txt"));
		String text = "기능 삼성 에러러 오류i1s 통화 음질이 디자인 예쁘다";
		//String text = "電話無 無料通話is 通話*";
		Map<String, Double> map = featureDic.getCounts(text, true);
		
		System.out.println("result == " + map);
	}
}
