package com.nhn.socialanalytics.opinion.search;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nhn.socialanalytics.nlp.sentiment.SentimentAnalyzer;
import com.nhn.socialanalytics.opinion.common.FieldConstants;
import com.nhn.socialanalytics.opinion.common.OpinionDocument;
import com.nhn.socialanalytics.opinion.common.OpinionTerm;
import com.nhn.socialanalytics.opinion.dao.lucene.DocIndexSearcher;
import com.nhn.socialanalytics.opinion.search.FeatureResultSet.FeatureSummary;
import com.nhn.socialanalytics.opinion.search.OpinionFilter.BaseTermFilter;
import com.nhn.socialanalytics.opinion.search.OpinionFilter.LinkedTermFilter;

public class OpinionSearcher {

	private DocIndexSearcher indexSearcher;
	
	public OpinionSearcher(DocIndexSearcher indexSearcher) {
		this.indexSearcher = indexSearcher;
	}
	
	public OpinionResultSet getOpinionResultSet(OpinionFilter filter) throws OpinionSearchException {
		if (filter == null) {
			throw new OpinionSearchException("OpinionFilter is null!");
		}
		
		try {			
			BaseTermFilter baseTermFilter = filter.getBaseTermFilter();			
			OpinionResultSet resultSet = new OpinionResultSet(baseTermFilter.getField(), filter.isByFeature());	
			resultSet.setObject(filter.getObject());
			resultSet.setLanguage(filter.getLanguage());
			
			List<LinkedTermFilter> linkedTermFilters = filter.getLinkedTermFilters();
			
			List<OpinionTerm> baseTerms = indexSearcher.searchTerms(
					filter.getObject(), 
					filter.getLanguage(), 
					baseTermFilter.getField(), 
					baseTermFilter.getMinDocFreq(), 
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
							linkedTermFilter.getMinDocFreq(), 
							linkedTermFilter.getMinCooccurrentDocFreq(),
							filter.isByFeature());
					
					baseTerm.putLinkedTerms(linkedTermFilter.getField(), linkedTerms);					
				}
				resultSet.add(baseTerm);
				resultSet.addFeature(baseTerm.getFeature());
			}
			
			return resultSet;
		} catch (IOException e) {
			e.printStackTrace();
			throw new OpinionSearchException(e.getMessage());			
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpinionSearchException(e.getMessage());
		}
	}
	
	public FeatureResultSet getFeatureResultSet(OpinionFilter filter) throws OpinionSearchException {
		if (filter == null) {
			throw new OpinionSearchException("OpinionFilter is null!");
		}
		
		try {
			FeatureResultSet resultSet = new FeatureResultSet();
			resultSet.setObject(filter.getObject());
			resultSet.setLanguage(filter.getLanguage());
			
			Map<String, FeatureSummary> featureMap = new HashMap<String, FeatureSummary>();
			
			List<OpinionDocument> docs = indexSearcher.searchFeatures(filter.getObject(), filter.getLanguage());
			
			for (OpinionDocument doc : docs) {
				double docPolarity = doc.getDocPolarity();
				double docPolarityStrength = doc.getDocPolarityStrength();
				String featureName = "";
				String clauseFeature = doc.getClauseMainFeature();
				String docFeature = doc.getDocMainFeature();
				
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
					if (docPolarity > 0.0)
						featureSummary.addPositiveNum();
					else if (docPolarity < 0.0)
						featureSummary.addNegativeNum();
					
					featureSummary.addPolarity(docPolarity * docPolarityStrength);
					
					featureMap.put(featureName, featureSummary);
				}
				else {
					featureSummary.addDocNum();
					if (docPolarity > 0.0)
						featureSummary.addPositiveNum();
					else if (docPolarity < 0.0)
						featureSummary.addNegativeNum();
					
					featureSummary.addPolarity(docPolarity * docPolarityStrength);					
				}				
			}
			
			for (Map.Entry<String, FeatureSummary> entry : featureMap.entrySet()) {
				resultSet.add(entry.getValue());
			}
			
			return resultSet;
		} catch (IOException e) {
			e.printStackTrace();
			throw new OpinionSearchException(e.getMessage());			
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpinionSearchException(e.getMessage());
		}
	}
	
	public static void main(String[] args) {		
		try {	
			File[] indexDirs = new File[1];
			indexDirs[0] = new File("./bin/data/appstore/index/20111219");
			String object = "naverline";
			File liwcFile = new File("./bin/liwc/LIWC_ja.txt");
			DocIndexSearcher indexSearcher = new DocIndexSearcher(indexDirs);
			indexSearcher.putStopwordFile(new File("./conf/stopword_ja.txt"));
			indexSearcher.putSentimentAnalyzer(FieldConstants.LANG_KOREAN, SentimentAnalyzer.getInstance(liwcFile));
			
			OpinionFilter filter = new OpinionFilter();
			filter.setObject(object);
			filter.setLanguage(FieldConstants.LANG_KOREAN);			
			filter.setBaseTermFilter(FieldConstants.SUBJECT, 5, true);
			filter.addLinkedTermFilter(FieldConstants.PREDICATE, 5, 1);
			filter.addLinkedTermFilter(FieldConstants.ATTRIBUTE, 5, 1);	
			filter.setByFeature(true);
			
			OpinionSearcher opinionSearcher = new OpinionSearcher(indexSearcher);
			OpinionResultSet ors = opinionSearcher.getOpinionResultSet(filter);
			
			System.out.println("OpinionResultSet : baseTerms == " + ors.size());
			
			FeatureResultSet frs = opinionSearcher.getFeatureResultSet(filter);
			
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

