/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.firebird.common.service.GenericManager;
import org.firebird.io.model.Vertex;

/**
 * A interface for vertex manager.
 * 
 * @author Young-Gue Bae
 */
public interface VertexManager extends GenericManager {

    /**
     * Gets the vertex list.
     *
     * @param websiteId the websiteId
     * @return List<Vertex> the vertex list
     */
	public List<Vertex> getVertices(int websiteId);
	
	 /**
     * Gets the vertex id list by scoring condition.
     *
     * @param vertex the vertex
     * @return List<String> the vertex id list
     */
	public List<String> getVertexIdsByScoringCondition(Vertex vertex);
	
    /**
     * Adds a vertex.
     *
     * @param vertex the vertex
     */
	public void addVertex(Vertex vertex);

    /**
     * Deletes a vertex.
     *
     * @param vertex the vertex
     */
	public void deleteVertex(Vertex vertex);
	
	/**
     * Updates a vertex score.
     *
     * @param vertex the vertex score
     */
	public void setVertexScore(Vertex vertex);
	
	/**
     * Updates a vertex cluster.
     *
     * @param vertex the vertex cluster
     */
	public void setVertexCluster(Vertex vertex);
	
    /**
     * Gets the edge betweenness clusters.
     *
      * @param websiteId the website id
     * @return List<Integer> the clusters
     */
	public List<Integer> getEdgeBetweennessClusters(int websiteId);
	
	/**
     * Gets the voltage clusters.
     *
      * @param websiteId the website id
     * @return List<Integer> the clusters
     */
	public List<Integer> getVoltageClusters(int websiteId);
	
	/**
     * Gets the vertices in the specific edge betweenness cluster.
     *
      * @param websiteId the website id
     * @param cluster the cluster id
     * @return List<Vertex> the vertices
     */
	public List<Vertex> getVerticesInEdgeBetweennessCluster(int websiteId, int cluster);
	
	/**
     * Gets the vertices in the specific voltage cluster.
     *
      * @param websiteId the website id
     * @param cluster the cluster id
     * @return List<Vertex> the vertices
     */
	public List<Vertex> getVerticesInVoltageCluster(int websiteId, int cluster);
	
	/**
     * Gets the edge betweenness cluster set.
     *
     * @param websiteId the website id
     * @return Set<Set<String>> the cluster set with vertex id
     */
	public Set<Set<String>> getEdgeBetweennessClusterSet(int websiteId);

	/**
     * Gets the voltage cluster set.
     *
     * @param websiteId the website id
     * @return Set<Set<String>> the cluster set with vertex id
     */
	public Set<Set<String>> getVoltageClusterSet(int websiteId);
	
	/**
     * Gets the edge betweenness cluster map.
     *
     * @param websiteId the website id
     * @return Map<Intger, Set<String>> the cluster set with vertices id
     */
	public Map<Integer, Set<String>> getEdgeBetweennessClusterMap(int websiteId);
	
	/**
     * Gets the voltage cluster map.
     *
     * @param websiteId the website id
     * @return Map<Intger, Set<String>> the cluster set with vertices id
     */
	public Map<Integer, Set<String>> getVoltageClusterMap(int websiteId);
	
}
