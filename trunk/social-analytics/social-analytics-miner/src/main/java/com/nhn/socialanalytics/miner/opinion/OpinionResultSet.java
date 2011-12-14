package com.nhn.socialanalytics.miner.opinion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
public class OpinionResultSet extends ArrayList<OpinionTerm> {
	
	private String type;
	private Set<String> features = new HashSet<String>();
	
	public OpinionResultSet(String type) { 
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
	
	public List<String> getFeatures() {		
		return new ArrayList<String>(features);
	}
	
	public void addFeature(String feature) {
		features.add(feature);
	}
		
}
