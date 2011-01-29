/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.webapp.web;

import com.visural.common.StringUtil;

/**
 * Facebook authentication information for my application.
 * 
 * @author YoungGue Bae
 */
public class Facebook {
	// get these from your FB Dev App
	private static final String api_key = "4fac2e03a70678c7961afdb6afbead48";
	private static final String secret = "022870dbe0a6bb9e9a8e86ea202d4210";
	private static final String client_id = "174260895927181";

	// set this to your servlet URL for the authentication servlet/filter
	private static final String redirect_uri = "http://bee-blz.appspot.com/fbauth/";

	// set this to the list of extended permissions you want
	private static final String[] perms = new String[] { 
		//"create_event",
		"email",
		"friends_about_me",
		"friends_activities",
		//"friends_events",
		"friends_likes",
		//"friends_location",
		//"friends_notes",
		//"friends_photo_video_tags",
		"friends_photos",
		"friends_status",
		"friends_videos",
		"offline_access",
		"publish_stream",
		"read_friendlists",
		//"read_mailbox",
		//"read_requests",
		"read_stream",
		"user_about_me",
		"user_activities",
		//"user_events",
		"user_likes",
		//"user_location",
		//"user_notes",
		//"user_online_presence",
		//"user_photo_video_tags",
		"user_photos",
		"user_status"
	};

	public static String getAPIKey() {
		return api_key;
	}

	public static String getSecret() {
		return secret;
	}

	public static String getLoginRedirectURL() {
		return "https://graph.facebook.com/oauth/authorize?client_id="
				+ client_id + "&display=page&redirect_uri=" + redirect_uri
				+ "&scope=" + StringUtil.delimitObjectsToString(",", perms);
	}

	public static String getAuthURL(String authCode) {
		return "https://graph.facebook.com/oauth/access_token?client_id="
				+ client_id + "&redirect_uri=" + redirect_uri
				+ "&client_secret=" + secret + "&code=" + authCode;
	}
}
