package com.nhn.socialanalytics.twitter.collect;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.nhn.socialanalytics.common.Config;
import com.nhn.socialanalytics.common.JobLogger;
import com.nhn.socialanalytics.common.collect.CollectHistoryBuffer;
import com.nhn.socialanalytics.common.collect.Collector;
import com.nhn.socialanalytics.common.util.DateUtil;
import com.nhn.socialanalytics.nlp.analysis.TextAnalyzer;
import com.nhn.socialanalytics.nlp.feature.FeatureClassifier;
import com.nhn.socialanalytics.nlp.lang.ja.JapaneseMorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ja.JapaneseSemanticAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanMorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanSemanticAnalyzer;
import com.nhn.socialanalytics.nlp.sentiment.SentimentAnalyzer;
import com.nhn.socialanalytics.opinion.dao.SourceDocumentGenerator;
import com.nhn.socialanalytics.opinion.dao.file.SourceDocumentFileWriter;
import com.nhn.socialanalytics.opinion.model.SourceDocument;
import com.nhn.socialanalytics.twitter.parse.TwitterParser;

public class TwitterDataCollector extends Collector {

	public static final String TARGET_SITE_NAME = "twitter";
	private static JobLogger logger = JobLogger.getLogger(TwitterDataCollector.class, "twitter-collect.log");
	
	private Twitter twitter;
	
	public TwitterDataCollector() {
		this.twitter = new TwitterFactory().getInstance();
	}
	
	public List<twitter4j.Tweet> searchTweets(Map<String, Integer> queryMap, Date since, Date until) {
		List<twitter4j.Tweet> result = new ArrayList<twitter4j.Tweet>();
		Set<String> idHashSet = new HashSet<String>();
		
		for (Map.Entry<String, Integer> entry : queryMap.entrySet()) {
			String query = entry.getKey();
			int maxPage = entry.getValue();			
			List<twitter4j.Tweet> tweets = this.searchTweets(query, since, until, maxPage);
			
			for (int i = 0; i < tweets.size(); i++) {
				twitter4j.Tweet tweet = (twitter4j.Tweet) tweets.get(i);
				String tweetId = String.valueOf(tweet.getId());				
				// check if it is duplicate
				if (!idHashSet.contains(tweetId)) {
					result.add(tweet);
				}				
				idHashSet.add(tweetId);
			}
		}
		
		return result;
	}
	
	public List<twitter4j.Tweet> searchTweets(String strQuery, Date since, Date until, int maxPage) {
		List<twitter4j.Tweet> tweets = new ArrayList<twitter4j.Tweet>();
		
		try {			
        	Query query = new Query(strQuery);
        	query.rpp(100);
        	
        	if (since != null)
        		query.setSince(DateUtil.convertDateToString("yyyy-MM-dd", since));
         	
        	if (until != null)
        		query.setUntil(DateUtil.convertDateToString("yyyy-MM-dd", until));
         	
        	logger.info("------------------------------------------------");
        	logger.info("query = " + query.getQuery() + " since: " + since + " page: " + maxPage);
         	
        	for (int page = 1; page <= maxPage; page++) {
	        	query.page(page);		        	
	            QueryResult result = twitter.search(query);
	            
	            logger.info("query result size[page:" + page + "] = " + result.getTweets().size());
	            tweets.addAll(result.getTweets());
        	}
           
		} catch (TwitterException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}		
		
		return tweets;
	}
	
	public void writeOutput(String objectId, List<twitter4j.Tweet> tweets) throws Exception {
		
		String currentDate = DateUtil.convertDateToString("yyyyMMddHHmmss", new Date());	

		// collect history buffer
		Set<String> idSet = new HashSet<String>();
		
		// post
		for (twitter4j.Tweet tweet : tweets) {
			String tweetId = String.valueOf(tweet.getId());
			String langCode = tweet.getIsoLanguageCode();			
			String createDate = DateUtil.convertDateToString("yyyyMMddHHmmss", tweet.getCreatedAt());
			String fromUserId = String.valueOf(tweet.getFromUserId());
			String fromUser = tweet.getFromUser();			
			String toUser = tweet.getToUser();	
			String profileImageUrl = tweet.getProfileImageUrl();
			String text = TwitterParser.extractContent(tweet.getText());
			
			// add new collected id into set
			idSet.add(tweetId);
						
			// if no duplication, write collected data
			if (!historyBuffer.checkDuplicate(tweetId)) {
				Locale locale = new Locale(langCode);
				
				// generate document
				List<String> texts = new ArrayList<String>();
				texts.add(text);
				SourceDocument doc = docGenerator.generate(locale, objectId, texts);
				
				// set document
				doc.setSite(TARGET_SITE_NAME);
				doc.setLanguage(locale.getLanguage());
				doc.setCollectDate(currentDate);
				doc.setDocId(tweetId);
				doc.setCreateDate(createDate);
				doc.setAuthorId(fromUserId);
				doc.setAuthorName(fromUser);
				doc.setSpam(false);
				doc.setText(text);
				
				// set custom fields
				doc.addCustomField("to_user", toUser);
				doc.addCustomField("profile_image_url", profileImageUrl);
				doc.addCustomField("tweet_type", "");
				
				// write document
				docWriter.write(doc);
			}		
		}
		
		docWriter.close();
		historyBuffer.writeCollectHistory(idSet);
	}
	
	public static void main(String[] args) {
		//String objectId = "fta";
		//String query = "한미FTA OR ISD";
		
		//String objectId = "gameshutdown";
		//String query = "게임셧다운제";

		//String objectId = "kakaotalk";
		//String query = "카카오톡 OR 카톡";
		
		//String objectId = "naverapp";
		//String query = "네이버앱";
		
		int maxPage = 30;
		
		String objectId = "naverline";
		Map<String, Integer> queryMap = new HashMap<String, Integer>();
		queryMap.put("네이버라인", maxPage);
		queryMap.put("naver ライン", maxPage);

		try {
			TwitterDataCollector collector = new TwitterDataCollector();
			
			TextAnalyzer textAnalyzer = new TextAnalyzer();
			textAnalyzer.putMorphemeAnalyzer(Locale.KOREAN, new KoreanMorphemeAnalyzer());
			textAnalyzer.putMorphemeAnalyzer(Locale.JAPANESE, new JapaneseMorphemeAnalyzer());
			textAnalyzer.putSemanticAnalyzer(Locale.KOREAN, new KoreanSemanticAnalyzer());
			textAnalyzer.putSemanticAnalyzer(Locale.JAPANESE, new JapaneseSemanticAnalyzer());
			textAnalyzer.putSentimentAnalyzer(Locale.KOREAN, new SentimentAnalyzer(new File(Config.getProperty("LIWC_KOREAN"))));
			textAnalyzer.putSentimentAnalyzer(Locale.JAPANESE, new SentimentAnalyzer(new File(Config.getProperty("LIWC_JAPANESE"))));
			textAnalyzer.putFeatureClassifier(objectId, Locale.KOREAN, new FeatureClassifier(new File(Config.getProperty("DEFAULT_FEATURE_KOREAN"))));
			textAnalyzer.putFeatureClassifier(objectId, Locale.JAPANESE, new FeatureClassifier(new File(Config.getProperty("DEFAULT_FEATURE_JAPANESE"))));
			
			SourceDocumentGenerator docGenerator = new SourceDocumentGenerator();
			docGenerator.setTextAnalyzer(textAnalyzer);
			
			File sourceDocFile = Collector.getSourceDocFile(Config.getProperty("TWITTER_COLLECT_DATA_DIR"), objectId, new Date());
			SourceDocumentFileWriter docWriter = new SourceDocumentFileWriter(sourceDocFile);
			
			collector.setSourceDocumentGenerator(docGenerator);
			collector.setSourceDocumentWriter(docWriter);
			
			File historyBufferFile = Collector.getCollectHistoryFile(Config.getProperty("TWITTER_COLLECT_DATA_DIR"), objectId);
			CollectHistoryBuffer historyBuffer = new CollectHistoryBuffer(historyBufferFile, 1);
			collector.setCollectHistoryBuffer(historyBuffer);

			Date since = DateUtil.addDay(new Date(), -30);
			List<twitter4j.Tweet> tweets = collector.searchTweets(queryMap, since, null);
			
			collector.writeOutput(objectId, tweets);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
