package com.nhn.socialanalytics.twitter.collect;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;

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
import com.nhn.socialanalytics.nlp.feature.FeatureClassifier;
import com.nhn.socialanalytics.nlp.lang.ja.JapaneseMorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ja.JapaneseSemanticAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanMorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanSemanticAnalyzer;
import com.nhn.socialanalytics.nlp.morpheme.MorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.semantic.SemanticAnalyzer;
import com.nhn.socialanalytics.nlp.semantic.SemanticClause;
import com.nhn.socialanalytics.nlp.semantic.SemanticSentence;
import com.nhn.socialanalytics.nlp.sentiment.SentimentAnalyzer;
import com.nhn.socialanalytics.opinion.common.FieldConstants;
import com.nhn.socialanalytics.opinion.common.OpinionDocument;
import com.nhn.socialanalytics.opinion.dao.lucene.DocIndexSearcher;
import com.nhn.socialanalytics.opinion.dao.lucene.DocIndexWriter;
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
	
	public void writeOutput(String dataDir, String indexDir, String objectId, 
			List<twitter4j.Tweet> tweets, Date collectDate, int historyBufferMaxRound) throws IOException, Exception {
		
		String currentDatetime = DateUtil.convertDateToString("yyyyMMddHHmmss", new Date());	
		File docIndexDir = super.getDocIndexDir(indexDir, collectDate);
		File dataFile = super.getDataFile(dataDir, objectId, collectDate);

		// collect history buffer
		Set<String> idSet = new HashSet<String>();
		CollectHistoryBuffer history = new CollectHistoryBuffer(super.getCollectHistoryFile(dataDir, objectId), historyBufferMaxRound);
				
		// text analyzer
		MorphemeAnalyzer morphemeKorean = super.getMorphemeAnalyzer(Collector.LANG_KOREAN);
		MorphemeAnalyzer morphemeJapanese = super.getMorphemeAnalyzer(Collector.LANG_JAPANESE);
		SemanticAnalyzer semanticKorean = super.getSemanticAnalyzer(Collector.LANG_KOREAN);
		SemanticAnalyzer semanticJapanese = super.getSemanticAnalyzer(Collector.LANG_JAPANESE);
		SentimentAnalyzer sentimentKorean = super.getSentimentAnalyzer(Collector.LANG_KOREAN);
		SentimentAnalyzer sentimentJapanese = super.getSentimentAnalyzer(Collector.LANG_JAPANESE);
		
		// feature classifier
		FeatureClassifier featureKorean = super.getFeatureClassifier(objectId, Collector.LANG_KOREAN);
		FeatureClassifier featureJapanese = super.getFeatureClassifier(objectId, Collector.LANG_JAPANESE);
		
		// indexer
		DocIndexWriter indexWriter = new DocIndexWriter(docIndexDir);		
		DocIndexSearcher indexSearcher = new DocIndexSearcher(super.getDocumentIndexDirsToSearch(indexDir, collectDate));
		
		// output data file
		boolean existDataFile = false;
		
		if (dataFile.exists())
			existDataFile = true;
		
		BufferedWriter brData = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dataFile.getPath(), true), "UTF-8"));
		
		if (!existDataFile) {
			brData.write("site" + DELIMITER +
					"object_id" + DELIMITER +
					"language" + DELIMITER +	
					"collect_date" + DELIMITER +
					"tweet_id" + DELIMITER +	
					"create_date" + DELIMITER +	
					"from_user" + DELIMITER +		
					"to_user" + DELIMITER +	
					"profile_image_url" + DELIMITER +	
					"text" + DELIMITER +		
					"text1" + DELIMITER +		
					"text2" + DELIMITER +		
					"feature" + DELIMITER +
					"main_feature" + DELIMITER +
					"clause" + DELIMITER +		
					"subject" + DELIMITER +		
					"predicate" + DELIMITER +		
					"attribute" + DELIMITER +	
					"modifier" + DELIMITER +	
					"polarity" + DELIMITER +		
					"polarity_strength"
					);
			brData.newLine();			
		}
		
		// post
		for (twitter4j.Tweet tweet : tweets) {
			String tweetId = String.valueOf(tweet.getId());
			String langCode = tweet.getIsoLanguageCode();			
			String createDate = DateUtil.convertDateToString("yyyyMMddHHmmss", tweet.getCreatedAt());
			String fromUserId = String.valueOf(tweet.getFromUserId());
			String fromUser = tweet.getFromUser();			
			String toUser = tweet.getToUser();	
			String profileImageUrl = tweet.getProfileImageUrl();
			
			/////////////////////////////////
			// add new collected id into set
			idSet.add(tweetId);
			/////////////////////////////////		
						
			// if no duplication, write collected data
			if (!history.checkDuplicate(tweetId)) {
				String text = TwitterParser.extractContent(tweet.getText());
				String textEmotiTagged = TwitterParser.convertEmoticonToTag(text);
				
				String language = "";
				String text1 = "";
				String text2 = "";
				String feature = "";
				String mainFeature = "";
				SemanticSentence semanticSentence = null;
				double polarity = 0.0;
				double polarityStrength = 0.0;
				
				if (langCode.equalsIgnoreCase("ko")) {
					language = FieldConstants.LANG_KOREAN;
					// morpheme analysis
					text1 = morphemeKorean.extractTerms(textEmotiTagged);
					text2 = morphemeKorean.extractCoreTerms(textEmotiTagged);		
					
					// semantic analysis
					semanticSentence = semanticKorean.analyze(textEmotiTagged);
					
					// sentiment analysis
					semanticSentence = sentimentKorean.analyzePolarity(semanticSentence);
					polarity = semanticSentence.getPolarity();
					polarityStrength = semanticSentence.getPolarityStrength();
					
					// feature classification
					String standardLabels = semanticSentence.extractStandardLabel(" ", " ", true, false, false);
					Map<String, Double> featureCounts = featureKorean.getFeatureCounts(standardLabels, true);
					feature = featureKorean.getFeatureLabel(featureCounts);
					mainFeature = featureKorean.getMainFeatureLabel(featureCounts);
				}
				else if (langCode.equalsIgnoreCase("ja")) {
					language = FieldConstants.LANG_JAPANESE;
					// morpheme analysis
					text1 = morphemeJapanese.extractTerms(textEmotiTagged);
					text2 = morphemeJapanese.extractCoreTerms(textEmotiTagged);		
					
					// semantic analysis
					semanticSentence = semanticJapanese.analyze(textEmotiTagged);
					
					// sentiment analysis
					semanticSentence = sentimentJapanese.analyzePolarity(semanticSentence);
					polarity = semanticSentence.getPolarity();
					polarityStrength = semanticSentence.getPolarityStrength();
					
					// feature classification
					String standardLabels = semanticSentence.extractStandardLabel(" ", " ", true, false, false);
					Map<String, Double> featureCounts = featureJapanese.getFeatureCounts(standardLabels, true);
					feature = featureJapanese.getFeatureLabel(featureCounts);
					mainFeature = featureJapanese.getMainFeatureLabel(featureCounts);
				}
				else {
					// morpheme analysis
					text1 = morphemeKorean.extractTerms(textEmotiTagged);
					text2 = morphemeKorean.extractCoreTerms(textEmotiTagged);		
					
					// semantic analysis
					semanticSentence = semanticKorean.analyze(textEmotiTagged);
					
					// sentiment analysis
					semanticSentence = sentimentKorean.analyzePolarity(semanticSentence);
					polarity = semanticSentence.getPolarity();
					polarityStrength = semanticSentence.getPolarityStrength();
					
					// feature classification
					String standardLabels = semanticSentence.extractStandardLabel(" ", " ", true, false, false);
					Map<String, Double> featureCounts = featureKorean.getFeatureCounts(standardLabels, true);
					feature = featureKorean.getFeatureLabel(featureCounts);
					mainFeature = featureKorean.getMainFeatureLabel(featureCounts);			
				}				
				
				String strClause = semanticSentence.extractStandardLabel(",", "-", true, false, false);
				String subject = semanticSentence.extractStandardSubjectLabel();
				String predicate = semanticSentence.extractStandardPredicateLabel();
				String attribute = semanticSentence.extractStandardAttributesLabel();
				String modifier = semanticSentence.extractStandardModifiersLabel();
				
				// write new collected data into source file
				brData.write(
						TARGET_SITE_NAME + DELIMITER +
						objectId + DELIMITER +
						langCode + DELIMITER +
						currentDatetime + DELIMITER +
						tweetId + DELIMITER +
						createDate + DELIMITER + 
						fromUser + DELIMITER +
						toUser + DELIMITER +
						profileImageUrl + DELIMITER +
						text + DELIMITER +
						text1 + DELIMITER +
						text2 + DELIMITER +
						feature + DELIMITER +
						mainFeature + DELIMITER +
						strClause + DELIMITER +
						subject + DELIMITER +
						predicate + DELIMITER +
						attribute + DELIMITER +
						modifier + DELIMITER +
						polarity + DELIMITER +
						polarityStrength		
						);
				brData.newLine();
				
				////////////////////////////////////////
				// write new collected data into index file
				////////////////////////////////////////
				Set<Document> existDocs = indexSearcher.searchDocuments(FieldConstants.DOC_ID, tweetId);
				
				if (existDocs.size() > 0) {
					for (Iterator<Document> it = existDocs.iterator(); it.hasNext();) {
						Document existDoc = (Document) it.next();
						String objects = existDoc.get(FieldConstants.OBJECT);
						objects = objects + " " + objectId;
						
						indexWriter.update(FieldConstants.OBJECT, objects, existDoc);
				     }
				}
				else {
					for (SemanticClause clause : semanticSentence) {
						OpinionDocument doc = new OpinionDocument();
						doc.setSite(TARGET_SITE_NAME);
						doc.setObject(objectId);
						doc.setLanguage(language);
						doc.setCollectDate(currentDatetime);
						doc.setDocId(tweetId);
						doc.setDate(createDate);
						doc.setAuthorId(fromUserId);
						doc.setAuthorName(fromUser);
						doc.setDocFeature(feature);
						doc.setDocMainFeature(mainFeature);
						doc.setSubject(clause.getSubject());
						doc.setPredicate(clause.getPredicate());
						doc.setAttribute(clause.makeAttributesLabel());
						doc.setModifier(modifier);
						doc.setText(text);
						doc.setDocPolarity(polarity);
						doc.setDocPolarityStrength(polarityStrength);
						doc.setClausePolarity(clause.getPolarity());
						doc.setClausePolarityStrength(clause.getPolarityStrength());

						// feature classification for semantic clause
						doc = super.setClauseFeatureToDocument(objectId, language, clause, doc);
						
						indexWriter.write(doc);
					}						
				}			
			}		
		}
		
		brData.close();
		indexWriter.close();
		history.writeCollectHistory(idSet);
	}
	
	public static void main(String[] args) {
		TwitterDataCollector collector = new TwitterDataCollector();
		collector.putMorphemeAnalyzer(Collector.LANG_KOREAN, new KoreanMorphemeAnalyzer());
		collector.putMorphemeAnalyzer(Collector.LANG_JAPANESE, new JapaneseMorphemeAnalyzer());
		collector.putSemanticAnalyzer(Collector.LANG_KOREAN, new KoreanSemanticAnalyzer());
		collector.putSemanticAnalyzer(Collector.LANG_JAPANESE, new JapaneseSemanticAnalyzer());
		collector.putSentimentAnalyzer(Collector.LANG_KOREAN, new SentimentAnalyzer(new File(Config.getProperty("LIWC_KOREAN"))));
		collector.putSentimentAnalyzer(Collector.LANG_JAPANESE, new SentimentAnalyzer(new File(Config.getProperty("LIWC_JAPANESE"))));
		
		//String objectId = "fta";
		//String query = "한미FTA OR ISD";
		
		//String objectId = "gameshutdown";
		//String query = "게임셧다운제";

		//String objectId = "kakaotalk";
		//String query = "카카오톡 OR 카톡";
		
		//String objectId = "naverapp";
		//String query = "네이버앱";
		
		//List<twitter4j.Tweet> tweets = collector.searchTweets(objectId, query, "2011-02-01", null, 10);
		
		String objectId = "naverline";
		Map<String, Integer> queryMap = new HashMap<String, Integer>();
		queryMap.put("네이버라인", 30);
		queryMap.put("naver ライン", 30);
		
		collector.putFeatureClassifier(objectId, Collector.LANG_KOREAN, new FeatureClassifier(new File(Config.getProperty("DEFAULT_FEATURE_KOREAN"))));
		collector.putFeatureClassifier(objectId, Collector.LANG_JAPANESE, new FeatureClassifier(new File(Config.getProperty("DEFAULT_FEATURE_JAPANESE"))));

		
		Date since = DateUtil.addDay(new Date(), -30);
		List<twitter4j.Tweet> tweets = collector.searchTweets(queryMap, since, null);
				
		try {
			collector.writeOutput("./bin/data/twitter/collect/", "./bin/data/twitter/index/", objectId, tweets, new Date(), 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
