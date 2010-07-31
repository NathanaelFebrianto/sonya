/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.graph.clustering;

import java.util.Date;
import java.util.List;

import org.firebird.analyzer.graph.SimpleGraphModeller;
import org.firebird.analyzer.util.JobLogger;
import org.firebird.analyzer.util.OutputFileReader;
import org.firebird.io.model.Edge;
import org.firebird.io.model.Vertex;
import org.firebird.io.service.EdgeManager;
import org.firebird.io.service.VertexManager;
import org.firebird.io.service.impl.EdgeManagerImpl;
import org.firebird.io.service.impl.VertexManagerImpl;

import edu.uci.ics.jung.graph.Graph;

/**
 * This class is a clustering job.
 * 
 * @author Young-Gue Bae
 */
public class ClusteringJob {
	/** logger */
	private static JobLogger logger = JobLogger.getLogger(ClusteringJob.class);
	
	/** configuration */
	private int websiteId = 1;
	private String ebOutputFile;
	private String voOutputFile;
	
	/**
	 * Constructor.
	 * 
	 */
	public ClusteringJob() {
		this.websiteId = 1;
		this.ebOutputFile = "D:/firebird/clusters_edgebetweenness.txt";
		this.voOutputFile = "D:/firebird/clusters_voltage.txt";
	}
	
	/*
	private void clusterByEdgeBetweennessClusterer(Graph<Vertex, Edge> graph, int numEdgesToRemove) throws Exception {
		Clusterer clusterer = new Clusterer(graph);
		clusterer.clusterByEdgeBetweennessClusterer(ebOutputFile, numEdgesToRemove);
	}
	*/
	private void clusterByEdgeBetweennessClusterer(Graph<String, String> graph, int numEdgesToRemove) throws Exception {
		Clusterer clusterer = new Clusterer(graph);
		clusterer.clusterByEdgeBetweennessClusterer(ebOutputFile, numEdgesToRemove);
	}
	
	/*
	private void clusterByVoltageClusterer(Graph<Vertex, Edge> graph, int numCandidates, int numClusters) throws Exception {
		Clusterer clusterer = new Clusterer(graph);
		clusterer.clusterByVoltageClusterer(voOutputFile, numCandidates, numClusters);
	}
	*/
	private void clusterByVoltageClusterer(Graph<String, String> graph, int numCandidates, int numClusters) throws Exception {
		Clusterer clusterer = new Clusterer(graph);
		clusterer.clusterByVoltageClusterer(voOutputFile, numCandidates, numClusters);
	}
	
	private void storeEdgeBetweennessClusterSet() throws Exception {
		logger.info("storeEdgeBetweennessClusterSet..............");
		
		OutputFileReader outputReader = new OutputFileReader(websiteId);
		List<Vertex> vertices = outputReader.loadEdgeBetweennessClusters(ebOutputFile);
		
		VertexManager vertexManager = new VertexManagerImpl();
		
		for (Vertex vertex : vertices) {
			vertexManager.setVertexCluster(vertex);
		}
	}
	
	private void storeVoltageClusterSet() throws Exception {
		logger.info("storeVoltageClusterSet..............");
		
		OutputFileReader outputReader = new OutputFileReader(websiteId);
		List<Vertex> vertices = outputReader.loadVoltageClusters(voOutputFile);
		
		VertexManager vertexManager = new VertexManagerImpl();
		
		for (Vertex vertex : vertices) {
			vertexManager.setVertexCluster(vertex);
		}
	}
	
	public static void main(String[] args) {
    	try {    		
    		String clusterType = Clusterer.EDGE_BETWEENNESS_CLUSTER;
    		//String clusterType = Clusterer.VOLTAGE_CLUSTER;
    		
    		ClusteringJob job = new ClusteringJob();
    		
    		VertexManager vertexManager = new VertexManagerImpl();
        	EdgeManager edgeManager = new EdgeManagerImpl();
        	//GraphModeller modeller = new GraphModeller();
        	SimpleGraphModeller modeller = new SimpleGraphModeller();
        	
        	List<Vertex> vertices = vertexManager.getVertices(1);
        	List<Edge> edges = edgeManager.getEdges(1, 1);        	
        	Graph<String, String> graph = modeller.createGraph(vertices, edges);

    		// start time
    		Date startTime = new Date();
    		logger.info("\n\n******************************************");
    		logger.info("Start Clustering - " + clusterType + " : " + startTime);
        	
    		// EdgeBetweenness Clusterer
    		if (clusterType.equals(Clusterer.EDGE_BETWEENNESS_CLUSTER)) {
        		int numEdgesToRemove = 15;
            	job.clusterByEdgeBetweennessClusterer(graph, numEdgesToRemove);    			
    		}
    		// Voltage Clusterer
    		else if (clusterType.equals(Clusterer.VOLTAGE_CLUSTER)) {
    	       	int numCandidates = 100;
            	int numClusters = 10;
            	job.clusterByVoltageClusterer(graph, numCandidates, numClusters);   			
    		}
        	
    		// end time
    		Date endTime = new Date();
    		logger.info("Finish Clustering : " + endTime);
    		logger.jobSummary("Clustering - " + clusterType, startTime, endTime);

    		// store to database
    		if (clusterType.equals(Clusterer.EDGE_BETWEENNESS_CLUSTER)) {
    			job.storeEdgeBetweennessClusterSet(); 			
    		}
    		else if (clusterType.equals(Clusterer.VOLTAGE_CLUSTER)) {
    	       	job.storeVoltageClusterSet();  			
    		}        	
     	} catch (Exception e) {
        	e.printStackTrace();
        	logger.error(e.getMessage(), e);
        }  
	}
	
}
