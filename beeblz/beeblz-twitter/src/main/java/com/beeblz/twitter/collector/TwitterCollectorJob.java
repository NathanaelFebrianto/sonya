package com.beeblz.twitter.collector;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.beeblz.common.JobLogger;

public class TwitterCollectorJob implements Job {
	
	// logger
	private static JobLogger logger = JobLogger.getLogger(TwitterCollectorJob.class, "twitter-collect.log");
	
	public TwitterCollectorJob() { }
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			// get job data map
			//JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
	        
	        //logger.info("@PARAM[???] == " + something);
			
			// start time
    		Date startTime = new Date();
			
			// This job simply prints out its job name and the
			// date and time that it is running
			String jobName = context.getJobDetail().getFullName();
			logger.info("Quartz says: " + jobName + " executing at " + startTime);
	    	
			String[] targetUsers = {
					"BarackObama",
					"realDonaldTrump",
					"BillGates",
					"Oprah",
					"kingsthings",
					"ladygaga",
					"britneyspears",
					"aplusk",		//
					"DalaiLama",
					"TechCrunch",
					"mashable",
					"cnnbrk",		//
					"BBCBreaking"	//
				};
			
			TwitterCollector collector = new TwitterCollector();
			collector.collectTweets(targetUsers);
	    	
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
