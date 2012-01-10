package com.nhn.socialanalytics.opinion.view;

import java.util.ArrayList;
import java.util.List;

import com.nhn.socialanalytics.opinion.common.DetailDoc;

public class TermNode {

	private String id;
	private String name;
	private String type;
	private int docFreq;	
	private int cooccurrentDocFreq;
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
	
	public int getDocFreq() {
		return docFreq;
	}

	public void setDocFreq(int docFreq) {
		this.docFreq = docFreq;
	}
	
	public int getCooccurrentDocFreq() {
		return cooccurrentDocFreq;
	}
	
	public void setCooccurrentDocFreq(int cooccurrentDocFreq) {
		this.cooccurrentDocFreq = cooccurrentDocFreq;
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
