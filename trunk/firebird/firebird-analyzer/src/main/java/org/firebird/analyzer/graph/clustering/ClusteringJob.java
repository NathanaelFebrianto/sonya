/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.graph.clustering;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.firebird.analyzer.graph.GraphModeller;
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
	
	public static void main(String[] args) {
    	try {    		
        	VertexManager vertexManager = new VertexManagerImpl();
        	EdgeManager edgeManager = new EdgeManagerImpl();
        	GraphModeller modeller = new GraphModeller();
        	
        	List<Vertex> vertices = vertexManager.getVertices(1);
        	List<Edge> edges = edgeManager.getEdges(1, 1);
        	
        	Graph<Vertex, Edge> graph = modeller.createGraph(vertices, edges);
    		
    		Clusterer clusterer = new Clusterer(graph);
    		
    		// EdgeBetweenness Clusterer
    		
    		System.out.println("Start Clustering : " + Calendar.getInstance().getTime());
    		int numEdgesToRemove = 1000;
    		Set<Set<Vertex>> clusterSet = clusterer.clusterByEdgeBetweennessClusterer(numEdgesToRemove);
    		System.out.println("Finish Clustering : " + Calendar.getInstance().getTime());
    		int cluster = 0;
    		for (Iterator<Set<Vertex>> it1 = clusterSet.iterator(); it1.hasNext();) {
    			System.out.println("----------------------------------");
    			System.out.println("cluster == " + cluster);
    			Set<Vertex> verticesSet = (Set<Vertex>) it1.next();
     			for (Iterator<Vertex> it2 = verticesSet.iterator(); it2.hasNext();) {
    				Vertex vertex = (Vertex) it2.next();
    				System.out.println("vertex == " + vertex.getId());
    			}
    			cluster++;
    		}
    		
    		// Voltage Clusterer
    		/*
    		System.out.println("Start Clustering : " + Calendar.getInstance().getTime());
    		int numCandidates = 3000;
    		int numClusters = 100;
    		Collection<Set<Vertex>> clusterSet = clusterer.clusterByVoltageClusterer(numCandidates, numClusters);
    		System.out.println("Finish Clustering : " + Calendar.getInstance().getTime());
    		int cluster = 0;
    		for (Iterator<Set<Vertex>> it1 = clusterSet.iterator(); it1.hasNext();) {
    			System.out.println("----------------------------------");
    			System.out.println("cluster == " + cluster);
    			Set<Vertex> verticesSet = (Set<Vertex>) it1.next();
     			for (Iterator<Vertex> it2 = verticesSet.iterator(); it2.hasNext();) {
    				Vertex vertex = (Vertex) it2.next();
    				System.out.println("vertex == " + vertex.getId());
    			}
    			cluster++;
    		}
    		*/ 		
     	} catch (Exception e) {
        	e.printStackTrace();
        }  
	}
	
}
