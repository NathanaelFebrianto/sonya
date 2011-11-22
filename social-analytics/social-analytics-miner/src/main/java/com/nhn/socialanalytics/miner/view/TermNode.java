package com.nhn.socialanalytics.miner.view;

import java.util.ArrayList;

import com.nhn.socialanalytics.miner.index.DetailDoc;

public class TermNode {

	private String id;
	private String name;
	private String type;
	private int tf;		
	private ArrayList<DetailDoc> docs = new ArrayList<DetailDoc>();

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

	public ArrayList<DetailDoc> getDocs() {
		return docs;
	}

	public void setDocs(ArrayList<DetailDoc> docs) {
		this.docs = docs;
	}
	
}
