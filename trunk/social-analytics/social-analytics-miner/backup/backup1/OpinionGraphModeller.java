package com.nhn.socialanalytics.miner.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nhn.socialanalytics.miner.opinion.OpinionTerm;

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

	public void addTerm(OpinionTerm baseTerm) {
		
		// update root node name
		rootNode.setName(baseTerm.getType());
		
		// parent node
		TermNode baseTermNode = new TermNode();
		baseTermNode.setType(baseTerm.getType());
		baseTermNode.setId(baseTerm.getTerm());
		baseTermNode.setName(baseTerm.getTerm());
		baseTermNode.setTF(baseTerm.getTF());
		baseTermNode.setPolarity(baseTerm.getPolarity());
		baseTermNode.setDocs(baseTerm.getDocs());
		
		// target term edge
		TermEdge rootEdge = new TermEdge();
		rootEdge.setTo(baseTermNode.getId());
		rootEdge.setFrom(rootNode.getId());
		
		termNodes.add(baseTermNode);
		termEdges.add(rootEdge);
		
		Map<String, List<OpinionTerm>> linkedTermsMap = baseTerm.getLinkedTerms();
		
		for (Map.Entry<String, List<OpinionTerm>> entry : linkedTermsMap.entrySet()) {
			String linkTermType = entry.getKey();
			List<OpinionTerm> linkedTerms = entry.getValue();

			// group node
			TermNode groupNode = new TermNode();
			groupNode.setId(linkTermType + baseTerm.getTerm());
			groupNode.setName(linkTermType);
			
			// group edge
			TermEdge groupEdge = new TermEdge();
			groupEdge.setTo(groupNode.getId());
			groupEdge.setFrom(baseTermNode.getId());
			
			//if (baseTerm.getLinkedTerms(linkTermType).size() > 0) {
				termNodes.add(groupNode);
				termEdges.add(groupEdge);
			//}
			
			// child linked term nodes
			for (OpinionTerm linkedTerm : linkedTerms) {
				// node
				TermNode node = new TermNode();
				node.setId(groupNode.getId() + "-" + linkedTerm.getTerm());
				node.setName(linkedTerm.getTerm());
				node.setTF(linkedTerm.getTF());
				node.setTFWithinTarget(linkedTerm.getTFWithBaseTerm());
				node.setPolarity(linkedTerm.getPolarity());
				node.setType(linkedTerm.getType());
				node.setDocs(linkedTerm.getDocs());
				
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
