package com.nhn.socialanalytics.me2day.collect;

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

import com.nhn.socialanalytics.common.Config;
import com.nhn.socialanalytics.common.JobLogger;
import com.nhn.socialanalytics.common.collect.CollectHistoryBuffer;
import com.nhn.socialanalytics.common.collect.Collector;
import com.nhn.socialanalytics.common.util.DateUtil;
import com.nhn.socialanalytics.me2day.model.Post;
import com.nhn.socialanalytics.me2day.parse.Me2dayParser;
import com.nhn.socialanalytics.miner.index.DetailDoc;
import com.nhn.socialanalytics.miner.index.DocIndexSearcher;
import com.nhn.socialanalytics.miner.index.DocIndexWriter;
import com.nhn.socialanalytics.miner.index.FieldConstants;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanMorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.lang.ko.KoreanSemanticAnalyzer;
import com.nhn.socialanalytics.nlp.morpheme.MorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.semantic.SemanticAnalyzer;
import com.nhn.socialanalytics.nlp.semantic.SemanticClause;
import com.nhn.socialanalytics.nlp.semantic.SemanticSentence;
import com.nhn.socialanalytics.nlp.sentiment.SentimentAnalyzer;

public class Me2dayDataCollector extends Collector {

	private static JobLogger logger = JobLogger.getLogger(Me2dayDataCollector.class, "me2day-collect.log");
	private static final String TARGET_SITE_NAME = "me2day";
	
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
	
	public void writeOutput(String dataDir, String indexDir, String objectId, 
			List<Post> posts, Date collectDate, int historyBufferMaxRound) throws IOException, Exception {
		
		String currentDatetime = DateUtil.convertDateToString("yyyyMMddHHmmss", new Date());	
		File docIndexDir = super.getDocIndexDir(indexDir, collectDate);
		File dataFile = super.getDataFile(dataDir, objectId, collectDate);
	
		// collect history buffer
		Set<String> idSet = new HashSet<String>();
		CollectHistoryBuffer history = new CollectHistoryBuffer(super.getCollectHistoryFile(dataDir, objectId), historyBufferMaxRound);
				
		// text analyzer
		MorphemeAnalyzer morphemeKorean = super.getMorphemeAnalyzer(Collector.LANG_KOREAN);
		SemanticAnalyzer semanticKorean = super.getSemanticAnalyzer(Collector.LANG_KOREAN);
		SentimentAnalyzer sentimentKorean = super.getSentimentAnalyzer(Collector.LANG_KOREAN);
		
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
					"collect_date" + DELIMITER +
					"post_id" + DELIMITER +	
					"create_date" + DELIMITER +	
					"author_id" + DELIMITER +		
					"author_name" + DELIMITER +		
					"permalink" + DELIMITER +
					"comment_count" + DELIMITER +
					"metoo_count" + DELIMITER +
					"text" + DELIMITER +		
					"text1" + DELIMITER +		
					"text2" + DELIMITER +
					"tag_text" + DELIMITER +
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
		for (Post post : posts) {
			String postId = post.getPostId();
			String createDate = DateUtil.convertDateToString("yyyyMMddHHmmss", post.getPublishDate());
			String authorId = post.getAuthorId();
			String authorName = post.getAuthorNickname();
			//String textBody = post.getTextBody();
			String body = post.getBody();
			String tagText = post.getTagText();
			
			int commentCount = post.getCommentCount();
			int metooCount = post.getMetooCount();
			String permalink = post.getPermalink();
			
			/////////////////////////////////
			// add new collected id into set
			idSet.add(postId);
			/////////////////////////////////				
						
			// if no duplication, write collected data
			if (!history.checkDuplicate(postId)) {
				String text = Me2dayParser.extractContent(body, "POST");
				String tagText1 = Me2dayParser.extractContent("TAG", tagText);				
				
				String textEmotiTagged = Me2dayParser.convertEmoticonToTag(text);
				
				String language = FieldConstants.LANG_KOREAN;
				
				String text1 = morphemeKorean.extractTerms(textEmotiTagged);
				String text2 = morphemeKorean.extractCoreTerms(textEmotiTagged);
				
				SemanticSentence semanticSentence = semanticKorean.analyze(textEmotiTagged);
				String subjectpredicate = semanticSentence.extractSubjectPredicateLabel();
				String subject = semanticSentence.extractSubjectLabel();
				String predicate = semanticSentence.extractPredicateLabel();
				String attribute = semanticSentence.extractAttributesLabel();
				
				semanticSentence = sentimentKorean.analyzePolarity(semanticSentence);
				double polarity = semanticSentence.getPolarity();
				double polarityStrength = semanticSentence.getPolarityStrength();
				
				// write new collected data into source file
				brData.write(
						TARGET_SITE_NAME + DELIMITER +
						objectId + DELIMITER +
						currentDatetime + DELIMITER +
						postId + DELIMITER +
						createDate + DELIMITER + 
						authorId + DELIMITER +
						authorName + DELIMITER +
						permalink + DELIMITER +
						commentCount + DELIMITER +
						metooCount + DELIMITER +
						text + DELIMITER +
						text1 + DELIMITER +
						text2 + DELIMITER +
						tagText1 + DELIMITER +
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
				Set<Document> existDocs = indexSearcher.searchDocuments(FieldConstants.DOC_ID, postId);
				
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
						doc.setSite(TARGET_SITE_NAME);
						doc.setObject(objectId);
						doc.setLanguage(language);
						doc.setCollectDate(currentDatetime);
						doc.setDocId(postId);
						doc.setDate(createDate);
						doc.setUserId(authorId);
						doc.setUserName(authorName);						
						doc.setSubject(clause.getSubject());
						doc.setPredicate(clause.getPredicate());
						doc.setAttribute(clause.makeAttributesLabel());
						doc.setText(text);
						doc.setPolarity(polarity);
						doc.setPolarityStrength(polarityStrength);
						doc.setClausePolarity(clause.getPolarity());
						doc.setClausePolarityStrength(clause.getPolarityStrength());
						
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
		Me2dayDataCollector collector = new Me2dayDataCollector();
		collector.putMorphemeAnalyzer(Collector.LANG_KOREAN, new KoreanMorphemeAnalyzer());
		collector.putSemanticAnalyzer(Collector.LANG_KOREAN, new KoreanSemanticAnalyzer());
		collector.putSentimentAnalyzer(Collector.LANG_KOREAN, new SentimentAnalyzer(new File(Config.getProperty("LIWC_KOREAN"))));
		
		String objectId = "fta";
		Map<String, Integer> queryMap = new HashMap<String, Integer>();
		queryMap.put("한미FTA ISD", 5);
		queryMap.put("FTA ISD", 5);
		
//		String objectId = "naverline";
//		Map<String, Integer> queryMap = new HashMap<String, Integer>();
//		queryMap.put("네이버라인", 10);
//		queryMap.put("네이버LINE", 10);
		
		Date since = DateUtil.addDay(new Date(), -30);
		Date until = DateUtil.addDay(new Date(), +1);
		
		List<Post> posts = collector.searchPosts(queryMap, Me2dayCrawler.TARGET_BODY, since, until);
				
		try {
			collector.writeOutput("./bin/data/me2day/collect/", "./bin/data/me2day/index/", objectId, posts, new Date(), 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
