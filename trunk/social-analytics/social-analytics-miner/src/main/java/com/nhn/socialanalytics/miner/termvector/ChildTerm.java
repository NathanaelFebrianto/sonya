package com.nhn.socialanalytics.miner.termvector;

import java.util.ArrayList;

public class ChildTerm {

	private String term;
	private int tf;
	private ArrayList<DetailDoc> docs = new ArrayList<DetailDoc>();
	
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
	public ArrayList<DetailDoc> getDocs() {
		return docs;
	}
	public void setDocs(ArrayList<DetailDoc> docs) {
		this.docs = docs;
	}
	
	public void addDoc(DetailDoc doc) {
		docs.add(doc);
	}	
	
}
