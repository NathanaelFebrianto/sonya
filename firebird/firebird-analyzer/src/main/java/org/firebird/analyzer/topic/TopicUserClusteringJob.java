/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.topic;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.firebird.analyzer.util.JobLogger;
import org.firebird.io.model.Edge;
import org.firebird.io.model.TopicUser;
import org.firebird.io.model.TopicUserCluster;
import org.firebird.io.model.Vertex;
import org.firebird.io.service.EdgeManager;
import org.firebird.io.service.TopicTermManager;
import org.firebird.io.service.TopicUserClusterManager;
import org.firebird.io.service.TopicUserManager;
import org.firebird.io.service.VertexManager;
import org.firebird.io.service.impl.EdgeManagerImpl;
import org.firebird.io.service.impl.TopicTermManagerImpl;
import org.firebird.io.service.impl.TopicUserClusterManagerImpl;
import org.firebird.io.service.impl.TopicUserManagerImpl;
import org.firebird.io.service.impl.VertexManagerImpl;

/**
 * This class is a topic-based user clusterer.
 * 
 * @author Young-Gue Bae
 */
public class TopicUserClusteringJob {
	/** logger */
	private static JobLogger logger = JobLogger.getLogger(TopicUserClusteringJob.class);
	
	/**
	 * Constructor.
	 * 
	 */
	public TopicUserClusteringJob() { }
	
	/**
	 * Clusters users by topics.
	 * 
	 * @param websiteId the website id
	 */
	public void cluster(int websiteId) {
		
		VertexManager vertexManager = new VertexManagerImpl();
		TopicTermManager topicManager = new TopicTermManagerImpl();
		TopicUserManager topicUserManager = new TopicUserManagerImpl();
		TopicUserClusterManager topicUserClusterManager = new TopicUserClusterManagerImpl();
		
		// initialize the topic user cluster
		topicUserClusterManager.deleteUsers(websiteId);
		
		List<Integer> clusters = vertexManager.getClusters(websiteId);		
		List<Integer> topics = topicManager.getTopics(websiteId);
		
		// start time
		Date startTime = Calendar.getInstance().getTime();
		logger.info("\n\n******************************************");
		logger.info("Start Topic-based User Clustering : " + startTime + "\n");		
		
		logger.info("#cluster	topic	user_id	is_topic_user	score");
		for (Integer cluster : clusters) {
			List<Vertex> users = vertexManager.getVerticesInCluster(websiteId, cluster);			
			for (Vertex user : users) {
				for (Integer topic : topics) {
					List<TopicUser> topicUsers = topicUserManager.getUsersInCluster(websiteId, topic, cluster);
					if (topicUsers != null && topicUsers.size() > 0) {
						TopicUserCluster userCluster = topicUserCluster(cluster, topic, user, topicUsers);
						// store into database
						topicUserClusterManager.addUser(userCluster);						
						// print
						String message = cluster + "\t" + topic + "\t" + userCluster.getUserId() +
							"\t" + userCluster.isTopicUser() + "\t" + userCluster.getScore();
						logger.info(message);
					}
				}				
			}
		}
		
		// end time
		Date endTime = Calendar.getInstance().getTime();
		logger.info("Finish Topic-based User Clustering : " + endTime);		
		logger.jobSummary("Topic-based User Clustering", startTime, endTime);
	}
	
	private TopicUserCluster topicUserCluster(int cluster, int topic, Vertex user, List<TopicUser> topicUsers) {
		TopicUserCluster userCluster = new TopicUserCluster();
		
		EdgeManager edgeManager = new EdgeManagerImpl();
		
		int numTopicUsers = topicUsers.size();
		float score = 0.0f;
		boolean isTopicUser = false;
		for (TopicUser topicUser : topicUsers) {
			
			float topicScore = topicUser.getScore();
			float weight = 0.0f;
			if (user.getUserId().equals(topicUser.getUserId())) {
				weight = 1.0f;
				isTopicUser = true;
			}
			else {
				Edge edge = edgeManager.getEdge(
						user.getWebsiteId(), 
						topicUser.getWebsiteId(), 
						user.getId(), 
						topicUser.getUserId(), 
						Edge.RELATIONSHIP_FOLLOWING);
				if (edge != null) {
					weight = 0.5f;
				}
				else {
					weight = 0.1f;
				}
			}
			score = score + (topicScore*weight);
		}
		
		float authority = new Float(user.getAuthority()).floatValue();
		score = (score / numTopicUsers) * authority;
		
		userCluster.setWebsiteId(user.getWebsiteId());
		userCluster.setTopicId(topic);
		userCluster.setUserId(user.getUserId());
		userCluster.setTopicUser(isTopicUser);
		userCluster.setCluster(cluster);
		userCluster.setScore(score);
		
		return userCluster;
	}
	
	public static void main(String[] args) {
		int websiteId = 1;
		TopicUserClusteringJob clusterer = new TopicUserClusteringJob();
		clusterer.cluster(websiteId);
	}
}
