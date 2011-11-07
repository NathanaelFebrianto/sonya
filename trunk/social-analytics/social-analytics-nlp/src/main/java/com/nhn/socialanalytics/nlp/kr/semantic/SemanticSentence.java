package com.nhn.socialanalytics.nlp.kr.semantic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

@SuppressWarnings("serial")
public class SemanticSentence extends ArrayList<SemanticClause> {
	
	private int id = -1;
	private String sentence;
	private double polarity;
	private double polarityStrength;
	
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
	
	public boolean add(SemanticClause clause) {
		if (clause == null)
			return false;

		String subject = clause.getSubject();
		String predicate = clause.getPredicate();
		
		char[] tags = checkSemanticClause(subject, predicate);
		
		System.out.println("tags[0] == " + tags[0]);
		System.out.println("tags[1] == " + tags[1]);		

		if (tags[0] == '0' && tags[1] == '0') {
			if (subject != null || predicate  != null) {
				return super.add(clause);
			} else if (clause.getObjects().size() > 0 || clause.getModifiers().size() > 0) {
				if (!existSameObjects(clause.getObjects()) && !existSameModifiers(clause.getModifiers()))
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
	
	public boolean existSameObjects(Set<String> objects) {
		for (SemanticClause clause : this) {			
			if (clause.getObjects().containsAll(objects))
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
	
	public void calculatePolarity() {
		this.sort(true);		
		
		double weightedPolarity = 0.0;
		boolean isSubjective = false;
		boolean isAllSamePriority = true;
		
		int prevPriority = 1;
		for (SemanticClause clause : this) {	
			weightedPolarity = weightedPolarity + (clause.getPolarity() * clause.getStrength());	
			
			if (clause.getPolarity() < 0.0)
				isSubjective = true;
			
			if (prevPriority < clause.getPriority())
				isAllSamePriority = false;
			
			prevPriority = clause.getPriority();
		}
		
		// polarity
		if (!isAllSamePriority && weightedPolarity > 0) {
			this.setPolarity(1.0);
			this.setPolarityStrength(Math.abs(weightedPolarity));
		}
		else if (!isAllSamePriority && weightedPolarity < 0) {
			this.setPolarity(-1.0);
			this.setPolarityStrength(Math.abs(weightedPolarity));
		}
		else if (isSubjective && (isAllSamePriority || weightedPolarity == 0)) {
			double posWordCount = this.sumPostiveWordCount();
			double negWordCount = this.sumNegativeWordCount();
			
			System.out.println("positive word count == " + posWordCount);
			System.out.println("negative word count == " + negWordCount);			
			
			if (posWordCount > negWordCount) {
				this.setPolarity(1.0);
				this.setPolarityStrength( posWordCount / (posWordCount + negWordCount) );
			}
			else if (posWordCount <= negWordCount) {
				this.setPolarity(-1.0);
				this.setPolarityStrength( negWordCount / (posWordCount + negWordCount) );
			}						
		}
		else {
			this.setPolarity(0.0);
			this.setPolarityStrength(Math.abs(weightedPolarity));
		}
		
		System.out.println("sentence polarity == " + this.getPolarity());
		System.out.println("sentence polarity strength == " + this.getPolarityStrength());
	}
	
	public String extractSubjectPredicateLabel() {
		StringBuffer sb = new StringBuffer();
		
		for (SemanticClause clause : this) {	
			String label = clause.makeLabel(false);
			if (label != null && !label.equals("")) {
				sb.append(label).append(" ");
			}			
		}
		
		return sb.toString();		
	}
	
	public String extractSubjectLabel() {
		StringBuffer sb = new StringBuffer();
		
		for (SemanticClause clause : this) {	
			String label = clause.getSubject();
			if (label != null && !label.equals("")) {
				sb.append(label).append(" ");
			}			
		}
		
		return sb.toString();		
	}
	
	public String extractPredicateLabel() {
		StringBuffer sb = new StringBuffer();
		
		for (SemanticClause clause : this) {	
			String label = clause.getPredicate();
			if (label != null && !label.equals("")) {
				sb.append(label).append(" ");
			}			
		}
		
		return sb.toString();		
	}
	
	public String extractObjectsLabel() {
		StringBuffer sb = new StringBuffer();
		
		for (SemanticClause clause : this) {	
			String label = clause.makeObjectsLabel();
			if (label != null) {
				sb.append(label).append(" ");
			}			
		}		
		return sb.toString();		
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
