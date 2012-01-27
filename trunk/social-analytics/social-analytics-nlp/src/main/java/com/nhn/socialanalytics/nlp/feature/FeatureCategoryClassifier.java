package com.nhn.socialanalytics.nlp.feature;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class FeatureCategoryClassifier {

	private FeatureDictionary dictionary;
	
	/** Arrays of features that aren't included in one instance analysis (corpus analysis only). **/
	private static final String[] absoluteCountFeatures = { "WC", "WPS", "DIC" };
	/** Set of features that aren't included in the models. **/
	private Set<String> absoluteCountFeatureSet;
	
	public FeatureCategoryClassifier(File catFile) {
		try {
			dictionary = new FeatureDictionary(catFile);
			
			// load absolute features
			absoluteCountFeatureSet = new LinkedHashSet<String>(Arrays.asList(absoluteCountFeatures));	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Map<String, Double> getFeatureCategoryCounts(String text, boolean absoluteCounts) {
		
		Map<String, Double> counts = new LinkedHashMap<String, Double>();	
		
		if (text == null || text.trim().equals("")) {
			counts.put("ETC", 0.0);
			return counts;
		}
		
		Map<String, Double> initCounts = dictionary.getCounts(text, absoluteCounts);
		Map<String, Double> sortedCounts = dictionary.sort(initCounts, false);
		
		sortedCounts.keySet().removeAll(absoluteCountFeatureSet);
		
		for (String feature : sortedCounts.keySet()) { 
			double count = (Double) sortedCounts.get(feature);
			if (count > 0.0)
				counts.put(feature, count);
		}
		
		if (counts.size() == 0)
			counts.put("ETC", 0.0);
		
		return counts;
	}
	
	public String getMainFeatureCategory(String text, boolean absoluteCounts) {
		Map<String, Double> featureCounts = this.getFeatureCategoryCounts(text, absoluteCounts);
		
		int i = 0;
		for (String feature : featureCounts.keySet()) { 
			if (i == 0)
				return feature;
		}
		
		return "ETC";
	}
	
	public String getMainFeatureCategory(Map<String, Double> featureCounts) {

		int i = 0;
		for (String feature : featureCounts.keySet()) { 
			if (i == 0)
				return feature;
		}
		
		return "ETC";
	}
	
	
	public static void main(String[] args) {
		FeatureCategoryClassifier classifier = new FeatureCategoryClassifier(new File("./dic/feature/feature_mobile_ko.txt"));
		
		String text = "기능이 에러가 대박이 삼성이 통화 음성in 무료통화os 오류 좋다 디자인 예쁘다 한국 갤럭시";
		Map<String, Double> featureCategories = classifier.getFeatureCategoryCounts(text, true);
		System.out.println("matched feature categories == " + featureCategories);
		System.out.println("main feature category == " + classifier.getMainFeatureCategory(text, true));
	}
}
