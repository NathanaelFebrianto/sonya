package com.nhn.socialanalytics.miner.opinion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
public class OpinionResultSet extends ArrayList<OpinionTerm> {
	
	private String type;
	private Set<String> features = new HashSet<String>();
	private boolean byFeature = false;
	
	public OpinionResultSet(String type) { 
		this.type = type;
	}
	
	public OpinionResultSet(String type, boolean byFeature) { 
		this.type = type;
		this.byFeature = byFeature;
	}
	
	public String getType() {
		return this.type;
	}
	
	public boolean isByFeature() {
		return this.byFeature;
	}
	
	public List<String> getFeatures() {		
		return new ArrayList<String>(features);
	}
	
	public void addFeature(String feature) {
		features.add(feature);
	}
		
}
