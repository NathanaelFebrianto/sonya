/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph;

import java.util.HashMap;
import java.util.List;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * A Social network graph modeller.
 * 
 * @author Young-Gue Bae
 */
public class GraphModeller {

	/** graph */
	Graph<org.firebird.io.model.Vertex, org.firebird.io.model.Edge> graph;

	/**
	 * Constructor.
	 * 
	 */
	public GraphModeller() {
		// create a directed graph
		graph = new DirectedSparseGraph<org.firebird.io.model.Vertex, org.firebird.io.model.Edge>();
	}

	/**
	 * Gets the graph.
	 * 
	 * @return Graph<Vertex, Edge> the graph
	 */
	public Graph<org.firebird.io.model.Vertex, org.firebird.io.model.Edge> getGraph() {
		return graph;
	}

	/**
	 * Creates the graph.
	 * 
	 * @param vertices the vertex list
	 * @param edges the edge list
	 */
	public void createGraph(List<org.firebird.io.model.Vertex> vertices, List<org.firebird.io.model.Edge> edges) {
		HashMap<String, org.firebird.io.model.Vertex> verticesMap = createVertices(vertices);
		createEdges(verticesMap, edges);
	}
	
	/**
	 * Creates the vertices.
	 * 
	 */
	private HashMap<String, org.firebird.io.model.Vertex> createVertices(List<org.firebird.io.model.Vertex> vertices) {
		HashMap<String, org.firebird.io.model.Vertex> verticesMap = new HashMap<String, org.firebird.io.model.Vertex>();

		for (int i = 0; i < vertices.size(); i++) {
			org.firebird.io.model.Vertex vertex = (org.firebird.io.model.Vertex) vertices.get(i);
			graph.addVertex(vertex);
			verticesMap.put(vertex.getId(), vertex);
		}
		return verticesMap;
	}

	/**
	 * Creates edges for this graph
	 * 
	 */
	private void createEdges(HashMap<String, org.firebird.io.model.Vertex> vertices,
			List<org.firebird.io.model.Edge> edges) {

		for (int i = 0; i < edges.size(); i++) {
			org.firebird.io.model.Edge edge = (org.firebird.io.model.Edge) edges.get(i);
			graph.addEdge(edge, vertices.get(edge.getVertex1()), vertices.get(edge.getVertex2()), EdgeType.DIRECTED);
		}
	}
}
