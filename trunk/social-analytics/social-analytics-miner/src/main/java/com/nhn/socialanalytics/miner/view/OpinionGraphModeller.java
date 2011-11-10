package com.nhn.socialanalytics.miner.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.nhn.socialanalytics.miner.termvector.ChildTerm;
import com.nhn.socialanalytics.miner.termvector.TargetTerm;

import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;

public class OpinionGraphModeller {

	List<TermNode> termNodes = new ArrayList<TermNode>();
	List<TermEdge> termEdges = new ArrayList<TermEdge>();
	TermNode rootNode;
	
	Forest<TermNode, TermEdge> graph;
	
	/** graph type */
	public final static int DIRECTED_SPARSE_GRAPH = 1;
	public final static int UNDIRECTED_SPARSE_GRAPH = 2;	

	/**
	 * Constructor.
	 * 
	 */
	public OpinionGraphModeller() {
		// create a directed graph by default
		this(DIRECTED_SPARSE_GRAPH);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param graphType the graph type
	 */
	public OpinionGraphModeller(int graphType) {
		// create a directed graph by default
		graph = new DelegateForest<TermNode, TermEdge>();
		
		rootNode = new TermNode();
		rootNode.setId("root");
		rootNode.setTerm("root");
		
		termNodes.add(rootNode);
		
		//graph.addVertex(rootNode);
	}

	/**
	 * Gets the graph.
	 * 
	 * @return Graph<Vertex, Edge> the graph
	 */
	public Forest<TermNode, TermEdge> getGraph() {
		return graph;
	}

	/**
	 * Creates the graph.
	 * 
	 * @param vertices the vertex list
	 * @param edges the edge list
	 * @return Graph<Vertex, Edge> the graph
	 */
	public Forest<TermNode, TermEdge> createGraph() {
		System.out.println("node size == " + termNodes.size());
		HashMap<String, TermNode> verticesMap = createVertices(termNodes);
		createEdges(verticesMap, termEdges);
		return graph;
	}
	
	/**
	 * Creates the vertices.
	 * 
	 */
	private HashMap<String, TermNode> createVertices(List<TermNode> vertices) {
		HashMap<String, TermNode> verticesMap = new HashMap<String, TermNode>();
		for (int i = 0; i < vertices.size(); i++) {
			TermNode vertex = (TermNode) vertices.get(i);
			
			System.out.println("node id == " + String.valueOf(vertex.getId()));
			
			graph.addVertex(vertex);
			verticesMap.put(String.valueOf(vertex.getId()), vertex);
		}
		return verticesMap;
	}

	/**
	 * Creates the edges.
	 * 
	 */
	private void createEdges(HashMap<String, TermNode> vertices, List<TermEdge> edges) {
		for (int i = 0; i < edges.size(); i++) {
			TermEdge edge = (TermEdge) edges.get(i);
			String edgeId = edge.getFrom() + "^" + edge.getTo();
			
			System.out.println("from : " + edge.getFrom() + " ---> to : " + edge.getTo());
			TermNode from = vertices.get(edge.getFrom());
			TermNode to = vertices.get(edge.getTo());
			if (from != null && to != null)
				graph.addEdge(edge, from, to);

		}
	}
	
	
	public void addTerms(TargetTerm subjectTerms, TargetTerm objectTerms) {
		TermNode predicateNode = new TermNode();
		predicateNode.setId(subjectTerms.getTerm());
		predicateNode.setTerm(subjectTerms.getTerm());
		predicateNode.setTF(subjectTerms.getTF());
		predicateNode.setType("predicate");
		
		termNodes.add(predicateNode);
		
		TermEdge rootEdge = new TermEdge();
		rootEdge.setTo(predicateNode.getId());
		rootEdge.setFrom(rootNode.getId());
		rootEdge.setId(rootEdge.getFrom() + "-" + rootEdge.getTo());		
		
		termEdges.add(rootEdge);
		
		// subject group node
		TermNode subjGroupNode = new TermNode();
		subjGroupNode.setId("subject" + subjectTerms.getTerm());
		subjGroupNode.setTerm("subject");
		
		if (subjectTerms.getChildTerms().size() > 0)
			termNodes.add(subjGroupNode);
				
		TermEdge subjEdge = new TermEdge();
		subjEdge.setTo(subjGroupNode.getId());
		subjEdge.setFrom(predicateNode.getId());
		subjEdge.setId(subjEdge.getFrom() + "-" + subjEdge.getTo());
		
		if (subjectTerms.getChildTerms().size() > 0)
			termEdges.add(subjEdge);
		
		// object group node
		TermNode objGroupNode = new TermNode();
		objGroupNode.setId("object" + subjectTerms.getTerm());
		objGroupNode.setTerm("object");
		
		if (objectTerms.getChildTerms().size() > 0)
			termNodes.add(objGroupNode);

		TermEdge objEdge = new TermEdge();
		objEdge.setTo(objGroupNode.getId());
		objEdge.setFrom(predicateNode.getId());
		objEdge.setId(objEdge.getFrom() + "-" + objEdge.getTo());
		
		if (objectTerms.getChildTerms().size() > 0)
			termEdges.add(objEdge);
		
		// subject term nodes
		for (ChildTerm subjectTerm : subjectTerms.getChildTerms()) {
			TermNode node = new TermNode();
			node.setId(subjGroupNode.getId() + "-" + subjectTerm.getTerm());
			node.setTerm(subjectTerm.getTerm());
			node.setTF(subjectTerm.getTF());
			node.setDocs(subjectTerm.getDocs());
			
			//System.out.println("subject node == " + node.getTerm());
			
			termNodes.add(node);
			
			TermEdge edge = new TermEdge();
			edge.setTo(node.getId());
			edge.setFrom(subjGroupNode.getId());
			edge.setId(edge.getFrom() + "-" + edge.getTo());
			
			termEdges.add(edge);
		}
		
		// object term nodes
		for (ChildTerm objectTerm : objectTerms.getChildTerms()) {
			TermNode node = new TermNode();
			node.setId(objGroupNode.getId() + "-" + objectTerm.getTerm());
			node.setTerm(objectTerm.getTerm());
			node.setTF(objectTerm.getTF());
			node.setDocs(objectTerm.getDocs());
			
			//System.out.println("object node == " + node.getTerm());
			
			termNodes.add(node);
			
			TermEdge edge = new TermEdge();
			edge.setTo(node.getId());
			edge.setFrom(objGroupNode.getId());
			edge.setId(edge.getFrom() + "-" + edge.getTo());
			
			termEdges.add(edge);
		}
	}
}
