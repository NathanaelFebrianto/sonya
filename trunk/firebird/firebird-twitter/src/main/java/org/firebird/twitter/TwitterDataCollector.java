/*
Copyright (c) 2009-2010, Young-Gue Bae
All rights reserved.
*/
package org.firebird.twitter;

import java.util.HashMap;
import java.util.List;

import org.firebird.io.dao.ibatis.VertexDaoiBatis;
import org.firebird.io.model.Vertex;
import org.firebird.io.service.VertexManager;
import org.firebird.io.service.impl.VertexManagerImpl;

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

	private int myId = 50900875;
	private Twitter twitter = null;
	private boolean isFollowing = false;
	private boolean isFollower = false;
	private int limitLevel = 1;
	private int limitPeople = 999999999;
	private int limitDegree = 1;
	private int numFriends = 0;
	private int numFollowers = 0;

	private HashMap<String, String> vertices = new HashMap<String, String>();
	private HashMap<String, String> edges = new HashMap<String, String>();

	private VertexManager vertexManager;
	
	private String baseUrl = null;

	/**
	 * Constructor.
	 * 
	 */
	public TwitterDataCollector() throws Exception {
		TwitterOAuthSupport twitterSupport = new TwitterOAuthSupport();
		twitter = twitterSupport.access(myId);
		baseUrl = twitter.getBaseURL();
		vertexManager = new VertexManagerImpl(new VertexDaoiBatis());
	}

    /**
     * Sets if collects the following relationship or not.
     *
     * @param isFollowing true if collects
     */
	public void setCollectFollowingRelationship(boolean isFollowing) {
		this.isFollowing = isFollowing;
	}

    /**
     * Sets if collects the follower relationship or not.
     *
     * @param isFollower true if collects
     */
	public void setCollectFollowerRelationship(boolean isFollower) {
		this.isFollower = isFollower;
	}

    /**
     * Sets the limit level.
     *
     * @param limitLevel the limit level
     */
	public void setLimitLevel(int limitLevel) {
		this.limitLevel = limitLevel;
	}

    /**
     * Sets the limit number of people.
     *
     * @param limitPeople the number of people
     */
	public void setLimitPeople(int limitPeople) {
		this.limitPeople = limitPeople;
	}

    /**
     * Sets the limit degree.
     *
     * @param limitDegree the limit degree
     */
	public void setLimitDegree(int limitDegree) {
		this.limitDegree = limitDegree;
	}

    /**
     * Collects the social network data from twitter.
     *
     * @param screenName the twitter user's screenName
     */
	public void collectSocialNetwork(String screenName) {
		System.out.println(">>>>>>>>>>>>>> option");
		System.out.println("collect following relationship == " + isFollowing);
		System.out.println("collect follower relationship == " + isFollowing);
		System.out.println("level limit == " + limitLevel);
		System.out.println("people limit == " + limitPeople);

		User user = null;

		try {
			user = twitter.showUser(screenName);

			System.out.println("rate limit == " + user.getRateLimitLimit());
			System.out.println("rate limit remaining  == "
					+ user.getRateLimitRemaining());
			System.out.println(">>>>>>>>>>>>>> start");

			if (isFollowing)
				this.collectFriendsOfUser(user, 1);

			if (isFollower)
				this.collectFollowersOfUser(user, 1);
		} catch (TwitterException te) {
			te.printStackTrace();
		}

		System.out.println(">>>>>>>>>>>>>> result");
		System.out.println("rate limit remaining  == "
				+ user.getRateLimitRemaining());
		System.out.println("friends == " + numFriends);
		System.out.println("followers == " + numFollowers);
		System.out.println("vertices == " + vertices.size());
		System.out.println("edges == " + edges.size());
	}

	private void collectFriendsOfUser(User user, int level) {
		try {
			if (level <= limitLevel && numFriends <= limitPeople) {
				// add vertex
				addVertex(user);

				List<User> friends = twitter.getFriendsStatuses(String.valueOf(user.getId()));

				level++;
				for (int i = 0; i < friends.size(); i++) {
					if (i == limitDegree)
						break;

					numFriends++;
					User friend = (User) friends.get(i);

					// add vertex
					addVertex(friend);

					// add edge
					addEdge(user, friend);

					// recursive call
					if (isFollowing)
						this.collectFriendsOfUser(friend, level);
					if (isFollower)
						this.collectFollowersOfUser(friend, level);
				}
			}
		} catch (TwitterException te) {
			te.printStackTrace();
		}
	}

	private void collectFollowersOfUser(User user, int level) {
		try {
			if (level <= limitLevel && numFollowers <= limitPeople) {
				// add vertex
				addVertex(user);

				List<User> followers = twitter.getFollowersStatuses(String
						.valueOf(user.getId()));

				level++;
				for (int i = 0; i < followers.size(); i++) {
					if (i == limitDegree)
						break;

					numFollowers++;
					User follower = (User) followers.get(i);

					// add vertex
					addVertex(follower);

					// add edge
					addEdge(follower, user);

					// recursive call
					if (isFollowing)
						this.collectFriendsOfUser(follower, level);
					if (isFollower)
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
			vertices.put(key, "");
			Vertex vertex = this.makeVertex(user);
			vertexManager.deleteVertex(vertex);
			vertexManager.addVertex(vertex);
		}
	}

	private void addEdge(User user1, User user2) {
		String key = String.valueOf(user1.getId()) + "->"
				+ String.valueOf(user2.getId());

		if (!edges.containsKey(key)) {
			edges.put(key, "following");
			System.out.println(user1.getScreenName() + " -> "
					+ user2.getScreenName());
		}
	}
	
	private void addUserBlogEntries(List<Status> statuses) {
		
	}

	private Vertex makeVertex(User user) {
		Vertex vertex = new Vertex();

		vertex.setWebsiteId(1);
		vertex.setId(user.getId());
		vertex.setName(user.getName());
		// vertex.setColor(String color);
		// vertex.setShape(String shape);
		// vertex.setSize(int size);
		// vertex.setOpacity(int opacity);
		vertex.setImageFile((user.getProfileImageURL() == null) ? null : user
				.getProfileImageURL().toString());
		vertex.setInDegree(user.getFollowersCount());
		vertex.setOutDegree(user.getFriendsCount());
		// vertex.setBetweenessCentrality(long betweenessCentrality);
		// vertex.setClosenessCentrality(long closenessCentrality);
		// vertex.setEigenvectorCentrality(long eigenvectorCentrality);
		// vertex.setClusteringCoefficient(long clusteringCoefficient);
		vertex.setFollowing(user.getFriendsCount());
		vertex.setFollowers(user.getFollowersCount());
		vertex.setUserNo(user.getId());
		vertex.setUserId(user.getScreenName());
		vertex.setUserName(user.getName());
		vertex.setUserUrl(baseUrl + user.getScreenName());
		vertex.setBlogEntryCount(user.getStatusesCount());
		vertex.setLastBlogEntryId(String.valueOf(user.getStatusId()));

		// Status status = twitter.showStatus(user.getStatusId());
		vertex.setLastBlogEntry(user.getStatusText());
		vertex.setLastBlogEntryType("1");
		vertex.setLastBlogEntryCreateDate(user.getStatusCreatedAt());
		vertex.setLastBlogEntryReplyTo(String.valueOf(user.getStatusInReplyToUserId()));
		// vertex.setLastBlogEntryDmTo(String lastBlogEntryDmTo);
		// vertex.setLastBlogEntryReferFrom(String lastBlogEntryReferFrom);
		vertex.setCreateDate(user.getCreatedAt());
		vertex.setLastUpdateDate(user.getCreatedAt());
		// vertex.setColCreateDate(String colCreateDate);
		// vertex.setColLastUpdateDate(String colLastUpdateDate);

		return vertex;
	}

	/*
	 * private Edge makeEdge(User user1, User user2) { return null; }
	 */
}
