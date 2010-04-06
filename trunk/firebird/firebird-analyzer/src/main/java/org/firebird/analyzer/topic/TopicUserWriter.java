/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.topic;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * This class generates the topic-users set.
 * 
 * @author Young-Gue Bae
 */
public class TopicUserWriter {

	/**
	 * Constructor.
	 * 
	 */
	public TopicUserWriter() {}
	
	/**
	 * Writes the topic users.
	 * 
	 * @param outputFile the output file
	 * @param topics the map of each topic
	 * @param users the map of each user
	 * @throws Exception
	 */
	public void write(String outputFile, 
					  Map<Integer, List<TopicWord>> topics, 
					  Map<String, List<UserWord>> users) throws Exception {
		
		File out = new File(outputFile);
		PrintWriter writer = new PrintWriter(new FileWriter(out));

		writer.println("#topic_id	user_id	user_match_words_count	score	topic_words	user_match_words");
		
		Iterator<Integer> it1 = topics.keySet().iterator();
		while (it1.hasNext()) {
			Integer topicId = (Integer) it1.next();
			List<TopicWord> topicWords = (List<TopicWord>) topics.get(topicId);
			Iterator<String> it2 = users.keySet().iterator();
			
			List<TopicUser> topicUsers = new ArrayList<TopicUser>();
			
			while (it2.hasNext()) {
				String userId = (String) it2.next();
				List<UserWord> userWords = (List<UserWord>) users.get(userId);
				
				TopicUser topicUser = this.getTopicUser(topicId.intValue(), userId, topicWords, userWords);
				if (topicUser != null) {
					topicUsers.add(topicUser);
				}					
			}
			// sort by score
			Collections.sort(topicUsers);
			for (TopicUser topicUser : topicUsers) {
				writer.println(topicUser.getTopicId() + "\t" +
							   topicUser.getUserId() + "\t" +
							   topicUser.getUserMatchWordsCount() + "\t" +
							   topicUser.getScore() + "\t" +
							   topicUser.toTopicWordsString() + "\t" +
							   topicUser.toUserMatchWordsString());
			}			
		}
		writer.close();
	}
	
	private TopicUser getTopicUser(int topicId, 
								   String userId,
								   List<TopicWord> topicWords, 
								   List<UserWord> userWords) throws Exception {
		TopicUser topicUser = new TopicUser();
		topicUser.setTopicId(topicId);
		topicUser.setUserId(userId);
		topicUser.setTopicWords(topicWords);
		List<UserWord> userMatchWords = topicUser.findUserMatchWords(userWords);
		if (userMatchWords.size() == 0) {
			return null;
		}
		topicUser.setUserMatchWords(userMatchWords);
		topicUser.setUserMatchWordsCount(userMatchWords.size());
		float score = topicUser.calculateScore();
		topicUser.setScore(score);
		
		System.out.println("------------------------------------------");
		System.out.println("topic id == " + topicId);
		System.out.println("user id == " + userId);
		System.out.println("topic words == " + topicUser.toTopicWordsString());
		System.out.println("user match words == " + topicUser.toUserMatchWordsString());
		
		return topicUser;
	}
	
}

