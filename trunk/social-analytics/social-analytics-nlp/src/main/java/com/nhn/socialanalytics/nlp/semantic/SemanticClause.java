package com.nhn.socialanalytics.nlp.semantic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("serial")
public class SemanticClause implements Serializable {

	private int id = -1;
	private String mainFeatureCategory = "";
	private Map<String, Double> featureCategories = new HashMap<String, Double>();
	private String subject;	
	private String standardSubject;
	private String predicate;
	private String standardPredicate;
	private Set<String> attributes = new HashSet<String>();
	private Set<String> standardAttributes = new HashSet<String>();
	private Set<String> modifiers = new HashSet<String>();
	private Set<String> standardModifiers = new HashSet<String>();
	private Map<String, Boolean> competitors = new HashMap<String, Boolean>();
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
	public String getMainFeatureCategory() {
		return mainFeatureCategory;
	}
	public void setMainFeatureCategory(String mainFeatureCategory) {
		this.mainFeatureCategory = mainFeatureCategory;
	}
	public Map<String, Double> getFeatureCategories() {
		return featureCategories;
	}
	public void setFeatureCategories(Map<String, Double> featureCategories) {
		this.featureCategories = featureCategories;
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
	public Map<String, Boolean> getCompetitors() {
		return competitors;
	}
	public void setCompetitors(Map<String, Boolean> competitors) {
		this.competitors = competitors;
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
		if (attribute != null && !attribute.trim().equals(""))
			attributes.add(attribute);
	}
	public void addStandardAttribute(String standardAttribute) {
		if (standardAttribute != null && !standardAttribute.trim().equals(""))
			standardAttributes.add(standardAttribute);
	}
	public void addModifier(String modifier) {
		if (modifier != null && !modifier.trim().equals(""))
			modifiers.add(modifier);
	}
	public void addStandardModifier(String standardModifier) {
		if (standardModifier != null && !standardModifier.trim().equals(""))
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
		clause.setMainFeatureCategory(mainFeatureCategory);
		clause.setFeatureCategories(featureCategories);
		clause.setSubject(subject);
		clause.setStandardSubject(standardSubject);
		clause.setPredicate(predicate);
		clause.setStandardPredicate(standardPredicate);
		clause.setAttributes(attributes);
		clause.setStandardAttributes(standardAttributes);
		clause.setModifiers(modifiers);
		clause.setStandardModifiers(standardModifiers);
		clause.setCompetitors(competitors);
		clause.setPolarity(polarity);
		clause.setPolarityStrength(polarityStrength);
		clause.setChilds(childClauses);	
		clause.setParentClause(parentClause);	
		clause.setPriority(priority);	

		return clause;
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
			label = label.trim();
		}
		
		if (includeModifier) {
			String strModifiers = "";
			for (String modifier : standardModifiers) {
				if (!modifier.equals(""))
					strModifiers = strModifiers + " " + modifier;
			}
			label = label + delimiter + strModifiers.trim();
			label = label.trim();
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
			
		return label.trim();
	}
	
	public String makeStandardSubjectPredicateLabel(String clauseDelimiter, String termDelimiter, 
			boolean includeFeatureCategory, boolean includeCompetitor, boolean includeChild) {
		String label = "";
		String strSubject = "";
		String strPredicate = "";
		
		if ((standardSubject == null || standardSubject.trim().equals("")) 
				&& (standardPredicate == null || standardPredicate.trim().equals(""))) {
			return "";
		}
		
		if (standardSubject != null)	strSubject = standardSubject;
		if (standardPredicate != null)	strPredicate = standardPredicate;
		label = label + strSubject + termDelimiter + strPredicate;
		label = label.trim();
		
		if (includeFeatureCategory) {
			label = mainFeatureCategory + termDelimiter + label;
			label = label.trim();
		}
		
		if (includeCompetitor) {
			StringBuffer sbCompetitorLabel = new StringBuffer();
			int i = 0;
			for (Map.Entry<String, Boolean> entry : competitors.entrySet()) {
				String competitorLabel = entry.getKey() + termDelimiter + label;
				sbCompetitorLabel.append(competitorLabel);
				if (i < competitors.size() - 1)
					sbCompetitorLabel.append(clauseDelimiter);
				
				i++;
			}
			
			label = sbCompetitorLabel.toString();
			
			if (competitors.size() == 0)
				return "";
		}
		
		if (includeChild) {
			String childLabels = null;
			for (SemanticClause child : childClauses) {
				if (childLabels != null)
					childLabels = childLabels + clauseDelimiter + child.makeStandardSubjectPredicateLabel(clauseDelimiter, termDelimiter, 
							includeFeatureCategory, includeCompetitor, includeChild);
				else
					childLabels = child.makeStandardSubjectPredicateLabel(clauseDelimiter, termDelimiter, 
							includeFeatureCategory, includeCompetitor, includeChild);
			}
			
			if (childLabels != null)
				label = childLabels + clauseDelimiter + label;			
		}
		
		return label.trim();
	}
	
	public String makeStandardSubjectAttributeLabel(String clauseDelimiter, String termDelimiter, 
			boolean includeFeatureCategory, boolean includeCompetitor) {
		String label = "";
		String strSubject = "";
		
		if ((standardSubject == null || standardSubject.trim().equals("")) 
				&& (standardAttributes.size() == 0)) {
			return "";
		}
		
		if (standardSubject != null)	strSubject = standardSubject;

		label = label + strSubject;
		label = label.trim();
		
		if (includeFeatureCategory) {
			label = mainFeatureCategory + termDelimiter + label;
			label = label.trim();
		}
		
		if (includeCompetitor) {
			StringBuffer sbCompetitorLabel = new StringBuffer();
			int i = 0;
			for (Map.Entry<String, Boolean> entry : competitors.entrySet()) {
				String competitorLabel = entry.getKey() + termDelimiter + label;
				int j = 0;
				for (String attribute : standardAttributes)  {
					if (!attribute.equals("")) {
						String attributeLabel = competitorLabel + termDelimiter + attribute;
						sbCompetitorLabel.append(attributeLabel);
						if (j < standardAttributes.size() - 1)
							sbCompetitorLabel.append(clauseDelimiter);
					}
					j++;
				}
				if (standardAttributes.size() == 0)
					sbCompetitorLabel.append(clauseDelimiter);
				
				if (i < competitors.size() - 1)
					sbCompetitorLabel.append(clauseDelimiter);
				
				i++;
			}
			
			label = sbCompetitorLabel.toString();
			
			if (competitors.size() == 0)
				return "";
		}
		else {
			StringBuffer sbAttributeLabel = new StringBuffer();
			int i = 0;
			if (standardAttributes.size() > 0) {
				for (String attribute : standardAttributes)  {
					if (!attribute.equals("")) {
						String attributeLabel = label + termDelimiter + attribute;
						sbAttributeLabel.append(attributeLabel);
						if (i < standardAttributes.size() - 1)
							sbAttributeLabel.append(clauseDelimiter);
					}
					i++;
				}
				
				label = sbAttributeLabel.toString();
			}
			else {
				label = label + termDelimiter;
			}
		}

		return label.trim();
	}
	
	public String makeStandardAttributesLabel(String delimiter) {
		StringBuffer sb = new StringBuffer();
		
		int i = 0;
		for (String attribute : standardAttributes)  {
			if (!attribute.equals("")) {
				sb.append(attribute);
				if (i < standardAttributes.size() - 1)
					sb.append(delimiter);
			}
			i++;
		}

		return sb.toString().trim();		
	}
	
	public String makeStandardModifiersLabel(String delimiter) {
		StringBuffer sb = new StringBuffer();
		
		int i = 0;
		for (String modifier : standardModifiers)  {
			if (!modifier.equals("")) {
				sb.append(modifier);
				if (i < standardModifiers.size() - 1)
					sb.append(delimiter);
			}
			i++;
		}

		return sb.toString().trim();		
	}
	
	public String makeFeaturesLabel(String delimiter, boolean includeScore) {
		StringBuffer sb = new StringBuffer();

		int i = 0;
		for (Map.Entry<String, Double> entry : featureCategories.entrySet()) {
			String featureCategory = entry.getKey();
			Double score = entry.getValue();
			
			if (includeScore)
				sb.append(featureCategory).append("(").append(score).append(")");
			else
				sb.append(featureCategory);
			
			if (i < featureCategories.size() - 1)
				sb.append(delimiter);
			
			i++;
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
			.append(" *featureCategories = ").append(featureCategories)
			.append(" *competitors = ").append(competitors)
			.append(" standardSubjectPredicateLabel = ").append(makeStandardSubjectPredicateLabel(" ", "_", false, false, false))
			.append(" featureCategorySubjectPredicateLabel = ").append(makeStandardSubjectPredicateLabel(" ", "_", true, false, false))
			.append(" competitorFeatureCategorySubjectPredicateLabel = ").append(makeStandardSubjectPredicateLabel(" ", "_", true, true, false))
			.append(" standardSubjectAttributeLabel = ").append(this.makeStandardSubjectAttributeLabel(" ", "_", false, false))
			.append(" featureCategorySubjectAttributeLabel = ").append(makeStandardSubjectAttributeLabel(" ", "_", true, false))
			.append(" competitorFeatureCategorySubjectAttributeLabel = ").append(makeStandardSubjectAttributeLabel(" ", "_", true, true))
			.append(" standardLabel = ").append(makeStandardLabel("_", true, true, false))

			.append(" polarity = ").append(polarity)
			.append(" polarityStrength = ").append(polarityStrength);		
		
		for (SemanticClause child : childClauses) {
			sb.append("\n").append("  <-- ").append(child.toString());
		}
		
		return sb.toString();
	}
	
}
