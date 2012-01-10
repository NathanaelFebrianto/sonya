package com.nhn.socialanalytics.opinion.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class OpinionFilter implements Serializable {

	String object;
	String language;
	boolean byFeature = false;
	BaseTermFilter baseTermFilter;
	List<LinkedTermFilter> linkedTermFilters = new ArrayList<LinkedTermFilter>();	
	
	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	public boolean isByFeature() {
		return byFeature;
	}

	public void setByFeature(boolean byFeature) {
		this.byFeature = byFeature;
	}

	public BaseTermFilter getBaseTermFilter() {
		return baseTermFilter;
	}

	public void setBaseTermFilter(BaseTermFilter baseTermFilter) {
		this.baseTermFilter = baseTermFilter;
	}
	
	public void setBaseTermFilter(String field, int minDocFreq, boolean excludeStopwords) {
		this.setBaseTermFilter(new BaseTermFilter(field, minDocFreq, excludeStopwords));
	}

	public List<LinkedTermFilter> getLinkedTermFilters() {
		return linkedTermFilters;
	}

	public void setLinkedTermFilters(List<LinkedTermFilter> linkedTermFilters) {
		this.linkedTermFilters = linkedTermFilters;
	}
	
	public void addLinkedTermFilter(LinkedTermFilter linkedTermFilter) {
		linkedTermFilters.add(linkedTermFilter);
	}
	
	public void addLinkedTermFilter(String field, int minDocFreq, int minCooccurrentDocFreq) {
		linkedTermFilters.add(new LinkedTermFilter(field, minDocFreq, minCooccurrentDocFreq));
	}

	class BaseTermFilter {		
		private String field;
		private int minDocFreq;
		private boolean excludeStopwords;
		
		public BaseTermFilter(String field, int minDocFreq, boolean excludeStopwords) {
			this.field = field;
			this.minDocFreq = minDocFreq;
			this.excludeStopwords = excludeStopwords;
		}
		
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		public int getMinDocFreq() {
			return minDocFreq;
		}
		public void setMinDocFreq(int minDocFreq) {
			this.minDocFreq = minDocFreq;
		}
		public boolean isExcludeStopwords() {
			return excludeStopwords;
		}
		public void setExcludeStopwords(boolean excludeStopwords) {
			this.excludeStopwords = excludeStopwords;
		}		
	}	
	
	class LinkedTermFilter {
		private String field;
		private int minDocFreq;	
		private int minCooccurrentDocFreq;
		
		public LinkedTermFilter(String field, int minDocFreq, int minCooccurrentDocFreq) {
			this.field = field;
			this.minDocFreq = minDocFreq;
			this.minCooccurrentDocFreq = minCooccurrentDocFreq;
		}
		
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		public int getMinDocFreq() {
			return minDocFreq;
		}
		public void setMinDocFreq(int minDocFreq) {
			this.minDocFreq = minDocFreq;
		}
		public int getMinCooccurrentDocFreq() {
			return minCooccurrentDocFreq;
		}
		public void setMinCooccurrentDocFreq(int minCooccurrentDocFreq) {
			this.minCooccurrentDocFreq = minCooccurrentDocFreq;
		}
	}

}
