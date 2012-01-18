package com.nhn.socialanalytics.appleappstore.collect;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nhn.socialanalytics.appleappstore.model.Review;
import com.nhn.socialanalytics.common.Config;
import com.nhn.socialanalytics.common.JobLogger;
import com.nhn.socialanalytics.common.collect.CollectObject;
import com.nhn.socialanalytics.common.collect.CollectObjectReader;
import com.nhn.socialanalytics.common.collect.Collector;
import com.nhn.socialanalytics.nlp.feature.FeatureClassifier;
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

			
			/////////////////////////////
			AppStoreDataCollector collector = new AppStoreDataCollector();
			collector.setSpamFilter(new File(Config.getProperty("COLLECT_SPAM_FILTER_APPSTORE")));
			collector.putMorphemeAnalyzer(Collector.LANG_KOREAN, new KoreanMorphemeAnalyzer());
			collector.putMorphemeAnalyzer(Collector.LANG_JAPANESE, new JapaneseMorphemeAnalyzer());
			collector.putSemanticAnalyzer(Collector.LANG_KOREAN, new KoreanSemanticAnalyzer());
			collector.putSemanticAnalyzer(Collector.LANG_JAPANESE, new JapaneseSemanticAnalyzer());
			collector.putSentimentAnalyzer(Collector.LANG_KOREAN, new SentimentAnalyzer(new File(Config.getProperty("LIWC_KOREAN"))));
			collector.putSentimentAnalyzer(Collector.LANG_JAPANESE, new SentimentAnalyzer(new File(Config.getProperty("LIWC_JAPANESE"))));
			
			String docDir = Config.getProperty("APPSTORE_DATA_DIR");
			String indexDir = Config.getProperty("APPSTORE_INDEX_DIR");
			
			CollectObjectReader colObjectReader = new CollectObjectReader(new File(Config.getProperty("COLLECT_OBJECTS")));
			List<CollectObject> colObjects = colObjectReader.getCollectObject(AppStoreDataCollector.TARGET_SITE_NAME);
			
			for (CollectObject colObject : colObjects) {
				String objectId = colObject.getObject();
				List<String> keywords = colObject.getSearchKeywords();
				String appId = keywords.get(0);
				int maxPage = colObject.getMaxPage();
				int historyBufferMaxRound = colObject.getHistoryBufferMaxRound();
				Map<String, String> featureClassifiers = colObject.getFeatureClassifiers();
				Map<String, List<String>> attributes = colObject.getExtendedAttributes();
				
				// app stores
				Set<String> appStores = new HashSet<String>();
				for (Map.Entry<String, List<String>> entry : attributes.entrySet()) {
					if (entry.getKey().equalsIgnoreCase("APPSTORE")) {
						List<String> values = entry.getValue();
						for (String value : values) {
							appStores.add(AppStores.getAppStore(value));
						}
					}
				}
				
				// feature classifiers
				for (Map.Entry<String, String> entry : featureClassifiers.entrySet()) {
					String language = entry.getKey();
					String featureFile = entry.getValue();	
					collector.putFeatureClassifier(objectId, language, new FeatureClassifier(new File(featureFile)));
				}
				
				List<Review> reviews = collector.getReviews(appStores, appId, maxPage);
				
				try {
					collector.writeOutput(docDir, indexDir, objectId, reviews, startTime, historyBufferMaxRound);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
				}				
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
