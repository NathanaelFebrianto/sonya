package com.nhn.socialanalytics.nlp.semantic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
public class SemanticClause implements Serializable {

	private int id = -1;
	private String mainFeature = "";
	private String subject;	
	private String standardSubject;
	private String predicate;
	private String standardPredicate;
	private Set<String> attributes = new HashSet<String>();
	private Set<String> standardAttributes = new HashSet<String>();
	private Set<String> modifiers = new HashSet<String>();
	private Set<String> standardModifiers = new HashSet<String>();
	private double polarity; 
	private double polarityStrength;
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
	public String getMainFeature() {
		return mainFeature;
	}
	public void setMainFeature(String mainFeature) {
		this.mainFeature = mainFeature;
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
	public double getPolarityStrength() {
		return polarityStrength;
	}
	public void setPolarityStrength(double polarityStrength) {
		this.polarityStrength = polarityStrength;
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
		clause.setMainFeature(mainFeature);
		clause.setSubject(subject);
		clause.setStandardSubject(standardSubject);
		clause.setPredicate(predicate);
		clause.setStandardPredicate(standardPredicate);
		clause.setAttributes(attributes);
		clause.setStandardAttributes(standardAttributes);
		clause.setModifiers(modifiers);
		clause.setStandardModifiers(standardModifiers);
		clause.setPolarity(polarity);
		clause.setPolarityStrength(polarityStrength);
		clause.setChilds(childClauses);	
		clause.setParentClause(parentClause);	
		clause.setPriority(priority);	

		return clause;
	}
	
	public String makeLabel(String delimiter, boolean includeAttribute, boolean includeModifier, boolean includeChild) {
		String label = "";
		String strSubject = "";
		String strPredicate = "";
		
		if (subject != null)	strSubject = subject;
		if (predicate != null)	strPredicate = predicate;
		label = strSubject + delimiter + strPredicate;
		
		if (includeAttribute) {
			String strAttributes = "";
			for (String attribute : attributes) {
				strAttributes = strAttributes + " " + attribute;
			}
			label = label + delimiter + strAttributes.trim();
		}
		
		if (includeModifier) {
			String strModifiers = "";
			for (String modifier : modifiers) {
				strModifiers = strModifiers + " " + modifier;
			}
			label = label + delimiter + strModifiers.trim();
		}
		
		if (includeChild) {
			String childLabels = null;
			for (SemanticClause child : childClauses) {
				if (childLabels != null)
					childLabels = childLabels + " " + child.makeLabel(delimiter, includeAttribute, includeModifier, includeChild);
				else
					childLabels = child.makeLabel(delimiter, includeAttribute, includeModifier, includeChild);
			}
			
			if (childLabels != null)
				label = childLabels + " " + label;			
		}
			
		return label;
	}
	
	public String makeStandardLabel(String delimiter, boolean includeAttribute, boolean includeModifier, boolean includeChild) {
		String label = "";
		String strSubject = "";
		String strPredicate = "";
		
		if (standardSubject != null)	strSubject = standardSubject;
		if (standardPredicate != null)	strPredicate = standardPredicate;
		label = strSubject + delimiter + strPredicate;
		
		if (includeAttribute) {
			String strAttributes = "";
			for (String attribute : standardAttributes) {
				if (!attribute.equals(""))
					strAttributes = strAttributes + " " + attribute;
			}
			label = label + delimiter + strAttributes.trim();
		}
		
		if (includeModifier) {
			String strModifiers = "";
			for (String modifier : standardModifiers) {
				if (!modifier.equals(""))
					strModifiers = strModifiers + " " + modifier;
			}
			label = label + delimiter + strModifiers.trim();
		}
		
		if (includeChild) {
			String childLabels = null;
			for (SemanticClause child : childClauses) {
				if (childLabels != null)
					childLabels = childLabels + " " + child.makeStandardLabel(delimiter, includeAttribute, includeModifier, includeChild);
				else
					childLabels = child.makeStandardLabel(delimiter, includeAttribute, includeModifier, includeChild);
			}
			
			if (childLabels != null)
				label = childLabels + " " + label;			
		}
			
		return label;
	}
	
	public String makeStandardSubjectPredicateLabel(String clauseDelimiter, String termDelimiter, boolean includeFeature, boolean includeChild) {
		String label = "";
		String strSubject = "";
		String strPredicate = "";
		
		if ((standardSubject == null || standardSubject.equals("")) 
				&& (standardPredicate == null || standardPredicate.equals(""))) {
			return "";
		}
		
		if (standardSubject != null)	strSubject = standardSubject;
		if (standardPredicate != null)	strPredicate = standardPredicate;
		label = label + strSubject + termDelimiter + strPredicate;
		
		if (includeFeature) {
			label = mainFeature + termDelimiter + label;
		}
					
		if (includeChild) {
			String childLabels = null;
			for (SemanticClause child : childClauses) {
				if (childLabels != null)
					childLabels = childLabels + clauseDelimiter + child.makeStandardSubjectPredicateLabel(clauseDelimiter, termDelimiter, includeFeature, includeChild);
				else
					childLabels = child.makeStandardSubjectPredicateLabel(clauseDelimiter, termDelimiter, includeFeature, includeChild);
			}
			
			if (childLabels != null)
				label = childLabels + clauseDelimiter + label;			
		}
		
		return label;
	}
	
	public String makeStandardSubjectAttributeLabel(String clauseDelimiter, String termDelimiter, boolean includeFeature) {
		String label = "";
		String strSubject = "";
		
		if ((standardSubject == null || standardSubject.equals("")) 
				&& (standardAttributes.size() == 0)) {
			return "";
		}
		
		if (standardSubject != null)	strSubject = standardSubject;

		label = label + strSubject;
		
		if (includeFeature) {
			label = mainFeature + termDelimiter + label;
		}
		
		int i = 0;
		for (String attribute : standardAttributes)  {
			if (!attribute.equals("")) {
				if (i < standardAttributes.size() - 1)
					label = label + termDelimiter + attribute + clauseDelimiter;
				else
					label = label + termDelimiter + attribute;
			}
		}
		
		return label;
	}

	public String makeAttributesLabel(String delimiter) {
		StringBuffer sb = new StringBuffer();
		
		int i = 0;
		for (String attribute : attributes) {
			if (!attribute.equals(""))
				if (i < standardAttributes.size() - 1)
					sb.append(attribute).append(delimiter);
				else
					sb.append(attribute);
		}		
		return sb.toString().trim();		
	}
	
	public String makeStandardAttributesLabel(String delimiter) {
		StringBuffer sb = new StringBuffer();
		
		int i = 0;
		for (String attribute : standardAttributes)  {
			if (!attribute.equals("")) {
				if (i < standardAttributes.size() - 1)
					sb.append(attribute).append(delimiter);
				else
					sb.append(attribute);
			}
		}

		return sb.toString().trim();		
	}
	
	public String makeModifiersLabel(String delimiter) {
		StringBuffer sb = new StringBuffer();
		
		int i = 0;
		for (String modifier : modifiers) {
			if (!modifier.equals("")) {
				if (i < modifiers.size() - 1)
					sb.append(modifier).append(delimiter);
				else
					sb.append(modifier);
			}
		}		
		return sb.toString().trim();		
	}
	
	public String makeStandardModifiersLabel(String delimiter) {
		StringBuffer sb = new StringBuffer();
		
		int i = 0;
		for (String modifier : standardModifiers)  {
			if (!modifier.equals("")) {
				if (i < standardModifiers.size() - 1)
					sb.append(modifier).append(delimiter);
				else
					sb.append(modifier);
			}
		}

		return sb.toString().trim();		
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer()
			//.append("id = ").append(id)	
			.append(" *priority = ").append(priority)
			.append(" *subject = ").append(subject)
			.append(" *predicate = ").append(predicate)
			.append(" *attributes = ").append(attributes.toString())
			.append(" *modifiers = ").append(modifiers.toString())			
			.append(" standardSubjectPredicateLabel = ").append(makeStandardSubjectPredicateLabel(" ", "-", false, false))
			.append(" standardLabel = ").append(makeStandardLabel("-", true, true, false))
			.append(" polarity = ").append(polarity)
			.append(" polarityStrength = ").append(polarityStrength);		
		
		for (SemanticClause child : childClauses) {
			sb.append("\n").append("  <-- ").append(child.toString());
		}
		
		return sb.toString();
	}
	
}
