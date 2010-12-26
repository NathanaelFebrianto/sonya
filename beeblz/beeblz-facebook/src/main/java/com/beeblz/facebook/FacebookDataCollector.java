/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.facebook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.beeblz.graph.GraphData;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.Facebook;
import com.restfb.FacebookClient;
import com.restfb.FacebookException;
import com.restfb.Parameter;
import com.restfb.types.User;

/**
 * Collects facebook data by using the facebook Graph API.
 * 
 * @author YoungGue Bae
 */
public class FacebookDataCollector {

	// my uid: 1480697938
	private static final String MY_ACCESS_TOKEN = "174260895927181|08c9a69debb61dbf2d983764-1480697938|r0D9FyG27SU9NSPEPQa35kx-GYE";

	private FacebookClient facebookClient;

	public FacebookDataCollector() {
		this.facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);
	}

	/**
	 * Gets my friend list.
	 * 
	 * @param includeMe true if including me
	 * @return GraphData the graph data with G(V,E)
	 */
	public GraphData getMyFriends(boolean includeMe) {
		GraphData graph = new GraphData();
		
		try {
			Parameter[] parameters = {Parameter.with("fields",	"id, name, email, picture, work")};
			
			User me = facebookClient.fetchObject("me", User.class, parameters);
			System.out.println("User name: " + me.getName());
			System.out.println("User ID: " + me.getId());
			
			long source = Long.valueOf(me.getId()).longValue();
			if (includeMe)
				graph.addNode(source, me.getName(), me.getPicture());	
			
			// get my friends
			Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class, parameters);
			System.out.println("Count of my friends: " + myFriends.getData().size());

			for (int i = 0; i < myFriends.getData().size(); i++) {
				User friend = (User)myFriends.getData().get(i);
				System.out.println("friend: " + friend.getId() + "|" + friend.getName() + "|" + friend.getPicture());
				
				long target = Long.valueOf(friend.getId()).longValue();
				
				graph.addNode(target, friend.getName(), friend.getPicture());
				if (includeMe)
					graph.addEdge(source, target, 0.5);				
			}
			
			// get mutual friends
			List<MutualFriend> mutualFriends = this.getMutualFriends(me.getId());

			for (int i = 0; i < mutualFriends.size(); i++) {
				MutualFriend mutualFriend = (MutualFriend) mutualFriends.get(i);
				System.out.println("result: " + mutualFriend.toString());

				long uid1 = Long.valueOf(mutualFriend.uid1).longValue();
				long uid2 = Long.valueOf(mutualFriend.uid2).longValue();

				graph.addEdge(uid1, uid2, 0.5);			
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return graph;
	}

	/**
	 * Check if two friends are mutual friends.
	 * 
	 * @param uid the user id
	 * @return List the mutual friends
	 */
	public List<MutualFriend> getMutualFriends(String uid) {		
		List<MutualFriend> result = new ArrayList<MutualFriend>();
		
		try {
			String query = "SELECT uid1, uid2 FROM friend " +
						   "WHERE uid1 IN " +
						   "(SELECT uid2 FROM friend WHERE uid1=" + uid + ") " + 
						   "AND uid2 IN " + 
						   "(SELECT uid2 FROM friend WHERE uid1=" + uid + ")";
			
			result = facebookClient.executeQuery(query, MutualFriend.class);
			System.out.println("Result Count: " + result.size());
		} catch (FacebookException fe) {
			fe.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Gets the contents created by the specific user.
	 * 
	 * @param uid the user id
	 * @param time after the specific time
	 * @return Contents the contents of object id with status, link, photo, video
	 */
	public Contents getContents(String uid, String time) {
		Contents result = null;
		
		try {
			Map<String, String> queries = new HashMap<String, String>();
			queries.put("statuses", "SELECT status_id FROM status WHERE uid = " + uid + " AND time >= " + time);
			queries.put("links", "SELECT link_id FROM link WHERE owner = " + uid + " AND created_time >= " + time);
			queries.put("photos", "SELECT object_id FROM photo " +
					"WHERE aid IN (SELECT aid FROM album WHERE owner = " + uid + ")" + " AND created >= " + time);
			queries.put("videos", "SELECT vid FROM video WHERE owner = " + uid + " AND created_time >= " + time);
			result =  facebookClient.executeMultiquery(queries, Contents.class);
			
			System.out.println("status == " + result.statuses.size());
			System.out.println("link == " + result.links.size());
			System.out.println("photo == " + result.photos.size());
			System.out.println("video == " + result.videos.size());
			
		} catch (FacebookException fe) {
			fe.printStackTrace();
		}
		return result;
	}	

	/**
	 * Gets the users who commented to the specific user's contents.
	 * 
	 * @param uid the user id
	 * @param time after the specific time
	 * @return List the users who commented
	 */
	public List<String> getUsersCommentTo(String uid, String time) {
		List<String> result = new ArrayList<String>();
		
		List<String> users1 = getUsersCommentToStatus(uid, time);
		List<String> users2 = getUsersCommentToLink(uid, time);
		List<String> users3 = getUsersCommentToPhoto(uid, time);
		List<String> users4 = getUsersCommentToVideo(uid, time);
		
		result.addAll(users1);
		result.addAll(users2);
		result.addAll(users3);
		result.addAll(users4);

		return result;	
	}
	
	/**
	 * Gets the users who commented to the specific user's statuses.
	 * 
	 * @param uid the user id
	 * @param time after the specific time
	 * @return List the users who commented
	 */
	public List<String> getUsersCommentToStatus(String uid, String time) {
		List<String> result = new ArrayList<String>();
		
		try {
			String query = "SELECT fromid " + 
			   "FROM comment WHERE object_id IN " + 
			   "(SELECT status_id FROM status WHERE uid = " + uid + " AND time >= " +  time + ")";			   		
			
			System.out.println("query: " + query);
			
			result = facebookClient.executeQuery(query, String.class);
			System.out.println("User count who commented to statuses: " + result.size());
			
		} catch (FacebookException fe) {
			fe.printStackTrace();
		}
		return result;	
	}

	/**
	 * Gets the users who commented to the specific user's links.
	 * 
	 * @param uid the user id
	 * @param time after the specific time
	 * @return List the users who commented
	 */
	public List<String> getUsersCommentToLink(String uid, String time) {
		List<String> result = new ArrayList<String>();
		
		try {
			String query = "SELECT fromid " + 
			   "FROM comment WHERE object_id IN " + 
			   "(SELECT link_id FROM link WHERE owner = " + uid + " AND created_time >= " + time + ")";			   		
			
			System.out.println("query: " + query);
			
			result = facebookClient.executeQuery(query, String.class);
			System.out.println("User count who commented to links: " + result.size());
			
		} catch (FacebookException fe) {
			fe.printStackTrace();
		}
		return result;	
	}
	
	/**
	 * Gets the users who commented to the specific user's photos.
	 * 
	 * @param uid the user id
	 * @param time after the specific time
	 * @return List the users who commented
	 */
	public List<String> getUsersCommentToPhoto(String uid, String time) {
		List<String> result = new ArrayList<String>();
		
		try {
			String query = "SELECT fromid " + 
			   "FROM comment WHERE object_id IN " + 
			   "(SELECT object_id FROM photo WHERE aid IN " +
			   "(SELECT aid FROM album WHERE owner = " + uid + ") AND created >= " +  time + ")";			   		
			
			System.out.println("query: " + query);
			
			result = facebookClient.executeQuery(query, String.class);
			System.out.println("User count who commented to photos: " + result.size());
			
		} catch (FacebookException fe) {
			fe.printStackTrace();
		}
		return result;	
	}
	
	/**
	 * Gets the users who commented to the specific user's videos.
	 * 
	 * @param uid the user id
	 * @param time after the specific time
	 * @return List the users who commented
	 */
	public List<String> getUsersCommentToVideo(String uid, String time) {
		List<String> result = new ArrayList<String>();
		
		try {
			String query = "SELECT fromid " + 
			   "FROM comment WHERE object_id IN " + 
			   "(SELECT vid FROM video WHERE owner = " + uid + " AND created_time >= " + time + ")";			   		
			
			System.out.println("query: " + query);
			
			result = facebookClient.executeQuery(query, String.class);
			System.out.println("User count who commented to videos: " + result.size());
			
		} catch (FacebookException fe) {
			fe.printStackTrace();
		}
		return result;	
	}
	
	/**
	 * Gets the users who liked to the specific user's contents.
	 * 
	 * @param uid the user id
	 * @param time after the specific time
	 * @return List the users who liked
	 */
	public List<String> getUsersLikeTo(String uid, String time) {
		List<String> result = new ArrayList<String>();
		
		List<String> users1 = getUsersLikeToStatus(uid, time);
		List<String> users2 = getUsersLikeToLink(uid, time);
		List<String> users3 = getUsersLikeToPhoto(uid, time);
		List<String> users4 = getUsersLikeToVideo(uid, time);
		
		result.addAll(users1);
		result.addAll(users2);
		result.addAll(users3);
		result.addAll(users4);

		return result;	
	}
	
	/**
	 * Gets the users who liked to the specific user's statuses.
	 * 
	 * @param uid the user id
	 * @param time after the specific time
	 * @return List the users who liked
	 */
	public List<String> getUsersLikeToStatus(String uid, String time) {
		List<String> result = new ArrayList<String>();
		
		try {
			String query = "SELECT user_id " + 
			   "FROM like WHERE object_id IN " + 
			   "(SELECT status_id FROM status WHERE uid = " + uid + " AND time >= " +  time + ")";			   		
			
			System.out.println("query: " + query);
			
			result = facebookClient.executeQuery(query, String.class);
			System.out.println("User count who liked to statuses: " + result.size());
			
		} catch (FacebookException fe) {
			fe.printStackTrace();
		}
		return result;	
	}
	
	/**
	 * Gets the users who liked to the specific user's links.
	 * 
	 * @param uid the user id
	 * @param time after the specific time
	 * @return List the users who liked
	 */
	public List<String> getUsersLikeToLink(String uid, String time) {
		List<String> result = new ArrayList<String>();
		
		try {
			String query = "SELECT user_id " + 
			   "FROM like WHERE object_id IN " + 
			   "(SELECT link_id FROM link WHERE owner = " + uid + " AND created_time >= " + time + ")";			   		
			
			System.out.println("query: " + query);
			
			result = facebookClient.executeQuery(query, String.class);
			System.out.println("User count who liked to links: " + result.size());
			
		} catch (FacebookException fe) {
			fe.printStackTrace();
		}
		return result;	
	}
	
	/**
	 * Gets the users who liked to the specific user's photos.
	 * 
	 * @param uid the user id
	 * @param time after the specific time
	 * @return List the users who liked
	 */
	public List<String> getUsersLikeToPhoto(String uid, String time) {
		List<String> result = new ArrayList<String>();
		
		try {
			String query = "SELECT user_id " + 
			   "FROM like WHERE object_id IN " + 
			   "(SELECT object_id FROM photo WHERE aid IN " +
			   "(SELECT aid FROM album WHERE owner = " + uid + ") AND created >= " +  time + ")";			   		
			
			System.out.println("query: " + query);
			
			result = facebookClient.executeQuery(query, String.class);
			System.out.println("User count who liked to photos: " + result.size());
			
		} catch (FacebookException fe) {
			fe.printStackTrace();
		}
		return result;	
	}
	
	/**
	 * Gets the users who liked to the specific user's videos.
	 * 
	 * @param uid the user id
	 * @param time after the specific time
	 * @return List the users who liked
	 */
	public List<String> getUsersLikeToVideo(String uid, String time) {
		List<String> result = new ArrayList<String>();
		
		try {
			String query = "SELECT user_id " + 
			   "FROM like WHERE object_id IN " + 
			   "(SELECT vid FROM video WHERE owner = " + uid + " AND created_time >= " + time + ")";			   		
			
			System.out.println("query: " + query);
			
			result = facebookClient.executeQuery(query, String.class);
			System.out.println("User count who liked to videos: " + result.size());
			
		} catch (FacebookException fe) {
			fe.printStackTrace();
		}
		return result;	
	}

	
	/**
	 * Gets the statues created by the specific user.
	 * 
	 * @param uid the user id
	 * @param time after the specific time
	 * @return List<Status> the list of status
	 */
	public List<Status> getStatuses(String uid, String time) {
		List<Status> result = new ArrayList<Status>();
		
		try {
			String query = "SELECT uid, status_id, message " + 
			   "FROM status WHERE uid = " + uid + " AND time >= " +  time;
			
			result = facebookClient.executeQuery(query, Status.class);
			System.out.println("Result count for status: " + result.size());
			
		} catch (FacebookException fe) {
			fe.printStackTrace();
		}
		return result;	
	}
	
	/**
	 * Gets the links created by the specific user.
	 * 
	 * @param uid the user id
	 * @param time after the specific time
	 * @return List<Link> the list of link
	 */
	public List<Link> getLinks(String uid, String time) {
		List<Link> result = new ArrayList<Link>();
		
		try {			
			String query = "SELECT link_id, owner, owner_comment, created_time, title, summary, url " + 
			   "FROM link WHERE owner = " + uid + " AND created_time >= " +  time;
			
			result = facebookClient.executeQuery(query, Link.class);
			System.out.println("Result count for link: " + result.size());
			
		} catch (FacebookException fe) {
			fe.printStackTrace();
		}
		return result;	
	}
	
	/**
	 * Gets the photos created by the specific user.
	 * 
	 * @param uid the user id
	 * @param time after the specific time
	 * @return List<Photo> the list of photo
	 */
	public List<Photo> getPhotos(String uid, String time) {
		List<Photo> result = new ArrayList<Photo>();
		
		try {
			String query = "SELECT pid, aid, owner, src_small, src_big, src, link, caption, created, modified, object_id " + 
			   "FROM photo WHERE aid IN (SELECT aid FROM album WHERE owner = " + uid + ")" + " AND created >= " +  time;
			
			result = facebookClient.executeQuery(query, Photo.class);
			System.out.println("Result count for photo: " + result.size());
			
		} catch (FacebookException fe) {
			fe.printStackTrace();
		}
		return result;	
	}
	
	/**
	 * Gets the videos created by the specific user.
	 * 
	 * @param uid the user id
	 * @param time after the specific time
	 * @return List<Video> the list of video
	 */
	public List<Video> getVideos(String uid, String time) {
		List<Video> result = new ArrayList<Video>();
		
		try {
			String query = "SELECT vid, owner, title, description, thumbnail_link, updated_time, created_time, src " + 
			   "FROM video WHERE owner = " + uid+ " AND created_time >= " +  time;
			
			result = facebookClient.executeQuery(query, Video.class);
			System.out.println("Result count for video: " + result.size());
			
		} catch (FacebookException fe) {
			fe.printStackTrace();
		}
		return result;	
	}

	private String getTimeDaysBefore(int day) {
		Calendar cal = Calendar.getInstance();

		long current = System.currentTimeMillis();
		cal.setTime(new Date(current));
		cal.add(Calendar.DAY_OF_YEAR, -day);
		long time = cal.getTimeInMillis();

		return String.valueOf(time).substring(0, 10);
	}

	public static void main(String[] args) {
		FacebookDataCollector collector = new FacebookDataCollector();		
		collector.getMyFriends(false);
		
		String uid = "100001480811442";	// Robin Koo
		String time = collector.getTimeDaysBefore(100);
				
		List<Status> statues = collector.getStatuses(uid, time);
		for (int i = 0; i < statues.size(); i++) {
			Status status = (Status)statues.get(i);
			System.out.println("status == " + status.toString());			
		}

		List<Link> links = collector.getLinks(uid, time);
		for (int i = 0; i < links.size(); i++) {
			Link link = (Link)links.get(i);
			System.out.println("link == " + link.toString());			
		}

		List<Photo> photos = collector.getPhotos(uid, time);
		for (int i = 0; i < photos.size(); i++) {
			Photo photo = (Photo)photos.get(i);
			System.out.println("photo == " + photo.toString());			
		}
		
		List<Video> videos = collector.getVideos(uid, time);
		for (int i = 0; i < videos.size(); i++) {
			Video video = (Video)videos.get(i);
			System.out.println("video == " + video.toString());
		}
		
		collector.getContents(uid, time).getContentIDs();		
		collector.getUsersCommentTo(uid, time);
		collector.getUsersLikeTo(uid, time);
	}
	
	public static class MutualFriend {  
		@Facebook
		String uid1;  
		@Facebook
		String uid2; 		
		
		@Override
		public String toString() {    
			return String.format("%s - %s", uid1, uid2);   
		}
	}
	
	public static class Contents {  
		@Facebook(contains = Status.class)  
		List<Status> statuses;
		@Facebook(contains = Link.class) 
		List<Link> links;
		@Facebook(contains = Photo.class) 
		List<Photo> photos;
		@Facebook(contains = Video.class) 
		List<Video> videos;
		
		public Set<String> getContentIDs() {
			Set<String> list = new HashSet<String>();
			if (statuses != null) {
				for (int i = 0; i < statuses.size(); i++) {
					Status status = (Status)statuses.get(i);
					list.add(status.status_id);
				}
			}
			if (links != null) {
				for (int i = 0; i < links.size(); i++) {
					Link link = (Link)links.get(i);
					list.add(link.link_id);
				}
			}
			if (photos != null) {
				for (int i = 0; i < photos.size(); i++) {
					Photo photo = (Photo)photos.get(i);
					list.add(photo.object_id);
				}
			}
			if (videos != null) {
				for (int i = 0; i < videos.size(); i++) {
					Video video = (Video)videos.get(i);
					list.add(video.vid);
				}
			}
			
			return list;
		}
	}
	
	public static class Status {
		@Facebook
		String uid;  
		@Facebook
		String status_id;
		@Facebook
		String time; 
		@Facebook
		String source;
		@Facebook
		String message;
		
		@Override
		public String toString() {    
			return String.format("%s | %s | %s | %s | %s", 
					uid, status_id, time, source, message);   
		}		
	}
	
	public static class Link {
		@Facebook
		String link_id;  
		@Facebook
		String owner;
		@Facebook
		String owner_comment; 
		@Facebook
		String created_time;
		@Facebook
		String title;
		@Facebook
		String summary;
		@Facebook
		String url;
		
		@Override
		public String toString() {    
			return String.format("%s | %s | %s | %s | %s | %s | %s", 
					link_id, owner, owner_comment, created_time, title, summary, url);   
		}		
	}
	
	public static class Photo {
		@Facebook
		String pid;  
		@Facebook
		String aid;
		@Facebook
		String owner; 
		@Facebook
		String src_small;
		@Facebook
		String src_big;
		@Facebook
		String src;
		@Facebook
		String link;
		@Facebook
		String caption;
		@Facebook
		String created;
		@Facebook
		String modified;
		@Facebook
		String object_id;
		
		@Override
		public String toString() {    
			return String.format("%s | %s | %s | %s | %s | %s | %s | %s | %s | %s | %s", 
					pid, aid, owner, src_small, src_big, src, link, caption, created, modified, object_id);   
		}		
	}
	
	public static class Video {
		@Facebook
		String vid;  
		@Facebook
		String owner;
		@Facebook
		String title; 
		@Facebook
		String description;
		@Facebook
		String thumbnail_link;
		@Facebook
		String updated_time;
		@Facebook
		String created_time;
		@Facebook
		String src;
		
		@Override
		public String toString() {    
			return String.format("%s | %s | %s | %s | %s | %s | %s | %s", 
					vid, owner, title, description, thumbnail_link, updated_time, created_time, src);   
		}		
	}

}
