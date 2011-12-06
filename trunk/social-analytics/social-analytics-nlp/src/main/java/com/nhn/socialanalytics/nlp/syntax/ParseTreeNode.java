package com.nhn.socialanalytics.nlp.syntax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.nhn.socialanalytics.nlp.morpheme.Element;

public class ParseTreeNode {
	
	private int id;
	private Element element;
	private ParseTreeNode parentNode;
	private List<ParseTreeEdge> childEdges;
	
	protected ParseTreeNode(Element element) {
		//Random rand = new Random();
		//id = rand.nextInt();
		id = 0;
		//System.out.println("node id == " + id);
		this.element = element;
	}
	
	public int getElementIndex() {
		if (element != null)
			return element.getIndex();		
		return -9999;
	}

	public List<ParseTreeEdge> getChildEdges() {
		return childEdges;
	}

	public void addChildEdge(ParseTreeEdge arc) {
		if (childEdges == null)
			childEdges = new ArrayList<ParseTreeEdge>();
		childEdges.add(arc);
	}

	public int getId() {
		return id;
	}

	public Element getElement() {
		return element;
	}

	public ParseTreeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(ParseTreeNode parentNode) {
		this.parentNode = parentNode;
	}

	public boolean contains(ParseTreeNode node) {
		if (element == node.element)
			return true;
		for (Iterator<ParseTreeEdge> iterChildEdge = childEdges.iterator(); iterChildEdge.hasNext();) {
			ParseTreeEdge edge = (ParseTreeEdge) iterChildEdge.next();
			ParseTreeNode child = edge.getChildNode();
			if (child.contains(node))
				return true;
		}

		return false;
	}

	public void traverse(int depth, String relation, StringBuffer sb) {
		int i;
		
		for (i = 0; i < depth; i++)
			sb.append("\t");

		if (relation != null)
			sb.append((new StringBuilder("<=[")).append(relation)
					.append("]=| ").toString());
		
		sb.append((new StringBuilder(String.valueOf(id))).append("\t")
				.append(element).append("\n").toString());
		i = 0;
		
		for (int size = childEdges != null ? childEdges.size() : 0; i < size; i++) {
			ParseTreeEdge edge = (ParseTreeEdge) childEdges.get(i);
			edge.getChildNode().traverse(depth + 1, edge.getRelation(), sb);
		}

	}

	public int traverse(int id) {
		this.id = id;
		int ret = id;
		int i = 0;
		for (int size = childEdges != null ? childEdges.size() : 0; i < size; i++) {
			ParseTreeEdge edge = (ParseTreeEdge) childEdges.get(i);
			ret = edge.getChildNode().traverse(ret + 1);
		}

		return ret;
	}

	public void traverse(List<ParseTreeNode> nodeList, List<ParseTreeEdge> edgeList) {
		nodeList.add(this);
		int i = 0;
		for (int size = childEdges != null ? childEdges.size() : 0; i < size; i++) {
			ParseTreeEdge edge = (ParseTreeEdge) childEdges.get(i);
			edgeList.add(edge);
			edge.getChildNode().traverse(nodeList, edgeList);		
		}
	}

}
