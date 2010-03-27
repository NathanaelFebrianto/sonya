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
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.Graph;

/**
 * Scoring algorithm for a graph.
 * HITS, Betweenness Centrality, Closeness Centrality, Degree etc.
 * 
 * @author Young-Gue Bae
 */
public final class Scorer {
	
	/** Scoring Config */
	ScoringConfig config;
	
	/** PageRank algorithm */
	PageRank<Vertex, Edge> pr;	
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
		this(graph, new ScoringConfig());
	}
	
	/**
	 * Constructor.
	 * 
	 * @param graph the Graph<Vertex, Edge>
	 * @param config the scoring cofig
	 */
	public Scorer (Graph<Vertex, Edge> graph, ScoringConfig config) {
		this.graph = graph;
		this.config = config;
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
	 * Sets the scoring config.
	 * 
	 * @param config the scoring cofig
	 */
	public void setConfig(ScoringConfig config) {
		this.config = config;
	}
	
	/**
	 * Evaluates the graph by the enabled algorithms.
	 * 
	 * @return Graph<Vertex, Edge> the graph
	 * @exception
	 */
	public Graph<Vertex, Edge> evaluate() throws Exception {
		// PageRank
		if (config.isEnblePageRank()) {
			pr = new PageRank<Vertex, Edge>(graph, 0.15);
			pr.evaluate();
		}
		else {
			pr = null;
		}		
		// HITS
		if (config.isEnbleHITS()) {
			hits = new HITS<Vertex, Edge>(graph);
			hits.evaluate();
		}
		else {
			hits = null;
		}
		// Betweenness Centraility
		if (config.isEnableBetweennessCentrality()) {
			bc = new BetweennessCentrality<Vertex, Edge>(graph);
			bc.evaluate();		
			bc.step();
		}
		else {
			bc = null;
		}
		// Closeness Centraility
		if (config.isEnableClosenessCentrality()) {
			cc = new ClosenessCentrality<Vertex, Edge>(graph);
		}
		else {
			cc = null;
		}
		// Eigenvector Centrality
		if (config.isEnableEigenvectorCentrality()) {
			ec = new EigenvectorCentrality<Vertex, Edge>(graph);
			ec.evaluate();		
			ec.step();
		}
		else {
			ec = null;
		}
		
		storeVertexScoreToGraph();
		storeEdgeScoreToGraph();
		
		return graph;
	}
	
	private void storeVertexScoreToGraph() throws Exception {
    	System.out.println("---------------------------------------------------------------------------------------------");
    	System.out.println("USERID | IN | OUT | PAGERANK | AUTHORITY | HUB | BETWEENNESS | CLOSENESS | EIGENVECTOR");
    	System.out.println("---------------------------------------------------------------------------------------------");		
		
		Collection<Vertex> vertices = graph.getVertices();
    	
    	Iterator<Vertex> it = vertices.iterator();
    	while(it.hasNext()) { 
    		StringBuffer out = new StringBuffer();
    		Vertex v = (Vertex)it.next();
    		
    		// Degree score
    		v.setDegree(graph.degree(v));
    		v.setInDegree(graph.inDegree(v));
    		v.setOutDegree(graph.outDegree(v));
    		
    		out.append(v.getId()).append("|");
    		out.append(v.getInDegree()).append("|");
    		out.append(v.getOutDegree()).append("|");
    		
    		// PageRank score
    		if (config.isEnblePageRank()) {
    			double scorePR = pr.getVertexScore(v);
    			v.setPageRank(scorePR);
    			out.append(new BigDecimal(scorePR).setScale(4, BigDecimal.ROUND_HALF_UP)).append("|");
    		} 
    		else {
    			out.append("").append("|");
     		}
    		
    		// HITS score
    		if (config.isEnbleHITS()) {
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
    		if (config.isEnableBetweennessCentrality()) {
    			double scoreBC = bc.getVertexRankScore(v);    			
    			v.setBetweennessCentrality(scoreBC);
    			
    			out.append(new BigDecimal(scoreBC).setScale(0, BigDecimal.ROUND_HALF_UP)).append("|");
    		}
    		else {
    			out.append("").append("|");
    		}
    		
    		// Closeness Centrality score
    		if (config.isEnableClosenessCentrality()) {
    			double scoreCC = cc.getVertexScore(v);    			
    			v.setClosenessCentrality(scoreCC);

    			if (Double.isNaN(scoreCC)) {
    				out.append("NaN").append("|");
    			}
    			else {
    				out.append(new BigDecimal(scoreCC).setScale(0, BigDecimal.ROUND_HALF_UP)).append("|");
    			}
    		}
    		else {
    			out.append("").append("|");
    		}
    		
    		// Eigenvector Centrality score
    		if (config.isEnableEigenvectorCentrality()) {
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
		if (config.isEnableBetweennessCentrality()) {
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
