package com.nhn.socialanalytics.twitter.collect;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.nhn.socialanalytics.common.Config;
import com.nhn.socialanalytics.common.JobLogger;
import com.nhn.socialanalytics.common.util.DateUtil;
import com.nhn.socialanalytics.miner.termvector.DocIndexWriter;
import com.nhn.socialanalytics.nlp.kr.morpheme.MorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.kr.semantic.SemanticAnalyzer;
import com.nhn.socialanalytics.nlp.kr.semantic.SemanticClause;
import com.nhn.socialanalytics.nlp.kr.semantic.SemanticSentence;
import com.nhn.socialanalytics.nlp.kr.sentiment.SentimentAnalyzer;
import com.nhn.socialanalytics.twitter.parse.TwitterParser;

public class TwitterDataCollector {

	private static JobLogger logger = JobLogger.getLogger(TwitterDataCollector.class, "twitter-collect.log");
	private File outputDir;
	
	private Twitter twitter;
	
	public TwitterDataCollector() {	
		outputDir = new File(Config.getProperty("TWITTER_SOURCE_DATA_DIR"));
		if (!outputDir.exists()) {
			outputDir.mkdir();
			logger.info(outputDir + " is created.");
		}
		
		twitter = new TwitterFactory().getInstance();
	}
	
	public List<twitter4j.Tweet> searchTweets(String objectId, String strQuery, String since, String until, int maxPage) {
		List<twitter4j.Tweet> tweets = new ArrayList<twitter4j.Tweet>();
		
		try {           	
        	Query query = new Query(strQuery);
        	query.rpp(100);
        	query.setSince(since);
        	
        	logger.info("------------------------------------------------");
        	logger.info("object id: " + objectId);
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
	
	public void writeOutput(String objectId, List<twitter4j.Tweet> tweets) throws IOException, Exception {
				
		MorphemeAnalyzer morph = MorphemeAnalyzer.getInstance();
		SemanticAnalyzer semantic = SemanticAnalyzer.getInstance();
		SentimentAnalyzer sentiment = SentimentAnalyzer.getInstance(new File("./bin/liwc/LIWC_ko.txt"));		
		DocIndexWriter indexWriter = new DocIndexWriter("./bin/twitter/index/kakaotalk");
		
		File file = new File(outputDir.getPath() + File.separator + objectId + ".txt");
		BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getPath()), "UTF-8"));
			
		br.write("object_id	tweet_id	created_at	from_user	to_user	text	text1	text2	subjectpredicate	subject	predicate	objects	polarity	polarity_strength");
		br.newLine();
		
		// post
		for (twitter4j.Tweet tweet : tweets) {
			String tweetId = String.valueOf(tweet.getId());
			String createTime = DateUtil.convertDateToString("yyyyMMddHHmmss", tweet.getCreatedAt());
			String fromUser = tweet.getFromUser();
			String toUser = tweet.getToUser();			
			
			String text = TwitterParser.extractContent(tweet.getText());
			String textEmotiTagged = TwitterParser.convertEmoticonToTag(text);
			
			String text1 = morph.extractTerms(textEmotiTagged);
			String text2 = morph.extractCoreTerms(textEmotiTagged);
			
			SemanticSentence semanticSentence = semantic.createSemanticSentence(textEmotiTagged);
			String subjectpredicates = semanticSentence.extractSubjectPredicateLabel();
			String subjects = semanticSentence.extractSubjectLabel();
			String predicates = semanticSentence.extractPredicateLabel();
			String objects = semanticSentence.extractObjectsLabel();
			
			semanticSentence = sentiment.analyzePolarity(semanticSentence);
			double polarity = semanticSentence.getPolarity();
			double polarityStrength = semanticSentence.getPolarityStrength();
			
			br.write(
					objectId + "\t" +
					tweetId + "\t" +
					createTime + "\t" + 
					fromUser + "\t" +
					toUser + "\t" +
					text + "\t" +
					text1 + "\t" +
					text2 + "\t" +
					subjectpredicates + "\t" +
					subjects + "\t" +
					predicates + "\t" +
					objects + "\t" +
					polarity + "\t" +
					polarityStrength		
					);
			br.newLine();
			

			for (SemanticClause clause : semanticSentence) {
				indexWriter.write(
						"twitter", 
						createTime, 
						fromUser, 
						tweetId, 
						clause.getSubject(),
						clause.getPredicate(), 
						clause.makeObjectsLabel(), 
						text);
			}
		
		}
		br.close();
		indexWriter.close();
	}
	
	public static void main(String[] args) {
		TwitterDataCollector collector = new TwitterDataCollector();
		
		//String objectId = "navertalk";
		//String query = "네이버톡 OR 네톡";

		String objectId = "kakaotalk";
		String query = "카카오톡 OR 카톡";
		
		//String objectId = "naverapp";
		//String query = "네이버앱";
		
		List<twitter4j.Tweet> tweets = collector.searchTweets(objectId, query, "2011-02-01", null, 10);
		
		try {
			collector.writeOutput(objectId, tweets);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
