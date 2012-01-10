package com.nhn.socialanalytics.miner.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nhn.socialanalytics.miner.index.FieldConstants;
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
		this(DIRECTED_SPARSE_GRAPH, null, false);
	}
	
	/**
	 * Constructor.
	 * 
	 */
	public OpinionGraphModeller(OpinionResultSet ors, boolean translate) {
		this(DIRECTED_SPARSE_GRAPH, ors, translate);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param graphType the graph type
	 */
	public OpinionGraphModeller(int graphType, OpinionResultSet ors, boolean translate) {
		graph = new DelegateForest<TermNode, TermEdge>();
		
		rootNode = new TermNode();
		rootNode.setId("ROOT");
		rootNode.setName("ROOT");		
		termNodes.add(rootNode);
		
		if (ors != null) {
			this.setOpinionResultSet(ors, translate);
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
	
	public Forest<TermNode, TermEdge> getEmptyGraph() {
		graph = new DelegateForest<TermNode, TermEdge>();
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

	public void setOpinionResultSet(OpinionResultSet ors, boolean translate) {
		boolean byFeature = ors.isByFeature();
		
		// update root node name
		rootNode.setName(ors.getType());
		
		// feature term nodes
		Map<String, TermNode> featureNodes = new HashMap<String, TermNode>();
		if (byFeature) {
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
		}
		
		// base term nodes
		for (OpinionTerm baseTerm : ors) {
			TermNode baseTermNode = new TermNode();
			baseTermNode.setId(baseTerm.getId());
			baseTermNode.setType(baseTerm.getType());
			baseTermNode.setName(baseTerm.getTerm());
			baseTermNode.setTF(baseTerm.getTF());
			baseTermNode.setPolarity(baseTerm.getPolarity());
			baseTermNode.setDocs(baseTerm.getDocs());
			
			String translatedText = baseTerm.getTerm();
			if (translate) {
				if (ors.getLanguage().equalsIgnoreCase(FieldConstants.LANG_JAPANESE)) {
					translatedText = Translator.translate("あ " + baseTerm.getTerm());
					translatedText = translatedText.substring(2);
				}
				else {
					translatedText = Translator.translate(baseTerm.getTerm());
				}
				baseTermNode.setName(baseTerm.getTerm() + "(" + translatedText + ")");
			}
			
			// base term edge
			TermEdge baseTermEdge = new TermEdge();
			baseTermEdge.setTo(baseTermNode.getId());
			
			if (byFeature)
				baseTermEdge.setFrom(featureNodes.get(baseTerm.getFeature()).getId());
			else
				baseTermEdge.setFrom(rootNode.getId());
			
			termNodes.add(baseTermNode);
			termEdges.add(baseTermEdge);			
			
			// group nodes for linked term nodes
			Map<String, List<OpinionTerm>> linkedTermsMap = baseTerm.getLinkedTerms();
			for (Map.Entry<String, List<OpinionTerm>> entry : linkedTermsMap.entrySet()) {
				String linkedTermType = entry.getKey();
				List<OpinionTerm> linkedTerms = entry.getValue();
				
				// group node for linked term nodes
				TermNode groupNode = new TermNode();
				//groupNode.setId(linkedTermType + baseTerm.getTerm());
				groupNode.setId(linkedTermType + baseTerm.getId());
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
					node.setId(groupNode.getId() + "-" + linkedTerm.getId());
					node.setName(linkedTerm.getTerm());
					node.setTF(linkedTerm.getTF());
					node.setLinkedTF(linkedTerm.getLinkedTF());
					node.setPolarity(linkedTerm.getPolarity());
					node.setType(linkedTerm.getType());
					node.setDocs(linkedTerm.getDocs());
					
					String translatedLinkedText = linkedTerm.getTerm();
					if (translate) {
						if (ors.getLanguage().equalsIgnoreCase(FieldConstants.LANG_JAPANESE)) {
							translatedLinkedText = Translator.translate("あ " + linkedTerm.getTerm());
							translatedLinkedText = translatedLinkedText.substring(2);
						}
						else {
							translatedLinkedText = Translator.translate(linkedTerm.getTerm());
						}
						node.setName(linkedTerm.getTerm() + "(" + translatedLinkedText + ")");
					}
					
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
