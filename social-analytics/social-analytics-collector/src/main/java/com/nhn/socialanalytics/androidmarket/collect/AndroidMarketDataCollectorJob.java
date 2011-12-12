package com.nhn.socialanalytics.androidmarket.collect;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
			String loginAccount = jobDataMap.getString("login.account");
			String loginPasswd = jobDataMap.getString("login.passwd");	
			//logger.info("@PARAM[key] == " + param);
			
			// start time
    		Date startTime = new Date();
			
			// This job simply prints out its job name and the
			// date and time that it is running
			String jobName = context.getJobDetail().getFullName();
			System.out.println("Quartz says: " + jobName + " executing at " + startTime);
			logger.info("Quartz says: " + jobName + " executing at " + startTime);

			File spamFilterFile = new File(Config.getProperty("COLLECT_SPAM_FILTER_ANDROIDMARKET"));
			AndroidMarketDataCollector collector = new AndroidMarketDataCollector(loginAccount, loginPasswd, spamFilterFile);	
			
			Set<Locale> locales = new HashSet<Locale>();
			locales.add(Locale.KOREA);
			//locales.add(Locale.ENGLISH);
			
			/////////////////////////////
			String objectId1 = "naverline";
			String appId1 = "jp.naver.line.android";
			
			Map<Locale, List<Comment>> comments1 = collector.getAppCommentsByLocales(locales, appId1, 7);
			
			try {
				String dataDir = Config.getProperty("ANDROIDMARKET_SOURCE_DATA_DIR");
				String indexDir = Config.getProperty("ANDROIDMARKET_INDEX_DIR");
				String liwcCatFile = Config.getProperty("LIWC_CAT_FILE");
				
				collector.writeOutput(dataDir, indexDir, liwcCatFile, objectId1, comments1, startTime, 5);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}

			/////////////////////////////
			String objectId2 = "naverapp";
			String appId2 = "com.nhn.android.search";
			
			Map<Locale, List<Comment>> comments2 = collector.getAppCommentsByLocales(locales, appId2, 7);
			
			try {
				String dataDir = Config.getProperty("ANDROIDMARKET_SOURCE_DATA_DIR");
				String indexDir = Config.getProperty("ANDROIDMARKET_INDEX_DIR");
				String liwcCatFile = Config.getProperty("LIWC_CAT_FILE");
				
				collector.writeOutput(dataDir, indexDir, liwcCatFile, objectId2, comments2, startTime, 5);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}

			/////////////////////////////
			String objectId3 = "kakaotalk";
			String appId3 = "com.kakao.talk";
			
			Map<Locale, List<Comment>> comments3 = collector.getAppCommentsByLocales(locales, appId3, 7);
			
			try {
				String dataDir = Config.getProperty("ANDROIDMARKET_SOURCE_DATA_DIR");
				String indexDir = Config.getProperty("ANDROIDMARKET_INDEX_DIR");
				String liwcCatFile = Config.getProperty("LIWC_CAT_FILE");
				
				collector.writeOutput(dataDir, indexDir, liwcCatFile, objectId3, comments3, startTime, 5);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}
			/////////////////////////////
			
			// end time
			Date endTime = new Date();
			System.out.println("Quartz says: " + jobName + " finished at "	+ endTime);
			System.out.println("Collecting for android market data from : " + startTime + " to: " + endTime);
			
			logger.info("Quartz says: " + jobName + " finished at "	+ endTime);
			logger.jobSummary("Collecting for android market data", startTime, endTime);
			
	 	} catch (Exception e) {
	    	e.printStackTrace();
	    } 
	}	
}
