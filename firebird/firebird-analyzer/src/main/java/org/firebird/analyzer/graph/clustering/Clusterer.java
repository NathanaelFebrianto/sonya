/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.graph.clustering;

import java.util.Collection;
import java.util.Set;

import org.firebird.io.model.Edge;
import org.firebird.io.model.Vertex;

import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.algorithms.cluster.VoltageClusterer;
import edu.uci.ics.jung.graph.Graph;

/**
 * Clustering algorithm for a graph.
 * EdgeBetweenness Clusterer, Voltage Clusterer.
 * 
 * @author Young-Gue Bae
 */
public final class Clusterer {
	
	/** graph */
	Graph<Vertex, Edge> graph;
	
	/**
	 * Constructor.
	 * 
	 * @param graph the Graph<Vertex, Edge>
	 */
	public Clusterer(Graph<Vertex, Edge> graph) {
		this.graph = graph;
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
	 * Clusters the graph by the EdgeBetweenness Clusterer.
	 * 
	 * @return Set<Set<Vertex>> the set of vertex
	 */
	public Set<Set<Vertex>> clusterByEdgeBetweennessClusterer(int numEdgesToRemove) {
		EdgeBetweennessClusterer<Vertex, Edge> ebClusterer = new EdgeBetweennessClusterer<Vertex, Edge>(numEdgesToRemove);
		Set<Set<Vertex>> set = ebClusterer.transform(graph);
		
		return set;
	}
	
	/**
	 * Clusters the graph by the Voltage Clusterer.
	 * 
	 * @return Collection<Set<Vertex>> the collection of vertex
	 */
	public Collection<Set<Vertex>> clusterByVoltageClusterer(int numCandidates, int numClusters) {
		VoltageClusterer<Vertex, Edge> voClusterer = new VoltageClusterer<Vertex, Edge>(graph, numCandidates);
		Collection<Set<Vertex>> collection = voClusterer.cluster(numClusters);
		return collection;
	}

}
