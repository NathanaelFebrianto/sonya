package com.nhn.socialanalytics.nlp.view;

import java.util.HashMap;
import java.util.List;

import com.nhn.socialanalytics.nlp.syntax.ParseTreeEdge;
import com.nhn.socialanalytics.nlp.syntax.ParseTreeNode;

import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;

public class GraphModeller {

	/** graph */
	Forest<ParseTreeNode, ParseTreeEdge> graph;
	
	/** graph type */
	public final static int DIRECTED_SPARSE_GRAPH = 1;
	public final static int UNDIRECTED_SPARSE_GRAPH = 2;	

	/**
	 * Constructor.
	 * 
	 */
	public GraphModeller() {
		// create a directed graph by default
		this(DIRECTED_SPARSE_GRAPH);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param graphType the graph type
	 */
	public GraphModeller(int graphType) {
		// create a directed graph by default
		graph = new DelegateForest<ParseTreeNode, ParseTreeEdge>();
	}

	/**
	 * Gets the graph.
	 * 
	 * @return Graph<Vertex, Edge> the graph
	 */
	public Forest<ParseTreeNode, ParseTreeEdge> getGraph() {
		return graph;
	}

	/**
	 * Creates the graph.
	 * 
	 * @param vertices the vertex list
	 * @param edges the edge list
	 * @return Graph<Vertex, Edge> the graph
	 */
	public Forest<ParseTreeNode, ParseTreeEdge> createGraph(List<ParseTreeNode> vertices, List<ParseTreeEdge> edges) {
		HashMap<String, ParseTreeNode> verticesMap = createVertices(vertices);
		createEdges(verticesMap, edges);
		return graph;
	}
	
	/**
	 * Creates the vertices.
	 * 
	 */
	private HashMap<String, ParseTreeNode> createVertices(List<ParseTreeNode> vertices) {
		HashMap<String, ParseTreeNode> verticesMap = new HashMap<String, ParseTreeNode>();
		for (int i = 0; i < vertices.size(); i++) {
			ParseTreeNode vertex = (ParseTreeNode) vertices.get(i);
			
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
	private void createEdges(HashMap<String, ParseTreeNode> vertices, List<ParseTreeEdge> edges) {
		for (int i = 0; i < edges.size(); i++) {
			ParseTreeEdge edge = (ParseTreeEdge) edges.get(i);
			String edgeId = edge.getFromId() + "^" + edge.getToId();
			
			System.out.println("from : " + edge.getFromId() + " ---> to : " + edge.getToId());
			ParseTreeNode from = vertices.get(String.valueOf(edge.getFromId()));
			ParseTreeNode to = vertices.get(String.valueOf(edge.getToId()));
			//if (from != null && to != null)
				graph.addEdge(edge, from, to);

		}
	}
}
