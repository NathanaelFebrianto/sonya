/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.topic;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.firebird.analyzer.util.JobLogger;
import org.firebird.analyzer.util.OutputFileReader;
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
	/** logger */
	private static JobLogger logger = JobLogger.getLogger(TopicAnalysisJob.class);
	
	/** configuration */
	private int websiteId;
	private String sourceDocDir;
	private String indexDir;
	private String vectorFile;
	private String dictFile;
	private String ldaDir;
	private String topicsDir;
	private String topicsFile;
	private String usersFile;
	private String topicUsersFile;
	
	/**
	 * Constructor.
	 * 
	 */
	public TopicAnalysisJob() { 
		this.websiteId = 1;
		this.sourceDocDir = "D:/firebird/text/";
		this.indexDir = "D:/firebird/index/";
		this.vectorFile = "D:/firebird/vectors";
		this.dictFile = "D:/firebird/dict.txt";
		this.ldaDir = "D:/firebird/lda/";
		this.topicsDir = "D:/firebird/topics/";
		this.topicsFile = "D:/firebird/topics.txt";
		this.usersFile = "D:/firebird/users.txt";
		this.topicUsersFile = "D:/firebird/topic_users.txt";
	}
	
	/**
	 *  Writes the source document files.
	 */
	private void writeDocSource() throws Exception {
		DocSourceWriter sourceWriter = new DocSourceWriter(sourceDocDir);
		
    	UserBlogEntryManager userBlogEntryManager = new UserBlogEntryManagerImpl();
    	List<String> users = userBlogEntryManager.getDistinctUsers(websiteId);
    	
    	for (int i = 0; i < users.size(); i++) {
    		String userId = (String) users.get(i);
    		List<UserBlogEntry> userBlogEntries = userBlogEntryManager.getUserBlogEntries(websiteId, userId);
    		sourceWriter.write(userId, userBlogEntries);
    	}		
	}
	
	/**
	 *  Writes the index files from source document files.
	 */
	private void writeIndex() throws Exception {
    	DocIndexWriter indexWriter = new DocIndexWriter();
		indexWriter.write(sourceDocDir, indexDir);		
	}
	
	/**
	 *  Writes the vectors from index.
	 */
	private void writeVector() throws Exception {
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
					 30,				// minDF
					 70);				// maxDFPercent
	}
	
	/**
	 *  Runs LDA analyzer.
	 */
	private void runLDAAnalyzer(int numTopics,
							    int topicSmoothing, 
							    int maxIter, 
							    int numReducers) throws Exception {
		LDAAnalyzer ldaAnalyzer = new LDAAnalyzer();
		ldaAnalyzer.analyze(
					vectorFile,			// input
					ldaDir, 			// output
					true, 				// overwrite
					numTopics, 			// numTopics
					2000, 				// numWords
					topicSmoothing, 	// topicSmoothing
					maxIter, 			// maxIter
					numReducers);		// numReducers
	}
	
	/**
	 *  Prints and writes the LDA topics.
	 */
	private void writeLDATopics(int maxIter, int numTopicWords) throws Exception {
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
	}
	
	/**
	 *  Searches the users and write the users by topics.
	 */
	private List<String> writeUsers(int maxIter, int numTopicWords) throws Exception {
		LDATopics ldaTopics = new LDATopics();
		List<String> topicTerms = ldaTopics.getUniqueTerms(
				ldaDir + "state-" + maxIter,	// input
				dictFile, 						// dictFile
				numTopicWords, 					// numWords
				null);							// dictType
	
		DocIndexSearcher docSearcher = new DocIndexSearcher(indexDir);
		docSearcher.write(usersFile, topicTerms);
		
		return topicTerms;
	}
	
	/**
	 *  Writes the topic-users.
	 */
	private void writeTopicUsers(int maxIter, int numTopicWords, List<String> topicTerms) throws Exception {
		LDATopics ldaTopics = new LDATopics();
		Map<Integer, List<TopicTerm>> mapTopics = ldaTopics.getTopics(
				ldaDir + "state-" + maxIter,	// input
				dictFile, 						// dictFile
				numTopicWords, 					// numWords
				null); 							// dictType
		
		DocIndexSearcher docSearcher = new DocIndexSearcher(indexDir);
		Map<String, List<UserTerm>> mapUsers = docSearcher.searchUsers(topicTerms);

		TopicUserWriter topicUserWriter = new TopicUserWriter();
		topicUserWriter.write(topicUsersFile, mapTopics, mapUsers);
	}
	
	/**
	 *  Stores the output into database.
	 */
	private void storeToDatabase() throws Exception {
		OutputFileReader outputReader = new OutputFileReader(websiteId);
		List<Dictionary> dictList = outputReader.loadDictionary(dictFile);
		List<TopicTerm> topicList = outputReader.loadTopics(topicsFile);
		List<UserTerm> userList = outputReader.loadUsersTerms(usersFile);
		List<TopicUser> topicUserList = outputReader.loadTopicUsers(topicUsersFile);
		
		this.storeDictionary(websiteId, dictList);
		this.storeTopics(websiteId, topicList);
		this.storeUserTerms(websiteId, userList);
		this.storeTopicUsers(websiteId, topicUserList);
	}
	
	private void storeDictionary(int websiteId, List<Dictionary> dictList) throws Exception {
		logger.info("storeDictionary.............");
		
		DictionaryManager dictManager = new DictionaryManagerImpl();
		dictManager.deleteDictionary(websiteId);
		
		for (Dictionary dictTerm : dictList) {
			dictManager.addTerm(dictTerm);
		}		
	}
	
	private void storeTopics(int websiteId, List<TopicTerm> topicList) throws Exception {
		logger.info("storeTopics.............");
		
		TopicTermManager topicManager = new TopicTermManagerImpl();
		topicManager.deleteTopics(websiteId);
		
		for (TopicTerm topicTerm : topicList) {
			topicManager.addTerm(topicTerm);
		}		
	}
	
	private void storeUserTerms(int websiteId, List<UserTerm> userList) throws Exception {
		logger.info("storeUserTerms.............");
		
		UserTermManager userManager = new UserTermManagerImpl();
		userManager.deleteUsers(websiteId);
		
		for (UserTerm userTerm : userList) {
			userManager.addTerm(userTerm);
		}		
	}
	
	private void storeTopicUsers(int websiteId, List<TopicUser> topicUserList) throws Exception {
		logger.info("storeTopicUsers.............");
		
		TopicUserManager topicUserManager = new TopicUserManagerImpl();
		topicUserManager.deleteUsers(websiteId);
		
		for (TopicUser topicUser : topicUserList) {
			topicUserManager.addUser(topicUser);
		}		
	}
	
	public static void main(String[] args) {
    	try {    		
    		TopicAnalysisJob job = new TopicAnalysisJob();
    		
    		// start time
    		Date startTime = new Date();
    		logger.info("\n\n******************************************");
    		logger.info("Start Topic Analysis : " + startTime);
    		
    		// 1. write source document files
    		job.writeDocSource();
        	
    		// 2. write index files from source document files
    		job.writeIndex();
        	
        	// 3. write vectors from index
    		job.writeVector();
    		
    		// 4. run LDA analyzer
    		int numTopics = 10;
    		int topicSmoothing = -1;
    		int maxIter = 40;	//maxIter = 40
    		int numReducers = 2;
    		job.runLDAAnalyzer(numTopics, topicSmoothing, maxIter, numReducers);
   		
    		// 5. print and write LDA topics
    		int numTopicWords = 50;
    		job.writeLDATopics(maxIter, numTopicWords);
 
    		// 6. search users and write users by topics
    		List<String> topicTerms = job.writeUsers(maxIter, numTopicWords);
    		
    		// 7. write topic-user
    		job.writeTopicUsers(maxIter, numTopicWords, topicTerms);

    		// end time
    		Date endTime = new Date();
    		logger.info("Finish Topic Analysis : " + endTime);
    		logger.jobSummary("Topic Analysis", startTime, endTime);
    		
    		// 8. store to database
    		logger.info("Start to store into database:");
    		job.storeToDatabase();
    		logger.info("Completed to store into database!"); 
    		
     	} catch (Exception e) {
        	e.printStackTrace();
        	logger.error(e.getMessage(), e);
        }  
	}
}
