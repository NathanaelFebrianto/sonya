package com.nhn.socialanalytics.miner.index;

import java.util.ArrayList;

public class TargetTerm {

	private String object;
	private String term;
	private int tf;	
	private ArrayList<ChildTerm> childTerms = new ArrayList<ChildTerm>();
	
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
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
	public ArrayList<ChildTerm> getChildTerms() {
		return childTerms;
	}
	public void setChildTerms(ArrayList<ChildTerm> childTerms) {
		this.childTerms = childTerms;
	}
	
	public ChildTerm getChildTerm(String term) {
		for (ChildTerm childTerm : childTerms) {
			if (childTerm.getTerm().equals(term)) {
				return childTerm;
			}
		}
		
		return null;
	}
	
	public void addChildTerm(ChildTerm childTerm) {
		if (this.getChildTerm(childTerm.getTerm()) == null) {
			//System.out.println("added term == " + childTerm.getTerm());
			childTerms.add(childTerm);
		}
	}

}
	