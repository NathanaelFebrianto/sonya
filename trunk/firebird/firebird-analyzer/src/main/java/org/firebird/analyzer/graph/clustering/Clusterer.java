/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.graph.clustering;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.firebird.analyzer.util.JobLogger;
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
	/** logger */
	private static JobLogger logger = JobLogger.getLogger(Clusterer.class);
	
	/** cluster type */
	public static final String EDGE_BETWEENNESS_CLUSTER = "EDGE_BETWEENNESS_CLUSTER";
	public static final String VOLTAGE_CLUSTER = "VOLTAGE";
	public static final String CNM_CLUSTER = "CNM";	//Clauset-Newman-Moore Algorithm
	
	/** graph */
	Graph<Vertex, Edge> graph;
	//Graph<String, String> graph;
	
	/**
	 * Constructor.
	 * 
	 * @param graph the Graph<Vertex, Edge>
	 */
	public Clusterer(Graph<Vertex, Edge> graph) {
		this.graph = graph;
	}
	/*
	public Clusterer(Graph<String, String> graph) {
		this.graph = graph;
	}
	*/
		
	/**
	 * Gets the graph.
	 * 
	 * @return Graph<Vertex, Edge> the graph
	 */
	public Graph<Vertex, Edge> getGraph() {
		return graph;
	}
	/*
	public Graph<String, String> getGraph() {
		return graph;
	}
	*/

	/**
	 * Clusters the graph by the EdgeBetweenness Clusterer.
	 * 
	 * @param outputFile the output file to write
	 * @param numEdgesToRemove the number of edges to remove
	 * @return Set<Set<Vertex>> the set of vertex
	 * @exception
	 */
	public Set<Set<Vertex>> clusterByEdgeBetweennessClusterer(String outputFile, int numEdgesToRemove) throws Exception {
		logger.info("Clustering by EdgeBetweennessClusterer..............");
		EdgeBetweennessClusterer<Vertex, Edge> clusterer = new EdgeBetweennessClusterer<Vertex, Edge>(numEdgesToRemove);
		Set<Set<Vertex>> clusterSet = clusterer.transform(graph);
		
		logger.info("Writing the clustering result to: " + outputFile);
		this.writeEdgeBetweennessClusterSet(outputFile, clusterSet);
		
		return clusterSet;
	}
	/*
	public Set<Set<String>> clusterByEdgeBetweennessClusterer(String outputFile, int numEdgesToRemove) throws Exception {
		logger.info("Clustering by EdgeBetweennessClusterer..............");
		EdgeBetweennessClusterer<String, String> clusterer = new EdgeBetweennessClusterer<String, String>(numEdgesToRemove);
		Set<Set<String>> clusterSet = clusterer.transform(graph);
		
		logger.info("Writing the clustering result to: " + outputFile);
		this.writeEdgeBetweennessClusterSet(outputFile, clusterSet);
		
		return clusterSet;
	}
	*/
	
	/**
	 * Clusters the graph by the Voltage Clusterer.
	 * 
	 * @param outputFile the output file to write
	 * @param numClusters the number of clusters
	 * @return Collection<Set<Vertex>> the collection of vertex
	 */
	public Collection<Set<Vertex>> clusterByVoltageClusterer(String outputFile, int numCandidates, int numClusters) throws Exception {
		logger.info("Clustering by VoltageClusterer..............");
		VoltageClusterer<Vertex, Edge> clusterer = new VoltageClusterer<Vertex, Edge>(graph, numCandidates);
		Collection<Set<Vertex>> clusterSet = clusterer.cluster(numClusters);
		
		logger.info("Writing the clustering result to: " + outputFile);
		this.writeVoltageClusterSet(outputFile, clusterSet);
		
		return clusterSet;
	}
	/*
	public Collection<Set<String>> clusterByVoltageClusterer(String outputFile, int numCandidates, int numClusters) throws Exception {
		logger.info("Clustering by VoltageClusterer..............");
		VoltageClusterer<String, String> clusterer = new VoltageClusterer<String, String>(graph, numCandidates);
		Collection<Set<String>> clusterSet = clusterer.cluster(numClusters);
		
		logger.info("Writing the clustering result to: " + outputFile);
		this.writeVoltageClusterSet(outputFile, clusterSet);
		
		return clusterSet;
	}
	*/
	
	private void writeEdgeBetweennessClusterSet(String outputFile, Set<Set<Vertex>> clusterSet) throws Exception {
		File out = new File(outputFile);
		PrintWriter writer = new PrintWriter(new FileWriter(out));

		writer.println("#cluster	vertex");
		
		int cluster = 1;
		for (Iterator<Set<Vertex>> it1 = clusterSet.iterator(); it1.hasNext();) {
			Set<Vertex> verticesSet = (Set<Vertex>) it1.next();
 			for (Iterator<Vertex> it2 = verticesSet.iterator(); it2.hasNext();) {
				Vertex vertex = (Vertex) it2.next();
				writer.println(cluster + "\t" + vertex.getId());
			}
			cluster++;
		}
		writer.close();
	}
	/*
	private void writeEdgeBetweennessClusterSet(String outputFile, Set<Set<String>> clusterSet) throws Exception {
		File out = new File(outputFile);
		PrintWriter writer = new PrintWriter(new FileWriter(out));

		writer.println("#cluster	vertex");
		
		int cluster = 1;
		for (Iterator<Set<String>> it1 = clusterSet.iterator(); it1.hasNext();) {
			Set<String> verticesSet = (Set<String>) it1.next();
 			for (Iterator<String> it2 = verticesSet.iterator(); it2.hasNext();) {
 				String vertex = (String) it2.next();
				writer.println(cluster + "\t" + vertex);
			}
			cluster++;
		}
		writer.close();
	}
	*/
	
	private void writeVoltageClusterSet(String outputFile, Collection<Set<Vertex>> clusterSet) throws Exception {
		File out = new File(outputFile);
		PrintWriter writer = new PrintWriter(new FileWriter(out));
		
		writer.println("#cluster	vertex");
		
		int cluster = 1;
		for (Iterator<Set<Vertex>> it1 = clusterSet.iterator(); it1.hasNext();) {
			Set<Vertex> verticesSet = (Set<Vertex>) it1.next();
 			for (Iterator<Vertex> it2 = verticesSet.iterator(); it2.hasNext();) {
				Vertex vertex = (Vertex) it2.next();
				writer.println(cluster + "\t" + vertex.getId());
			}
			cluster++;
		}
		writer.close();
	}
	/*
	private void writeVoltageClusterSet(String outputFile, Collection<Set<String>> clusterSet) throws Exception {
		File out = new File(outputFile);
		PrintWriter writer = new PrintWriter(new FileWriter(out));
		writer.println("#cluster	vertex");
		
		int cluster = 1;
		for (Iterator<Set<String>> it1 = clusterSet.iterator(); it1.hasNext();) {
			Set<String> verticesSet = (Set<String>) it1.next();
 			for (Iterator<String> it2 = verticesSet.iterator(); it2.hasNext();) {
 				String vertex = (String) it2.next();
				writer.println(cluster + "\t" + vertex);
			}
			cluster++;
		}
		writer.close();
	}
	*/
	
}
