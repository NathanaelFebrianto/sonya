package com.nhn.socialanalytics.miner.opinion;

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
	
	public void setBaseTermFilter(String field, int minTF, boolean excludeStopwords) {
		this.setBaseTermFilter(new BaseTermFilter(field, minTF, excludeStopwords));
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
	
	public void addLinkedTermFilter(String field, int minTF, int linkedMinTF) {
		linkedTermFilters.add(new LinkedTermFilter(field, minTF, linkedMinTF));
	}

	class BaseTermFilter {		
		private String field;
		private int minTF;
		private boolean excludeStopwords;
		
		public BaseTermFilter(String field, int minTF, boolean excludeStopwords) {
			this.field = field;
			this.minTF = minTF;
			this.excludeStopwords = excludeStopwords;
		}
		
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		public int getMinTF() {
			return minTF;
		}
		public void setMinTF(int minTF) {
			this.minTF = minTF;
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
		private int minTF;	
		private int linkedMinTF;
		
		public LinkedTermFilter(String field, int minTF, int linkedMinTF) {
			this.field = field;
			this.minTF = minTF;
			this.linkedMinTF = linkedMinTF;
		}
		
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		public int getMinTF() {
			return minTF;
		}
		public void setMinTF(int minTF) {
			this.minTF = minTF;
		}
		public int getLinkedMinTF() {
			return linkedMinTF;
		}
		public void setLinkedMinTF(int linkedMinTF) {
			this.linkedMinTF = linkedMinTF;
		}
	}

}
