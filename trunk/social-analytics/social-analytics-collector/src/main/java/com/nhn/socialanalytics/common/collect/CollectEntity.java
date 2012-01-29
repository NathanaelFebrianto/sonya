package com.nhn.socialanalytics.common.collect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class CollectEntity implements Serializable {
	
	private String site;
	private String entityId;
	private String entityName;
	private List<String> searchKeywords = new ArrayList<String>();
	private int maxPage;
	private int historyBufferMaxRound;
	private List<String> languages = new ArrayList<String>();
	private Map<String, String> featureDictionaries = new HashMap<String, String>();
	private String competitorDictionary;
	private Map<String, List<String>> extendedAttributes = new HashMap<String, List<String>>();
	
	public String getSite() {
		return site;
	}
	
	public void setSite(String site) {
		this.site = site;
	}
	
	public String getEntityId() {
		return entityId;
	}
	
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	
	public String getEntityName() {
		return entityName;
	}
	
	public void setEntityName(String entityName) {
		this.entityName = entityName;
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
	
	public Map<String, String> getFeatureDictionaries() {
		return featureDictionaries;
	}
	
	public void setFeatureDictionaries(Map<String, String> featureDictionaries) {
		this.featureDictionaries = featureDictionaries;
	}
	
	public void addFeatureDictionary(String language, String featureDictionary) {
		featureDictionaries.put(language, featureDictionary);
	}
	
	public String getCompetitorDictionary() {
		return competitorDictionary;
	}
	
	public void setCompetitorDictionary(String competitorDictionary) {
		this.competitorDictionary = competitorDictionary;
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
			.append("ENTITY_ID = ").append(entityId).append("\n")
			.append("ENTITY_NAME = ").append(entityName).append("\n")
			.append("SEARCH_KEYWORDS = ").append(searchKeywords).append("\n")
			.append("MAX_PAGE = ").append(maxPage).append("\n")
			.append("HISTORY_BUFFER_MAX_ROUND = ").append(historyBufferMaxRound).append("\n")
			.append("LANGUAGES = ").append(languages).append("\n")
			.append("FEATURE_DICTIONARIES = ").append(featureDictionaries).append("\n")
			.append("COMPETITOR_DICTIONARY = ").append(competitorDictionary).append("\n")
			.append("EXTENDED_ATTRIBUTES = ").append(extendedAttributes).append("\n");
		
		return sb.toString();
	}
}
