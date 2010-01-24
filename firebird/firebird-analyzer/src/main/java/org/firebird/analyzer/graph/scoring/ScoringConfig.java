/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.graph.scoring;

import java.io.Serializable;

/**
 * Configuration for scoring.
 * 
 * @author Young-Gue Bae
 */
public class ScoringConfig implements Serializable {

	private static final long serialVersionUID = -4632497956756091212L;
	private boolean enbleHITS = false;
	private boolean enableBetweennessCentrality = false;
	private boolean enableClosenessCentrality = false;
	private boolean enableEigenvectorCentrality = false;
	
	public boolean isEnbleHITS() {
		return enbleHITS;
	}
	public void setEnbleHITS(boolean enbleHITS) {
		this.enbleHITS = enbleHITS;
	}
	public boolean isEnableBetweennessCentrality() {
		return enableBetweennessCentrality;
	}
	public void setEnableBetweennessCentrality(boolean enableBetweennessCentrality) {
		this.enableBetweennessCentrality = enableBetweennessCentrality;
	}
	public boolean isEnableClosenessCentrality() {
		return enableClosenessCentrality;
	}
	public void setEnableClosenessCentrality(boolean enableClosenessCentrality) {
		this.enableClosenessCentrality = enableClosenessCentrality;
	}
	public boolean isEnableEigenvectorCentrality() {
		return enableEigenvectorCentrality;
	}
	public void setEnableEigenvectorCentrality(boolean enableEigenvectorCentrality) {
		this.enableEigenvectorCentrality = enableEigenvectorCentrality;
	}
	
}
