package com.nhn.socialanalytics.androidmarket.collect;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;

import com.gc.android.market.api.model.Market.Comment;
import com.nhn.socialanalytics.common.Config;
import com.nhn.socialanalytics.common.JobLogger;
import com.nhn.socialanalytics.common.collect.CollectHistoryBuffer;
import com.nhn.socialanalytics.common.collect.Collector;
import com.nhn.socialanalytics.common.util.DateUtil;
import com.nhn.socialanalytics.common.util.StringUtil;
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
	
	public void writeOutput(String dataDir, String indexDir, String objectId, 
			Map<Locale, List<Comment>> commentsMap, Date collectDate, int historyBufferMaxRound) throws Exception {		
		
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
					"locale" + DELIMITER +	
					"collect_date" + DELIMITER +
					"comment_id" + DELIMITER +	
					"create_date" + DELIMITER +	
					"author_id" + DELIMITER +		
					"author_name" + DELIMITER +	
					"rating" + DELIMITER +	
					"is_spam" + DELIMITER +						
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
				
				/////////////////////////////////
				// add new collected id into set
				idSet.add(commentId);
				/////////////////////////////////			
							
				// if no duplication, write collected data
				if (!history.checkDuplicate(commentId)) {
					boolean isSpam = super.isSpam(text);
					String textEmotiTagged = StringUtil.convertEmoticonToTag(text);
					
					String language = "";
					String text1 = "";
					String text2 = "";
					String feature = "";
					String mainFeature = "";
					SemanticSentence semanticSentence = null;
					double polarity = 0.0;
					double polarityStrength = 0.0;
					
					if (locale.equals(Locale.KOREA)) {
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
						/*
						String standardLabels = semanticSentence.extractStandardSubjectLabel() + " " +
								semanticSentence.extractStandardPredicateLabel() + " " +
								semanticSentence.extractStandardAttributesLabel();
						Map<String, Double> featureCounts = featureKorean.getFeatureCounts(text1, true);
						*/
						
						String standardLabels = semanticSentence.extractStandardLabel(" ", " ", true, false, false);
						Map<String, Double> featureCounts = featureKorean.getFeatureCounts(standardLabels, true);
						feature = featureKorean.getFeatureLabel(featureCounts);
						mainFeature = featureKorean.getMainFeatureLabel(featureCounts);
					}
					else if (locale.equals(Locale.JAPAN)) {
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
						text1 = morphemeKorean.extractTerms(textEmotiTagged);
						text2 = morphemeKorean.extractCoreTerms(textEmotiTagged);		
						
						semanticSentence = semanticKorean.analyze(textEmotiTagged);
						
						semanticSentence = sentimentKorean.analyzePolarity(semanticSentence);
						polarity = semanticSentence.getPolarity();
						polarityStrength = semanticSentence.getPolarityStrength();	
						
						// feature classification
						String standardLabels = semanticSentence.extractStandardLabel(" ", " ", true, false, false);
						Map<String, Double> featureCounts = featureKorean.getFeatureCounts(standardLabels, true);
						feature = featureKorean.getFeatureLabel(featureCounts);
						mainFeature = featureKorean.getMainFeatureLabel(featureCounts);
					}
										
					//String strClause = semanticSentence.extractStandardSubjectPredicateLabel();
					String strClause = semanticSentence.extractStandardLabel(",", "-", true, false, false);
					String subject = semanticSentence.extractStandardSubjectLabel();
					String predicate = semanticSentence.extractStandardPredicateLabel();
					String attribute = semanticSentence.extractStandardAttributesLabel();	
					String modifier = semanticSentence.extractStandardModifiersLabel();
				
					// write new collected data into source file
					brData.write(
							TARGET_SITE_NAME + DELIMITER +
							objectId + DELIMITER +
							locale + DELIMITER +
							currentDatetime + DELIMITER +
							commentId + DELIMITER +
							createDate + DELIMITER + 
							authorId + DELIMITER +		
							authorName + DELIMITER +
							rating + DELIMITER +
							isSpam + DELIMITER +							
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
					if (!isSpam) {
						Set<Document> existDocs = indexSearcher.searchDocuments(FieldConstants.DOC_ID, commentId);
						
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
								doc.setDocId(commentId);
								doc.setDate(createDate);
								doc.setAuthorId(authorId);
								doc.setAuthorName(authorName);	
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
			}
		}
			
		brData.close();
		indexWriter.close();		
		history.writeCollectHistory(idSet);
	}
	
	public static void main(String[] args) {
		
		String loginAccount = args[0];
		String loginPasswd = args[1];
		AndroidMarketDataCollector collector = new AndroidMarketDataCollector(loginAccount, loginPasswd);
		collector.setSpamFilter(new File(Config.getProperty("COLLECT_SPAM_FILTER_ANDROIDMARKET")));		
		collector.putMorphemeAnalyzer(Collector.LANG_KOREAN, new KoreanMorphemeAnalyzer());
		collector.putMorphemeAnalyzer(Collector.LANG_JAPANESE, new JapaneseMorphemeAnalyzer());
		collector.putSemanticAnalyzer(Collector.LANG_KOREAN, new KoreanSemanticAnalyzer());
		collector.putSemanticAnalyzer(Collector.LANG_JAPANESE, new JapaneseSemanticAnalyzer());
		collector.putSentimentAnalyzer(Collector.LANG_KOREAN, new SentimentAnalyzer(new File(Config.getProperty("LIWC_KOREAN"))));
		collector.putSentimentAnalyzer(Collector.LANG_JAPANESE, new SentimentAnalyzer(new File(Config.getProperty("LIWC_JAPANESE"))));
		
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
		
		collector.putFeatureClassifier(objectId, Collector.LANG_KOREAN, new FeatureClassifier(new File(Config.getProperty("DEFAULT_FEATURE_KOREAN"))));
		collector.putFeatureClassifier(objectId, Collector.LANG_JAPANESE, new FeatureClassifier(new File(Config.getProperty("DEFAULT_FEATURE_JAPANESE"))));
		
		//List<Comment> comments = collector.getAppComments(locales, appId, 2);
		Map<Locale, List<Comment>> commentsMap = collector.getAppCommentsByLocales(locales, appId, 20);
		try {
			collector.writeOutput("./bin/data/androidmarket/collect/", "./bin/data/androidmarket/index/", objectId, commentsMap, new Date(), 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
