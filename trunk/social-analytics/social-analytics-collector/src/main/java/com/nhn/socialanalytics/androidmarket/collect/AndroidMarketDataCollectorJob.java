package com.nhn.socialanalytics.androidmarket.collect;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gc.android.market.api.model.Market.Comment;
import com.nhn.socialanalytics.common.Config;
import com.nhn.socialanalytics.common.JobLogger;

public class AndroidMarketDataCollectorJob implements Job {
	// logger
	private static JobLogger logger = JobLogger.getLogger(AndroidMarketDataCollectorJob.class, "androidmarket-collect.log");
	
	public AndroidMarketDataCollectorJob() { }
	
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

			String loginAccount = "xxx@gmail.com";
			String loginPasswd = "xxx";
			AndroidMarketDataCollector collector = new AndroidMarketDataCollector(loginAccount, loginPasswd);	
			
			Set<Locale> locales = new HashSet<Locale>();
			locales.add(Locale.KOREA);
			//locales.add(Locale.ENGLISH);
			
			/////////////////////////////
			String objectId1 = "naverline";
			String appId1 = "jp.naver.line.android";
			
			List<Comment> comments1 = collector.getAppComments(locales, appId1, 5);
			
			try {
				String dataDir = Config.getProperty("ANDROIDMARKET_SOURCE_DATA_DIR");
				String indexDir = Config.getProperty("ANDROIDMARKET_INDEX_DIR");
				String liwcCatFile = Config.getProperty("LIWC_CAT_FILE");
				
				collector.writeOutput(dataDir, indexDir, liwcCatFile, objectId1, comments1, startTime);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}

			/////////////////////////////
			String objectId2 = "naverapp";
			String appId2 = "com.nhn.android.search";
			
			List<Comment> comments2 = collector.getAppComments(locales, appId2, 5);
			
			try {
				String dataDir = Config.getProperty("ANDROIDMARKET_SOURCE_DATA_DIR");
				String indexDir = Config.getProperty("ANDROIDMARKET_INDEX_DIR");
				String liwcCatFile = Config.getProperty("LIWC_CAT_FILE");
				
				collector.writeOutput(dataDir, indexDir, liwcCatFile, objectId2, comments2, startTime);
				
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