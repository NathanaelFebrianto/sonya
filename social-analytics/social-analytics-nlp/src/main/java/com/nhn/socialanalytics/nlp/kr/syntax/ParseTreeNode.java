package com.nhn.socialanalytics.nlp.kr.syntax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.nhn.socialanalytics.nlp.kr.morpheme.Eojeol;

public class ParseTreeNode {
	
	private int id;
	private Eojeol eojeol;
	private ParseTreeNode parentNode;
	private List<ParseTreeEdge> childEdges;
	
	protected ParseTreeNode(Eojeol eojeol) {
		//Random rand = new Random();
		//id = rand.nextInt();
		id = 0;
		//System.out.println("node id == " + id);
		this.eojeol = eojeol;
	}
	
	public int getEojeolIndex() {
		if (eojeol != null)
			return eojeol.getIndex();		
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

	public Eojeol getEojeol() {
		return eojeol;
	}

	public ParseTreeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(ParseTreeNode parentNode) {
		this.parentNode = parentNode;
	}

	public boolean contains(ParseTreeNode node) {
		if (eojeol == node.eojeol)
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
				.append(eojeol).append("\n").toString());
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
