/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.service.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.firebird.analyzer.graph.GraphModeller;
import org.firebird.analyzer.graph.scoring.Scorer;
import org.firebird.analyzer.graph.scoring.ScoringConfig;
import org.firebird.analyzer.service.AnalysisManager;
import org.firebird.common.service.GenericManagerImpl;
import org.firebird.io.model.Edge;
import org.firebird.io.model.Vertex;
import org.firebird.io.service.EdgeManager;
import org.firebird.io.service.VertexManager;
import org.firebird.io.service.impl.EdgeManagerImpl;
import org.firebird.io.service.impl.VertexManagerImpl;

import edu.uci.ics.jung.graph.Graph;

/**
 * A implementation for analysis manager.
 * 
 * @author Young-Gue Bae
 */
public class AnalysisManagerImpl extends GenericManagerImpl implements AnalysisManager {

	/**
     * Constructor.
     *
     */
    public AnalysisManagerImpl() {
    }
    
	/**
	 * Evaluates the scores of the graph.
	 * such as HITS, Betweenness Centrality, Closeness Centrality, Degree etc.
	 * 
	 * @param config the scoring config
	 * @param websiteId1 the website id1
	 * @param websiteId2 the website id2
	 */
	public void scoringGraph(ScoringConfig config, int websiteId1, int websiteId2) throws Exception {
    	try {
        	VertexManager vertexManager = new VertexManagerImpl();
        	EdgeManager edgeManager = new EdgeManagerImpl();
        	GraphModeller modeller = new GraphModeller();
        	
        	// create a graph
        	List<Vertex> vertices = vertexManager.getVertices(websiteId1);
        	List<Edge> edges = edgeManager.getEdges(websiteId1, websiteId2);        	
        	Graph<Vertex, Edge> graph = modeller.createGraph(vertices, edges);
    		
        	// scoring
        	Scorer scorer = new Scorer(graph, config);
    		graph = scorer.evaluate();    		
    		
    		// store the vertices' scores to database
    		Collection<Vertex> vertexScores = graph.getVertices();        	
        	Iterator<Vertex> it1 = vertexScores.iterator();
        	while(it1.hasNext()) { 
        		Vertex v = (Vertex)it1.next();
        		vertexManager.setVertexScore(v);
        	}
        	// store the edges' scores to database
        	Collection<Edge> edgeScores = graph.getEdges();        	
        	Iterator<Edge> it2 = edgeScores.iterator();
        	while(it2.hasNext()) { 
        		Edge e = (Edge)it2.next();
        		edgeManager.setEdgeScore(e);
        	}
        } catch (Exception ex) {
        	ex.printStackTrace();
        	throw ex;
        }		
	}
}
