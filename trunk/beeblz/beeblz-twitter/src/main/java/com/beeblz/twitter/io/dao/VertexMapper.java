/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.sna.io.dao;

import java.util.List;

import com.beeblz.sna.io.GenericMapper;
import com.beeblz.sna.io.model.Vertex;

/**
 * A interface for vertex mapper.
 * 
 * @author Young-Gue Bae
 */
public interface VertexMapper extends GenericMapper {

    /**
     * Selects vertex list(vertices).
     *
     * @param websiteId the websiteId
     * @return List<Vertex> the vertex list
     */
	public List<Vertex> selectVertices(int websiteId);
	
	 /**
     * Gets the vertex id list by scoring condition.
     *
     * @param vertex the vertex
     * @return List<String> the vertex id list
     */
	public List<String> selectVertexIdsByScoringCondition(Vertex vertex);
	
    /**
     * Inserts a vertex.
     *
     * @param vertex the vertex
     */
	public void insertVertex(Vertex vertex);

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
	public void updateVertexScore(Vertex vertex);

	/**
     * Updates a vertex cluster.
     *
     * @param vertex the vertex cluster
     */
	public void updateVertexCluster(Vertex vertex);
	
    /**
     * Selects edge betweenness clusters.
     *
     * @param websiteId the websiteId
     * @return List<Integer> the clusters
     */
	public List<Integer> selectEdgeBetweennessClusters(int websiteId);
	
    /**
     * Selects voltage clusters.
     *
     * @param websiteId the websiteId
     * @return List<Integer> the clusters
     */
	public List<Integer> selectVoltageClusters(int websiteId);

    /**
     * Selects CNM(Clauset-Newman-Moore) clusters.
     *
     * @param websiteId the websiteId
     * @return List<Integer> the clusters
     */
	public List<Integer> selectCnmClusters(int websiteId);
	
	/**
     * Selects vertices in the specific edge betweenness cluster.
     *
     * @param vertex the vertex with the specific website id and cluster id.
     * @return List<Vertex> the vertices
     */
	public List<Vertex> selectVerticesInEdgeBetweennessCluster(Vertex vertex);
	
	/**
     * Selects vertices in the specific voltage cluster.
     *
     * @param vertex the vertex with the specific website id and cluster id.
     * @return List<Vertex> the vertices
     */
	public List<Vertex> selectVerticesInVoltageCluster(Vertex vertex);

	/**
     * Selects vertices in the specific CNM(Clauset-Newman-Moore) cluster.
     *
     * @param vertex the vertex with the specific website id and cluster id.
     * @return List<Vertex> the vertices
     */
	public List<Vertex> selectVerticesInCnmCluster(Vertex vertex);
	
}
