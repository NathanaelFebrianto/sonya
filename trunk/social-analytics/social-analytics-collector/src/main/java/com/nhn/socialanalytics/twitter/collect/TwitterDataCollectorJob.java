package com.nhn.socialanalytics.twitter.collect;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nhn.socialanalytics.common.Config;
import com.nhn.socialanalytics.common.JobLogger;
import com.nhn.socialanalytics.common.util.DateUtil;

public class TwitterDataCollectorJob implements Job {
	// logger
	private static JobLogger logger = JobLogger.getLogger(TwitterDataCollectorJob.class, "twitter-collect.log");
	
	public TwitterDataCollectorJob() { }
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			// get job data map
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();	
			//String param = jobDataMap.getString("key");			
			//logger.info("@PARAM[key] == " + param);
			
			// start time
    		Date startTime = new Date();
			
			// This job simply prints out its job name and the
			// date and time that it is running
			String jobName = context.getJobDetail().getFullName();
			System.out.println("Quartz says: " + jobName + " executing at " + startTime);
			logger.info("Quartz says: " + jobName + " executing at " + startTime);			

			TwitterDataCollector collector = new TwitterDataCollector();
			
			/////////////////////////////			
			String objectId1 = "naverline";			
			Map<String, Integer> queryMap1 = new HashMap<String, Integer>();
			queryMap1.put("네이버라인 OR 네이버LINE", 5);
			
			Date sinceDate1 = DateUtil.addDay(new Date(), -30);
			
			List<twitter4j.Tweet> tweets1 = collector.searchTweets(queryMap1, sinceDate1, null);
			
			try {
				String dataDir = Config.getProperty("TWITTER_SOURCE_DATA_DIR");
				String indexDir = Config.getProperty("TWITTER_INDEX_DIR");
				String liwcCatFile = Config.getProperty("LIWC_CAT_FILE");
				
				collector.writeOutput(dataDir, indexDir, liwcCatFile, objectId1, tweets1, startTime, 1);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}

			/////////////////////////////
			String objectId2 = "fta";			
			Map<String, Integer> queryMap2 = new HashMap<String, Integer>();
			queryMap2.put("한미FTA OR ISD", 10);
			
			Date sinceDate2 = DateUtil.addDay(new Date(), -1);
			
			List<twitter4j.Tweet> tweets2 = collector.searchTweets(queryMap2, sinceDate2, null);
			
			try {
				String dataDir = Config.getProperty("TWITTER_SOURCE_DATA_DIR");
				String indexDir = Config.getProperty("TWITTER_INDEX_DIR");
				String liwcCatFile = Config.getProperty("LIWC_CAT_FILE");
				
				collector.writeOutput(dataDir, indexDir, liwcCatFile, objectId2, tweets2, startTime, 1);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}
			/////////////////////////////
			
			// end time
			Date endTime = new Date();
			System.out.println("Quartz says: " + jobName + " finished at "	+ endTime);
			System.out.println("Collecting for Twitter data from : " + startTime + " to: " + endTime);
			
			logger.info("Quartz says: " + jobName + " finished at "	+ endTime);
			logger.jobSummary("Collecting for Twitter data", startTime, endTime);
			
	 	} catch (Exception e) {
	    	e.printStackTrace();
	    } 
	}	
}
