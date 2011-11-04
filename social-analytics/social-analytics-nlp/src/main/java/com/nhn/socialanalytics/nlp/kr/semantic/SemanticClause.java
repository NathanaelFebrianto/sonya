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
	private double polarity; 
	private double strength;
	private double positiveWordCount;
	private double negativeWordCount;
	private int priority;
	
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
	public double getPolarity() {
		return polarity;
	}
	public void setPolarity(double polarity) {
		this.polarity = polarity;
	}
	public double getStrength() {
		return strength;
	}
	public void setStrength(double strength) {
		this.strength = strength;
	}
	public double getPositiveWordCount() {
		return positiveWordCount;
	}
	public void setPositiveWordCount(double positiveWordCount) {
		this.positiveWordCount = positiveWordCount;
	}
	public double getNegativeWordCount() {
		return negativeWordCount;
	}
	public void setNegativeWordCount(double negativeWordCount) {
		this.negativeWordCount = negativeWordCount;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
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
		clause.setPriority(priority);	

		return clause;
	}
	
	public String makeLabel(boolean includeChild) {
		String label = null;		
		
		if (subject != null && predicate != null) {
			label = subject + "ㅡ" + predicate;
		}
		else if (subject == null && predicate != null) {
			label = predicate;
			if (objects.size() > 0) {
				for (String object : objects) {
					label = label + " " + object + "ㅡ" + predicate;
				}
			}			
		}
		else if (subject != null && predicate == null) {
			label = subject;
		}						
		
		if (includeChild) {
			String childLabels = null;
			for (SemanticClause child : childClauses) {
				if (childLabels != null)
					childLabels = childLabels + " " + child.makeLabel(includeChild);
				else
					childLabels = child.makeLabel(includeChild);
			}
			
			if (childLabels != null)
				label = childLabels + " " + label;			
		}
		
		return label;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer()
			//.append("id = ").append(id)	
			.append(" *priority = ").append(priority)
			.append(" *subject = ").append(subject)
			.append(" *predicate = ").append(predicate)
			.append(" *objects = ").append(objects.toString())
			.append(" *modifiers = ").append(modifiers.toString())			
			.append(" standardLabel = ").append(makeLabel(false))
			.append(" polarity = ").append(polarity)
			.append(" strength = ").append(strength);		
		
		for (SemanticClause child : childClauses) {
			sb.append("\n").append("  <-- ").append(child.toString());
		}
		
		return sb.toString();
	}
}
