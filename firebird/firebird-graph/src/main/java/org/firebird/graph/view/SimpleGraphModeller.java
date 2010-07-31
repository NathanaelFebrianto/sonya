/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view;

import java.util.ArrayList;
import java.util.Collection;
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
public class SimpleGraphModeller {

	/** graph */
	Graph<String, String> graph;
	
	/** graph type */
	public final static int DIRECTED_SPARSE_GRAPH = 1;
	public final static int UNDIRECTED_SPARSE_GRAPH = 2;	

	/**
	 * Constructor.
	 * 
	 */
	public SimpleGraphModeller() {
		// create a directed graph by default
		this(DIRECTED_SPARSE_GRAPH);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param graphType the graph type
	 */
	public SimpleGraphModeller(int graphType) {
		// create a directed graph by default
		if (graphType == DIRECTED_SPARSE_GRAPH)
			graph = new DirectedSparseGraph<String, String>();
		else if (graphType == UNDIRECTED_SPARSE_GRAPH)
			graph = new UndirectedSparseGraph<String, String>();
		else
			graph = new DirectedSparseGraph<String, String>();
	}
	
	/**
	 * Clears the graph.
	 */
	public void clearGraph() {
		Collection<String> vertices = graph.getVertices();
		Object[] arrVertices = vertices.toArray().clone();
		for (int i = 0; i < arrVertices.length; i++) {
			graph.removeVertex((String)arrVertices[i]);
		}
		
		Collection<String> edges = graph.getEdges();
		Object[] arrEdges = edges.toArray().clone();
		for (int i = 0; i < arrEdges.length; i++) {
			graph.removeEdge((String)arrEdges[i]);
		}
	}

	/**
	 * Gets the graph.
	 * 
	 * @return Graph<Vertex, Edge> the graph
	 */
	public Graph<String, String> getGraph() {
		return graph;
	}

	/**
	 * Creates the graph.
	 * 
	 * @param vertices the vertex list
	 * @param edges the edge list
	 * @return Graph<Vertex, Edge> the graph
	 */
	public Graph<String, String> createGraph(List<Vertex> vertices, List<Edge> edges) {
		createVertices(vertices);
		createEdges(edges);
		return graph;
	}
	
	/**
	 * Creates the vertices.
	 * 
	 */
	private List<String> createVertices(List<Vertex> vertices) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < vertices.size(); i++) {
			Vertex vertex = (Vertex) vertices.get(i);
			graph.addVertex(vertex.getId());
			list.add(vertex.getId());
		}
		return list;
	}

	/**
	 * Creates the edges.
	 * 
	 */
	private void createEdges(List<Edge> edges) {
		for (int i = 0; i < edges.size(); i++) {
			Edge edge = (Edge) edges.get(i);
			String edgeId = edge.getVertex1() + "^" + edge.getVertex2();
			if (edge.getDirected())
				graph.addEdge(edgeId, edge.getVertex1(), edge.getVertex2(), EdgeType.DIRECTED);
			else
				graph.addEdge(edgeId, edge.getVertex1(), edge.getVertex2(), EdgeType.UNDIRECTED);
		}
	}
}
