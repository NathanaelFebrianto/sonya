package com.nhn.socialanalytics.miner.opinion;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.nhn.socialanalytics.miner.index.DocIndexSearcher;
import com.nhn.socialanalytics.miner.index.FieldConstants;
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
			OpinionResultSet resultSet = new OpinionResultSet(baseTermFilter.getField());			
			List<LinkedTermFilter> linkedTermFilters = filter.getLinkedTermFilters();
			
			List<OpinionTerm> baseTerms = indexSearcher.searchTerms(
					filter.getObject(), 
					filter.getLanguage(), 
					baseTermFilter.getField(), 
					baseTermFilter.getMinTF(), 
					baseTermFilter.isExcludeStopwords());
			
			for (OpinionTerm baseTerm : baseTerms) {
				System.out.println("--------------------------------------");
				for (LinkedTermFilter linkedTermFilter : linkedTermFilters) {
					List<OpinionTerm> linkedTerms = indexSearcher.searchLinkedTerms(
							filter.getObject(), 
							filter.getLanguage(), 
							baseTerm, 
							linkedTermFilter.getField(), 
							linkedTermFilter.getMinTF(), 
							linkedTermFilter.getLinkedMinTF());
					
					baseTerm.putLinkedTerms(linkedTermFilter.getField(), linkedTerms);					
				}
				resultSet.add(baseTerm);
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
			indexDirs[0] = new File("./bin/data/appstore/index/20111214");
			String object = "naverline";
			File liwcFile = new File("./bin/liwc/LIWC_ja.txt");
			DocIndexSearcher searcher = new DocIndexSearcher(indexDirs);
			searcher.putStopwordFile(new File("./conf/stopword_ja.txt"));
			searcher.putSentimentAnalyzer(FieldConstants.LANG_JAPANESE, SentimentAnalyzer.getInstance(liwcFile));
			
			OpinionFilter filter = new OpinionFilter();
			filter.setObject(object);
			filter.setLanguage(FieldConstants.LANG_JAPANESE);			
			filter.setBaseTermFilter(FieldConstants.SUBJECT, 5, true);
			filter.addLinkedTermFilter(FieldConstants.PREDICATE, 5, 1);
			filter.addLinkedTermFilter(FieldConstants.ATTRIBUTE, 5, 1);			
			
			OpinionMiner miner = new OpinionMiner(searcher);
			OpinionResultSet ors = miner.getOpinionResultSet(filter);
			
			System.out.println("OpinionResultSet : baseTerms == " + ors.size());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

