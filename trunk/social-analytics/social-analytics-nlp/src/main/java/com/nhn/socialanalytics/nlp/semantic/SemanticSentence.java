package com.nhn.socialanalytics.nlp.semantic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("serial")
public class SemanticSentence extends ArrayList<SemanticClause> {
	
	private int id = -1;
	private String sentence;
	private double polarity;
	private double polarityStrength;
	private String mainFeature = "";
	private Map<String, Double> features = new HashMap<String, Double>();
	private Map<String, Boolean> competitors = new HashMap<String, Boolean>();
	
	public SemanticSentence(int id, String sentence) {
		this.id = id;
		this.sentence = sentence;
	}
	
	public int getId() {
		return id;
	}

	public String getSentence() {
		return sentence;
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
	public String getMainFeature() {
		return mainFeature;
	}
	public void setMainFeature(String mainFeature) {
		this.mainFeature = mainFeature;
	}
	public Map<String, Double> getFeatures() {
		return features;
	}
	public void setFeatures(Map<String, Double> features) {
		this.features = features;
	}
	public Map<String, Boolean> getCompetitors() {
		return competitors;
	}
	public void setCompetitors(Map<String, Boolean> competitors) {
		this.competitors = competitors;
	}
	
	public boolean add(SemanticClause clause) {
		if (clause == null)
			return false;

		String subject = clause.getSubject();
		String predicate = clause.getPredicate();
		
		char[] tags = checkSemanticClause(subject, predicate);
		
		//System.out.println("tags[0] == " + tags[0]);
		//System.out.println("tags[1] == " + tags[1]);		

		if (tags[0] == '0' && tags[1] == '0') {
			if (subject != null || predicate  != null) {
				return super.add(clause);
			} else if (clause.getAttributes().size() > 0 || clause.getModifiers().size() > 0) {
				if (!existSameObjects(clause.getAttributes()) && !existSameModifiers(clause.getModifiers()))
					return super.add(clause);
			}
		}
		else if (tags[0] == '1' && tags[1] == '1') {
			SemanticClause sc = findSemanticClause(subject, predicate);
			remove(sc);
			return super.add(clause);
		}
		else if (tags[0] == '1' && tags[1] == '0') { 
			SemanticClause sc = findSemanticClauseBySubject(subject);
			remove(sc);
			return super.add(clause);
		}
		else if (tags[0] == '0' && tags[1] == '1') { 
			SemanticClause sc = findSemanticClauseByPredicate(predicate);
			remove(sc);
			return super.add(clause);
		}
		
		return false;
	}
	
	
	public char[] checkSemanticClause(String subject, String predicate) {
		char[] tags = { '0', '0' };
		
		for (SemanticClause clause : this) {			
			if (clause.getSubject() != null && clause.getSubject().equalsIgnoreCase(subject))
				tags[0] = '1';
			
			if (clause.getPredicate() != null && clause.getPredicate().equalsIgnoreCase(predicate))
				tags[1] = '1';
		}
		
		return tags;
	}
	
	public SemanticClause findSemanticClause(String subject, String predicate) {
		for (SemanticClause clause : this) {			
			if (clause.getSubject() != null && clause.getPredicate() != null &&
					clause.getSubject().equalsIgnoreCase(subject) && 
					clause.getPredicate().equalsIgnoreCase(predicate)) {
				return clause;
			}			
		}
		
		return null;
	}
	
	public SemanticClause findSemanticClauseBySubject(String subject) {
		for (SemanticClause clause : this) {			
			if (clause.getSubject() != null && 
					clause.getSubject().equalsIgnoreCase(subject) && 
					clause.getPredicate() == null) {
				return clause;
			}			
		}
		
		return null;
	}

	public SemanticClause findSemanticClauseBySubjectWithPredicate(String subject) {
		for (SemanticClause clause : this) {			
			if (clause.getSubject() != null && 
					clause.getSubject().equalsIgnoreCase(subject) && 
					clause.getPredicate() != null) {
				return clause;
			}			
		}
		
		return null;
	}
	
	public SemanticClause findSemanticClauseByPredicate(String predicate) {
		for (SemanticClause clause : this) {			
			if (clause.getSubject() == null && clause.getPredicate() != null &&
					clause.getPredicate().equalsIgnoreCase(predicate)) {
				return clause;
			}			
		}
		
		return null;
	}
	
	public boolean existSameObjects(Set<String> attributes) {
		for (SemanticClause clause : this) {			
			if (clause.getAttributes().containsAll(attributes))
				return true;
		}
		return false;	
	}
	
	public boolean existSameModifiers(Set<String> modifiers) {
		for (SemanticClause clause : this) {			
			if (clause.getModifiers().containsAll(modifiers))
				return true;
		}
		return false;	
	}
	
	public void sort(boolean ascending) {
		Collections.sort(this, new SemanticSentenceComparator(ascending));
	}
	
	public double sumPostiveWordCount() {
		double posWordCount = 0.0;
		for (SemanticClause clause : this) {	
			posWordCount = posWordCount + clause.getPositiveWordCount();	
		}
		return posWordCount;
	}
	
	public double sumNegativeWordCount() {
		double negWordCount = 0.0;
		for (SemanticClause clause : this) {	
			negWordCount = negWordCount + clause.getNegativeWordCount();	
		}
		return negWordCount;
	}
	
	public String extractStandardLabel(String clauseDelimiter, String termDelimiter, 
			boolean includeAttribute, boolean includeModifier, boolean includeChild) {
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < this.size(); i++) {	
			SemanticClause clause = this.get(i);
			String label = clause.makeStandardLabel(termDelimiter, includeAttribute, includeModifier, includeChild);
			if (label != null && !label.trim().equals("")) {
				sb.append(label);
				
				if (i < this.size() - 1)
					sb.append(clauseDelimiter);
			} 
		}
		
		return sb.toString().trim();	
	}
	
	public String extractStandardSubjectPredicateLabel(String clauseDelimiter, String termDelimiter, 
			boolean includeFeature, boolean includeCompetitor, boolean includeChild) {
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < this.size(); i++) {	
			SemanticClause clause = this.get(i);

			String label = clause.makeStandardSubjectPredicateLabel(clauseDelimiter, termDelimiter, includeFeature, includeCompetitor, includeChild);
			
			if (label != null && !label.trim().equals("")) {
				sb.append(label);
				
				if (i < this.size() - 1)
					sb.append(clauseDelimiter);
			} 
		}
		
		return sb.toString().trim();	
	}
	
	public String extractStandardSubjectAttributeLabel(String clauseDelimiter, String termDelimiter, 
			boolean includeFeature, boolean includeCompetitor) {
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < this.size(); i++) {	
			SemanticClause clause = this.get(i);

			String label = clause.makeStandardSubjectAttributeLabel(clauseDelimiter, termDelimiter, includeFeature, includeCompetitor);
			
			if (label != null && !label.trim().equals("")) {
				sb.append(label);
				
				if (i < this.size() - 1)
					sb.append(clauseDelimiter);
			} 
		}
		
		return sb.toString().trim();	
	}
	
	public String extractStandardSubjectLabel(String clauseDelimiter, String termDelimiter, 
			boolean includeFeature, boolean includeCompetitor) {
		StringBuffer sb = new StringBuffer();
		
		int i = 0;
		for (SemanticClause clause : this) {	
			String label = clause.getStandardSubject();
			if (label != null && !label.equals("")) {
				if (includeCompetitor) {
					int j = 0;
					for (Map.Entry<String, Boolean> entry : competitors.entrySet()) {
						sb.append(entry.getKey()).append(termDelimiter);
						
						if (includeFeature)
							sb.append(clause.getMainFeature()).append(termDelimiter);
						
						sb.append(label);
						
						if (j < competitors.size() - 1)
							sb.append(clauseDelimiter);
						
						j++;
					}		
				}
				else {
					if (includeFeature)
						sb.append(clause.getMainFeature()).append(termDelimiter);
					
					sb.append(label);
					if (i < this.size() - 1)
						sb.append(clauseDelimiter);				
				}
			}
			i++;
		}
		
		return sb.toString().trim();		
	}
	
	public String extractStandardPredicateLabel(String clauseDelimiter, String termDelimiter, 
			boolean includeFeature, boolean includeCompetitor) {
		StringBuffer sb = new StringBuffer();
		
		int i = 0;
		for (SemanticClause clause : this) {	
			String label = clause.getStandardPredicate();
			if (label != null && !label.equals("")) {
				if (includeCompetitor) {
					int j = 0;
					for (Map.Entry<String, Boolean> entry : competitors.entrySet()) {
						sb.append(entry.getKey()).append(termDelimiter);
						
						if (includeFeature)
							sb.append(clause.getMainFeature()).append(termDelimiter);
						
						sb.append(label);
						
						if (j < competitors.size() - 1)
							sb.append(clauseDelimiter);
						
						j++;
					}		
				}
				else {
					if (includeFeature)
						sb.append(clause.getMainFeature()).append(termDelimiter);
					
					sb.append(label);
					if (i < this.size() - 1)
						sb.append(clauseDelimiter);
				}
			}
			i++;
		}
		
		return sb.toString().trim();		
	}
	
	public String extractStandardAttributesLabel(String delimiter) {
		StringBuffer sb = new StringBuffer();
		
		int i = 0;
		for (SemanticClause clause : this) {	
			String label = clause.makeStandardAttributesLabel(delimiter);
			if (label != null && !label.equals("")) {
				sb.append(label);
				if (i < this.size() - 1)
					sb.append(delimiter);
			}
			i++;
		}
		
		return sb.toString().trim();		
	}

	public String extractStandardModifiersLabel(String delimiter) {
		StringBuffer sb = new StringBuffer();
		
		int i = 0;
		for (SemanticClause clause : this) {	
			String label = clause.makeStandardModifiersLabel(delimiter);
			if (label != null && !label.equals("")) {
				sb.append(label);
				if (i < this.size() - 1)
					sb.append(delimiter);
			}
			i++;
		}
		
		return sb.toString().trim();		
	}
	
	public String extractFeaturesLabel(String delimiter, boolean includeScore) {
		StringBuffer sb = new StringBuffer();

		int i = 0;
		for (Map.Entry<String, Double> entry : features.entrySet()) {
			String feature = entry.getKey();
			Double score = entry.getValue();
			
			if (includeScore)
				sb.append(feature).append("(").append(score).append(")");
			else
				sb.append(feature);
			
			if (i < features.size() - 1)
				sb.append(delimiter);
			
			i++;
		}

		return sb.toString().trim();
	}
	
	public String extractCompetitorsLabel(String delimiter) {
		StringBuffer sb = new StringBuffer();

		int i = 0;
		for (Map.Entry<String, Boolean> entry : competitors.entrySet()) {
			String competitor = entry.getKey();
			//boolean isCompetitor = entry.getValue();
			
			sb.append(competitor);
			
			if (i < competitors.size() - 1)
				sb.append(delimiter);
			
			i++;
		}

		return sb.toString().trim();
	}
	
	class SemanticSentenceComparator implements Comparator<SemanticClause> {
		private boolean ascending = true;
		
		public SemanticSentenceComparator(boolean ascending) {
			this.ascending = ascending;				
		}
		
		public int compare(SemanticClause c1, SemanticClause c2) {		
			Integer p1 = c1.getPriority();
			Integer p2 = c2.getPriority();
			if (ascending)
				return p1.compareTo(p2);
			else
				return p2.compareTo(p1);
		}		
	}

}
