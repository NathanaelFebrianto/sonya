package com.nhn.socialanalytics.nlp.kr.syntax;

import java.util.ArrayList;
import java.util.List;

public class ParseTree {
	
	private String sentence;
	private ParseTreeNode root;
	private List<ParseTreeNode> nodeList;
	private List<ParseTreeEdge> edgeList;

	public ParseTree() {
		sentence = null;
		root = new ParseTreeNode(null);
		nodeList = null;
		edgeList = null;
	}

	public void setRoot(ParseTreeNode ptn) {
		root.addChildEdge(new ParseTreeEdge("연결", ptn, root, 1, 1));
//		if (root == null) {
//			root = ptn;
//		} else {
//			root.addChildEdge(new ParseTreeEdge("연결", ptn, root, 1, 1));
//		}
	}
	
	public ParseTreeNode getRoot() {
		return this.root;
	}

	public void traverse(StringBuffer sb) {
		root.traverse(0, null, sb);
	}

	public void setId() {
		root.traverse(0);
	}

	public void setAllList() {
		nodeList = new ArrayList<ParseTreeNode>();
		edgeList = new ArrayList<ParseTreeEdge>();
		root.traverse(nodeList, edgeList);
	}

	public List<ParseTreeNode> getNodeList() {
		return nodeList;
	}

	public List<ParseTreeEdge> getEdgeList() {
		return edgeList;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	
	public ParseTreeNode findNode(int nodeId) {
		for (ParseTreeNode node : nodeList) {
			if (node.getId() == nodeId)
				return node;
		}		
		return null;
	}

}
