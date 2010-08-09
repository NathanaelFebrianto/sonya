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
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * This class is a scheduler for colleting tweet data.
 * 
 * @author Young-Gue Bae
 */
public class TwitterBlogDataCollectJob implements Job {

	/** logger */
	private static CollectJobLogger logger = CollectJobLogger.getLogger(TwitterBlogDataCollectJob.class);

	/**
	 * Constructor.
	 */
	public TwitterBlogDataCollectJob() { }

    /**
     * <p>
     * Called by the <code>{@link org.quartz.Scheduler}</code> when a
     * <code>{@link org.quartz.Trigger}</code> fires that is associated with
     * the <code>Job</code>.
     * </p>
     * 
     * @throws JobExecutionException
     *             if there is an exception while executing the job.
     */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			// start time
    		Date startTime = new Date();
			
			// This job simply prints out its job name and the
			// date and time that it is running
			String jobName = context.getJobDetail().getFullName();
			logger.info("Quartz says: " + jobName + " executing at "	+ startTime);
			
	    	CollectorConfig config = new CollectorConfig();
	    	config.setDBStorage(true);
	    	config.setCollectUserBlogEntry(true);
	    	
	     	VertexManager vertexManager = new VertexManagerImpl();
	    	
	     	Vertex condition = new Vertex();
	    	condition.setWebsiteId(1);
	    	condition.setPageRank(0.001);
	    	condition.setAuthority(0.02);        	
	    	//condition.setHub(0);
	    	//condition.setBetweennessCentrality(0);
	    	//condition.setClosenessCentrality(0);
	    	//condition.setEigenvectorCentrality(0);
	    	
	    	List<String> screenNames = vertexManager.getVertexIdsByScoringCondition(condition); 
	
			TwitterDataCollector collector = new TwitterDataCollector(config);
	    	collector.collectBlogEntries(screenNames);
	    	// end time
			Date endTime = new Date();
			logger.info("Quartz says: " + jobName + " finished at "	+ endTime);
			logger.jobSummary("Collecting for tweet data", startTime, endTime);			
	 	} catch (Exception e) {
	    	e.printStackTrace();
	    	logger.error(e.getMessage(), e);
	    } 
	}
	
}
