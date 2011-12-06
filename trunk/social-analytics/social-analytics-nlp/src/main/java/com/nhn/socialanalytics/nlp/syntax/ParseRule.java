package com.nhn.socialanalytics.nlp.syntax;

public class ParseRule {

	public String relation;
	public String[] dependentTags;
	public String[] governerTags;
	public int distance;
	public int priority;

	public ParseRule(String relation, String[] dependentTags, String[] governerTags,
			int distance, int priority) {
		this.relation = null;
		this.dependentTags = dependentTags;
		this.governerTags = governerTags;
		this.distance = 1;
		this.priority = 10;
		this.relation = relation;
		this.distance = distance;
		this.priority = priority;
	}

	public ParseTreeEdge govern(ParseTreeNode prevNode, ParseTreeNode nextNode, int distance) {
		if ( prevNode.getToken().containsTagOf(dependentTags)
			&& nextNode.getToken().containsTagOf(governerTags)
			&& distance <= this.distance )
			return new ParseTreeEdge(relation, prevNode, nextNode, distance, priority);
		
		return null;
	}
}
