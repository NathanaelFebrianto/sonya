package com.nhn.socialanalytics.nlp.kr.semantic;

import java.util.ArrayList;
import java.util.Set;

@SuppressWarnings("serial")
public class SemanticSentence extends ArrayList<SemanticClause> {
	
	private int id = -1;
	private String sentence;
	
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

}
