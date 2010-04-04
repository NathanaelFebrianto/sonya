/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.text;

import java.util.List;
import java.util.Map;

import org.firebird.io.model.UserBlogEntry;
import org.firebird.io.service.UserBlogEntryManager;
import org.firebird.io.service.impl.UserBlogEntryManagerImpl;

/**
 * This class is a text analysis job.
 * 1. write source document files
 * 2. write index files from source document files
 * 3. write vectors from index
 * 4. run LDA analyzer
 * 5. print LDA topics
 * 
 * @author Young-Gue Bae
 */
public class TextAnalysisJob {

	public static void main(String[] args) {
    	try {    		

    		// 1. write source document files
    		DocSourceWriter sourceWriter = new DocSourceWriter("D:/firebird/text/");
    		
        	UserBlogEntryManager userBlogEntryManager = new UserBlogEntryManagerImpl();
        	List<String> users = userBlogEntryManager.getDistinctUsers();
        	
        	for (int i = 0; i < users.size(); i++) {
        		String userId = (String) users.get(i);
        		List<UserBlogEntry> userBlogEntries = userBlogEntryManager.getUserBlogEntries(userId);
        		sourceWriter.write(userId, userBlogEntries);
        	}
        	
    		// 2. write index files from source document files
        	DocIndexWriter indexWriter = new DocIndexWriter();
    		indexWriter.write("D:/firebird/text/", "D:/firebird/index/");
        	
        	// 3. write vectors from index
    		DocVectorWriter vectorWriter = new DocVectorWriter();
    		vectorWriter.write("D:/firebird/index/",	// inputDir
    					 "D:/firebird/vectors", 		// outputFile
    					 "body",						// field
    					 null,							// idField
    					 "D:/firebird/dict.txt",		// dictOut
    					 null,							// weightOpt
    					 null,							// delimiter
    					 null,							// power
    					 Long.MAX_VALUE,				// max
    					 null,							// outputWriter
    					 5,								// minDF
    					 70);							// maxDFPercent
    		
    		// 4. run LDA analyzer
    		LDAAnalyzer ldaAnalyzer = new LDAAnalyzer();
    		ldaAnalyzer.analyze("D:/firebird/vectors",	// input
    					"D:/firebird/lda/", 			// output
    					true, 							// overwrite
    					10, 							// numTopics
    					10000, 							// numWords
    					-1, 							// topicSmoothing
    					40, 							// maxIter
    					2);								// numReducers
    		
    		// 5. print and write LDA topics
    		LDATopics ldaTopics = new LDATopics();
    		ldaTopics.writeEachTopics("D:/firebird/lda/state-40",	// input
    					"D:/firebird/dict.txt", 				// dictFile
    					"D:/firebird/topics/", 					// output
    					50, 									// numWords
    					null);									// dictType
    		ldaTopics.writeTopics("D:/firebird/lda/state-40",	// input
						"D:/firebird/dict.txt", 				// dictFile
						"D:/firebird/topics.txt", 				// output
						50, 									// numWords
						null);									// dictType
 
    		// 6. search users and write users by topics
    		List<String> topicWords = ldaTopics.getUniqueWords("D:/firebird/lda/state-40",// input
					"D:/firebird/dict.txt", 					// dictFile
					50, 										// numWords
					null);										// dictType
    		
    		DocIndexSearcher docSearcher = new DocIndexSearcher("D:/firebird/index/");
    		docSearcher.write("D:/firebird/users.txt", topicWords);
    		
    		// 7. write topic-user
    		Map<Integer, List<TopicWord>> mapTopics = ldaTopics.getTopics("D:/firebird/lda/state-40",// input
								"D:/firebird/dict.txt", 		// dictFile
								50, 							// numWords
								null);							// dictType
    		Map<String, List<UserWord>> mapUsers = docSearcher.searchUsers(topicWords);
    		
    		TopicUserWriter topicUserWriter = new TopicUserWriter();
    		topicUserWriter.write("D:/firebird/topic_users.txt", 
    							  mapTopics, 
    							  mapUsers);
    		
     	} catch (Exception e) {
        	e.printStackTrace();
        }  
	}

}
