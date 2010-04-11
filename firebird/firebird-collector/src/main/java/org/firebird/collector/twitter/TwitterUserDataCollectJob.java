/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.collector.twitter;

import java.util.Date;

import org.firebird.collector.CollectorConfig;
import org.firebird.collector.util.CollectJobLogger;

/**
 * This class is a twitter user data collector job.
 * 
 * @author Young-Gue Bae
 */
public class TwitterUserDataCollectJob {
	/** logger */
	private static CollectJobLogger logger = CollectJobLogger.getLogger(TwitterUserDataCollectJob.class);
	
	/**
	 * Constructor.
	 */
	public TwitterUserDataCollectJob() { }
	
	/**
	 * Collects the user data from twitter.
	 * 
	 * @param config the configuration
	 * @param screenName the screen name
	 * @throws Exception
	 */
	public void collect(CollectorConfig config, String screenName) throws Exception {
    	TwitterDataCollector collector = new TwitterDataCollector(config);
    	collector.collect(screenName);		
	}
	
	public static void main(String[] args) {
    	try {    		
    		// start time
    		Date startTime = new Date();
    		logger.info("\n\n******************************************");
    		logger.info("Start Collecting for twitter user data : " + startTime);
    		
    		TwitterUserDataCollectJob job = new TwitterUserDataCollectJob();
    		
    		CollectorConfig config = new CollectorConfig();
        	config.setDBStorage(true);
        	config.setCollectFriend(true);
        	config.setCollectFollower(true);
        	config.setCollectUserBlogEntry(false);
        	config.setLevelLimit(2);
        	config.setDegreeLimit(50);
        	config.setPeopleLimit(2000);
        	
        	job.collect(config, "sorenG");
    		
        	// end time
    		Date endTime = new Date();
    		logger.info("Finish Collecting : " + endTime);
    		logger.jobSummary("Collecting for twitter user data", startTime, endTime);
    		
     	} catch (Exception e) {
        	e.printStackTrace();
        	logger.error(e.getMessage(), e);
        }  
	}
	
}
