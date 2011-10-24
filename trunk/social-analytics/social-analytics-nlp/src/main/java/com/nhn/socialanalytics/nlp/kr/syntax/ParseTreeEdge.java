package com.nhn.socialanalytics.nlp.kr.syntax;

public class ParseTreeEdge {

	private String relation;
	private ParseTreeNode childNode;
	private int distance;
	private int priority;
	
	public ParseTreeEdge(String relation, ParseTreeNode childNode,
			ParseTreeNode parentNode, int distance, int priority) {
		this.relation = null;
		this.childNode = null;
		this.distance = 0;
		this.priority = 0;
		this.relation = relation;
		this.childNode = childNode;
		this.childNode.setParentNode(parentNode);
		this.distance = distance;
		this.priority = priority;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public ParseTreeNode getChildNode() {
		return childNode;
	}

	public void setChildNode(ParseTreeNode childNode) {
		this.childNode = childNode;
	}

	public int getFromId() {
		return childNode.getParentNode().getId();
		//return childNode.getId();
		
	}

	public int getToId() {
		return childNode.getId();
		//return childNode.getParentNode().getId();
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
