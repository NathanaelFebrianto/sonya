/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.collector.service.impl;

import java.util.List;

import org.firebird.collector.CollectorConfig;
import org.firebird.collector.service.CollectManager;
import org.firebird.collector.twitter.TwitterDataCollector;
import org.firebird.common.service.GenericManagerImpl;
import org.firebird.io.model.Vertex;
import org.firebird.io.service.VertexManager;
import org.firebird.io.service.impl.VertexManagerImpl;

/**
 * A implementation for collect manager.
 * 
 * @author Young-Gue Bae
 */
public class CollectManagerImpl extends GenericManagerImpl implements CollectManager {

	/**
     * Constructor.
     *
     */
    public CollectManagerImpl() {
    }
	
	/**
	 * Collects the twitter data.
	 * 
	 * @param config the collect config
	 * @param screenName the user's screen name
	 */
	public void collectTwitter(CollectorConfig config, String screenName) throws Exception {
		TwitterDataCollector collector = new TwitterDataCollector();
		collector.setConfig(config);
		collector.collect(screenName);
	}
	
	/**
	 * Collects the twitter's blog entries data.
	 * 
	 * @param condition the condition
	 */
	public void collectTwitterBlogEntries(Vertex condition) throws Exception {
    	CollectorConfig config = new CollectorConfig();
    	config.setDBStorage(true);
    	config.setCollectUserBlogEntry(true);
    	
    	TwitterDataCollector collector = new TwitterDataCollector(config);
    	VertexManager vertexManager = new VertexManagerImpl();
    	List<String> screenNames = vertexManager.getVertexIdsByScoringCondition(condition); 
       	collector.collectBlogEntries(screenNames);
	}
	
}
