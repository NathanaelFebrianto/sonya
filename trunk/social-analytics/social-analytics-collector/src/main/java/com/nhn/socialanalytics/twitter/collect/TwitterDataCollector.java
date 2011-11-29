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

import com.nhn.socialanalytics.common.Collector;
import com.nhn.socialanalytics.common.JobLogger;
import com.nhn.socialanalytics.common.util.DateUtil;
import com.nhn.socialanalytics.miner.index.DetailDoc;
import com.nhn.socialanalytics.miner.index.DocIndexSearcher;
import com.nhn.socialanalytics.miner.index.DocIndexWriter;
import com.nhn.socialanalytics.miner.index.FieldConstants;
import com.nhn.socialanalytics.nlp.kr.morpheme.MorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.kr.semantic.SemanticAnalyzer;
import com.nhn.socialanalytics.nlp.kr.semantic.SemanticClause;
import com.nhn.socialanalytics.nlp.kr.semantic.SemanticSentence;
import com.nhn.socialanalytics.nlp.kr.sentiment.SentimentAnalyzer;
import com.nhn.socialanalytics.twitter.parse.TwitterParser;

public class TwitterDataCollector extends Collector {

	private static JobLogger logger = JobLogger.getLogger(TwitterDataCollector.class, "twitter-collect.log");
	private Twitter twitter;
	
	public TwitterDataCollector() {	
		twitter = new TwitterFactory().getInstance();
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
	
	public void writeOutput(String dataDir, String indexDir, String liwcCatFile, 
			String objectId, List<twitter4j.Tweet> tweets, Date collectDate) throws IOException, Exception {
		
		String currentDatetime = DateUtil.convertDateToString("yyyyMMddHHmmss", new Date());	
		File docIndexDir = super.getDocIndexDir(indexDir, collectDate);
		File dataFile = super.getDataFile(dataDir, objectId, collectDate);
		File newCollectedIdSetFile = super.getIDSetFile(dataDir, objectId);	
		
		///////////////////////////
		// get the id hash set collected previously to compare with new collected data set
		// and remove duplication
		Set<String> prevColIdSet = super.loadPrevCollectedHashSet(dataDir, objectId);
		///////////////////////////
				
		MorphemeAnalyzer morph = MorphemeAnalyzer.getInstance();
		SemanticAnalyzer semantic = SemanticAnalyzer.getInstance();
		SentimentAnalyzer sentiment = SentimentAnalyzer.getInstance(liwcCatFile);
		
		DocIndexWriter indexWriter = new DocIndexWriter(docIndexDir);		
		DocIndexSearcher indexSearcher = new DocIndexSearcher(super.getDocumentIndexDirsToSearch(indexDir, collectDate));
		
		// output data file
		boolean existDataFile = false;
		
		if (dataFile.exists())
			existDataFile = true;
		
		BufferedWriter brData = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dataFile.getPath(), true), "UTF-8"));
		
		//////////////////////////////
		// new collected id set file
		BufferedWriter brCollectedIdSet = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(newCollectedIdSetFile.getPath(), false), "UTF-8"));
		//////////////////////////////
		
		if (!existDataFile) {
			brData.write("site" + DELIMITER +
					"object_id" + DELIMITER +
					"collect_date" + DELIMITER +
					"tweet_id" + DELIMITER +	
					"create_date" + DELIMITER +	
					"from_user" + DELIMITER +		
					"to_user" + DELIMITER +		
					"text" + DELIMITER +		
					"text1" + DELIMITER +		
					"text2" + DELIMITER +		
					"subjectpredicate" + DELIMITER +		
					"subject" + DELIMITER +		
					"predicate" + DELIMITER +		
					"attribute" + DELIMITER +		
					"polarity" + DELIMITER +		
					"polarity_strength"
					);
			brData.newLine();			
		}
		
		// post
		for (twitter4j.Tweet tweet : tweets) {
			String tweetId = String.valueOf(tweet.getId());
			String createDate = DateUtil.convertDateToString("yyyyMMddHHmmss", tweet.getCreatedAt());
			String fromUserId = String.valueOf(tweet.getFromUserId());
			String fromUser = tweet.getFromUser();			
			String toUser = tweet.getToUser();
			
			/////////////////////////////////
			// write new collected all id into file
			brCollectedIdSet.write(tweetId);
			brCollectedIdSet.newLine();
			/////////////////////////////////			
						
			// if no duplication, write collected data
			if (!prevColIdSet.contains(tweetId)) {
				String text = TwitterParser.extractContent(tweet.getText());
				String textEmotiTagged = TwitterParser.convertEmoticonToTag(text);
				
				String text1 = morph.extractTerms(textEmotiTagged);
				String text2 = morph.extractCoreTerms(textEmotiTagged);
				
				SemanticSentence semanticSentence = semantic.createSemanticSentence(textEmotiTagged);
				String subjectpredicate = semanticSentence.extractSubjectPredicateLabel();
				String subject = semanticSentence.extractSubjectLabel();
				String predicate = semanticSentence.extractPredicateLabel();
				String attribute = semanticSentence.extractAttributesLabel();
				
				semanticSentence = sentiment.analyzePolarity(semanticSentence);
				double polarity = semanticSentence.getPolarity();
				double polarityStrength = semanticSentence.getPolarityStrength();
				
				// write new collected data into source file
				brData.write(
						"twitter" + DELIMITER +
						objectId + DELIMITER +
						currentDatetime + DELIMITER +
						tweetId + DELIMITER +
						createDate + DELIMITER + 
						fromUser + DELIMITER +
						toUser + DELIMITER +
						text + DELIMITER +
						text1 + DELIMITER +
						text2 + DELIMITER +
						subjectpredicate + DELIMITER +
						subject + DELIMITER +
						predicate + DELIMITER +
						attribute + DELIMITER +
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
						DetailDoc doc = new DetailDoc();
						doc.setSite("twitter");
						doc.setObject(objectId);
						doc.setCollectDate(currentDatetime);
						doc.setDocId(tweetId);
						doc.setDate(createDate);
						doc.setUserId(fromUserId);
						doc.setUserName(fromUser);
						doc.setSubject(clause.getSubject());
						doc.setPredicate(clause.getPredicate());
						doc.setAttribute(clause.makeAttributesLabel());
						doc.setText(text);
						
						indexWriter.write(doc);
					}						
				}			
			}		
		}
		
		brData.close();
		brCollectedIdSet.close();		
		indexWriter.close();
	}
	
	public static void main(String[] args) {
		TwitterDataCollector collector = new TwitterDataCollector();
		
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
		//queryMap.put("한미FTA OR ISD", 5);
		//queryMap.put("FTA OR ISD", 5);
		queryMap.put("네이버 라인 OR 네이버 LINE", 10);
		queryMap.put("NAVER LINE", 10);
		
		Date since = DateUtil.addDay(new Date(), -30);
		List<twitter4j.Tweet> tweets = collector.searchTweets(queryMap, since, null);
				
		try {
			collector.writeOutput("./bin/data/twitter/collect/", "./bin/data/twitter/index/", "./bin/liwc/LIWC_ko.txt", objectId, tweets, new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
