package com.nhn.socialanalytics.nlp.kr.semantic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
public class SemanticClause implements Serializable {

	private int id = -1;
	private String subject;	
	private String predicate;
	private Set<String> objects = new HashSet<String>();
	private Set<String> modifiers = new HashSet<String>();
	private String standardLabel;
	private int polarity; 
	private double strength;
	private int seq;
	
	private List<SemanticClause> childClauses = new ArrayList<SemanticClause>();
	
	public SemanticClause() {
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Set<String> getObjects() {
		return objects;
	}
	public void setObjects(Set<String> objects) {
		this.objects = objects;
	}
	public Set<String> getModifiers() {
		return modifiers;
	}
	public void setModifiers(Set<String> modifiers) {
		this.modifiers = modifiers;
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
	
	public void addObject(String object) {
		objects.add(object);
	}	
	public void addModifier(String modifier) {
		modifiers.add(modifier);
	}
	
	public void addChild(SemanticClause child) {
		childClauses.add(child);
	}
	public void setChilds(List<SemanticClause> childClauses) {
		this.childClauses = childClauses;
	}
	
	public SemanticClause clone() {
		SemanticClause clause = new SemanticClause();
		clause.setSubject(subject);
		clause.setPredicate(predicate);
		clause.setObjects(objects);
		clause.setModifiers(modifiers);
		clause.setStandardLabel(standardLabel);
		clause.setPolarity(polarity);
		clause.setStrength(strength);
		clause.setChilds(childClauses);	

		return clause;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer()
			//.append("id = ").append(id)	
			.append(" *subject = ").append(subject)
			.append(" *predicate = ").append(predicate)
			.append(" *objects = ").append(objects.toString())
			.append(" *modifiers = ").append(modifiers.toString())			
			.append(" standardLabel = ").append(standardLabel)
			.append(" polarity = ").append(polarity)
			.append(" strength = ").append(strength);		
		
		for (SemanticClause child : childClauses) {
			sb.append("\n").append("  <-- ").append(child.toString());
		}
		
		return sb.toString();
	}
}
