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
import com.nhn.socialanalytics.common.collect.Collector;
import com.nhn.socialanalytics.nlp.feature.FeatureClassifier;
import com.nhn.socialanalytics.nlp.lang.ja.JapaneseMorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ja.JapaneseSemanticAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanMorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanSemanticAnalyzer;
import com.nhn.socialanalytics.nlp.sentiment.SentimentAnalyzer;

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

			AndroidMarketDataCollector collector = new AndroidMarketDataCollector(loginAccount, loginPasswd);
			collector.setSpamFilter(new File(Config.getProperty("COLLECT_SPAM_FILTER_ANDROIDMARKET")));
			collector.putMorphemeAnalyzer(Collector.LANG_KOREAN, new KoreanMorphemeAnalyzer());
			collector.putMorphemeAnalyzer(Collector.LANG_JAPANESE, new JapaneseMorphemeAnalyzer());
			collector.putSemanticAnalyzer(Collector.LANG_KOREAN, new KoreanSemanticAnalyzer());
			collector.putSemanticAnalyzer(Collector.LANG_JAPANESE, new JapaneseSemanticAnalyzer());
			collector.putSentimentAnalyzer(Collector.LANG_KOREAN, new SentimentAnalyzer(new File(Config.getProperty("LIWC_KOREAN"))));
			collector.putSentimentAnalyzer(Collector.LANG_JAPANESE, new SentimentAnalyzer(new File(Config.getProperty("LIWC_JAPANESE"))));
			
			/////////////////////////////
			String objectId1 = "naverline";
			String appId1 = "jp.naver.line.android";
			Set<Locale> locales1 = new HashSet<Locale>();
			locales1.add(Locale.KOREA);
			locales1.add(Locale.JAPAN);
			
			collector.putFeatureClassifier(objectId1, Collector.LANG_KOREAN, new FeatureClassifier(new File(Config.getProperty("DEFAULT_FEATURE_KOREAN"))));
			collector.putFeatureClassifier(objectId1, Collector.LANG_JAPANESE, new FeatureClassifier(new File(Config.getProperty("DEFAULT_FEATURE_JAPANESE"))));
			
			Map<Locale, List<Comment>> comments1 = collector.getAppCommentsByLocales(locales1, appId1, 5);
			
			try {
				String dataDir = Config.getProperty("ANDROIDMARKET_SOURCE_DATA_DIR");
				String indexDir = Config.getProperty("ANDROIDMARKET_INDEX_DIR");
				
				collector.writeOutput(dataDir, indexDir, objectId1, comments1, startTime, 5);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}

			/////////////////////////////
			String objectId2 = "naverapp";
			String appId2 = "com.nhn.android.search";
			Set<Locale> locales2 = new HashSet<Locale>();
			locales2.add(Locale.KOREA);
			
			collector.putFeatureClassifier(objectId2, Collector.LANG_KOREAN, new FeatureClassifier(new File(Config.getProperty("DEFAULT_FEATURE_KOREAN"))));

			Map<Locale, List<Comment>> comments2 = collector.getAppCommentsByLocales(locales2, appId2, 5);
			
			try {
				String dataDir = Config.getProperty("ANDROIDMARKET_SOURCE_DATA_DIR");
				String indexDir = Config.getProperty("ANDROIDMARKET_INDEX_DIR");
				
				collector.writeOutput(dataDir, indexDir, objectId2, comments2, startTime, 5);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}

			/////////////////////////////
			String objectId3 = "kakaotalk";
			String appId3 = "com.kakao.talk";
			Set<Locale> locales3 = new HashSet<Locale>();
			locales3.add(Locale.KOREA);
			locales3.add(Locale.JAPAN);
			
			collector.putFeatureClassifier(objectId3, Collector.LANG_KOREAN, new FeatureClassifier(new File(Config.getProperty("DEFAULT_FEATURE_KOREAN"))));
			collector.putFeatureClassifier(objectId3, Collector.LANG_JAPANESE, new FeatureClassifier(new File(Config.getProperty("DEFAULT_FEATURE_JAPANESE"))));
			
			Map<Locale, List<Comment>> comments3 = collector.getAppCommentsByLocales(locales3, appId3, 5);
			
			try {
				String dataDir = Config.getProperty("ANDROIDMARKET_SOURCE_DATA_DIR");
				String indexDir = Config.getProperty("ANDROIDMARKET_INDEX_DIR");
				
				collector.writeOutput(dataDir, indexDir, objectId3, comments3, startTime, 5);
				
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
