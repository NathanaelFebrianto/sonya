package com.nhn.socialanalytics.twitter.collect;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nhn.socialanalytics.common.Config;
import com.nhn.socialanalytics.common.JobLogger;
import com.nhn.socialanalytics.common.collect.CollectHistoryBuffer;
import com.nhn.socialanalytics.common.collect.CollectObject;
import com.nhn.socialanalytics.common.collect.CollectObjectReader;
import com.nhn.socialanalytics.common.collect.Collector;
import com.nhn.socialanalytics.common.util.DateUtil;
import com.nhn.socialanalytics.nlp.analysis.TextAnalyzer;
import com.nhn.socialanalytics.nlp.competitor.CompetitorExtractor;
import com.nhn.socialanalytics.nlp.feature.FeatureCategoryClassifier;
import com.nhn.socialanalytics.nlp.lang.ja.JapaneseMorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ja.JapaneseSemanticAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanMorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanSemanticAnalyzer;
import com.nhn.socialanalytics.nlp.sentiment.SentimentAnalyzer;
import com.nhn.socialanalytics.opinion.dao.SourceDocumentGenerator;
import com.nhn.socialanalytics.opinion.dao.file.SourceDocumentFileWriter;

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
			
			// set text analyzer
			TextAnalyzer textAnalyzer = new TextAnalyzer();
			textAnalyzer.putMorphemeAnalyzer(Locale.KOREAN, new KoreanMorphemeAnalyzer());
			textAnalyzer.putMorphemeAnalyzer(Locale.JAPANESE, new JapaneseMorphemeAnalyzer());
			textAnalyzer.putSemanticAnalyzer(Locale.KOREAN, new KoreanSemanticAnalyzer());
			textAnalyzer.putSemanticAnalyzer(Locale.JAPANESE, new JapaneseSemanticAnalyzer());
			textAnalyzer.putSentimentAnalyzer(Locale.KOREAN, new SentimentAnalyzer(new File(Config.getProperty("LIWC_KOREAN"))));
			textAnalyzer.putSentimentAnalyzer(Locale.JAPANESE, new SentimentAnalyzer(new File(Config.getProperty("LIWC_JAPANESE"))));
			// set document generator
			SourceDocumentGenerator docGenerator = new SourceDocumentGenerator();
			docGenerator.setTextAnalyzer(textAnalyzer);
			collector.setSourceDocumentGenerator(docGenerator);

			CollectObjectReader colObjectReader = new CollectObjectReader(new File(Config.getProperty("COLLECT_OBJECTS")));
			List<CollectObject> colObjects = colObjectReader.getCollectObject(TwitterDataCollector.TARGET_SITE_NAME);
			
			for (CollectObject colObject : colObjects) {
				String objectId = colObject.getObjectId();
				List<String> keywords = colObject.getSearchKeywords();
				int maxPage = colObject.getMaxPage();
				int historyBufferMaxRound = colObject.getHistoryBufferMaxRound();
				Map<String, String> featureDictionaries = colObject.getFeatureDictionaries();
				String competiorDictionary = colObject.getCompetitorDictionary();
				Map<String, List<String>> attributes = colObject.getExtendedAttributes();
				
				// set collect history buffer
				File historyBufferFile = Collector.getCollectHistoryFile(Config.getProperty("TWITTER_COLLECT_DATA_DIR"), objectId);
				CollectHistoryBuffer historyBuffer = new CollectHistoryBuffer(historyBufferFile, historyBufferMaxRound);
				collector.setCollectHistoryBuffer(historyBuffer);
				
				// set document writer
				File sourceDocFile = Collector.getSourceDocFile(Config.getProperty("TWITTER_COLLECT_DATA_DIR"), objectId, new Date());
				SourceDocumentFileWriter docWriter = new SourceDocumentFileWriter(sourceDocFile);
				collector.setSourceDocumentWriter(docWriter);
				
				// set feature dictionaries
				for (Map.Entry<String, String> entry : featureDictionaries.entrySet()) {
					String language = entry.getKey();
					String featureDicFile = entry.getValue();	
					
					textAnalyzer.putFeatureCategoryClassifier(objectId, new Locale(language), new FeatureCategoryClassifier(new File(featureDicFile)));
				}
				
				// set competitor dictionary
				textAnalyzer.putCompetitorExtractor(objectId, new CompetitorExtractor(new File(competiorDictionary)));
				
				// query map
				Map<String, Integer> queryMap = new HashMap<String, Integer>();
				for (String keyword : keywords) {
					queryMap.put(keyword, maxPage);
				}
				
				Date sinceDate = DateUtil.addDay(new Date(), -2);
				//Date = DateUtil.addDay(new Date(), +1);
				
				List<twitter4j.Tweet> tweets = collector.searchTweets(queryMap, sinceDate, null);
				
				try {
					collector.writeOutput(objectId, tweets);
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
