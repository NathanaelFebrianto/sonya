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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.firebird.analyzer.util.JobLogger;
import org.firebird.io.model.TopicTerm;
import org.firebird.io.model.TopicUser;
import org.firebird.io.model.UserTerm;


/**
 * This class generates the topic-users set.
 * 
 * @author Young-Gue Bae
 */
public class TopicUserWriter {
	/** logger */
	private static JobLogger logger = JobLogger.getLogger(TopicUserWriter.class);
	
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
					  Map<Integer, List<TopicTerm>> topics, 
					  Map<String, List<UserTerm>> users) throws Exception {
		
		File out = new File(outputFile);
		PrintWriter writer = new PrintWriter(new FileWriter(out));

		writer.println("#topic_id	user_id	user_match_terms_count	score	topic_terms	user_match_terms");
		
		Iterator<Integer> it1 = topics.keySet().iterator();
		while (it1.hasNext()) {
			Integer topicId = (Integer) it1.next();
			List<TopicTerm> topicTerms = (List<TopicTerm>) topics.get(topicId);
			Iterator<String> it2 = users.keySet().iterator();
			
			List<TopicUser> topicUsers = new ArrayList<TopicUser>();
			
			while (it2.hasNext()) {
				String userId = (String) it2.next();
				List<UserTerm> userTerms = (List<UserTerm>) users.get(userId);
				
				TopicUser topicUser = this.getTopicUser(topicId.intValue(), userId, topicTerms, userTerms);
				if (topicUser != null) {
					topicUsers.add(topicUser);
				}					
			}
			// sort by score
			Collections.sort(topicUsers);
			for (TopicUser topicUser : topicUsers) {
				writer.println(topicUser.getTopicId() + "\t" +
							   topicUser.getUserId() + "\t" +
							   topicUser.getUserMatchTermsCount() + "\t" +
							   topicUser.getScore() + "\t" +
							   topicUser.toTopicTermsString() + "\t" +
							   topicUser.toUserMatchTermsString());
			}			
		}
		writer.close();
	}
	
	private TopicUser getTopicUser(int topicId, 
								   String userId,
								   List<TopicTerm> topicTerms, 
								   List<UserTerm> userTerms) throws Exception {
		TopicUser topicUser = new TopicUser();
		topicUser.setTopicId(topicId);
		topicUser.setUserId(userId);
		topicUser.setTopicTerms(topicTerms);
		List<UserTerm> userMatchTerms = topicUser.findUserMatchTerms(userTerms);
		if (userMatchTerms.size() == 0) {
			return null;
		}
		topicUser.setUserMatchTerms(userMatchTerms);
		topicUser.setUserMatchTermsCount(userMatchTerms.size());
		float score = topicUser.calculateScore();
		topicUser.setScore(score);
		
		logger.info("------------------------------------------");
		logger.info("topic id == " + topicId);
		logger.info("user id == " + userId);
		logger.info("topic terms == " + topicUser.toTopicTermsString());
		logger.info("user match terms == " + topicUser.toUserMatchTermsString());
		
		return topicUser;
	}
	
}

