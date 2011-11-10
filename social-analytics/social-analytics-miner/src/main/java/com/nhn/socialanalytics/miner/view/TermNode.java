package com.nhn.socialanalytics.miner.view;

import java.util.ArrayList;

import com.nhn.socialanalytics.miner.termvector.DetailDoc;

public class TermNode {

	private String id;
	private String term;
	private String type;
	private int tf;		
	private ArrayList<DetailDoc> docs = new ArrayList<DetailDoc>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
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

	public ArrayList<DetailDoc> getDocs() {
		return docs;
	}

	public void setDocs(ArrayList<DetailDoc> docs) {
		this.docs = docs;
	}
	
}
