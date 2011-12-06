package com.nhn.socialanalytics.nlp.morpheme;


public abstract class Token {
	
	protected int id;
	protected int index;
	protected String source;
	protected String term;
	protected char pos;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public char getPos() {
		return pos;
	}
	public void setPos(char pos) {
		this.pos = pos;
	}
	
	public abstract boolean containsTagOf(String[] tags);
	
	public boolean containsPosTagOf(String[] tags) {
		 for (int i = 0; i < tags.length; i++) {
			String pos = String.valueOf(this.getPos());
			if (tags[i].equals(pos)) {
				return true;
			}
		 }		 
		 return false;
	}
}
