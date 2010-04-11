/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.collector.twitter;

import java.util.Date;
import java.util.List;

import org.firebird.collector.CollectorConfig;
import org.firebird.collector.util.CollectJobLogger;
import org.firebird.io.model.Vertex;
import org.firebird.io.service.VertexManager;
import org.firebird.io.service.impl.VertexManagerImpl;

/**
 * This class is a twitter blog collector job.
 * 
 * @author Young-Gue Bae
 */
public class TwitterBlogDataCollectJob {

	/** logger */
	private static CollectJobLogger logger = CollectJobLogger.getLogger(TwitterBlogDataCollectJob.class);
	
	/**
	 * Constructor.
	 */
	public TwitterBlogDataCollectJob() { }
	
	/**
	 * Collects the user data from twitter.
	 * 
	 * @param config the configuration
	 * @param screenName the screen name
	 * @throws Exception
	 */
	public void collect(CollectorConfig config, List<String> screenNames) throws Exception {
    	TwitterDataCollector collector = new TwitterDataCollector(config);
    	collector.collectBlogEntries(screenNames);		
	}
	
	public static void main(String[] args) {
    	try {    		
    		// start time
    		Date startTime = new Date();
    		logger.info("\n\n******************************************");
    		logger.info("Start Collecting for twitter blog data : " + startTime);
    		
    		TwitterBlogDataCollectJob job = new TwitterBlogDataCollectJob();
    		
        	CollectorConfig config = new CollectorConfig();
        	config.setDBStorage(true);
        	config.setCollectUserBlogEntry(true);
        	
         	VertexManager vertexManager = new VertexManagerImpl();
        	
         	Vertex condition = new Vertex();
        	condition.setWebsiteId(1);
        	//condition.setPageRank(0);
        	condition.setAuthority(0.01);        	
        	//condition.setHub(0);
        	//condition.setBetweennessCentrality(0);
        	//condition.setClosenessCentrality(0);
        	//condition.setEigenvectorCentrality(0);
        	
        	List<String> screenNames = vertexManager.getVertexIdsByScoringCondition(condition); 
        	
        	job.collect(config, screenNames);

        	// end time
    		Date endTime = new Date();
    		logger.info("Finish Collecting : " + endTime);
    		logger.jobSummary("Collecting for twitter blog data", startTime, endTime);
    		
     	} catch (Exception e) {
        	e.printStackTrace();
        	logger.error(e.getMessage(), e);
        }  
	}
	
}
