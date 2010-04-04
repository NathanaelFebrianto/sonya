/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.graph.clustering;

import java.io.Serializable;

/**
 * Configuration for clustering.
 * 
 * @author Young-Gue Bae
 */
public class ClusteringConfig implements Serializable {

	private static final long serialVersionUID = -943446734475441936L;
	private boolean enbleEdgeBetweennessCluster = false;
	private boolean enbleVoltageCluster = false;
	
	private int numEdgesToRemove;	// for EdgeBetweennessCluster
	private int numCandidates;	// for VoltageCluster
	private int numClusters;	// for VoltageCluster
	
	public boolean isEnbleEdgeBetweennessCluster() {
		return enbleEdgeBetweennessCluster;
	}
	public void setEnbleEdgeBetweennessCluster(boolean enbleEdgeBetweennessCluster) {
		this.enbleEdgeBetweennessCluster = enbleEdgeBetweennessCluster;
	}
	public boolean isEnbleVoltageCluster() {
		return enbleVoltageCluster;
	}
	public void setEnbleVoltageCluster(boolean enbleVoltageCluster) {
		this.enbleVoltageCluster = enbleVoltageCluster;
	}
	public int getNumEdgesToRemove() {
		return this.numEdgesToRemove;
	}
	public void setNumEdgesToRemove(int numEdgesToRemove) {
		this.numEdgesToRemove = numEdgesToRemove;
	}
	public int getNumCandidates() {
		return this.numCandidates;
	}
	public void setNumCandidates(int numCandidates) {
		this.numCandidates = numCandidates;
	}
	public int getNumClusters() {
		return this.numClusters;
	}
	public void setNumClusters(int numClusters) {
		this.numClusters = numClusters;
	}
}
