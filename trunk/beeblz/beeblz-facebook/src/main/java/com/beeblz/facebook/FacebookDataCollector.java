/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.facebook;

import java.util.HashMap;
import java.util.List;

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

	private static final String MY_ACCESS_TOKEN = "174260895927181|08c9a69debb61dbf2d983764-1480697938|r0D9FyG27SU9NSPEPQa35kx-GYE";

	private FacebookClient facebookClient;

	public FacebookDataCollector() {
		this.facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);
	}

	/**
	 * Gets my friend list.
	 * 
	 * @return GraphData the graph data with G(V,E)
	 */
	public GraphData getMyFriends() {
		GraphData graph = new GraphData();
		
		try {
			Parameter[] parameters = {Parameter.with("fields",	"id, name, email, picture, work")};
			
			User me = facebookClient.fetchObject("me", User.class, parameters);
			System.out.println("User name: " + me.getName());
			System.out.println("User ID: " + me.getId());
			
			long source = Long.valueOf(me.getId()).longValue();
			graph.addNode(source, me.getName(), me.getPicture());			
			
			// get my friends
			Connection<User> myFriends = facebookClient.fetchConnection("me/friends", User.class, parameters);
			System.out.println("Count of my friends: " + myFriends.getData().size());

			for (int i = 0; i < myFriends.getData().size(); i++) {
				User friend = (User)myFriends.getData().get(i);
				System.out.println("friend: " + friend.getId() + "|" + friend.getName() + "|" + friend.getPicture());
				
				long target = Long.valueOf(friend.getId()).longValue();
				
				graph.addNode(target, friend.getName(), friend.getPicture());
				graph.addEdge(source, target, 0.5);				
			}
			
			// get mutual friends
			List<MutualFriend> mutualFriends = this.getMutualFriends(me.getId());
			
			if (mutualFriends != null) {
		    	for (int i = 0; i < mutualFriends.size(); i++) {
		    		MutualFriend mutualFriend = (MutualFriend)mutualFriends.get(i);
		    		System.out.println("result: " + mutualFriend.toString());
		    		
		    		long uid1 = Long.valueOf(mutualFriend.uid1).longValue();
		    		long uid2 = Long.valueOf(mutualFriend.uid2).longValue();
		    		
		    		graph.addEdge(uid1, uid2, 0.5);
		    	}							
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return graph;
	}

	/**
	 * Check if two friends are mutual friends.
	 * 
	 * @param uid
	 * @return
	 */
	public List<MutualFriend> getMutualFriends(String uid) {		
		try {
			String query = "SELECT uid1, uid2 FROM friend " +
						   "WHERE uid1 IN " +
						   "(SELECT uid2 FROM friend WHERE uid1=" + uid + ") " + 
						   "AND uid2 IN " + 
						   "(SELECT uid2 FROM friend WHERE uid1=" + uid + ")";
			
			List<MutualFriend> result = facebookClient.executeQuery(query, MutualFriend.class);
			System.out.println("Result Count: " + result.size());
			
			return result;
		
		} catch (FacebookException fe) {
			fe.printStackTrace();
		}
		return null;
	}

	/**
	 * Ranks the close friend list of the specific user.
	 * 
	 * @param uid
	 */
	public void rankFriends(String uid) {
		
	}

	public static void main(String[] args) {
		FacebookDataCollector collector = new FacebookDataCollector();		
		collector.getMyFriends();
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
	};
}
