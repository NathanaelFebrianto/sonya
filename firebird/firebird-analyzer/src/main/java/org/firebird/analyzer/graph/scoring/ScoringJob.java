/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.graph.scoring;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.firebird.analyzer.graph.GraphModeller;
import org.firebird.analyzer.util.JobLogger;
import org.firebird.io.model.Edge;
import org.firebird.io.model.Vertex;
import org.firebird.io.service.EdgeManager;
import org.firebird.io.service.VertexManager;
import org.firebird.io.service.impl.EdgeManagerImpl;
import org.firebird.io.service.impl.VertexManagerImpl;

import edu.uci.ics.jung.graph.Graph;

/**
 * This class is a scoring job.
 * 
 * @author Young-Gue Bae
 */
public class ScoringJob {
	/** logger */
	private static JobLogger logger = JobLogger.getLogger(Scorer.class);
	
	public static void main(String[] args) {
    	try {    		
        	VertexManager vertexManager = new VertexManagerImpl();
        	EdgeManager edgeManager = new EdgeManagerImpl();
        	GraphModeller modeller = new GraphModeller();
        	
        	// create a graph
        	List<Vertex> vertices = vertexManager.getVertices(1);
        	List<Edge> edges = edgeManager.getEdges(1, 1);        	
        	Graph<Vertex, Edge> graph = modeller.createGraph(vertices, edges);

    		// set config
        	ScoringConfig config = new ScoringConfig();
    		config.setEnblePageRank(true);
    		config.setEnbleHITS(true);
    		config.setEnableBetweennessCentrality(true);
    		config.setEnableClosenessCentrality(true);
    		config.setEnableEigenvectorCentrality(false);
        	
    		// start time
    		Date startTime = new Date();
    		logger.info("\n\n******************************************");
    		logger.info("Start Scoring : " + startTime + "\n");
    		
        	// scoring
        	Scorer scorer = new Scorer(graph, config);
    		graph = scorer.evaluate(); 
    		
    		// end time
    		Date endTime = new Date();
    		logger.info("Finish Scoring : " + endTime);
    		logger.jobSummary("Scoring", startTime, endTime);
    		
    		logger.info("Start to store into database:");
    		// store the vertices' scores to database    		
    		Collection<Vertex> vertexScores = graph.getVertices();        	
        	Iterator<Vertex> it1 = vertexScores.iterator();
        	while(it1.hasNext()) { 
        		Vertex v = (Vertex)it1.next();

        		// check "NaN"
        		if (Double.valueOf(v.getPageRank()).isNaN())
        			v.setPageRank(0);
        		if (Double.valueOf(v.getAuthority()).isNaN())
        			v.setAuthority(0);
        		if (Double.valueOf(v.getBetweennessCentrality()).isNaN())
        			v.setBetweennessCentrality(0);
        		if (Double.valueOf(v.getClosenessCentrality()).isNaN())
        			v.setClosenessCentrality(0);
        		if (Double.valueOf(v.getEigenvectorCentrality()).isNaN())
        			v.setEigenvectorCentrality(0);

        		vertexManager.setVertexScore(v);
        	}
        	
        	// store the edges' scores to database
        	Collection<Edge> edgeScores = graph.getEdges();        	
        	Iterator<Edge> it2 = edgeScores.iterator();
        	while(it2.hasNext()) { 
        		Edge e = (Edge)it2.next();
        		edgeManager.setEdgeScore(e);
        	}
        	logger.info("Completed to store into database!"); 
     	} catch (Exception e) {
        	e.printStackTrace();
        	logger.error(e.getMessage(), e);
        }  
	}
	
}
