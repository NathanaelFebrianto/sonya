/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.collector.service.impl;

import org.firebird.collector.CollectorConfig;
import org.firebird.collector.service.CollectManager;
import org.firebird.collector.twitter.TwitterDataCollector;
import org.firebird.common.service.GenericManagerImpl;

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
}
