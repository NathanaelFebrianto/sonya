/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.graph;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import prefuse.data.Graph;
import prefuse.data.Table;
import prefuse.visual.VisualItem;
import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;


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
    
    /**
     * Converts the prefuse graph data into the jung graph data.
     * 
     * @param g the prefuse graph data
     * @return graph the jung graph data
     */
    public edu.uci.ics.jung.graph.Graph<Long,Long> convertJungGraph(prefuse.data.Graph g) {
    	
    	edu.uci.ics.jung.graph.Graph<Long,Long> graph = new UndirectedSparseGraph<Long,Long>();
		
    	if (g != null) {
			Iterator<?> nodeIter = g.nodes();
			while (nodeIter.hasNext()) {
				VisualItem node = (VisualItem)nodeIter.next();
				Long nodeId = node.getLong("id");
				graph.addVertex(nodeId);
			}
			
			Iterator<?> edgeIter = g.edges();
			while (edgeIter.hasNext()) {
				VisualItem edge = (VisualItem)edgeIter.next();
				Long node1 = edge.getLong("node1");
				Long node2 = edge.getLong("node1");
				Long edgeId = Long.valueOf(node1.toString() + node2.toString());
				graph.addEdge(edgeId, node1, node2, EdgeType.UNDIRECTED);
			}
		}    	
    	return graph;
    }
    
    /**
     * Clusters the graph by edge betweenness clusterer.
     * 
     * @param g the prefuse graph data
     * @return Set<Set<Long>> the clustered node set
     */
    public Set<Set<Long>> clusterGraph(prefuse.data.Graph g) {
    	edu.uci.ics.jung.graph.Graph<Long,Long> graph = convertJungGraph(g);
    	
    	EdgeBetweennessClusterer<Long,Long> clusterer = new EdgeBetweennessClusterer<Long,Long>(10);
		Set<Set<Long>> clusterSet = clusterer.transform(graph);
		List<Long> edges = clusterer.getEdgesRemoved();
		
    	return clusterSet;    	
    }
    	
}
