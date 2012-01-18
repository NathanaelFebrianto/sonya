package com.nhn.socialanalytics.nlp.morpheme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("serial")
public class Sentence extends ArrayList<Token> {

	private String sentence;
	private HashSet<Integer> indexSet = new HashSet<Integer>();
	
	public Sentence(String sentence) {
		this.sentence = sentence;
	}

	public String getSentence() {
		return sentence;
	}
	
	public boolean add(Token token) {
		if (token != null) {
			indexSet.add(token.getIndex());
			return super.add(token);
		}
		
		return false;
	}
	
	public int getUniqueSize() {
		return indexSet.size();
	}

	public List<Token> getTokens(int index) {
		List<Token> list = new ArrayList<Token>();
		
		for (int i = 0; i < size(); i++) {
			Token token = (Token) get(i);
			if (token.getIndex() == index)
				list.add(token);				
		}		
		return list;
	}
	
	public void sort(boolean ascending) {
		Collections.sort(this, new TokenComparator(ascending));
	}
	
	class TokenComparator implements Comparator<Token> {
		private boolean ascending = true;
		
		public TokenComparator(boolean ascending) {
			this.ascending = ascending;				
		}
		
		public int compare(Token e1, Token e2) {		
			Integer s1 = e1.getIndex();
			Integer s2 = e2.getIndex();
			if (ascending)
				return s1.compareTo(s2);
			else
				return s2.compareTo(s1);
		}		
	}
}
