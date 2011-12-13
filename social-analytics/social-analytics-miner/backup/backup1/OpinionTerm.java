package com.nhn.socialanalytics.miner.opinion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nhn.socialanalytics.miner.index.DetailDoc;

@SuppressWarnings("serial")
public class OpinionTerm implements Serializable {
	
	private String type;
	private String object;
	private String term;
	private int tf;
	private int tfWithBaseTerm;
	private double polarity;
	private Map<String, List<OpinionTerm>> linkedTerms = new HashMap<String, List<OpinionTerm>>();
	private List<DetailDoc> docs = new ArrayList<DetailDoc>();

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
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
	
	public int getTFWithBaseTerm() {
		return tfWithBaseTerm;
	}
	
	public void setTFWithBaseTerm(int tfWithBaseTerm) {
		this.tfWithBaseTerm = tfWithBaseTerm;
	}	
	
	public double getPolarity() {
		return polarity;
	}
	
	public void setPolarity(double polarity) {
		this.polarity = polarity;
	}
	
	public List<OpinionTerm> getLinkedTerms(String type) {
		return linkedTerms.get(type);
	}
	
	public Map<String, List<OpinionTerm>> getLinkedTerms() {
		return linkedTerms;
	}
	
	public void putLinkedTerms(String type, List<OpinionTerm> linkedTerms) {
		this.linkedTerms.put(type, linkedTerms);
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
		
	public OpinionTerm getLinkedTerm(String type, String term) {
		List<OpinionTerm> terms = linkedTerms.get(type);
		
		if (terms == null)
			return null;
		
		for (OpinionTerm linkedTerm : terms) {
			if (linkedTerm.getTerm().equals(term)) {
				return linkedTerm;
			}
		}		
		return null;
	}
	
	/*
	public void addLinkedTerm(String type, OpinionTerm linkedTerm) {
		if (this.getLinkedTerm(type, linkedTerm.getTerm()) == null) {
			List<OpinionTerm> terms = linkedTerms.get(type);
			
			if (terms == null) {
				terms = new ArrayList<OpinionTerm>();
				linkedTerms.put(type, terms);
			}
			
			terms.add(linkedTerm);
		}
	}
	*/

}
