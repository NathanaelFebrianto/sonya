/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.topic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.firebird.analyzer.graph.clustering.Clusterer;
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
	
	private Map<Integer, List<TopicUser>> allTopicUsersMap = new HashMap<Integer, List<TopicUser>>();
	private Map<Integer, Map<Integer, List<TopicUser>>> clusterTopicUsersMap = new HashMap<Integer, Map<Integer, List<TopicUser>>>();
	
	/**
	 * Constructor.
	 * 
	 */
	public TopicUserClusteringJob() { }

	/**
	 * Clusters users by topics.
	 * 
	 * @param clusterType the cluster type
	 * @param minScore the minimum score
	 * @param topUserNum the top user number in topic users
	 * @param websiteId the website id
	 */
	public void cluster(int websiteId, String clusterType, float minScore, int topUserNum) {
		
		VertexManager vertexManager = new VertexManagerImpl();
		TopicTermManager topicManager = new TopicTermManagerImpl();
		TopicUserClusterManager topicUserClusterManager = new TopicUserClusterManagerImpl();
		
		// initialize the topic user cluster
		topicUserClusterManager.deleteUsers(websiteId);
		
		List<Integer> clusters = new ArrayList<Integer>();
		if (clusterType.equals(Clusterer.EDGE_BETWEENNESS_CLUSTER)) {
			clusters = vertexManager.getEdgeBetweennessClusters(websiteId);
		}
		else if (clusterType.equals(Clusterer.VOLTAGE_CLUSTER)) {
			clusters = vertexManager.getVoltageClusters(websiteId);
		}
		 		
		List<Integer> topics = topicManager.getTopics(websiteId);		
		
		// start time
		Date startTime = Calendar.getInstance().getTime();
		logger.info("\n\n******************************************");
		logger.info("Start Topic-based User Clustering - " + clusterType + " : " + startTime + "\n");		
		
		logger.info("Get all topic users...");
		this.allTopicUsersMap = this.getAllTopicUsers(websiteId, topics);		
		
		logger.info("#cluster	topic	user_id	is_topic_user	score");
		
		for (Integer cluster : clusters) {			
			List<Vertex> users = new ArrayList<Vertex>();
			if (clusterType.equals(Clusterer.EDGE_BETWEENNESS_CLUSTER)) {
				users = vertexManager.getVerticesInEdgeBetweennessCluster(websiteId, cluster);
			}
			else if (clusterType.equals(Clusterer.VOLTAGE_CLUSTER)) {
				users = vertexManager.getVerticesInVoltageCluster(websiteId, cluster);
			}
			
			Map<Integer, List<TopicUser>> topicUsersMap = this.getTopTopicUsersByCluster(websiteId, topics, clusterType, cluster, minScore, topUserNum);

			for (Vertex user : users) {	
				
				for (Iterator<Integer> it = topicUsersMap.keySet().iterator(); it.hasNext();) {
					Integer topic = (Integer) it.next();
					TopicUserCluster userCluster = null;

					// check if the user is a topic user in the original topic users list.
					// if a original topic user, just stores the original user's topic score * authority.
					TopicUser originalTopicUser = this.getTopicUser(topic, user.getUserId());
					if (originalTopicUser != null) {
						userCluster = new TopicUserCluster();
						
						float authority = new Float(user.getAuthority()).floatValue();
						float topicScore = originalTopicUser.getScore();
						float score = topicScore * authority;
						
						userCluster.setWebsiteId(websiteId);
						userCluster.setTopicId(topic);
						userCluster.setUserId(user.getUserId());
						userCluster.setTopicUser(true);
						userCluster.setCluster(cluster);
						userCluster.setAuthority(user.getAuthority());
						userCluster.setTopicScore(topicScore);
						userCluster.setAuthorityTopicScore(score);
					}	
					else {
						List<TopicUser> topicUsers = (List<TopicUser>) topicUsersMap.get(topic);				
						
						if (topicUsers != null && topicUsers.size() > 0) {
							userCluster = this.topicUserCluster(cluster, topic, user, topicUsers);
						}						
					}
					// store into database
					if (userCluster != null) {
						topicUserClusterManager.addUser(userCluster);						
						// print
						String message = cluster + 
							"\t" + topic + 
							"\t" + userCluster.getUserId() +
							"\t" + userCluster.isTopicUser() + 
							"\t" + userCluster.getAuthority() + 
							"\t" + userCluster.getTopicScore() + 
							"\t" + userCluster.getAuthorityTopicScore();
						//logger.info(message);
					}
				}
			}

			logger.info("cluster == " + cluster);
		}
		
		// end time
		Date endTime = Calendar.getInstance().getTime();
		logger.info("Finish Topic-based User Clustering : " + endTime);		
		logger.jobSummary("Topic-based User Clustering - " + clusterType, startTime, endTime);
	}
	
	private Map<Integer, List<TopicUser>> getAllTopicUsers(int websiteId, List<Integer> topics) {
		TopicUserManager topicUserManager = new TopicUserManagerImpl();
		Map<Integer, List<TopicUser>> allTopicUsersMap = new HashMap<Integer, List<TopicUser>>();
		
		for (Integer topic : topics) {
			List<TopicUser> topicUsers = topicUserManager.getUsers(websiteId, topic);
			allTopicUsersMap.put(topic, topicUsers);
		}
		
		return allTopicUsersMap;
	}
	
	private TopicUser getTopicUser(int topic, String userId) {
		List<TopicUser> topicUsers = allTopicUsersMap.get(topic);
		if (topicUsers != null) {
			for (TopicUser topicUser : topicUsers) {
				if (userId.equals(topicUser.getUserId()))
					return topicUser;
			}
		}
		return null;
	}
		
	private Map<Integer, List<TopicUser>> getTopTopicUsersByCluster(int websiteId, 
										List<Integer> topics, String clusterType, int cluster, float minScore, int topUserNum) {
		TopicUserManager topicUserManager = new TopicUserManagerImpl();	 	 
		
		Map<Integer, List<TopicUser>> topicUsersSet = (Map<Integer, List<TopicUser>>) clusterTopicUsersMap.get(cluster);
		if (topicUsersSet != null && topicUsersSet.size() > 0) {
			return topicUsersSet;
		}
		
		topicUsersSet = new HashMap<Integer, List<TopicUser>>();
		
		for (Integer topic : topics) {
			List<TopicUser> topicUsers = null;
			if (clusterType.equals(Clusterer.EDGE_BETWEENNESS_CLUSTER)) {
				topicUsers = topicUserManager.getUsersInEdgeBetweennessCluster(websiteId, topic, cluster, minScore, topUserNum);
			}
			else if (clusterType.equals(Clusterer.VOLTAGE_CLUSTER)) {
				topicUsers = topicUserManager.getUsersInVoltageCluster(websiteId, topic, cluster, minScore, topUserNum);
			}
			
			if (topicUsers != null && topicUsers.size() > 0) {
				topicUsersSet.put(Integer.valueOf(topic), topicUsers);
			}			
		}		
		if (topicUsersSet != null && topicUsersSet.size() > 0) {
			clusterTopicUsersMap.put(Integer.valueOf(cluster), topicUsersSet);
		}
		
		return topicUsersSet;
	}
	
	/*
	private TopicUserCluster topicUserCluster(int cluster, int topic, Vertex user, List<TopicUser> topicUsers) {
		TopicUserCluster userCluster = new TopicUserCluster();
		
		EdgeManager edgeManager = new EdgeManagerImpl();
		
		int numTopicUsers = topicUsers.size();
		float score = 0.0f;	
		boolean isTopicUser = false;
		
		// check if the user is a topic user in the original topic users list.
		// if true, get the user's topic score to calculate.
		boolean isOriginalTopicUser = false;
		TopicUser originalTopicUser = this.getTopicUser(topic, user.getUserId());
		if (originalTopicUser != null) {
			isOriginalTopicUser = true;
		}		
		
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
		
		//float authority = new Float(user.getAuthority()).floatValue();
		//score = (score / numTopicUsers) * authority;
		
		if (isTopicUser == false && isOriginalTopicUser == true) {
			score = ((score / numTopicUsers) + originalTopicUser.getScore()) / 2;
		}
		else {
			score = (score / numTopicUsers);
		}		
		
		userCluster.setWebsiteId(user.getWebsiteId());
		userCluster.setTopicId(topic);
		userCluster.setUserId(user.getUserId());
		userCluster.setTopicUser(isOriginalTopicUser);
		userCluster.setCluster(cluster);
		userCluster.setScore(score);
		
		return userCluster;
	}
	*/

	private TopicUserCluster topicUserCluster(int cluster, int topic, Vertex user, List<TopicUser> topicUsers) {
		TopicUserCluster userCluster = new TopicUserCluster();
		
		EdgeManager edgeManager = new EdgeManagerImpl();
		
		int numTopicUsers = topicUsers.size();
		float score = 0.0f;	
		float topicScore = 0.0f;	
		
		for (TopicUser topicUser : topicUsers) {			
			float topicUserScore = topicUser.getScore();
			float weight = 0.0f;

			Edge edge = edgeManager.getEdge(
					user.getWebsiteId(), 
					topicUser.getWebsiteId(), 
					user.getId(), 
					topicUser.getUserId(), 
					Edge.RELATIONSHIP_FOLLOWING);
			if (edge != null) {	// if following
				weight = 0.8f;
			}
			else {
				weight = 0.2f;
			}
			topicScore = topicScore + (topicUserScore*weight);
		}
		
		float authority = new Float(user.getAuthority()).floatValue();
		topicScore = (topicScore / numTopicUsers);
		score = topicScore * authority;
		
		userCluster.setWebsiteId(user.getWebsiteId());
		userCluster.setTopicId(topic);
		userCluster.setUserId(user.getUserId());
		userCluster.setTopicUser(false);
		userCluster.setCluster(cluster);
		userCluster.setAuthority(user.getAuthority());
		userCluster.setTopicScore(topicScore);
		userCluster.setAuthorityTopicScore(score);
		
		return userCluster;
	}
	
	public static void main(String[] args) {
		//String clusterType = Clusterer.EDGE_BETWEENNESS_CLUSTER;
		String clusterType = Clusterer.VOLTAGE_CLUSTER;
		//float minScore = 40f;
		float minScore = 25f;
		int topUserNum = 5;
		int websiteId = 1;
		
		TopicUserClusteringJob clusterer = new TopicUserClusteringJob();
		clusterer.cluster(websiteId, clusterType, minScore, topUserNum);
	}
}
