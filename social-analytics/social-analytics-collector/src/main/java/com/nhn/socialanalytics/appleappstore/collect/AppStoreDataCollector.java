package com.nhn.socialanalytics.appleappstore.collect;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.Document;

import com.nhn.socialanalytics.appleappstore.model.Review;
import com.nhn.socialanalytics.common.Collector;
import com.nhn.socialanalytics.common.JobLogger;
import com.nhn.socialanalytics.common.util.DateUtil;
import com.nhn.socialanalytics.common.util.StringUtil;
import com.nhn.socialanalytics.miner.index.DetailDoc;
import com.nhn.socialanalytics.miner.index.DocIndexSearcher;
import com.nhn.socialanalytics.miner.index.DocIndexWriter;
import com.nhn.socialanalytics.miner.index.FieldConstants;
import com.nhn.socialanalytics.nlp.kr.morpheme.MorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.kr.semantic.SemanticAnalyzer;
import com.nhn.socialanalytics.nlp.kr.semantic.SemanticClause;
import com.nhn.socialanalytics.nlp.kr.semantic.SemanticSentence;
import com.nhn.socialanalytics.nlp.kr.sentiment.SentimentAnalyzer;


public class AppStoreDataCollector extends Collector {
	
	private static JobLogger logger = JobLogger.getLogger(AppStoreDataCollector.class, "appstore-collect.log");
	private AppStoreCrawler crawler;	

	public AppStoreDataCollector() {
		this(null);
	}
	
	public AppStoreDataCollector(File spamFilterFile) {
		super(spamFilterFile);
		crawler = new AppStoreCrawler();
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
	
	public void writeOutput(String dataDir, String indexDir, String liwcCatFile, 
			String objectId, List<Review> reviews, Date collectDate) throws IOException, Exception {
		
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
					"review_id" + DELIMITER +	
					"create_date" + DELIMITER +	
					"author_id" + DELIMITER +		
					"author_name" + DELIMITER +	
					"topic" + DELIMITER +	
					"version" + DELIMITER +	
					"rating" + DELIMITER +	
					"is_spam" + DELIMITER +	
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
		
		// review
		for (Review review : reviews) {
			String reviewId = review.getReviewId();
			String authorId = review.getAuthorId();
			String authorName = review.getAuthorName();
			String topic = review.getTopic();
			String text = review.getText();
			String version = review.getVersion();
			String createDate = review.getCreateDate();
			int rating = review.getRating();
			
			/////////////////////////////////
			// write new collected all id into file
			brCollectedIdSet.write(reviewId);
			brCollectedIdSet.newLine();
			/////////////////////////////////			
						
			// if no duplication, write collected data
			if (!prevColIdSet.contains(reviewId)) {
				boolean isSpam = super.isSpam(text);
				String textEmotiTagged = StringUtil.convertEmoticonToTag(text);
				
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
						"appstore" + DELIMITER +
						objectId + DELIMITER +
						currentDatetime + DELIMITER +
						reviewId + DELIMITER +
						createDate + DELIMITER + 
						authorId + DELIMITER +		
						authorName + DELIMITER +	
						topic + DELIMITER +
						version + DELIMITER +
						rating + DELIMITER +
						isSpam + DELIMITER +
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
				if (!isSpam) {
					Set<Document> existDocs = indexSearcher.searchDocuments(FieldConstants.DOC_ID, reviewId);
					
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
							doc.setSite("appstore");
							doc.setObject(objectId);
							doc.setCollectDate(currentDatetime);
							doc.setDocId(reviewId);
							doc.setDate(createDate);
							doc.setUserId(authorId);
							doc.setUserName(authorName);
							doc.setSubject(clause.getSubject());
							doc.setPredicate(clause.getPredicate());
							doc.setAttribute(clause.makeAttributesLabel());
							doc.setText(text);
							
							indexWriter.write(doc);
						}						
					}					
				}			
			}		
		}
		
		brData.close();
		brCollectedIdSet.close();		
		indexWriter.close();
	}
	
	public static void main(String[] args) {
		AppStoreDataCollector collector = new AppStoreDataCollector();
		
		Set<String> appStores = new HashSet<String>();
		appStores.add(AppStores.getAppStore("Korea"));
		appStores.add(AppStores.getAppStore("Australia"));
		
		String objectId = "naverline";
		String appId = "443904275";
		
		List<Review> reviews = collector.getReviews(appStores, appId, 2);
		
		try {
			collector.writeOutput("./bin/data/appstore/collect/", "./bin/data/appstore/index/", "./bin/liwc/LIWC_ko.txt", objectId, reviews, new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
