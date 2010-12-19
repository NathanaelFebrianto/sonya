/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.graph;

import prefuse.data.Graph;
import prefuse.data.Table;


/**
 * Graph data for prefuse.
 * 
 * @author YoungGue Bae
 */
public class GraphData {
	
	// graph
	private prefuse.data.Graph graph = null;
	
    // nodes    
    public static final Class[] NTYPES = 
        { long.class, String.class, String.class };
    
    public static final String[] NHEADERS =
        { "id", "label", "image" };    
    
    private Table nodes = null;
     
    // edges    
    public static final Class[] ETYPES =
        { long.class, long.class, double.class };
    
    public static final String[] EHEADERS =
        { "node1", "node2", "weight" };
    
    private Table edges = null;
    
    /**
     * Graph data.
     */
    public GraphData() {
    	super();
    	nodes = new Table();
    	nodes.addColumn(NHEADERS[0], NTYPES[0]);
    	nodes.addColumn(NHEADERS[1], NTYPES[1]);
    	nodes.addColumn(NHEADERS[2], NTYPES[2]);
    	
    	edges = new Table();
    	edges.addColumn(EHEADERS[0], ETYPES[0]);
    	edges.addColumn(EHEADERS[1], ETYPES[1]);
    	edges.addColumn(EHEADERS[2], ETYPES[2]);
    	
    	graph = new Graph(nodes, edges, false, 
                 NHEADERS[0], EHEADERS[0], EHEADERS[1]);
    }
    
    /**
     * Adds a node.
     * 
     * @param id
     * @param name
     * @param picture
     */
    public void addNode(long id, String name, String picture) {
    	int row = nodes.addRow();
    	nodes.set(row, NHEADERS[0], id);
    	nodes.set(row, NHEADERS[1], name);
    	nodes.set(row, NHEADERS[2], picture);
    }
    
    /**
     * Adds a edge.
     * 
     * @param node1
     * @param node2
     * @param weight
     */
    public void addEdge(long node1, long node2, double weight) {
    	int row = edges.addRow();
    	edges.set(row, EHEADERS[0], node1);
    	edges.set(row, EHEADERS[1], node2);
    	edges.set(row, EHEADERS[2], weight);    	
    }
    
    /**
     * Gets the graph.
     * 
     * @return prefuse.data.Graph
     */
    public prefuse.data.Graph getGraph() {
    	return this.graph;
    }
    	
}
