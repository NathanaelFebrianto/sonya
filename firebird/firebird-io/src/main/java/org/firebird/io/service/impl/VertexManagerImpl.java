/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.io.service.impl;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.firebird.common.service.GenericManagerImpl;
import org.firebird.io.dao.VertexMapper;
import org.firebird.io.model.Vertex;
import org.firebird.io.service.VertexManager;

/**
 * A implementation for vertex manager.
 * 
 * @author Young-Gue Bae
 */
public class VertexManagerImpl extends GenericManagerImpl implements VertexManager {

	//private VertexMapper mapper;

	/**
     * Constructor.
     *
     */
    public VertexManagerImpl() {
    }
	
	/**
     * Constructor.
     *
     * @param mapper the vertex mapper
     */
    public VertexManagerImpl(VertexMapper mapper) {
        //this.mapper = mapper;
    }

    /**
     * Gets the vertex list.
     *
     * @param websiteId the websiteId
     * @return List<Vertex> the vertex list
     */
	public List<Vertex> getVertices(int websiteId) {
		// mapper.selectVertices(websiteId);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		List<Vertex> vertices = mapper.selectVertices(websiteId);
    		return vertices;
    	} finally {
    		session.close();
    	}
	}
	
	 /**
     * Gets the vertex id list by scoring condition.
     *
     * @param vertex the vertex
     * @return List<String> the vertex id list
     */
	public List<String> getVertexIdsByScoringCondition(Vertex vertex) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		List<String> vertices = mapper.selectVertexIdsByScoringCondition(vertex);
    		return vertices;
    	} finally {
    		session.close();
    	}
	}
    
    /**
     * Adds a vertex.
     *
     * @param vertex the vertex
     */
	public void addVertex(Vertex vertex) {
		//mapper.insertVertex(vertex);

		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		mapper.insertVertex(vertex);
    		session.commit();
    	} finally {
    		session.close();
    	}
	}
	
    /**
     * Deletes a vertex.
     *
     * @param vertex the vertex
     */
	public void deleteVertex(Vertex vertex) {
		//mapper.deleteVertex(vertex);
		
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		mapper.deleteVertex(vertex);
    		session.commit();
    	} finally {
    		session.close();
    	}
	}
	
	/**
     * Updates a vertex score.
     *
     * @param vertex the vertex score
     */
	public void setVertexScore(Vertex vertex) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		mapper.updateVertexScore(vertex);
    		session.commit();
    	} finally {
    		session.close();
    	}		
	}
	
	/**
     * Updates a vertex cluster.
     *
     * @param vertex the vertex cluster
     */
	public void setVertexCluster(Vertex vertex) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		mapper.updateVertexCluster(vertex);
    		session.commit();
    	} finally {
    		session.close();
    	}		
	}
	
    /**
     * Gets the edge betweenness clusters.
     *
      * @param websiteId the website id
     * @return List<Integer> the clusters
     */
	public List<Integer> getEdgeBetweennessClusters(int websiteId) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		List<Integer> clusters = mapper.selectEdgeBetweennessClusters(websiteId);
    		return clusters;
    	} finally {
    		session.close();
    	}
	}
	
	/**
     * Gets the voltage clusters.
     *
      * @param websiteId the website id
     * @return List<Integer> the clusters
     */
	public List<Integer> getVoltageClusters(int websiteId) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		List<Integer> clusters = mapper.selectVoltageClusters(websiteId);
    		return clusters;
    	} finally {
    		session.close();
    	}
	}

	/**
     * Gets the CNM(Clauset-Newman-Moore) clusters.
     *
      * @param websiteId the website id
     * @return List<Integer> the clusters
     */
	public List<Integer> getCnmClusters(int websiteId) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		List<Integer> clusters = mapper.selectCnmClusters(websiteId);
    		return clusters;
    	} finally {
    		session.close();
    	}		
	}
	
	/**
     * Gets the vertices in the specific edge betweenness cluster.
     *
      * @param websiteId the website id
     * @param cluster the cluster id
     * @return List<Vertex> the vertices
     */
	public List<Vertex> getVerticesInEdgeBetweennessCluster(int websiteId, int cluster) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		
    		Vertex vertex = new Vertex();
    		vertex.setWebsiteId(websiteId);
    		vertex.setEdgeBetweennessCluster(cluster);    		
    		List<Vertex> vertices = mapper.selectVerticesInEdgeBetweennessCluster(vertex);
    		return vertices;
    	} finally {
    		session.close();
    	}		
	}
	
	/**
     * Gets the vertices in the specific voltage cluster.
     *
      * @param websiteId the website id
     * @param cluster the cluster id
     * @return List<Vertex> the vertices
     */
	public List<Vertex> getVerticesInVoltageCluster(int websiteId, int cluster) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		
    		Vertex vertex = new Vertex();
    		vertex.setWebsiteId(websiteId);
    		vertex.setVoltageCluster(cluster);    		
    		List<Vertex> vertices = mapper.selectVerticesInVoltageCluster(vertex);
    		return vertices;
    	} finally {
    		session.close();
    	}		
	}
	
	/**
     * Gets the vertices in the specific CNM(Clauset-Newman-Moore) cluster.
     *
      * @param websiteId the website id
     * @param cluster the cluster id
     * @return List<Vertex> the vertices
     */
	public List<Vertex> getVerticesInCnmCluster(int websiteId, int cluster) {
		SqlSession session = sqlSessionFactory.openSession();
    	try {
    		VertexMapper mapper = session.getMapper(VertexMapper.class);
    		
    		Vertex vertex = new Vertex();
    		vertex.setWebsiteId(websiteId);
    		vertex.setCnmCluster(cluster);    		
    		List<Vertex> vertices = mapper.selectVerticesInCnmCluster(vertex);
    		return vertices;
    	} finally {
    		session.close();
    	}				
	}
	
	/**
     * Gets the edge betweenness cluster set.
     *
     * @param websiteId the website id
     * @return Set<Set<String>> the cluster set with vertex id
     */
	public Set<Set<String>> getEdgeBetweennessClusterSet(int websiteId) {
		Set<Set<String>> clusterSet = new LinkedHashSet<Set<String>>();
		List<Integer> clusters = this.getEdgeBetweennessClusters(websiteId);
		
		for (Integer cluster : clusters) {
			if (cluster != null) {
				Set<String> verticesSet = new LinkedHashSet<String>();			
				List<Vertex> vertices = this.getVerticesInEdgeBetweennessCluster(websiteId, cluster);			
				for (Vertex vertex : vertices) {
					verticesSet.add(vertex.getId());
				}
				clusterSet.add(verticesSet);
			}
		}		
		return clusterSet;
	}
	
	/**
     * Gets the voltage cluster set.
     *
     * @param websiteId the website id
     * @return Set<Set<String>> the cluster set with vertex id
     */
	public Set<Set<String>> getVoltageClusterSet(int websiteId) {
		Set<Set<String>> clusterSet = new LinkedHashSet<Set<String>>();
		List<Integer> clusters = this.getVoltageClusters(websiteId);
		
		for (Integer cluster : clusters) {
			if (cluster != null) {
				Set<String> verticesSet = new LinkedHashSet<String>();			
				List<Vertex> vertices = this.getVerticesInVoltageCluster(websiteId, cluster);			
				for (Vertex vertex : vertices) {
					verticesSet.add(vertex.getId());
				}
				clusterSet.add(verticesSet);
			}
		}		
		return clusterSet;
	}
	
	/**
     * Gets the CNM(Clauset-Newman-Moore) cluster set.
     *
     * @param websiteId the website id
     * @return Set<Set<String>> the cluster set with vertex id
     */
	public Set<Set<String>> getCnmClusterSet(int websiteId) {
		Set<Set<String>> clusterSet = new LinkedHashSet<Set<String>>();
		List<Integer> clusters = this.getCnmClusters(websiteId);
		
		for (Integer cluster : clusters) {
			if (cluster != null) {
				Set<String> verticesSet = new LinkedHashSet<String>();			
				List<Vertex> vertices = this.getVerticesInCnmCluster(websiteId, cluster);			
				for (Vertex vertex : vertices) {
					verticesSet.add(vertex.getId());
				}
				clusterSet.add(verticesSet);
			}
		}		
		return clusterSet;		
	}
	
	/**
     * Gets the edge betweenness cluster map.
     *
     * @param websiteId the website id
     * @return Map<Intger, Set<String>> the cluster set with vertices id
     */
	public Map<Integer, Set<String>> getEdgeBetweennessClusterMap(int websiteId) {
		Map<Integer, Set<String>> clusterSet = new HashMap<Integer, Set<String>>();
		List<Integer> clusters = this.getEdgeBetweennessClusters(websiteId);
		
		for (Integer cluster : clusters) {
			if (cluster != null) {
				Set<String> verticesSet = new LinkedHashSet<String>();			
				List<Vertex> vertices = this.getVerticesInEdgeBetweennessCluster(websiteId, cluster);			
				for (Vertex vertex : vertices) {
					verticesSet.add(vertex.getId());
				}
				clusterSet.put(cluster, verticesSet);
			}
		}		
		return clusterSet;
	}
	
	/**
     * Gets the voltage cluster map.
     *
     * @param websiteId the website id
     * @return Map<Intger, Set<String>> the cluster set with vertices id
     */
	public Map<Integer, Set<String>> getVoltageClusterMap(int websiteId) {
		Map<Integer, Set<String>> clusterSet = new HashMap<Integer, Set<String>>();
		List<Integer> clusters = this.getVoltageClusters(websiteId);
		
		for (Integer cluster : clusters) {
			if (cluster != null) {
				Set<String> verticesSet = new LinkedHashSet<String>();			
				List<Vertex> vertices = this.getVerticesInVoltageCluster(websiteId, cluster);			
				for (Vertex vertex : vertices) {
					verticesSet.add(vertex.getId());
				}
				clusterSet.put(cluster, verticesSet);
			}
		}		
		return clusterSet;
	}

	/**
     * Gets the CNM(Clauset-Newman-Moore) cluster map.
     *
     * @param websiteId the website id
     * @return Map<Intger, Set<String>> the cluster set with vertices id
     */
	public Map<Integer, Set<String>> getCnmClusterMap(int websiteId) {
		Map<Integer, Set<String>> clusterSet = new HashMap<Integer, Set<String>>();
		List<Integer> clusters = this.getCnmClusters(websiteId);
		
		for (Integer cluster : clusters) {
			if (cluster != null) {
				Set<String> verticesSet = new LinkedHashSet<String>();			
				List<Vertex> vertices = this.getVerticesInCnmCluster(websiteId, cluster);			
				for (Vertex vertex : vertices) {
					verticesSet.add(vertex.getId());
				}
				clusterSet.put(cluster, verticesSet);
			}
		}		
		return clusterSet;
	}
	
}
