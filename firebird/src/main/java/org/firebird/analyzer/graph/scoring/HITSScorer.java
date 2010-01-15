/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.graph.scoring;

import java.util.HashMap;
import java.util.List;

import org.firebird.io.service.EdgeManager;
import org.firebird.io.service.VertexManager;
import org.firebird.io.service.impl.EdgeManagerImpl;
import org.firebird.io.service.impl.VertexManagerImpl;

import edu.uci.ics.jung.algorithms.scoring.HITS;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * HITS algorithm.
 * Assigns hub and authority scores to each vertex depending on the topology of the network. 
 * The essential idea is that a vertex is a hub to the extent that it links 
 * to authoritative vertices, and is an authority to the extent 
 * that it links to 'hub' vertices.
 * 
 * @author Young-Gue Bae
 */
public class HITSScorer {
	
	/** graph */
	DirectedSparseGraph<org.firebird.io.model.Vertex, org.firebird.io.model.Edge> graph;
	
	/**
	 * Constructor.
	 * 
	 */
	public HITSScorer () {
		graph = new DirectedSparseGraph<org.firebird.io.model.Vertex, org.firebird.io.model.Edge>();
	}
	
	public void analyze() {
		VertexManager vertexManager = new VertexManagerImpl();
    	List<org.firebird.io.model.Vertex> vertices = vertexManager.getVertices(1);
    	
    	EdgeManager edgeManager = new EdgeManagerImpl();
    	List<org.firebird.io.model.Edge> edges = edgeManager.getEdges(1, 1);
    	
    	this.createGraph(vertices, edges);
    	
    	HITS<org.firebird.io.model.Vertex, org.firebird.io.model.Edge> hits 
    		= new HITS<org.firebird.io.model.Vertex, org.firebird.io.model.Edge>(graph, 0.5);
    	hits.evaluate();
 
    	for (int i = 0; i < vertices.size(); i++) {
    		org.firebird.io.model.Vertex v = (org.firebird.io.model.Vertex)vertices.get(i);
    		HITS.Scores score = hits.getVertexScore(v);
    		System.out.println(score.toString() + "," + v.getId());
    		//System.out.println("HITS [" + v.getUserName() + "] authoriry = " + score.authority + ", hub = " + score.hub);
    	}
	}

	/**
	 * Creates the graph.
	 * 
	 */
	private void createGraph(List<org.firebird.io.model.Vertex> vertices, List<org.firebird.io.model.Edge> edges) {
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
