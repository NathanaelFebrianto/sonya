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
	private String standardSubject;
	private String predicate;
	private String standardPredicate;
	private Set<String> attributes = new HashSet<String>();
	private Set<String> standardAttributes = new HashSet<String>();
	private Set<String> modifiers = new HashSet<String>();
	private Set<String> standardModifiers = new HashSet<String>();
	private double polarity; 
	private double strength;
	private double positiveWordCount;
	private double negativeWordCount;
	private int priority;
	
	private SemanticClause parentClause;
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
	public String getStandardSubject() {
		return standardSubject;
	}
	public void setStandardSubject(String standardSubject) {
		this.standardSubject = standardSubject;
	}
	public String getPredicate() {
		return predicate;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	public String getStandardPredicate() {
		return standardPredicate;
	}
	public void setStandardPredicate(String standardPredicate) {
		this.standardPredicate = standardPredicate;
	}
	public Set<String> getAttributes() {
		return attributes;
	}
	public void setAttributes(Set<String> attributes) {
		this.attributes = attributes;
	}
	public Set<String> getStandardAttributes() {
		return standardAttributes;
	}
	public void setStandardAttributes(Set<String> standardAttributes) {
		this.standardAttributes = standardAttributes;
	}
	public Set<String> getModifiers() {
		return modifiers;
	}
	public void setModifiers(Set<String> modifiers) {
		this.modifiers = modifiers;
	}
	public Set<String> getStandardModifiers() {
		return standardModifiers;
	}
	public void setStandardModifiers(Set<String> standardModifiers) {
		this.standardModifiers = standardModifiers;
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
		
	public void addAttribute(String attribute) {
		attributes.add(attribute);
	}
	public void addStandardAttribute(String standardAttribute) {
		standardAttributes.add(standardAttribute);
	}
	public void addModifier(String modifier) {
		modifiers.add(modifier);
	}
	public void addStandardModifier(String standardModifier) {
		standardModifiers.add(standardModifier);
	}
	
	public void addChild(SemanticClause child) {
		childClauses.add(child);
		child.setParentClause(this);
	}
	public void setChilds(List<SemanticClause> childClauses) {
		this.childClauses = childClauses;
	}
	
	public SemanticClause getParentClause() {
		return this.parentClause;
	}
	public void setParentClause(SemanticClause parentClause) {
		this.parentClause = parentClause;
	}
	
	public SemanticClause clone() {
		SemanticClause clause = new SemanticClause();
		clause.setSubject(subject);
		clause.setStandardSubject(standardSubject);
		clause.setPredicate(predicate);
		clause.setStandardPredicate(standardPredicate);
		clause.setAttributes(attributes);
		clause.setStandardAttributes(standardAttributes);
		clause.setModifiers(modifiers);
		clause.setStandardModifiers(standardModifiers);
		clause.setPolarity(polarity);
		clause.setStrength(strength);
		clause.setChilds(childClauses);	
		clause.setParentClause(parentClause);	
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
			if (attributes.size() > 0) {
				for (String attribute : attributes) {
					label = label + " " + attribute + "ㅡ" + predicate;
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
	
	public String makeStandardLabel(boolean includeChild) {
		String label = null;		
		
		if (standardSubject != null && standardPredicate != null) {
			label = standardSubject + "ㅡ" + standardPredicate;
		}
		else if (standardSubject == null && standardPredicate != null) {
			label = standardPredicate;
			if (standardAttributes.size() > 0) {
				for (String attribute : standardAttributes) {
					label = label + " " + attribute + "ㅡ" + standardPredicate;
				}
			}			
		}
		else if (standardSubject != null && standardPredicate == null) {
			label = standardSubject;
		}						
		
		if (includeChild) {
			String childLabels = null;
			for (SemanticClause child : childClauses) {
				if (childLabels != null)
					childLabels = childLabels + " " + child.makeStandardLabel(includeChild);
				else
					childLabels = child.makeStandardLabel(includeChild);
			}
			
			if (childLabels != null)
				label = childLabels + " " + label;
		}
		
		return label;
	}

	public String makeAttributesLabel() {
		StringBuffer sb = new StringBuffer();
		
		for (String attribute : attributes) {
			sb.append(attribute).append(" ");
		}		
		return sb.toString();		
	}
	
	public String makeStandardAttributesLabel() {
		StringBuffer sb = new StringBuffer();
		
		for (String attribute : standardAttributes) {
			sb.append(attribute).append(" ");
		}		
		return sb.toString();		
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer()
			//.append("id = ").append(id)	
			.append(" *priority = ").append(priority)
			.append(" *subject = ").append(subject)
			.append(" *predicate = ").append(predicate)
			.append(" *attributes = ").append(attributes.toString())
			.append(" *modifiers = ").append(modifiers.toString())			
			.append(" standardLabel = ").append(makeStandardLabel(false))
			.append(" polarity = ").append(polarity)
			.append(" strength = ").append(strength);		
		
		for (SemanticClause child : childClauses) {
			sb.append("\n").append("  <-- ").append(child.toString());
		}
		
		return sb.toString();
	}
}
