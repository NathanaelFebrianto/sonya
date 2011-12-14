package com.nhn.socialanalytics.miner.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nhn.socialanalytics.miner.opinion.OpinionResultSet;
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
		this(DIRECTED_SPARSE_GRAPH, null);
	}
	
	/**
	 * Constructor.
	 * 
	 */
	public OpinionGraphModeller(OpinionResultSet ors) {
		this(DIRECTED_SPARSE_GRAPH, ors);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param graphType the graph type
	 */
	public OpinionGraphModeller(int graphType, OpinionResultSet ors) {
		graph = new DelegateForest<TermNode, TermEdge>();
		
		rootNode = new TermNode();
		rootNode.setId("ROOT");
		rootNode.setName("ROOT");		
		termNodes.add(rootNode);
		
		if (ors != null) {
			this.setOpinionResultSet(ors);
			this.createGraph();
		}
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

	public void setOpinionResultSet(OpinionResultSet ors) {
		
		// update root node name
		rootNode.setName(ors.getType());
		
		// feature term nodes
		Map<String, TermNode> featureNodes = new HashMap<String, TermNode>();
		List<String> features = ors.getFeatures();
		for (String feature : features) {
			TermNode featureNode = new TermNode();
			featureNode.setId(feature);
			featureNode.setType("FEATURE");
			featureNode.setName(feature);				
			
			// feature term edge
			TermEdge featureEdge = new TermEdge();
			featureEdge.setTo(featureNode.getId());
			featureEdge.setFrom(rootNode.getId());
			
			termNodes.add(featureNode);
			termEdges.add(featureEdge);
			
			featureNodes.put(feature, featureNode);
		}
		
		// base term nodes
		for (OpinionTerm baseTerm : ors) {
			TermNode baseTermNode = new TermNode();
			baseTermNode.setId(baseTerm.getTerm());
			baseTermNode.setType(baseTerm.getType());
			baseTermNode.setName(baseTerm.getTerm());
			baseTermNode.setTF(baseTerm.getTF());
			baseTermNode.setPolarity(baseTerm.getPolarity());
			baseTermNode.setDocs(baseTerm.getDocs());
			
			// base term edge
			TermEdge baseTermEdge = new TermEdge();
			baseTermEdge.setTo(baseTermNode.getId());
			baseTermEdge.setFrom(featureNodes.get(baseTerm.getFeature()).getId());
			
			termNodes.add(baseTermNode);
			termEdges.add(baseTermEdge);			
			
			// group nodes for linked term nodes
			Map<String, List<OpinionTerm>> linkedTermsMap = baseTerm.getLinkedTerms();
			for (Map.Entry<String, List<OpinionTerm>> entry : linkedTermsMap.entrySet()) {
				String linkedTermType = entry.getKey();
				List<OpinionTerm> linkedTerms = entry.getValue();
				
				// group node for linked term nodes
				TermNode groupNode = new TermNode();
				groupNode.setId(linkedTermType + baseTerm.getTerm());
				groupNode.setName(linkedTermType);
				
				// group edge for linked term nodes
				TermEdge groupEdge = new TermEdge();
				groupEdge.setTo(groupNode.getId());
				groupEdge.setFrom(baseTermNode.getId());
				
				if (linkedTerms.size() > 0) {
					termNodes.add(groupNode);
					termEdges.add(groupEdge);
				}
				
				// linked term nodes
				for (OpinionTerm linkedTerm : linkedTerms) {
					// node
					TermNode node = new TermNode();
					node.setId(groupNode.getId() + "-" + linkedTerm.getTerm());
					node.setName(linkedTerm.getTerm());
					node.setTF(linkedTerm.getTF());
					node.setLinkedTF(linkedTerm.getLinkedTF());
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
	
}
