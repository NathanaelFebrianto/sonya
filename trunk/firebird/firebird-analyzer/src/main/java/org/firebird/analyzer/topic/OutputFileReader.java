/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.topic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.mahout.common.FileLineIterator;

/**
 * This class reads output data from output files.
 * dict.txt
 * topics.txt
 * users.txt
 * topic_users.txt
 * 
 * @author Young-Gue Bae
 */
public class OutputFileReader {
	
	private static final Pattern TAB_PATTERN = Pattern.compile("\t");
	private int websiteId = 1;
	
	/**
	 * Constructor.
	 * 
	 * @param websiteId the websiteId
	 */
	public OutputFileReader(int websiteId) {
		this.websiteId = websiteId;
	}
	
	/**
	 * Reads words in a dictionary file. Format is: First line is the number of entries.
	 * 
	 * @param dictFile the dictionary file
	 * @return List<Dictionary> the list of term in dictionary
	 * @exception
	 */
	public List<Dictionary> loadDictionary(File dictFile) throws IOException {
		List<Dictionary> result = new ArrayList<Dictionary>();
		
		InputStream is = new FileInputStream(dictFile);
		FileLineIterator it = new FileLineIterator(is);

		int numEntries = Integer.parseInt(it.next());
		System.out.println(numEntries);

		while (it.hasNext()) {
			String line = it.next();
			if (line.startsWith("#")) {
				continue;
			}
			String[] tokens = OutputFileReader.TAB_PATTERN.split(line);
			if (tokens.length < 3) {
				continue;
			}		
			
			String term = tokens[0];
			int docFreq = Integer.parseInt(tokens[1]);
			int seq = Integer.parseInt(tokens[2]);
			
			Dictionary dict = new Dictionary();
			dict.setWebsiteId(websiteId);
			dict.setSeq(seq);
			dict.setTerm(term);
			dict.setDocFreq(docFreq);
			
			result.add(dict);
		}
		return result;
	}

	/**
	 * Reads topic terms in a topic file. Format is: First line is the column header.
	 * 
	 * @param topicFile the topic file
	 * @return List<TopicTerm> the list of topic term
	 * @exception
	 */
	public List<TopicTerm> loadTopics(File topicFile) throws IOException {
		List<TopicTerm> result = new ArrayList<TopicTerm>();
		
		InputStream is = new FileInputStream(topicFile);
		FileLineIterator it = new FileLineIterator(is);

		while (it.hasNext()) {
			String line = it.next();
			if (line.startsWith("#")) {
				continue;
			}
			String[] tokens = OutputFileReader.TAB_PATTERN.split(line);
			if (tokens.length < 3) {
				continue;
			}		
			
			int topicId = Integer.parseInt(tokens[0]);
			String term = tokens[1];
			double score = Double.parseDouble(tokens[2]);
			
			TopicTerm topicTerm = new TopicTerm();
			topicTerm.setWebsiteId(websiteId);
			topicTerm.setTopicId(topicId);
			topicTerm.setTerm(term);
			topicTerm.setScore(score);
			
			result.add(topicTerm);
		}
		return result;
	}

	/**
	 * Reads user terms in a users file. Format is: First line is the column header.
	 * 
	 * @param usersFile the users file
	 * @return List<UserTerm> the list of user term
	 * @exception
	 */
	public List<UserTerm> loadUsersTerms(File usersFile) throws IOException {
		List<UserTerm> result = new ArrayList<UserTerm>();
		
		InputStream is = new FileInputStream(usersFile);
		FileLineIterator it = new FileLineIterator(is);

		while (it.hasNext()) {
			String line = it.next();
			if (line.startsWith("#")) {
				continue;
			}
			String[] tokens = OutputFileReader.TAB_PATTERN.split(line);
			if (tokens.length < 7) {
				continue;
			}
			
			String userId = tokens[0];
			int docId = Integer.parseInt(tokens[1]);
			String term = tokens[2];
			int termFreq = Integer.parseInt(tokens[3]);
			float tf = Float.parseFloat(tokens[4]);
			float idf = Float.parseFloat(tokens[5]);
			String timeline = tokens[6];
			
			UserTerm userTerm = new UserTerm();
			userTerm.setWebsiteId(websiteId);
			userTerm.setUserId(userId);
			userTerm.setDocId(docId);
			userTerm.setTerm(term);
			userTerm.setTermFreq(termFreq);
			userTerm.setTF(tf);
			userTerm.setIDF(idf);
			userTerm.setTimeline(timeline);
			
			result.add(userTerm);
		}
		return result;
	}
	
	/**
	 * Reads topic users in a topic users file. Format is: First line is the column header.
	 * 
	 * @param topicUsersFile the topic users file
	 * @return List<TopicUser> the list of topic user
	 * @exception
	 */
	public List<TopicUser> loadTopicUsers(File topicUsersFile) throws IOException {
		List<TopicUser> result = new ArrayList<TopicUser>();
		
		InputStream is = new FileInputStream(topicUsersFile);
		FileLineIterator it = new FileLineIterator(is);

		while (it.hasNext()) {
			String line = it.next();
			if (line.startsWith("#")) {
				continue;
			}
			String[] tokens = OutputFileReader.TAB_PATTERN.split(line);
			if (tokens.length < 7) {
				continue;
			}
			
			//#topic_id	user_id	user_match_terms_count	score	topic_terms	user_match_terms
			
			int topicId = Integer.parseInt(tokens[0]);
			String userId = tokens[1];
			int userMatchTermsCount = Integer.parseInt(tokens[2]);
			float score = Float.parseFloat(tokens[3]);
			String strTopicTerms = tokens[4];
			String strUserMatchTerms = tokens[5];
			
			TopicUser topicUser = new TopicUser();
			topicUser.setWebsiteId(websiteId);
			topicUser.setTopicId(topicId);
			topicUser.setUserId(userId);
			topicUser.setUserMatchTermsCount(userMatchTermsCount);
			topicUser.setScore(score);
			topicUser.setTopicTermsString(strTopicTerms);
			topicUser.setUserMatchTermsString(strUserMatchTerms);
			
			result.add(topicUser);
		}
		return result;
	}
	
}
