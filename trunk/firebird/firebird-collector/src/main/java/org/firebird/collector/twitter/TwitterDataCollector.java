/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.collector.twitter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.firebird.collector.CollectorConfig;
import org.firebird.collector.util.CollectJobLogger;
import org.firebird.io.model.Edge;
import org.firebird.io.model.UserBlogEntry;
import org.firebird.io.model.Vertex;
import org.firebird.io.service.EdgeManager;
import org.firebird.io.service.UserBlogEntryManager;
import org.firebird.io.service.VertexManager;
import org.firebird.io.service.impl.EdgeManagerImpl;
import org.firebird.io.service.impl.UserBlogEntryManagerImpl;
import org.firebird.io.service.impl.VertexManagerImpl;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * A collector for twitter data by twitter4j open API.
 * 
 * @author Young-Gue Bae
 */
public class TwitterDataCollector {
	/** logger */
	private static CollectJobLogger logger = CollectJobLogger.getLogger(TwitterDataCollector.class);

	private CollectorConfig config;
	
	private int myId = 50900875;
	private Twitter twitter = null;
	private int numFriends = 0;
	private int numFollowers = 0;

	private HashMap<String, Vertex> vertices = new HashMap<String, Vertex>();
	private HashMap<String, Edge> edges = new HashMap<String, Edge>();

	private VertexManager vertexManager;
	private EdgeManager edgeManager;
	private UserBlogEntryManager userBlogEntryManager;

	/**
	 * Constructor.
	 * 
	 */
	public TwitterDataCollector() {
		this(new CollectorConfig());
	}
	
	/**
	 * Constructor.
	 * 
	 * @param config the collector config
	 */
	public TwitterDataCollector(CollectorConfig config) {
		this.config = config;
		
		if (config.isDBStorage()) {
			vertexManager = new VertexManagerImpl();
			edgeManager = new EdgeManagerImpl();
			userBlogEntryManager = new UserBlogEntryManagerImpl();
		}
	}
	
	/**
	 * Sets the collector config.
	 * 
	 * @param config the collector cofig
	 */
	public void setConfig(CollectorConfig config) {
		this.config = config;
		
		if (config.isDBStorage()) {
			vertexManager = new VertexManagerImpl();
			edgeManager = new EdgeManagerImpl();
			userBlogEntryManager = new UserBlogEntryManagerImpl();
		}		
	}

     /**
     * Collects the social network data from twitter.
     *
     * @param screenName the twitter user's screenName
     * @exception Exception
     */
	public void collect(String screenName) throws Exception {		
		TwitterOAuthSupport twitterSupport = new TwitterOAuthSupport();
		twitter = twitterSupport.access(myId);
		
		System.out.println(">>>>>>>>>>>>>> collector option : ");
		System.out.println("database storage mode == " + config.isDBStorage());
		System.out.println("collect friend(following) relationship == " + config.isCollectFriend());
		System.out.println("collect follower relationship == " + config.isCollectFollower());
		System.out.println("level limit == " + config.getLevelLimit());
		System.out.println("degree limit == " + config.getDegreeLimit());
		System.out.println("people limit == " + config.getPeopleLimit());
		
		logger.info(">>>>>>>>>>>>>> collector option : ");
		logger.info("database storage mode == " + config.isDBStorage());
		logger.info("collect friend(following) relationship == " + config.isCollectFriend());
		logger.info("collect follower relationship == " + config.isCollectFollower());
		logger.info("level limit == " + config.getLevelLimit());
		logger.info("degree limit == " + config.getDegreeLimit());
		logger.info("people limit == " + config.getPeopleLimit());

		User user = null;

		try {
			user = twitter.showUser(screenName);

			System.out.println("rate limit remaining  == " + user.getRateLimitStatus().getRemainingHits() + " / " + user.getRateLimitStatus().getHourlyLimit());
			System.out.println(">>>>>>>>>>>>>> start");

			logger.info("rate limit remaining  == " + user.getRateLimitStatus().getRemainingHits() + " / " + user.getRateLimitStatus().getHourlyLimit());
			logger.info(">>>>>>>>>>>>>> start");
			
			if (config.getCollectPriority() == CollectorConfig.COLLECT_PRIORITY_FOLLOWER) {
				if (config.isCollectFollower())
					this.collectFollowersOfUser(user, 0);
				
				if (config.isCollectFriend())
					this.collectFriendsOfUser(user, 0);				
			}
			else if (config.getCollectPriority() == CollectorConfig.COLLECT_PRIORITY_FRIEND) {
				if (config.isCollectFriend())
					this.collectFriendsOfUser(user, 0);	
				
				if (config.isCollectFollower())
					this.collectFollowersOfUser(user, 0);
			}
			else {
				if (config.isCollectFriend())
					this.collectFriendsOfUser(user, 0);	
				
				if (config.isCollectFollower())
					this.collectFollowersOfUser(user, 0);				
			}			
		} catch (TwitterException te) {
			te.printStackTrace();
			logger.error(te.getMessage(), te);
		}

		System.out.println(">>>>>>>>>>>>>> result");
		System.out.println("rate limit remaining  == " + user.getRateLimitStatus().getRemainingHits());
		System.out.println("friends == " + numFriends);
		System.out.println("followers == " + numFollowers);
		System.out.println("vertices == " + vertices.size());
		System.out.println("edges == " + edges.size());
		
		logger.info(">>>>>>>>>>>>>> result");
		logger.info("rate limit remaining  == " + user.getRateLimitStatus().getRemainingHits());
		logger.info("friends == " + numFriends);
		logger.info("followers == " + numFollowers);
		logger.info("vertices == " + vertices.size());
		logger.info("edges == " + edges.size());
	}
	
    /**
     * Collects the blog entries data from twitter.
     *
     * @param List the list of twitter user's screenName
     * @exception Exception
     */
	public void collectBlogEntries(List<String> screenNames) throws Exception {		
		TwitterOAuthSupport twitterSupport = new TwitterOAuthSupport();
		twitter = twitterSupport.access(myId);
		
		System.out.println(">>>>>>>>>>>>>> collect blog entries....");
		System.out.println("database storage mode == " + config.isDBStorage());		
		System.out.println(">>>>>>>>>>>>>> start");
		System.out.println("number of users == " + screenNames.size());
		
		logger.info(">>>>>>>>>>>>>> collect blog entries....");
		logger.info("database storage mode == " + config.isDBStorage());		
		logger.info(">>>>>>>>>>>>>> start");
		logger.info("number of users == " + screenNames.size());
		
		for (int i = 0; i < screenNames.size(); i++) {			
			try {
				System.out.println("collect blog entries for [" + (i+1) + "/" + screenNames.size() + " : " + (String)screenNames.get(i) + "]");
				logger.info("collect blog entries for [" + (i+1) + "/" + screenNames.size() + " : " + (String)screenNames.get(i) + "]");
				
				List<Status> statuses = twitter.getUserTimeline((String)screenNames.get(i));
				
				System.out.println("number of blog entries == " + statuses.size());
				logger.info("number of blog entries == " + statuses.size());
				
				this.addUserBlogEntries(statuses);
			} catch (TwitterException te) {
				te.printStackTrace();
				logger.error(te.getMessage(), te);
			}	
		}			
	}
	
    /**
     * Collects the blog entries data from twitter.
     *
     * @param List the list of twitter user's screenName
     * @param startPage the start page number
     * @param endPage the end page number
     * @exception Exception
     */
	public void collectBlogEntriesByPaging(List<String> screenNames, int startPage, int endPage) throws Exception {		
		TwitterOAuthSupport twitterSupport = new TwitterOAuthSupport();
		twitter = twitterSupport.access(myId);
		
		System.out.println(">>>>>>>>>>>>>> collect blog entries....");
		System.out.println("database storage mode == " + config.isDBStorage());
		
		System.out.println(">>>>>>>>>>>>>> start");
		System.out.println("number of pages == " + startPage + " ~ " + endPage);
		System.out.println("number of users == " + screenNames.size());

		logger.info(">>>>>>>>>>>>>> collect blog entries....");
		logger.info("database storage mode == " + config.isDBStorage());		
		logger.info(">>>>>>>>>>>>>> start");
		logger.info("number of pages == " + startPage + " ~ " + endPage);
		logger.info("number of users == " + screenNames.size());
		
		for (int i = 0; i < screenNames.size(); i++) {
			try {
				System.out.println("collect blog entries for [" + (i+1) + "/" + screenNames.size() + " : " + (String)screenNames.get(i) + "]");
				logger.info("collect blog entries for [" + (i+1) + "/" + screenNames.size() + " : " + (String)screenNames.get(i) + "]");
				
				List<Status> statuses = new ArrayList<Status>();
				
				for (int j = startPage; j <= endPage; j++) {
					statuses.addAll(twitter.getUserTimeline((String)screenNames.get(i), new Paging(j)));
				}

				System.out.println("number of blog entries == " + statuses.size());
				logger.info("number of blog entries == " + statuses.size());
				
				this.addUserBlogEntries(statuses);	
			} catch (TwitterException te) {
				te.printStackTrace();
				logger.error(te.getMessage(), te);
			}	
		}			
	}
	
    /**
     * Gets the vertices from the memory storage.
     *
     * @return List<Vertex> the vertex list
     */
	public List<Vertex> getVertices() {
		Collection<Vertex> c = vertices.values();
		List<Vertex> v = new ArrayList<Vertex>(c);
		return v;
	}
	
    /**
     * Gets the edges from the memory storage.
     *
     * @return List<Edge> the edge list
     */
	public List<Edge> getEdges() {
		Collection<Edge> c = edges.values();
		List<Edge> e = new ArrayList<Edge>(c);
		return e;
	}

	private void collectFriendsOfUser(User user, int level) {
		try {
			if (level < config.getLevelLimit() && numFriends <= config.getPeopleLimit()) {
				// add vertex
				addVertex(user);

				List<User> friends = twitter.getFriendsStatuses(user.getId());
				
				level++;
				for (int i = 0; i < friends.size(); i++) {
					// level > 1 -> collect everyone for an initial user whatever degree limit
					// if (level > 1 && i == config.getDegreeLimit())
					if (i == config.getDegreeLimit())
						break;

					numFriends++;
					User friend = (User) friends.get(i);

					// add vertex
					addVertex(friend);

					// add edge
					addEdge(user, friend);

					// recursive call
					if (config.isCollectFriend())
						this.collectFriendsOfUser(friend, level);
					if (config.isCollectFollower())
						this.collectFollowersOfUser(friend, level);
				}
			}
		} catch (TwitterException te) {
			te.printStackTrace();
		}
	}

	private void collectFollowersOfUser(User user, int level) {
		try {
			if (level < config.getLevelLimit() && numFollowers <= config.getPeopleLimit()) {
				// add vertex
				addVertex(user);

				List<User> followers = twitter.getFollowersStatuses(user.getId());

				level++;
				for (int i = 0; i < followers.size(); i++) {
					// level > 1 -> collect everyone for an initial user whatever degree limit
					// if (level > 1 && i == config.getDegreeLimit())
					if (i == config.getDegreeLimit())
						break;

					numFollowers++;
					User follower = (User) followers.get(i);

					// add vertex
					addVertex(follower);

					// add edge
					addEdge(follower, user);

					// recursive call
					if (config.isCollectFriend())
						this.collectFriendsOfUser(follower, level);
					if (config.isCollectFollower())
						this.collectFollowersOfUser(follower, level);
				}				
			}
		} catch (TwitterException te) {
			te.printStackTrace();
		}
	}

	private void addVertex(User user) {
		String key = String.valueOf(user.getId());

		if (!vertices.containsKey(key)) {
			Vertex vertex = this.makeVertex(user);
			vertices.put(key, vertex);
			
			if (config.isDBStorage()) {
				vertexManager.deleteVertex(vertex);
				vertexManager.addVertex(vertex);
			
				// collect user blog entries
				if (config.isCollectUserBlogEntry()) {
					try {
						this.addUserBlogEntries(twitter.getUserTimeline(user.getScreenName()));
					} catch (TwitterException te) {
						te.printStackTrace();
					}				
				}
			}
		}
	}

	private void addEdge(User user1, User user2) {
		String key = String.valueOf(user1.getId()) + "->" + String.valueOf(user2.getId());

		if (!edges.containsKey(key)) {
			Edge edge = this.makeEdge(user1, user2);
			edges.put(key, edge);
			System.out.println(user1.getScreenName() + " -> " + user2.getScreenName());
			
			if (config.isDBStorage()) {
				edgeManager.deleteEdge(edge);
				edgeManager.addEdge(edge);
			}
		}
	}
	
	private void addUserBlogEntries(List<Status> statuses) {		
		for (int i = 0; i < statuses.size(); i++) {
			Status status = (Status)statuses.get(i);
			UserBlogEntry userBlogEntry = makeUserBlogEntry(status);
			
			try {
				// delete already-exist user blog entries
				userBlogEntryManager.deleteUserBlogEntry(userBlogEntry.getWebsiteId(), userBlogEntry.getUserId(), userBlogEntry.getBlogEntryId());
				
				// add user blog entry
				userBlogEntryManager.addUserBlogEntry(userBlogEntry);
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error(ex.getMessage(), ex);
				logger.info("id == " + userBlogEntry.getUserId());
				logger.info("blog_entry_id == " + userBlogEntry.getBlogEntryId());
			}
		}		
	}

	private Vertex makeVertex(User user) {
		Vertex vertex = new Vertex();
		
		Status status = user.getStatus();

		vertex.setWebsiteId(1);
		vertex.setId(user.getScreenName());
		vertex.setNo(user.getId());
		vertex.setName(user.getName());
		//vertex.setColor();
		//vertex.setShape();
		//vertex.setSize();
		//vertex.setOpacity();
		vertex.setImageFile((user.getProfileImageURL() == null) ? null : user.getProfileImageURL().toString());
		//vertex.setInDegree(user.getFollowersCount());
		//vertex.setOutDegree(user.getFriendsCount());
		//vertex.setBetweennessCentrality();
		//vertex.setClosenessCentrality();
		//vertex.setEigenvectorCentrality();
		//vertex.setClusteringCoefficient();
		vertex.setFriendsCount(user.getFriendsCount());
		vertex.setFollowersCount(user.getFollowersCount());
		vertex.setUserNo(user.getId());
		vertex.setUserId(user.getScreenName());
		vertex.setUserName(user.getName());
		vertex.setUserUrl("http://twitter.com/" + user.getScreenName());
		vertex.setBlogEntryCount(user.getStatusesCount());
		vertex.setLastBlogEntryId(status != null ? String.valueOf(status.getId()) : null);
		vertex.setLastBlogEntryBody(status != null ? status.getText() : null);
		vertex.setLastBlogEntryType(UserBlogEntry.BLOGENTRY_TYPE_GENERAL);
		vertex.setLastBlogEntryCreateDate(status != null ? status.getCreatedAt() : null);
		vertex.setLastBlogEntryReplyTo(status != null ? status.getInReplyToScreenName() : null);
		//vertex.setLastBlogEntryDmTo();		
		/*
		if (status != null && status.isRetweet()) {
			Status rt = status.getRetweetedStatus();
			vertex.setLastBlogEntryReferFrom(rt.getUser().getScreenName());
		}
		*/
		vertex.setCreateDate(user.getCreatedAt());
		vertex.setLastUpdateDate(user.getCreatedAt());

		return vertex;
	}
	
	private Edge makeEdge(User user1, User user2) {
		Edge edge = new Edge();
		
		edge.setWebsiteId1(1);
		edge.setWebsiteId2(1);
		edge.setVertex1(user1.getScreenName());
		edge.setVertex2(user2.getScreenName());
		edge.setVertexNo1(user1.getId());
		edge.setVertexNo2(user2.getId());
		//edge.setColor();
		//edge.setWidth();
		//edge.setOpacity();
		edge.setDirected(true);
		edge.setRelationship(Edge.RELATIONSHIP_FOLLOWING);
		//edge.setCloseness();
		//edge.setReplyCount();
		//edge.setDmCount();
		//edge.setReferCount();
		//edge.setLastReplyDate();
		//edge.setLastDmDate();
		//edge.setLastReferDate();
		//edge.setCreateDate();
		//edge.setLastUpdateDate();
		
		return edge; 
	}	 
	
	private UserBlogEntry makeUserBlogEntry(Status status) {
		UserBlogEntry userBlogEntry = new UserBlogEntry();

		userBlogEntry.setWebsiteId(1);
		userBlogEntry.setUserId(status.getUser().getScreenName());
		userBlogEntry.setUserNo(status.getUser().getId());
		userBlogEntry.setBlogEntryId(String.valueOf(status.getId()));
		//userBlogEntry.setTitle(status.getSource());
		userBlogEntry.setBody(status.getText());
		userBlogEntry.setSourceWebsiteId(1);
		userBlogEntry.setBlogEntryType(UserBlogEntry.BLOGENTRY_TYPE_GENERAL);
		userBlogEntry.setPermaLinkUrl(null);
		userBlogEntry.setUserLinkUrl(null);
		userBlogEntry.setReplyTo(status.getInReplyToScreenName());
		userBlogEntry.setDmTo(null);
		/*
		if (status != null && status.isRetweet()) {			
			Status rt = status.getRetweetedStatus();
			userBlogEntry.setReferFrom(rt.getUser().getScreenName());
			System.out.println("RT == " + rt.getText());
			System.out.println("RT @user== " + rt.getUser().getScreenName());
		}
		*/	
		userBlogEntry.setCreateDate(status.getCreatedAt());
		userBlogEntry.setLastUpdateDate(status.getCreatedAt());
		
		return userBlogEntry;
	}
}
