package com.nhn.socialanalytics.me2day.collect;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.nhn.socialanalytics.common.Config;
import com.nhn.socialanalytics.common.JobLogger;
import com.nhn.socialanalytics.common.collect.CollectHistoryBuffer;
import com.nhn.socialanalytics.common.collect.Collector;
import com.nhn.socialanalytics.common.util.DateUtil;
import com.nhn.socialanalytics.me2day.model.Post;
import com.nhn.socialanalytics.me2day.parse.Me2dayParser;
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

public class Me2dayDataCollector extends Collector {

	public static final String TARGET_SITE_NAME = "me2day";
	private static JobLogger logger = JobLogger.getLogger(Me2dayDataCollector.class, "me2day-collect.log");	
	
	private Me2dayCrawler crawler;
	
	public Me2dayDataCollector() {
		this.crawler = new Me2dayCrawler();
	}
	
	public List<Post> searchPosts(Map<String, Integer> queryMap, String target, Date since, Date until) {
		List<Post> posts = new ArrayList<Post>();
		try {           	
			String strSince = DateUtil.convertDateToString("yyyy.MM.dd", since);
			String strUntil = DateUtil.convertDateToString("yyyy.MM.dd", until);
			
        	logger.info("------------------------------------------------");
        	logger.info("queryMap = " + queryMap + " target: " + target + " since: " + strSince + " until: " + strUntil);
         	
        	posts = crawler.searchPosts(queryMap, target, since, until);
	            
	        logger.info("result size [queryMap:" + queryMap + "] = " + posts.size());           
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}		
		
		return posts;
	}
	
	public void writeOutput(String objectId, List<Post> posts) throws Exception {
		
		String currentDate = DateUtil.convertDateToString("yyyyMMddHHmmss", new Date());	

		// collect history buffer
		Set<String> idSet = new HashSet<String>();
		
		// post
		for (Post post : posts) {
			String postId = post.getPostId();
			String createDate = DateUtil.convertDateToString("yyyyMMddHHmmss", post.getPublishDate());
			String authorId = post.getAuthorId();
			String authorName = post.getAuthorNickname();
			//String textBody = post.getTextBody();
			String body = post.getBody();
			String tagText = post.getTagText();
			String text = Me2dayParser.extractContent(body, "POST");
			tagText = Me2dayParser.extractContent(tagText, "TAG");	
			
			int commentCount = post.getCommentCount();
			int metooCount = post.getMetooCount();
			String permalink = post.getPermalink();
			String profileImageUrl = post.getAuthorProfileImage();
			
			// add new collected id into set
			idSet.add(postId);
						
			// if no duplication, write collected data
			if (!historyBuffer.checkDuplicate(postId)) {
				Locale locale = Locale.KOREAN;
				
				// generate document
				List<String> texts = new ArrayList<String>();
				texts.add(text);
				SourceDocument doc = docGenerator.generate(locale, objectId, texts);
				
				// set document
				doc.setSite(TARGET_SITE_NAME);
				doc.setLanguage(locale.getLanguage());
				doc.setCollectDate(currentDate);
				doc.setDocId(postId);
				doc.setCreateDate(createDate);
				doc.setAuthorId(authorId);
				doc.setAuthorName(authorName);
				doc.setSpam(false);
				doc.setText(text);
				
				// set custom fields
				doc.addCustomField("permalink", permalink);
				doc.addCustomField("profile_image_url", profileImageUrl);
				doc.addCustomField("comment_count", commentCount);
				doc.addCustomField("metoo_count", metooCount);
				doc.addCustomField("tag_text", tagText);
				
				// write document
				docWriter.write(doc);
			}		
		}
		
		docWriter.close();
		historyBuffer.writeCollectHistory(idSet);
	}
	
	public static void main(String[] args) {
		
		int maxPage = 10;
//		String objectId = "fta";
//		Map<String, Integer> queryMap = new HashMap<String, Integer>();
//		queryMap.put("한미FTA ISD", maxPage);
//		queryMap.put("FTA ISD", maxPage);

		String objectId = "naverline";
		Map<String, Integer> queryMap = new HashMap<String, Integer>();
		queryMap.put("네이버라인", maxPage);
		queryMap.put("네이버LINE", maxPage);
				
		try {
			Me2dayDataCollector collector = new Me2dayDataCollector();
			
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
			
			File sourceDocFile = Collector.getSourceDocFile(Config.getProperty("ME2DAY_COLLECT_DATA_DIR"), objectId, new Date());
			SourceDocumentFileWriter docWriter = new SourceDocumentFileWriter(sourceDocFile);
			
			collector.setSourceDocumentGenerator(docGenerator);
			collector.setSourceDocumentWriter(docWriter);
			
			File historyBufferFile = Collector.getCollectHistoryFile(Config.getProperty("ME2DAY_COLLECT_DATA_DIR"), objectId);
			CollectHistoryBuffer historyBuffer = new CollectHistoryBuffer(historyBufferFile, 1);
			collector.setCollectHistoryBuffer(historyBuffer);
			
			Date since = DateUtil.addDay(new Date(), -30);
			Date until = DateUtil.addDay(new Date(), +1);
			
			List<Post> posts = collector.searchPosts(queryMap, Me2dayCrawler.TARGET_BODY, since, until);
			
			collector.writeOutput(objectId, posts);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
