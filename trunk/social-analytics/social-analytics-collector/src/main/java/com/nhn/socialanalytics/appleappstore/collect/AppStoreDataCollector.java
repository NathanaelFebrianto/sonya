package com.nhn.socialanalytics.appleappstore.collect;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.nhn.socialanalytics.appleappstore.model.Review;
import com.nhn.socialanalytics.common.Config;
import com.nhn.socialanalytics.common.JobLogger;
import com.nhn.socialanalytics.common.collect.CollectHistoryBuffer;
import com.nhn.socialanalytics.common.collect.Collector;
import com.nhn.socialanalytics.common.util.DateUtil;
import com.nhn.socialanalytics.nlp.analysis.TextAnalyzer;
import com.nhn.socialanalytics.nlp.feature.FeatureCategoryClassifier;
import com.nhn.socialanalytics.nlp.lang.ja.JapaneseMorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ja.JapaneseSemanticAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanMorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanSemanticAnalyzer;
import com.nhn.socialanalytics.nlp.sentiment.SentimentAnalyzer;
import com.nhn.socialanalytics.opinion.dao.SourceDocumentGenerator;
import com.nhn.socialanalytics.opinion.dao.file.SourceDocumentFileWriter;
import com.nhn.socialanalytics.opinion.model.SourceDocument;


public class AppStoreDataCollector extends Collector {
	
	public static final String TARGET_SITE_NAME = "appstore";
	private static JobLogger logger = JobLogger.getLogger(AppStoreDataCollector.class, "appstore-collect.log");
		
	private AppStoreCrawler crawler;

	public AppStoreDataCollector() {
		this.crawler = new AppStoreCrawler();
	}
	
	public List<Review> getReviews(Set<String> appStores, String appId) {
		List<Review> result = new ArrayList<Review>();
		
		try {           	
        	logger.info("------------------------------------------------");
        	logger.info("appStores = " + appStores + " appId: " + appId);
         	
        	result = crawler.getReviews(appStores, appId);
	            
	        logger.info("result size [appStores:" + appStores + "] = " + result.size());           
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}		
		
		return result;
	}
	
	public List<Review> getReviews(Set<String> appStores, String appId, int maxPage) {
		List<Review> result = new ArrayList<Review>();
		
		try {           	
        	logger.info("------------------------------------------------");
        	logger.info("appStores = " + appStores + " appId: " + appId + " page: " + maxPage);
         	
        	result = crawler.getReviews(appStores, appId, maxPage);
	            
	        logger.info("result size [appStores:" + appStores + "] = " + result.size());           
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}		
		
		return result;
	}
	
	public void writeOutput(String objectId, List<Review> reviews) throws Exception {
		
		String currentDate = DateUtil.convertDateToString("yyyyMMddHHmmss", new Date());	

		// collect history buffer
		Set<String> idSet = new HashSet<String>();
		
		// review
		for (Review review : reviews) {			
			String appStoreId = review.getAppStoreId();
			String country = review.getCountry();
			String reviewId = review.getReviewId();
			String authorId = review.getAuthorId();
			String authorName = review.getAuthorName();
			String topic = review.getTopic();
			String text = review.getText();
			text = text.replaceAll("\t", " ").replaceAll("\n", " ");
			String version = review.getVersion();
			String createDate = review.getCreateDate();
			int rating = review.getRating();
			
			// add new collected id into set
			idSet.add(reviewId);
						
			// if no duplication, write collected data
			if (!historyBuffer.checkDuplicate(reviewId)) {
				boolean isSpam = super.isSpam(topic + " " + text);

				Locale locale = null;
				if (review.getCountry().equalsIgnoreCase("Korea"))
					locale = Locale.KOREAN;
				else if (review.getCountry().equalsIgnoreCase("Japan"))
					locale = Locale.JAPANESE;
				
				// generate document
				List<String> texts = new ArrayList<String>();
				texts.add(topic);
				texts.add(text);
				SourceDocument doc = docGenerator.generate(locale, objectId, texts);
				
				// set document
				doc.setSite(TARGET_SITE_NAME);
				doc.setLanguage(locale.getLanguage());
				doc.setCollectDate(currentDate);
				doc.setDocId(reviewId);
				doc.setCreateDate(createDate);
				doc.setAuthorId(authorId);
				doc.setAuthorName(authorName);
				doc.setSpam(isSpam);
				doc.setText(text);
				
				// set custom fields
				doc.addCustomField("country", country);
				doc.addCustomField("version", version);
				doc.addCustomField("rating", rating);
				doc.addCustomField("appstore_id", appStoreId);
				doc.addCustomField("topic", topic);
				
				// write document
				docWriter.write(doc);
			}		
		}
		
		docWriter.close();
		historyBuffer.writeCollectHistory(idSet);
	}
	
	public static void main(String[] args) {
		Set<String> appStores = new HashSet<String>();
		appStores.add(AppStores.getAppStore("Korea"));
		appStores.add(AppStores.getAppStore("Japan"));
		
		String objectId = "naverline";
		String appId = "443904275";		
		
		//String objectId = "naverapp";
		//String appId = "393499958";
		
		//String objectId = "kakaotalk";
		//String appId = "362057947";
		
		try {
			AppStoreDataCollector collector = new AppStoreDataCollector();
			
			collector.setSpamFilter(new File(Config.getProperty("COLLECT_SPAM_FILTER_APPSTORE")));
			
			TextAnalyzer textAnalyzer = new TextAnalyzer();
			textAnalyzer.putMorphemeAnalyzer(Locale.KOREAN, new KoreanMorphemeAnalyzer());
			textAnalyzer.putMorphemeAnalyzer(Locale.JAPANESE, new JapaneseMorphemeAnalyzer());
			textAnalyzer.putSemanticAnalyzer(Locale.KOREAN, new KoreanSemanticAnalyzer());
			textAnalyzer.putSemanticAnalyzer(Locale.JAPANESE, new JapaneseSemanticAnalyzer());
			textAnalyzer.putSentimentAnalyzer(Locale.KOREAN, new SentimentAnalyzer(new File(Config.getProperty("LIWC_KOREAN"))));
			textAnalyzer.putSentimentAnalyzer(Locale.JAPANESE, new SentimentAnalyzer(new File(Config.getProperty("LIWC_JAPANESE"))));
			textAnalyzer.putFeatureCategoryClassifier(objectId, Locale.KOREAN, new FeatureCategoryClassifier(new File(Config.getProperty("FEATURE_MOBILE_KOREAN"))));
			textAnalyzer.putFeatureCategoryClassifier(objectId, Locale.JAPANESE, new FeatureCategoryClassifier(new File(Config.getProperty("FEATURE_MOBILE_JAPANESE"))));
			
			SourceDocumentGenerator docGenerator = new SourceDocumentGenerator();
			docGenerator.setTextAnalyzer(textAnalyzer);
			
			File sourceDocFile = Collector.getSourceDocFile(Config.getProperty("APPSTORE_COLLECT_DATA_DIR"), objectId, new Date());
			SourceDocumentFileWriter docWriter = new SourceDocumentFileWriter(sourceDocFile);
			
			collector.setSourceDocumentGenerator(docGenerator);
			collector.setSourceDocumentWriter(docWriter);
			
			File historyBufferFile = Collector.getCollectHistoryFile(Config.getProperty("APPSTORE_COLLECT_DATA_DIR"), objectId);
			CollectHistoryBuffer historyBuffer = new CollectHistoryBuffer(historyBufferFile, 2);
			collector.setCollectHistoryBuffer(historyBuffer);
			
			//int maxPage = 30;
			int maxPage = 5;
			List<Review> reviews = collector.getReviews(appStores, appId, maxPage);
			//List<Review> reviews = collector.getReviews(appStores, appId);
			//List<Review> reviews = collector.getReviews(AppStores.getAllAppStores(), appId);
			
			collector.writeOutput(objectId, reviews);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
