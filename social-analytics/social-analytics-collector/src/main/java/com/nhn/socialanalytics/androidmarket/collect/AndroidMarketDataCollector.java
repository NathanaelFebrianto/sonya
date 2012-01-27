package com.nhn.socialanalytics.androidmarket.collect;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.gc.android.market.api.model.Market.Comment;
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

public class AndroidMarketDataCollector extends Collector { 
	
	public static final String TARGET_SITE_NAME = "androidmarket";
	private static JobLogger logger = JobLogger.getLogger(AndroidMarketDataCollector.class, "androidmarket-collect.log");
	
	private AndroidMarketCrawler crawler;
	
	public AndroidMarketDataCollector(String loginAccount, String loginPasswd) {
		this.crawler = new AndroidMarketCrawler(loginAccount, loginPasswd);
	}
	
	public Map<Locale, List<Comment>> getAppCommentsByLocales(Set<Locale> locales, String appId, int maxPage) {
		Map<Locale, List<Comment>> commentMap = new HashMap<Locale, List<Comment>>();
		try {           	
        	logger.info("------------------------------------------------");
        	logger.info("appStores = " + locales + " appId: " + appId + " page: " + maxPage);
        	commentMap = crawler.getAppCommentsByLocales(locales, appId, maxPage);	            
	        logger.info("result map size [locales:" + locales + "] = " + commentMap.size());          
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}		
		
		return commentMap;
	}
	
	public List<Comment> getAppComments(Set<Locale> locales, String appId, int maxPage) {
		List<Comment> commentList = new ArrayList<Comment>();
		try {           	
        	logger.info("------------------------------------------------");
        	logger.info("appStores = " + locales + " appId: " + appId + " page: " + maxPage);
        	commentList = crawler.getAppComments(locales, appId, maxPage);	            
	        logger.info("result size [locales:" + locales + "] = " + commentList.size());          
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}		
		
		return commentList;
	}
	
	public void writeOutput(String objectId, Map<Locale, List<Comment>> commentsMap) throws Exception {		
		
		String currentDate = DateUtil.convertDateToString("yyyyMMddHHmmss", new Date());	

		// collect history buffer
		Set<String> idSet = new HashSet<String>();
		
		for (Map.Entry<Locale, List<Comment>> entry : commentsMap.entrySet()) {
			Locale locale = entry.getKey();
			List<Comment> comments = entry.getValue();
			
			// comment
			for (Comment comment : comments) {
				String authorId  = comment.getAuthorId();
				String authorName = comment.getAuthorName();
				int rating = comment.getRating();
				String text = comment.getText();
				text = text.replaceAll("\t", " ").replaceAll("\n", " ");
				String createDate = DateUtil.convertLongToString("yyyyMMddHHmmss", comment.getCreationTime());
				String commentId = createDate + "-" + authorId;
				
				// add new collected id into set
				idSet.add(commentId);
							
				// if no duplication, write collected data
				if (!historyBuffer.checkDuplicate(commentId)) {
					boolean isSpam = super.isSpam(text);

					// generate document
					List<String> texts = new ArrayList<String>();
					texts.add(text);
					SourceDocument doc = docGenerator.generate(new Locale(locale.getLanguage()), objectId, texts);
					
					// set document
					doc.setSite(TARGET_SITE_NAME);
					doc.setLanguage(locale.getLanguage());
					doc.setCollectDate(currentDate);
					doc.setDocId(commentId);
					doc.setCreateDate(createDate);
					doc.setAuthorId(authorId);
					doc.setAuthorName(authorName);
					doc.setSpam(isSpam);
					doc.setText(text);
					
					// set custom fields
					doc.addCustomField("locale", locale);
					doc.addCustomField("rating", rating);
					
					// write document
					docWriter.write(doc);
				}		
			}
		}
			
		docWriter.close();
		historyBuffer.writeCollectHistory(idSet);
	}
	
	public static void main(String[] args) {
		
		String loginAccount = args[0];
		String loginPasswd = args[1];
		
		//Set<Locale> locales = AndroidMarkets.getAllAndroidMarkets();
		Set<Locale> locales = new HashSet<Locale>();
		locales.add(Locale.KOREA);
		locales.add(Locale.JAPAN);
		
		//String query = "네이버톡";
		//String query = "pname:com.nhn.android.navertalk";
		//collector.searchApps(Locale.KOREA, query, 1);
		
		String objectId = "naverline";
		String appId = "jp.naver.line.android";
		
		//String objectId = "kakaotalk";		
		//String appId = "com.kakao.talk";
		
		//String objectId = "naverapp";
		//String appId = "com.nhn.android.search";		
		
		try {
			AndroidMarketDataCollector collector = new AndroidMarketDataCollector(loginAccount, loginPasswd);
			
			collector.setSpamFilter(new File(Config.getProperty("COLLECT_SPAM_FILTER_ANDROIDMARKET")));
			
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
			
			File sourceDocFile = Collector.getSourceDocFile(Config.getProperty("ANDROIDMARKET_COLLECT_DATA_DIR"), objectId, new Date());
			SourceDocumentFileWriter docWriter = new SourceDocumentFileWriter(sourceDocFile);
			
			collector.setSourceDocumentGenerator(docGenerator);
			collector.setSourceDocumentWriter(docWriter);
			
			File historyBufferFile = Collector.getCollectHistoryFile(Config.getProperty("ANDROIDMARKET_COLLECT_DATA_DIR"), objectId);
			CollectHistoryBuffer historyBuffer = new CollectHistoryBuffer(historyBufferFile, 2);
			collector.setCollectHistoryBuffer(historyBuffer);
			
			int maxPage = 10;
			//List<Comment> comments = collector.getAppComments(locales, appId, maxPage);
			Map<Locale, List<Comment>> commentsMap = collector.getAppCommentsByLocales(locales, appId, maxPage);
			
			collector.writeOutput(objectId, commentsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
