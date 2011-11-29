package com.nhn.socialanalytics.appleappstore.collect;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nhn.socialanalytics.appleappstore.model.Review;
import com.nhn.socialanalytics.common.Config;
import com.nhn.socialanalytics.common.JobLogger;

public class AppStoreDataCollectorJob implements Job {
	// logger
	private static JobLogger logger = JobLogger.getLogger(AppStoreDataCollectorJob.class, "appstore-collect.log");
	
	public AppStoreDataCollectorJob() { }
	
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

			File spamFilterFile = new File(Config.getProperty("COLLECT_SPAM_FILTER_FILE"));
			AppStoreDataCollector collector = new AppStoreDataCollector(spamFilterFile);	
			
			Set<String> appStores = new HashSet<String>();
			appStores.add(AppStores.getAppStore("Korea"));
			
			/////////////////////////////			
			String objectId1 = "naverline";
			String appId1 = "443904275";
			
			List<Review> reviews1 = collector.getReviews(appStores, appId1, 5);
			
			try {
				String dataDir = Config.getProperty("APPSTORE_SOURCE_DATA_DIR");
				String indexDir = Config.getProperty("APPSTORE_INDEX_DIR");
				String liwcCatFile = Config.getProperty("LIWC_CAT_FILE");
				
				collector.writeOutput(dataDir, indexDir, liwcCatFile, objectId1, reviews1, startTime);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}

			/////////////////////////////
			String objectId2 = "naverapp";
			String appId2 = "393499958";
			
			List<Review> reviews2 = collector.getReviews(appStores, appId2, 5);
			
			try {
				String dataDir = Config.getProperty("APPSTORE_SOURCE_DATA_DIR");
				String indexDir = Config.getProperty("APPSTORE_INDEX_DIR");
				String liwcCatFile = Config.getProperty("LIWC_CAT_FILE");
				
				collector.writeOutput(dataDir, indexDir, liwcCatFile, objectId2, reviews2, startTime);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}
			
			/////////////////////////////
			String objectId3 = "kakaotalk";
			String appId3 = "362057947";
			
			List<Review> reviews3 = collector.getReviews(appStores, appId3, 5);
			
			try {
				String dataDir = Config.getProperty("APPSTORE_SOURCE_DATA_DIR");
				String indexDir = Config.getProperty("APPSTORE_INDEX_DIR");
				String liwcCatFile = Config.getProperty("LIWC_CAT_FILE");
				
				collector.writeOutput(dataDir, indexDir, liwcCatFile, objectId3, reviews3, startTime);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}
			/////////////////////////////
			
			// end time
			Date endTime = new Date();
			System.out.println("Quartz says: " + jobName + " finished at "	+ endTime);
			System.out.println("Collecting for twitter data from : " + startTime + " to: " + endTime);
			
			logger.info("Quartz says: " + jobName + " finished at "	+ endTime);
			logger.jobSummary("Collecting for twitter data", startTime, endTime);
			
	 	} catch (Exception e) {
	    	e.printStackTrace();
	    } 
	}	
}
