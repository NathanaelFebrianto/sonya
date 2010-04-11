/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.collector.service;

import org.firebird.collector.CollectorConfig;
import org.firebird.common.service.GenericManager;
import org.firebird.io.model.Vertex;

/**
 * A interface for collect manager.
 * 
 * @author Young-Gue Bae
 */
public interface CollectManager extends GenericManager {

	/**
	 * Collects the twitter data.
	 * 
	 * @param config the collect config
	 * @param screenName the user's screen name
	 */
	public void collectTwitter(CollectorConfig config, String screenName) throws Exception;

	/**
	 * Collects the twitter's blog entries data.
	 * 
	 * @param condition the condition
	 */
	public void collectTwitterBlogEntries(Vertex condition) throws Exception;
}
