package com.nhn.socialanalytics.twitter.collect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.nhn.socialanalytics.common.Config;
import com.nhn.socialanalytics.common.JobLogger;
import com.nhn.socialanalytics.common.util.DateUtil;
import com.nhn.socialanalytics.miner.termvector.DetailDoc;
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
	private static final String DELIMITER = "\t";
	
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
	
	private Set<String> loadPrevCollectedHashSet(String objectId) throws IOException, UnsupportedEncodingException {
		Set<String> idSet = new HashSet<String>();
		
		try {			
			String prevColIdSetFile = outputDir.getPath() + File.separator + objectId + ".txt";
			
			InputStream is = new FileInputStream(new File(prevColIdSetFile));
			BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8")); 
			
			String line;
			while ((line = in.readLine()) != null) {
				String[] tokens = Pattern.compile(DELIMITER).split(line);				
				String tweetId = tokens[0];
				idSet.add(tweetId);
			}
			is.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			return idSet;
		}
		
		return idSet;
	}
	
	public void writeOutput(String objectId, List<twitter4j.Tweet> tweets) throws IOException, Exception {
		String currentDate = DateUtil.convertDateToString("yyyyMMdd", new Date());	
		String docIndexDir = Config.getProperty("TWITTER_INDEX_DIR") + objectId + "_" + currentDate;
		String strOutputFile = outputDir.getPath() + File.separator + objectId + "_" + currentDate + ".txt";
		String strNewColIdSetFile = outputDir.getPath() + File.separator + objectId + ".txt";
		
		// get the id hash set collected previously to compare with new collected data set
		// and remove duplication
		Set<String> prevColIdSet = this.loadPrevCollectedHashSet(objectId);
		
		MorphemeAnalyzer morph = MorphemeAnalyzer.getInstance();
		SemanticAnalyzer semantic = SemanticAnalyzer.getInstance();
		SentimentAnalyzer sentiment = SentimentAnalyzer.getInstance(new File(Config.getProperty("LIWC_CAT_FILE")));			
		
		DocIndexWriter indexWriter = new DocIndexWriter(docIndexDir, true);
		
		// output file
		boolean existOutputFile = false;
		File outputFile = new File(strOutputFile);
		
		if (outputFile.exists())
			existOutputFile = true;
		
		BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile.getPath(), true), "UTF-8"));
		
		// new collected id set file
		File newColIdSetFile = new File(strNewColIdSetFile);
		BufferedWriter brColIdSetFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newColIdSetFile.getPath(), false), "UTF-8"));
		
		if (!existOutputFile) {
			br.write("object_id" + DELIMITER +
					"tweet_id" + DELIMITER +	
					"created_at" + DELIMITER +	
					"from_user" + DELIMITER +		
					"to_user" + DELIMITER +		
					"text" + DELIMITER +		
					"text1" + DELIMITER +		
					"text2" + DELIMITER +		
					"subjectpredicate" + DELIMITER +		
					"subject" + DELIMITER +		
					"predicate" + DELIMITER +		
					"objects" + DELIMITER +		
					"polarity" + DELIMITER +		
					"polarity_strength");
			br.newLine();			
		}
		
		// post
		for (twitter4j.Tweet tweet : tweets) {
			String tweetId = String.valueOf(tweet.getId());
			String createTime = DateUtil.convertDateToString("yyyyMMddHHmmss", tweet.getCreatedAt());
			String fromUserId = String.valueOf(tweet.getFromUserId());
			String fromUser = tweet.getFromUser();			
			String toUser = tweet.getToUser();
			
			// write new collected all id into file
			brColIdSetFile.write(tweetId);
			brColIdSetFile.newLine();
			
			// if no duplication, write collected data
			if (!prevColIdSet.contains(tweetId)) {
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
				
				// write new collected data into source file
				br.write(
						objectId + DELIMITER +
						tweetId + DELIMITER +
						createTime + DELIMITER + 
						fromUser + DELIMITER +
						toUser + DELIMITER +
						text + DELIMITER +
						text1 + DELIMITER +
						text2 + DELIMITER +
						subjectpredicates + DELIMITER +
						subjects + DELIMITER +
						predicates + DELIMITER +
						objects + DELIMITER +
						polarity + DELIMITER +
						polarityStrength		
						);
				br.newLine();			

				// write new collected data into index file
				for (SemanticClause clause : semanticSentence) {
					DetailDoc doc = new DetailDoc();
					doc.setSite("twitter");
					doc.setDate(createTime);
					doc.setUserId(fromUserId);
					doc.setUserName(fromUser);
					doc.setDocId(tweetId);
					doc.setSubject(clause.getSubject());
					doc.setPredicate(clause.getPredicate());
					doc.setObjects(clause.makeObjectsLabel());
					doc.setText(text);
					
					indexWriter.write(doc);
				}				
			}		
		}
		
		br.close();
		brColIdSetFile.close();		
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
