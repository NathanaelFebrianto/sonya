package com.nhn.socialanalytics.miner.opinion;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class OpinionResultSet extends ArrayList<OpinionTerm>{
	
	private String type;
	
	public OpinionResultSet(String type) { 
		this.type = type;
	}
	
	public String getType() {
		return this.type;
	}
		
}
