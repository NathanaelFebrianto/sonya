package com.nhn.socialanalytics.miner.opinion;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nhn.socialanalytics.miner.index.DetailDoc;
import com.nhn.socialanalytics.miner.index.DocIndexSearcher;
import com.nhn.socialanalytics.miner.index.FieldConstants;
import com.nhn.socialanalytics.miner.opinion.FeatureResultSet.FeatureSummary;
import com.nhn.socialanalytics.miner.opinion.OpinionFilter.BaseTermFilter;
import com.nhn.socialanalytics.miner.opinion.OpinionFilter.LinkedTermFilter;
import com.nhn.socialanalytics.nlp.sentiment.SentimentAnalyzer;

public class OpinionMiner {

	private DocIndexSearcher indexSearcher;
	
	public OpinionMiner(DocIndexSearcher indexSearcher) {
		this.indexSearcher = indexSearcher;
	}
	
	public OpinionResultSet getOpinionResultSet(OpinionFilter filter) throws OpinionException {
		if (filter == null) {
			throw new OpinionException("OpinionFilter is null!");
		}
		
		try {			
			BaseTermFilter baseTermFilter = filter.getBaseTermFilter();			
			OpinionResultSet resultSet = new OpinionResultSet(baseTermFilter.getField(), filter.isByFeature());			
			List<LinkedTermFilter> linkedTermFilters = filter.getLinkedTermFilters();
			
			List<OpinionTerm> baseTerms = indexSearcher.searchTerms(
					filter.getObject(), 
					filter.getLanguage(), 
					baseTermFilter.getField(), 
					baseTermFilter.getMinTF(), 
					baseTermFilter.isExcludeStopwords(),
					filter.isByFeature());
			
			for (OpinionTerm baseTerm : baseTerms) {
				System.out.println("--------------------------------------");
				for (LinkedTermFilter linkedTermFilter : linkedTermFilters) {
					List<OpinionTerm> linkedTerms = indexSearcher.searchLinkedTerms(
							filter.getObject(), 
							filter.getLanguage(), 
							baseTerm, 
							linkedTermFilter.getField(), 
							linkedTermFilter.getMinTF(), 
							linkedTermFilter.getLinkedMinTF(),
							filter.isByFeature());
					
					baseTerm.putLinkedTerms(linkedTermFilter.getField(), linkedTerms);					
				}
				resultSet.add(baseTerm);
				resultSet.addFeature(baseTerm.getFeature());
			}
			
			return resultSet;
		} catch (IOException e) {
			e.printStackTrace();
			throw new OpinionException(e.getMessage());			
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpinionException(e.getMessage());
		}
	}
	
	public FeatureResultSet getFeatureResultSet(OpinionFilter filter) throws OpinionException {
		if (filter == null) {
			throw new OpinionException("OpinionFilter is null!");
		}
		
		try {
			FeatureResultSet resultSet = new FeatureResultSet();
			
			Map<String, FeatureSummary> featureMap = new HashMap<String, FeatureSummary>();
			
			List<DetailDoc> docs = indexSearcher.searchFeatures(filter.getObject(), filter.getLanguage());
			
			for (DetailDoc doc : docs) {
				double polarity = doc.getPolarity();
				double polarityStrength = doc.getPolarityStrength();
				String featureName = "";
				String clauseFeature = doc.getClauseMainFeature();
				String docFeature = doc.getMainFeature();
				
				featureName = clauseFeature;
				
				/*
				if (clauseFeature != null && !clauseFeature.equals("") && !clauseFeature.equalsIgnoreCase("ETC"))
					featureName = clauseFeature;
				else
					featureName = docFeature;
				*/
				
				FeatureSummary featureSummary = featureMap.get(featureName);
				if (featureSummary == null) {
					featureSummary = resultSet.newFeatureSummary();
					featureSummary.setFeature(featureName);
					featureSummary.addDocNum();
					if (polarity > 0.0)
						featureSummary.addPositiveNum();
					else if (polarity < 0.0)
						featureSummary.addNegativeNum();
					
					featureSummary.addPolarity(polarity * polarityStrength);
					
					featureMap.put(featureName, featureSummary);
				}
				else {
					featureSummary.addDocNum();
					if (polarity > 0.0)
						featureSummary.addPositiveNum();
					else if (polarity < 0.0)
						featureSummary.addNegativeNum();
					
					featureSummary.addPolarity(polarity * polarityStrength);					
				}				
			}
			
			for (Map.Entry<String, FeatureSummary> entry : featureMap.entrySet()) {
				resultSet.add(entry.getValue());
			}
			
			return resultSet;
		} catch (IOException e) {
			e.printStackTrace();
			throw new OpinionException(e.getMessage());			
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpinionException(e.getMessage());
		}
	}
	
	public static void main(String[] args) {		
		try {	
			File[] indexDirs = new File[1];
			indexDirs[0] = new File("./bin/data/appstore/index/20111215");
			String object = "naverline";
			File liwcFile = new File("./bin/liwc/LIWC_ja.txt");
			DocIndexSearcher searcher = new DocIndexSearcher(indexDirs);
			searcher.putStopwordFile(new File("./conf/stopword_ja.txt"));
			searcher.putSentimentAnalyzer(FieldConstants.LANG_KOREAN, SentimentAnalyzer.getInstance(liwcFile));
			
			OpinionFilter filter = new OpinionFilter();
			filter.setObject(object);
			filter.setLanguage(FieldConstants.LANG_KOREAN);			
			filter.setBaseTermFilter(FieldConstants.SUBJECT, 5, true);
			filter.addLinkedTermFilter(FieldConstants.PREDICATE, 5, 1);
			filter.addLinkedTermFilter(FieldConstants.ATTRIBUTE, 5, 1);	
			filter.setByFeature(true);
			
			OpinionMiner miner = new OpinionMiner(searcher);
			OpinionResultSet ors = miner.getOpinionResultSet(filter);
			
			System.out.println("OpinionResultSet : baseTerms == " + ors.size());
			
			FeatureResultSet frs = miner.getFeatureResultSet(filter);
			
			int i = 0;
			for (FeatureSummary featureSummary : frs) {
				if (i == 0)	System.out.println(featureSummary.getHeaderText());				
				System.out.println(featureSummary.toString());
				i++;
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

