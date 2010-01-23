/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.graph.scoring;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import org.firebird.io.model.Edge;
import org.firebird.io.model.Vertex;

import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import edu.uci.ics.jung.algorithms.scoring.HITS;
import edu.uci.ics.jung.graph.Graph;

/**
 * Scoring algorithm for a graph.
 * HITS, Betweenness Centrality, Closeness Centrality, Degree etc.
 * 
 * @author Young-Gue Bae
 */
public final class Scorer {
	
	/** HITS algorithm */
	HITS<Vertex, Edge> hits;
	/** Betweenness Centrality algorithm */
	BetweennessCentrality<Vertex, Edge> bc;	
	/** Closeness Centrality algorithm */
	ClosenessCentrality<Vertex, Edge> cc;
	/** Eigenvector Centrality algorithm */
	EigenvectorCentrality<Vertex, Edge> ec;
	
	/** graph */
	Graph<Vertex, Edge> graph;
	
	/**
	 * Constructor.
	 * 
	 * @param graph the Graph<Vertex, Edge>
	 */
	public Scorer (Graph<Vertex, Edge> graph) {
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
	 * Enables HITS algorithm.
	 * 
	 * @param true if enable
	 */	
	public void enableHITS(boolean enable) {
		if (enable)
			hits = new HITS<Vertex, Edge>(graph);
		else
			hits = null;
	}
	
	/**
	 * Enables Betweenness Centrality algorithm.
	 * 
	 * @param true if enable
	 */	
	public void enableBetweennessCentrality(boolean enable) {
		if (enable)
			bc = new BetweennessCentrality<Vertex, Edge>(graph);
		else
			bc = null;
	}
	
	/**
	 * Enables Closeness Centrality algorithm.
	 * 
	 * @param true if enable
	 */	
	public void enableClosenessCentrality(boolean enable) {
		if (enable)
			cc = new ClosenessCentrality<Vertex, Edge>(graph);
		else
			cc = null;
	}
	
	/**
	 * Enables Eigenvector Centrality algorithm.
	 * 
	 * @param true if enable
	 */	
	public void enableEigenvectorCentrality(boolean enable) {
		if (enable)
			ec = new EigenvectorCentrality<Vertex, Edge>(graph);
		else
			ec = null;
	}
		
	/**
	 * Evaluates the graph by the enabled algorithms.
	 * 
	 * @exception
	 */
	public void evaluate() throws Exception {
		// HITS
		if (hits != null) {
			hits.evaluate();
		}
		// Betweenness Centraility
		if (bc != null) {
			bc.evaluate();		
			bc.step();
		}
		// Closeness Centraility
		if (cc != null) {
		}
		// Eigenvector Centrality
		if (ec != null) {
			ec.evaluate();		
			ec.step();
		}
		
		storeVertexScoreToGraph();
		storeEdgeScoreToGraph();
	}
	
	private void storeVertexScoreToGraph() throws Exception {
    	System.out.println("--------------------------------------------------------------------------------");
    	System.out.println("USERID | IN | OUT | AUTHORITY | HUB | BETWEENNESS | CLOSENESS | EIGENVECTOR");
    	System.out.println("--------------------------------------------------------------------------------");		
		
		Collection<Vertex> vertices = graph.getVertices();
    	
    	Iterator<Vertex> it = vertices.iterator();
    	while(it.hasNext()) { 
    		StringBuffer out = new StringBuffer();
    		Vertex v = (Vertex)it.next();
    		
    		// Degree score
    		v.setInDegree(graph.inDegree(v));
    		v.setOutDegree(graph.outDegree(v));
    		
    		out.append(v.getId()).append("|");
    		out.append(v.getInDegree()).append("|");
    		out.append(v.getOutDegree()).append("|");
    		    		
    		// HITS score
    		if (hits != null) {
    			HITS.Scores scoreHITS = hits.getVertexScore(v);    			
    			v.setAuthority(scoreHITS.authority);
    			v.setHub(scoreHITS.hub);
 
    			out.append(new BigDecimal(scoreHITS.authority).setScale(4, BigDecimal.ROUND_HALF_UP)).append("|");
        		out.append(new BigDecimal(scoreHITS.hub).setScale(4, BigDecimal.ROUND_HALF_UP)).append("|");        		
    		} 
    		else {
    			out.append("").append("|");
    			out.append("").append("|");
    		}
    			
    		// Betweenness Centrality score
    		if (bc != null) {
    			double scoreBC = bc.getVertexRankScore(v);    			
    			v.setBetweennessCentrality(scoreBC);
    			
    			out.append(new BigDecimal(scoreBC).setScale(0, BigDecimal.ROUND_HALF_UP)).append("|");
    		}
    		else {
    			out.append("").append("|");
    		}
    		
    		// Closeness Centrality score
    		if (cc != null) {
    			double scoreCC = cc.getVertexScore(v);    			
    			v.setClosenessCentrality(scoreCC);
    			
    			out.append(new BigDecimal(scoreCC).setScale(0, BigDecimal.ROUND_HALF_UP)).append("|");
    		}
    		else {
    			out.append("").append("|");
    		}
    		
    		// Eigenvector Centrality score
    		if (ec != null) {
    			double scoreEC = ec.getVertexScore(v);    			
    			v.setEigenvectorCentrality(scoreEC);
    			
    			out.append(new BigDecimal(scoreEC).setScale(0, BigDecimal.ROUND_HALF_UP));
    		}
    		else {
    			out.append("");
    		}

    		System.out.println(out.append("\n"));
    	}   	
	}

	private void storeEdgeScoreToGraph() throws Exception {
		// Betweenness Centrality score
		if (bc != null) {
        	Collection<Edge> edges = graph.getEdges();
        	
        	System.out.println("----------------------------------------------");
        	System.out.println("Algorithm : Betweenness Centrality");
        	System.out.println("USERID1 -> USERID2 | BETWEENNESS");
        	System.out.println("----------------------------------------------");
        	
        	Iterator<Edge> it = edges.iterator();
        	while(it.hasNext()) { 
        		StringBuffer out = new StringBuffer();
        		
        		Edge e = (Edge)it.next();
        		double edgeScore = bc.getEdgeRankScore(e);
        		e.setBetweennessCentrality(edgeScore);
        		
        		out.append(e.getVertex1() + " -> " + e.getVertex2()).append("|");
        		out.append(new BigDecimal(edgeScore).setScale(0, BigDecimal.ROUND_HALF_UP));
        		
        		System.out.println(out.append("\n"));
         	}    		
    	}		
	}
	
	/**
	 * Stores the output into the database.
	 * 
	 */
	public void storeToDatabase() {
		
	}
	
}
