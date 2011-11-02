package com.nhn.socialanalytics.nlp.kr.semantic;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SemanticClause implements Serializable {

	private int subjectId;
	private int predicateId;
	private int modifierId;
	private String subject;	
	private String predicate;
	private String standardLabel;
	private int polarity; 
	private double strength;
	private String modifiers;
	
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	public int getPredicateId() {
		return predicateId;
	}
	public void setPredicateId(int predicateId) {
		this.predicateId = predicateId;
	}
	public int getModifierId() {
		return modifierId;
	}
	public void setModifierId(int modifierId) {
		this.modifierId = modifierId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getPredicate() {
		return predicate;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	public String getStandardLabel() {
		return standardLabel;
	}
	public void setStandardLabel(String standardLabel) {
		this.standardLabel = standardLabel;
	}
	public int getPolarity() {
		return polarity;
	}
	public void setPolarity(int polarity) {
		this.polarity = polarity;
	}
	public double getStrength() {
		return strength;
	}
	public void setStrength(double strength) {
		this.strength = strength;
	}
	public String getModifiers() {
		return modifiers;
	}
	public void setModifiers(String modifiers) {
		this.modifiers = modifiers;
	}
	
}
