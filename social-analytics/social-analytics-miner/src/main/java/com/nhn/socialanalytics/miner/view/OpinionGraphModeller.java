package com.nhn.socialanalytics.miner.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nhn.socialanalytics.miner.index.ChildTerm;
import com.nhn.socialanalytics.miner.index.TargetTerm;

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
		rootNode.setId("ROOT");
		rootNode.setName("ROOT");
		
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
			
			//System.out.println("node id == " + String.valueOf(vertex.getId()));
			
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
						
			System.out.println("from : " + edge.getFrom() + " ---> to : " + edge.getTo());
			TermNode from = vertices.get(edge.getFrom());
			TermNode to = vertices.get(edge.getTo());
			if (from != null && to != null)
				graph.addEdge(edge, from, to);

		}
	}

	public void addTerms(String targetField, Map<String, TargetTerm> termMap) {
		
		// update root node name
		rootNode.setName(targetField);
		
		// parent node
		TermNode parentNode = new TermNode();
		parentNode.setType(targetField);
		
		int i = 0;
		for (Map.Entry<String, TargetTerm> entry : termMap.entrySet()) {
			String fieldName = entry.getKey();
			TargetTerm targetTerm = entry.getValue();

			if (i == 0) {
				// target term node
				parentNode.setId(targetTerm.getTerm());
				parentNode.setName(targetTerm.getTerm());
				parentNode.setTF(targetTerm.getTF());
				parentNode.setPolarity(targetTerm.getPolarity());
				
				// target term edge
				TermEdge rootEdge = new TermEdge();
				rootEdge.setTo(parentNode.getId());
				rootEdge.setFrom(rootNode.getId());
				
				termNodes.add(parentNode);
				termEdges.add(rootEdge);
			}		
			
			i++;			
			
			// group node
			TermNode groupNode = new TermNode();
			groupNode.setId(fieldName + targetTerm.getTerm());
			groupNode.setName(fieldName);
			
			// group edge
			TermEdge groupEdge = new TermEdge();
			groupEdge.setTo(groupNode.getId());
			groupEdge.setFrom(parentNode.getId());
			
			if (targetTerm.getChildTerms().size() > 0) {
				termNodes.add(groupNode);
				termEdges.add(groupEdge);
			}
			
			// child term nodes
			for (ChildTerm childTerm : targetTerm.getChildTerms()) {
				// node
				TermNode node = new TermNode();
				node.setId(groupNode.getId() + "-" + childTerm.getTerm());
				node.setName(childTerm.getTerm());
				node.setTF(childTerm.getTF());
				node.setPolarity(childTerm.getPolarity());
				node.setType(fieldName);
				node.setDocs(childTerm.getDocs());
				
				// edge
				TermEdge edge = new TermEdge();
				edge.setTo(node.getId());
				edge.setFrom(groupNode.getId());
				
				termNodes.add(node);
				termEdges.add(edge);
			}
		}
	}
	
}
