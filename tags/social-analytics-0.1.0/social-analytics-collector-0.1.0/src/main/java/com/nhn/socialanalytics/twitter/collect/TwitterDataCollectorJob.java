package com.nhn.socialanalytics.twitter.collect;

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
import com.nhn.socialanalytics.common.collect.CollectObject;
import com.nhn.socialanalytics.common.collect.CollectObjectReader;
import com.nhn.socialanalytics.common.collect.Collector;
import com.nhn.socialanalytics.common.util.DateUtil;
import com.nhn.socialanalytics.nlp.feature.FeatureClassifier;
import com.nhn.socialanalytics.nlp.lang.ja.JapaneseMorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ja.JapaneseSemanticAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanMorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanSemanticAnalyzer;
import com.nhn.socialanalytics.nlp.sentiment.SentimentAnalyzer;

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
			

			/////////////////////////////
			TwitterDataCollector collector = new TwitterDataCollector();
			collector.putMorphemeAnalyzer(Collector.LANG_KOREAN, new KoreanMorphemeAnalyzer());
			collector.putMorphemeAnalyzer(Collector.LANG_JAPANESE, new JapaneseMorphemeAnalyzer());
			collector.putSemanticAnalyzer(Collector.LANG_KOREAN, new KoreanSemanticAnalyzer());
			collector.putSemanticAnalyzer(Collector.LANG_JAPANESE, new JapaneseSemanticAnalyzer());
			collector.putSentimentAnalyzer(Collector.LANG_KOREAN, new SentimentAnalyzer(new File(Config.getProperty("LIWC_KOREAN"))));
			collector.putSentimentAnalyzer(Collector.LANG_JAPANESE, new SentimentAnalyzer(new File(Config.getProperty("LIWC_JAPANESE"))));
			
			String dataDir = Config.getProperty("TWITTER_SOURCE_DATA_DIR");
			String indexDir = Config.getProperty("TWITTER_INDEX_DIR");
			
			CollectObjectReader colObjectReader = new CollectObjectReader(new File(Config.getProperty("COLLECT_OBJECTS")));
			List<CollectObject> colObjects = colObjectReader.getCollectObject(TwitterDataCollector.TARGET_SITE_NAME);

			for (CollectObject colObject : colObjects) {
				String objectId = colObject.getObject();
				List<String> keywords = colObject.getSearchKeywords();
				int maxPage = colObject.getMaxPage();
				int historyBufferMaxRound = colObject.getHistoryBufferMaxRound();
				Map<String, String> featureClassifiers = colObject.getFeatureClassifiers();
				
				// query map
				Map<String, Integer> queryMap = new HashMap<String, Integer>();
				for (String keyword : keywords) {
					queryMap.put(keyword, maxPage);
				}
				
				// feature classifiers
				for (Map.Entry<String, String> entry : featureClassifiers.entrySet()) {
					String language = entry.getKey();
					String featureFile = entry.getValue();	
					collector.putFeatureClassifier(objectId, language, new FeatureClassifier(new File(featureFile)));
				}
				
				Date sinceDate = DateUtil.addDay(new Date(), -2);
				//Date = DateUtil.addDay(new Date(), +1);
				
				List<twitter4j.Tweet> tweets = collector.searchTweets(queryMap, sinceDate, null);
				
				try {
					collector.writeOutput(dataDir, indexDir, objectId, tweets, startTime, historyBufferMaxRound);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
				}				
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
