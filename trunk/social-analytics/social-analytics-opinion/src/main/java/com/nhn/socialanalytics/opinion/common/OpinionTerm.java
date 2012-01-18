package com.nhn.socialanalytics.opinion.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class OpinionTerm implements Serializable {
	
	private String id;
	private String type;
	private String object;
	private String feature;
	private String term;
	private int docFreq;
	private int cooccurrentDocFreq;
	private double polarity;
	private Map<String, List<OpinionTerm>> linkedTerms = new HashMap<String, List<OpinionTerm>>();
	private List<OpinionDocument> docs = new ArrayList<OpinionDocument>();

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
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
	
	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}
	
	public String getTerm() {
		return term;
	}
	
	public void setTerm(String term) {
		this.term = term;
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
	
	public List<OpinionTerm> getLinkedTerms(String type) {
		return linkedTerms.get(type);
	}
	
	public Map<String, List<OpinionTerm>> getLinkedTerms() {
		return linkedTerms;
	}
	
	public void putLinkedTerms(String type, List<OpinionTerm> linkedTerms) {
		this.linkedTerms.put(type, linkedTerms);
	}
	
	public List<OpinionDocument> getDocs() {
		return docs;
	}
	
	public OpinionDocument getDoc(String docId) {
		for (OpinionDocument doc : docs) {
			if (doc.getDocId().equals(docId))
				return doc;
		}
		return null;
	}
	
	public void setDocs(List<OpinionDocument> docs) {
		this.docs = docs;
	}
	
	public void addDoc(OpinionDocument doc) {
		if (doc == null)
			return;
		
		if (!docs.contains(doc) && this.getDoc(doc.getDocId()) == null)
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

}
