package com.nhn.socialanalytics.me2day.collect;

import java.io.File;
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
import com.nhn.socialanalytics.common.collect.Collector;
import com.nhn.socialanalytics.common.util.DateUtil;
import com.nhn.socialanalytics.me2day.model.Post;
import com.nhn.socialanalytics.nlp.feature.FeatureClassifier;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanMorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanSemanticAnalyzer;
import com.nhn.socialanalytics.nlp.sentiment.SentimentAnalyzer;

public class Me2dayDataCollectorJob implements Job {
	// logger
	private static JobLogger logger = JobLogger.getLogger(Me2dayDataCollectorJob.class, "me2day-collect.log");
	
	public Me2dayDataCollectorJob() { }
	
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

			Me2dayDataCollector collector = new Me2dayDataCollector();
			collector.putMorphemeAnalyzer(Collector.LANG_KOREAN, new KoreanMorphemeAnalyzer());
			collector.putSemanticAnalyzer(Collector.LANG_KOREAN, new KoreanSemanticAnalyzer());
			collector.putSentimentAnalyzer(Collector.LANG_KOREAN, new SentimentAnalyzer(new File(Config.getProperty("LIWC_KOREAN"))));
			
			/////////////////////////////
			String objectId1 = "naverline";
			Map<String, Integer> queryMap1 = new HashMap<String, Integer>();
			queryMap1.put("네이버라인", 5);
			queryMap1.put("네이버LINE", 5);
			queryMap1.put("NAVERLINE", 5);
			
			collector.putFeatureClassifier(objectId1, Collector.LANG_KOREAN, new FeatureClassifier(new File(Config.getProperty("DEFAULT_FEATURE_KOREAN"))));
			
			Date sinceDate1 = DateUtil.addDay(new Date(), -30);
			Date untilDate1 = DateUtil.addDay(new Date(), +1);
			
			List<Post> posts1 = collector.searchPosts(queryMap1, Me2dayCrawler.TARGET_BODY, sinceDate1, untilDate1);
			
			try {
				String dataDir = Config.getProperty("ME2DAY_SOURCE_DATA_DIR");
				String indexDir = Config.getProperty("ME2DAY_INDEX_DIR");
				
				collector.writeOutput(dataDir, indexDir, objectId1, posts1, startTime, 1);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}

			/////////////////////////////
			String objectId2 = "fta";
			Map<String, Integer> queryMap2 = new HashMap<String, Integer>();
			queryMap2.put("한미FTA", 10);
			queryMap2.put("FTA ISD", 10);
			
			collector.putFeatureClassifier(objectId2, Collector.LANG_KOREAN, new FeatureClassifier(new File(Config.getProperty("DEFAULT_FEATURE_KOREAN"))));
			
			Date sinceDate2 = DateUtil.addDay(new Date(), -1);
			Date untilDate2 = DateUtil.addDay(new Date(), +1);
			
			List<Post> posts2 = collector.searchPosts(queryMap2, Me2dayCrawler.TARGET_BODY, sinceDate2, untilDate2);
			
			try {
				String dataDir = Config.getProperty("ME2DAY_SOURCE_DATA_DIR");
				String indexDir = Config.getProperty("ME2DAY_INDEX_DIR");
				
				collector.writeOutput(dataDir, indexDir, objectId2, posts2, startTime, 1);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}
			/////////////////////////////
			
			// end time
			Date endTime = new Date();
			System.out.println("Quartz says: " + jobName + " finished at "	+ endTime);
			System.out.println("Collecting for Me2day data from : " + startTime + " to: " + endTime);
			
			logger.info("Quartz says: " + jobName + " finished at "	+ endTime);
			logger.jobSummary("Collecting for Me2day data", startTime, endTime);
			
	 	} catch (Exception e) {
	    	e.printStackTrace();
	    } 
	}	
}
