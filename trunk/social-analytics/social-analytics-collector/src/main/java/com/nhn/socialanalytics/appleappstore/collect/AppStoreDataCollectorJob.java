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
import com.nhn.socialanalytics.common.collect.Collector;
import com.nhn.socialanalytics.nlp.lang.ja.JapaneseMorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ja.JapaneseSemanticAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanMorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanSemanticAnalyzer;
import com.nhn.socialanalytics.nlp.sentiment.SentimentAnalyzer;

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

			AppStoreDataCollector collector = new AppStoreDataCollector();
			collector.setSpamFilter(new File(Config.getProperty("COLLECT_SPAM_FILTER_APPSTORE")));
			collector.putMorphemeAnalyzer(Collector.LANG_KOREAN, new KoreanMorphemeAnalyzer());
			collector.putMorphemeAnalyzer(Collector.LANG_JAPANESE, new JapaneseMorphemeAnalyzer());
			collector.putSemanticAnalyzer(Collector.LANG_KOREAN, new KoreanSemanticAnalyzer());
			collector.putSemanticAnalyzer(Collector.LANG_JAPANESE, new JapaneseSemanticAnalyzer());
			collector.putSentimentAnalyzer(Collector.LANG_KOREAN, new SentimentAnalyzer(new File(Config.getProperty("LIWC_KOREAN"))));
			collector.putSentimentAnalyzer(Collector.LANG_JAPANESE, new SentimentAnalyzer(new File(Config.getProperty("LIWC_JAPANESE"))));
			
			/////////////////////////////			
			String objectId1 = "naverline";
			String appId1 = "443904275";
			Set<String> appStores1 = new HashSet<String>();
			appStores1.add(AppStores.getAppStore("Korea"));
			appStores1.add(AppStores.getAppStore("Japan"));
			
			List<Review> reviews1 = collector.getReviews(appStores1, appId1, 3);
			
			try {
				String dataDir = Config.getProperty("APPSTORE_SOURCE_DATA_DIR");
				String indexDir = Config.getProperty("APPSTORE_INDEX_DIR");
				
				collector.writeOutput(dataDir, indexDir, objectId1, reviews1, startTime, 5);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}

			/////////////////////////////
			String objectId2 = "naverapp";
			String appId2 = "393499958";
			Set<String> appStores2 = new HashSet<String>();
			appStores2.add(AppStores.getAppStore("Korea"));
			
			List<Review> reviews2 = collector.getReviews(appStores2, appId2, 3);
			
			try {
				String dataDir = Config.getProperty("APPSTORE_SOURCE_DATA_DIR");
				String indexDir = Config.getProperty("APPSTORE_INDEX_DIR");
				
				collector.writeOutput(dataDir, indexDir, objectId2, reviews2, startTime, 5);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}
			
			/////////////////////////////
			String objectId3 = "kakaotalk";
			String appId3 = "362057947";
			Set<String> appStores3 = new HashSet<String>();
			appStores3.add(AppStores.getAppStore("Korea"));
			
			List<Review> reviews3 = collector.getReviews(appStores3, appId3, 3);
			
			try {
				String dataDir = Config.getProperty("APPSTORE_SOURCE_DATA_DIR");
				String indexDir = Config.getProperty("APPSTORE_INDEX_DIR");
				
				collector.writeOutput(dataDir, indexDir, objectId3, reviews3, startTime, 5);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}
			/////////////////////////////
			
			// end time
			Date endTime = new Date();
			System.out.println("Quartz says: " + jobName + " finished at "	+ endTime);
			System.out.println("Collecting for AppStore data from : " + startTime + " to: " + endTime);
			
			logger.info("Quartz says: " + jobName + " finished at "	+ endTime);
			logger.jobSummary("Collecting for AppStore data", startTime, endTime);
			
	 	} catch (Exception e) {
	    	e.printStackTrace();
	    } 
	}	
}
