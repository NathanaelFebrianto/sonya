/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.graph;

import java.util.HashMap;
import java.util.List;

import org.firebird.io.model.Edge;
import org.firebird.io.model.Vertex;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * A Social network graph modeller.
 * 
 * @author Young-Gue Bae
 */
public class GraphModeller {

	/** graph */
	Graph<Vertex, Edge> graph;
	
	/** graph type */
	public final static int DIRECTED_SPARSE_GRAPH = 1;
	public final static int UNDIRECTED_SPARSE_GRAPH = 2;	

	/**
	 * Constructor.
	 * 
	 */
	public GraphModeller() {
		// create a directed graph by default
		graph = new DirectedSparseGraph<Vertex, Edge>();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param type the graph type
	 */
	public GraphModeller(int type) {
		if (type == DIRECTED_SPARSE_GRAPH)
			graph = new DirectedSparseGraph<Vertex, Edge>();
		else if (type == UNDIRECTED_SPARSE_GRAPH)
			graph = new UndirectedSparseGraph<Vertex, Edge>();
		else
			graph = new DirectedSparseGraph<Vertex, Edge>();
	}	

	/**
	 * Gets the graph.
	 * 
	 * @return Graph<Vertex, Edge> the graph
	 */
	public Graph<Vertex, Edge> getGraph() {
		return graph;
	}

	/**
	 * Creates the graph.
	 * 
	 * @param vertices the vertex list
	 * @param edges the edge list
	 * @return Graph<Vertex, Edge> the graph
	 */
	public Graph<Vertex, Edge> createGraph(List<Vertex> vertices, List<Edge> edges) {
		HashMap<String, Vertex> verticesMap = createVertices(vertices);
		createEdges(verticesMap, edges);
		return graph;
	}
	
	/**
	 * Creates the vertices.
	 * 
	 */
	private HashMap<String, Vertex> createVertices(List<Vertex> vertices) {
		HashMap<String, Vertex> verticesMap = new HashMap<String, Vertex>();

		for (int i = 0; i < vertices.size(); i++) {
			Vertex vertex = (Vertex) vertices.get(i);
			graph.addVertex(vertex);
			verticesMap.put(vertex.getId(), vertex);
		}
		return verticesMap;
	}

	/**
	 * Creates the edges.
	 * 
	 */
	private void createEdges(HashMap<String, Vertex> vertices, List<Edge> edges) {

		for (int i = 0; i < edges.size(); i++) {
			Edge edge = (Edge) edges.get(i);
			if (edge.getDirected())
				graph.addEdge(edge, vertices.get(edge.getVertex1()), vertices.get(edge.getVertex2()), EdgeType.DIRECTED);
			else
				graph.addEdge(edge, vertices.get(edge.getVertex1()), vertices.get(edge.getVertex2()), EdgeType.UNDIRECTED);
		}
	}
}
