/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.topic;

import java.util.List;
import java.util.Map;

import org.firebird.io.model.Dictionary;
import org.firebird.io.model.TopicTerm;
import org.firebird.io.model.TopicUser;
import org.firebird.io.model.UserBlogEntry;
import org.firebird.io.model.UserTerm;
import org.firebird.io.service.DictionaryManager;
import org.firebird.io.service.TopicTermManager;
import org.firebird.io.service.TopicUserManager;
import org.firebird.io.service.UserBlogEntryManager;
import org.firebird.io.service.UserTermManager;
import org.firebird.io.service.impl.DictionaryManagerImpl;
import org.firebird.io.service.impl.TopicTermManagerImpl;
import org.firebird.io.service.impl.TopicUserManagerImpl;
import org.firebird.io.service.impl.UserBlogEntryManagerImpl;
import org.firebird.io.service.impl.UserTermManagerImpl;

/**
 * This class is a topic analysis job.
 * 1. write source document files
 * 2. write index files from source document files
 * 3. write vectors from index
 * 4. run LDA analyzer
 * 5. print LDA topics
 * 
 * @author Young-Gue Bae
 */
public class TopicAnalysisJob {

	public static void main(String[] args) {
    	try {    		
    		int websiteId = 1;
    		
    		String sourceDocDir = "D:/firebird/text/";
    		String indexDir = "D:/firebird/index/";
    		String vectorFile = "D:/firebird/vectors";
    		String dictFile = "D:/firebird/dict.txt";
    		String ldaDir = "D:/firebird/lda/";
    		String topicsDir = "D:/firebird/topics/";
    		String topicsFile = "D:/firebird/topics.txt";
    		String usersFile = "D:/firebird/users.txt";
    		String topicUsersFile = "D:/firebird/topic_users.txt";
    		
    		// 1. write source document files
    		DocSourceWriter sourceWriter = new DocSourceWriter(sourceDocDir);
    		
        	UserBlogEntryManager userBlogEntryManager = new UserBlogEntryManagerImpl();
        	List<String> users = userBlogEntryManager.getDistinctUsers(websiteId);
        	
        	for (int i = 0; i < users.size(); i++) {
        		String userId = (String) users.get(i);
        		List<UserBlogEntry> userBlogEntries = userBlogEntryManager.getUserBlogEntries(websiteId, userId);
        		sourceWriter.write(userId, userBlogEntries);
        	}
        	
    		// 2. write index files from source document files
        	DocIndexWriter indexWriter = new DocIndexWriter();
    		indexWriter.write(sourceDocDir, indexDir);
        	
        	// 3. write vectors from index
    		DocVectorWriter vectorWriter = new DocVectorWriter();
    		vectorWriter.write(
    					 indexDir,			// inputDir
    					 vectorFile, 		// outputFile
    					 "body",			// field
    					 null,				// idField
    					 dictFile,			// dictOut
    					 null,				// weightOpt
    					 null,				// delimiter
    					 null,				// power
    					 Long.MAX_VALUE,	// max
    					 null,				// outputWriter
    					 5,					// minDF
    					 70);				// maxDFPercent
    		
    		// 4. run LDA analyzer
    		int numTopics = 10;
    		int numTopicWords = 50;
    		int topicSmoothing = -1;
    		int maxIter = 40;
    		int numReducers = 2;
    		
    		LDAAnalyzer ldaAnalyzer = new LDAAnalyzer();
    		ldaAnalyzer.analyze(
    					vectorFile,			// input
    					ldaDir, 			// output
    					true, 				// overwrite
    					numTopics, 			// numTopics
    					1000, 				// numWords
    					topicSmoothing, 	// topicSmoothing
    					maxIter, 			// maxIter
    					numReducers);		// numReducers
   		
    		// 5. print and write LDA topics
    		LDATopics ldaTopics = new LDATopics();
    		ldaTopics.writeEachTopics(
    					ldaDir + "state-" + maxIter,	// input
    					dictFile, 						// dictFile
    					topicsDir, 						// output
    					numTopicWords, 					// numWords
    					null);							// dictType
    		ldaTopics.writeTopics(
    					ldaDir + "state-" + maxIter,	// input
    					dictFile, 						// dictFile
    					topicsFile, 					// output
    					numTopicWords, 					// numWords
						null);							// dictType
 
    		// 6. search users and write users by topics
    		List<String> topicTerms = ldaTopics.getUniqueTerms(
    					ldaDir + "state-" + maxIter,	// input
    					dictFile, 						// dictFile
    					numTopicWords, 					// numWords
    					null);							// dictType
    		
    		DocIndexSearcher docSearcher = new DocIndexSearcher(indexDir);
    		docSearcher.write(usersFile, topicTerms);
    		
    		// 7. write topic-user
    		Map<Integer, List<TopicTerm>> mapTopics = ldaTopics.getTopics(
    					ldaDir + "state-" + maxIter,	// input
    					dictFile, 						// dictFile
    					numTopicWords, 					// numWords
    					null);							// dictType
    		Map<String, List<UserTerm>> mapUsers = docSearcher.searchUsers(topicTerms);
    		
    		TopicUserWriter topicUserWriter = new TopicUserWriter();
    		topicUserWriter.write(
    					topicUsersFile, 
    					mapTopics, 
    					mapUsers);
    		
    		// 8. store to database
    		System.out.println("Start to store into database:");
    		OutputFileReader outputReader = new OutputFileReader(websiteId);
    		List<Dictionary> dictList = outputReader.loadDictionary(dictFile);
    		List<TopicTerm> topicList = outputReader.loadTopics(topicsFile);
    		List<UserTerm> userList = outputReader.loadUsersTerms(usersFile);
    		List<TopicUser> topicUserList = outputReader.loadTopicUsers(topicUsersFile);
    		
    		storeDictionary(websiteId, dictList);
    		storeTopics(websiteId, topicList);
    		storeUserTerms(websiteId, userList);
    		storeTopicUsers(websiteId, topicUserList);
    		System.out.println("Completed to store into database!");
   		
     	} catch (Exception e) {
        	e.printStackTrace();
        }  
	}
	
	private static void storeDictionary(int websiteId, List<Dictionary> dictList) throws Exception {
		System.out.println("storeDictionary.............");
		
		DictionaryManager dictManager = new DictionaryManagerImpl();
		dictManager.deleteDictionary(websiteId);
		
		for (Dictionary dictTerm : dictList) {
			dictManager.addTerm(dictTerm);
		}		
	}
	
	private static void storeTopics(int websiteId, List<TopicTerm> topicList) throws Exception {
		System.out.println("storeTopics.............");
		
		TopicTermManager topicManager = new TopicTermManagerImpl();
		topicManager.deleteTopics(websiteId);
		
		for (TopicTerm topicTerm : topicList) {
			topicManager.addTerm(topicTerm);
		}		
	}
	
	private static void storeUserTerms(int websiteId, List<UserTerm> userList) throws Exception {
		System.out.println("storeUserTerms.............");
		
		UserTermManager userManager = new UserTermManagerImpl();
		userManager.deleteUsers(websiteId);
		
		for (UserTerm userTerm : userList) {
			userManager.addTerm(userTerm);
		}		
	}
	
	private static void storeTopicUsers(int websiteId, List<TopicUser> topicUserList) throws Exception {
		System.out.println("storeTopicUsers.............");
		
		TopicUserManager topicUserManager = new TopicUserManagerImpl();
		topicUserManager.deleteUsers(websiteId);
		
		for (TopicUser topicUser : topicUserList) {
			topicUserManager.addUser(topicUser);
		}		
	}

}
