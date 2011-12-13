package com.nhn.socialanalytics.miner.opinion;

import com.nhn.socialanalytics.miner.index.DocIndexSearcher;

public class OpinionMiner {

	private DocIndexSearcher indexSearcher;
	
	public OpinionMiner(DocIndexSearcher indexSearcher) {
		this.indexSearcher = indexSearcher;
	}
	
}
