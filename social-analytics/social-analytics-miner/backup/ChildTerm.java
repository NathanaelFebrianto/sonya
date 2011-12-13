package com.nhn.socialanalytics.miner.index;

import java.util.ArrayList;
import java.util.List;

public class ChildTerm {

	private String term;
	private int tf;
	private int tfWithinTarget;
	private double polarity;
	private List<DetailDoc> docs = new ArrayList<DetailDoc>();
	
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public int getTF() {
		return tf;
	}
	public void setTF(int tf) {
		this.tf = tf;
	}
	public int getTFWithinTarget() {
		return tfWithinTarget;
	}
	public void setTFWithinTarget(int tfWithinTarget) {
		this.tfWithinTarget = tfWithinTarget;
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
	
	public void addDoc(DetailDoc doc) {
		docs.add(doc);
	}	
	
}
