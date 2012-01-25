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
import com.nhn.socialanalytics.common.collect.CollectHistoryBuffer;
import com.nhn.socialanalytics.common.collect.CollectObject;
import com.nhn.socialanalytics.common.collect.CollectObjectReader;
import com.nhn.socialanalytics.common.collect.Collector;
import com.nhn.socialanalytics.nlp.analysis.TextAnalyzer;
import com.nhn.socialanalytics.nlp.feature.FeatureClassifier;
import com.nhn.socialanalytics.nlp.lang.ja.JapaneseMorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ja.JapaneseSemanticAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanMorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanSemanticAnalyzer;
import com.nhn.socialanalytics.nlp.sentiment.SentimentAnalyzer;
import com.nhn.socialanalytics.opinion.dao.SourceDocumentGenerator;
import com.nhn.socialanalytics.opinion.dao.file.SourceDocumentFileWriter;

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


			/////////////////////////////
			AndroidMarketDataCollector collector = new AndroidMarketDataCollector(loginAccount, loginPasswd);
			
			// set spam filter
			collector.setSpamFilter(new File(Config.getProperty("COLLECT_SPAM_FILTER_ANDROIDMARKET")));

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
			List<CollectObject> colObjects = colObjectReader.getCollectObject(AndroidMarketDataCollector.TARGET_SITE_NAME);
			
			for (CollectObject colObject : colObjects) {
				String objectId = colObject.getObjectId();
				List<String> keywords = colObject.getSearchKeywords();
				String appId = keywords.get(0);
				int maxPage = colObject.getMaxPage();
				int historyBufferMaxRound = colObject.getHistoryBufferMaxRound();
				Map<String, String> featureClassifiers = colObject.getFeatureClassifiers();
				Map<String, List<String>> attributes = colObject.getExtendedAttributes();
				
				// set collect history buffer
				File historyBufferFile = Collector.getCollectHistoryFile(Config.getProperty("ANDROIDMARKET_COLLECT_DATA_DIR"), objectId);
				CollectHistoryBuffer historyBuffer = new CollectHistoryBuffer(historyBufferFile, historyBufferMaxRound);
				collector.setCollectHistoryBuffer(historyBuffer);
				
				// set document writer
				File sourceDocFile = Collector.getSourceDocFile(Config.getProperty("ANDROIDMARKET_COLLECT_DATA_DIR"), objectId, new Date());
				SourceDocumentFileWriter docWriter = new SourceDocumentFileWriter(sourceDocFile);
				collector.setSourceDocumentWriter(docWriter);
			
				// set feature classifiers
				for (Map.Entry<String, String> entry : featureClassifiers.entrySet()) {
					String language = entry.getKey();
					String featureFile = entry.getValue();	
					
					textAnalyzer.putFeatureClassifier(objectId, new Locale(language), new FeatureClassifier(new File(featureFile)));
				}
				
				// get market locales
				Set<Locale> locales = new HashSet<Locale>();
				for (Map.Entry<String, List<String>> entry : attributes.entrySet()) {
					if (entry.getKey().equalsIgnoreCase("LOCALE")) {
						List<String> values = entry.getValue();
						for (String value : values) {
							locales.add(AndroidMarkets.getLocale(value));
						}
					}
				}
				
				// crawl data
				Map<Locale, List<Comment>> comments = collector.getAppCommentsByLocales(locales, appId, maxPage);
				
				// write to file
				try {
					collector.writeOutput(objectId, comments);					
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
				}				
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
