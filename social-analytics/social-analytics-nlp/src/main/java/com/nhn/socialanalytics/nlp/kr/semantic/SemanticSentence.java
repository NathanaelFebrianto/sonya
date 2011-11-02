package com.nhn.socialanalytics.nlp.kr.semantic;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class SemanticSentence extends ArrayList<SemanticClause> {
	
	private int id;
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
		if (clause != null) {
			return super.add(clause);
		}
		
		return false;
	}

}
