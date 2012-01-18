package com.nhn.socialanalytics.common.collect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class CollectObject implements Serializable {
	
	private String site;
	private String object;
	private List<String> searchKeywords = new ArrayList<String>();
	private int maxPage;
	private int historyBufferMaxRound;
	private List<String> languages = new ArrayList<String>();
	private Map<String, String> featureClassifiers = new HashMap<String, String>();
	private Map<String, List<String>> extendedAttributes = new HashMap<String, List<String>>();
	
	public String getSite() {
		return site;
	}
	
	public void setSite(String site) {
		this.site = site;
	}
	
	public String getObject() {
		return object;
	}
	
	public void setObject(String object) {
		this.object = object;
	}
	
	public List<String> getSearchKeywords() {
		return searchKeywords;
	}
	
	public void setSearchKeywords(List<String> searchKeywords) {
		this.searchKeywords = searchKeywords;
	}
	
	public void addSearchKeyword(String searchKeyword) {
		searchKeywords.add(searchKeyword);
	}
	
	public int getMaxPage() {
		return maxPage;
	}
	
	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}
	
	public int getHistoryBufferMaxRound() {
		return historyBufferMaxRound;
	}
	
	public void setHistoryBufferMaxRound(int historyBufferMaxRound) {
		this.historyBufferMaxRound = historyBufferMaxRound;
	}
	
	public List<String> getLanguages() {
		return languages;
	}
	
	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}
	
	public void addLanguage(String languag) {
		languages.add(languag);
	}
	
	public Map<String, String> getFeatureClassifiers() {
		return featureClassifiers;
	}
	
	public void setFeatureClassifiers(Map<String, String> featureClassifiers) {
		this.featureClassifiers = featureClassifiers;
	}
	
	public void addFeatureClassifier(String language, String featureClassifier) {
		featureClassifiers.put(language, featureClassifier);
	}
	
	public Map<String, List<String>> getExtendedAttributes() {
		return extendedAttributes;
	}
	
	public void setExtendedAttributes(Map<String, List<String>> extendedAttributes) {
		this.extendedAttributes = extendedAttributes;
	}
	
	public void addExtendedAttribute(String key, String value) {
		List<String> values = extendedAttributes.get(key);
		if (values == null) {
			values = new ArrayList<String>();
			values.add(value);
			extendedAttributes.put(key, values);
		}
		else {
			values.add(value);
		}		
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer()
			.append("SITE = ").append(site).append("\n")
			.append("OBJECT_ID = ").append(object).append("\n")
			.append("SEARCH_KEYWORDS = ").append(searchKeywords).append("\n")
			.append("MAX_PAGE = ").append(maxPage).append("\n")
			.append("HISTORY_BUFFER_MAX_ROUND = ").append(historyBufferMaxRound).append("\n")
			.append("LANGUAGES = ").append(languages).append("\n")
			.append("FEATURE_CLASSIFIERS = ").append(featureClassifiers).append("\n")
			.append("EXTENDED_ATTRIBUTES = ").append(extendedAttributes).append("\n");
		
		return sb.toString();
	}
}
