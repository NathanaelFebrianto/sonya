package com.nhn.socialanalytics.miner.view;

import java.util.ArrayList;
import java.util.List;

import com.nhn.socialanalytics.miner.index.DetailDoc;

public class TermNode {

	private String id;
	private String name;
	private String type;
	private int tf;	
	private double polarity;
	private List<DetailDoc> docs = new ArrayList<DetailDoc>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public int getTF() {
		return tf;
	}

	public void setTF(int tf) {
		this.tf = tf;
	}
	
	public double getPolarity() {
		return polarity;
	}
	
	public void setPolarity(double polarity) {
		this.polarity = polarity;
	}

	public List<DetailDoc> getDocs() {
		return docs;
	}

	public void setDocs(List<DetailDoc> docs) {
		this.docs = docs;
	}
	
}
