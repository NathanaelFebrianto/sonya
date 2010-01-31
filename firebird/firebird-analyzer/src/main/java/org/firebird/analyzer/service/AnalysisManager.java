/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.service;

import org.firebird.analyzer.graph.scoring.ScoringConfig;
import org.firebird.common.service.GenericManager;

/**
 * A interface for analysis manager.
 * 
 * @author Young-Gue Bae
 */
public interface AnalysisManager extends GenericManager {

	/**
	 * Evaluates the scores of the graph.
	 * such as HITS, Betweenness Centrality, Closeness Centrality, Degree etc.
	 * 
	 * @param config the scoring config
	 * @param websiteId1 the website id1
	 * @param websiteId2 the website id2
	 */
	public void scoringGraph(ScoringConfig config, int websiteId1, int websiteId2) throws Exception;
}
