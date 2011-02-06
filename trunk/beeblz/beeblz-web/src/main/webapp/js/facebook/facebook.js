/**
 * Copyright Beeblz.com.
 * facebook.js
 *
 */

/**
 * Facebook. 
 */
var Facebook = function() {};


Facebook.prototype = {
	/*
	 * Initializes the facebook library with the API key.
	 * 
	 * @param appId the application id.
	 */
	init : function(appId) {
		FB.init({
			appId : appId, 
		    status : true, // check login status
		    cookie : true, // enable cookies to allow the server to access the session
		    xfbml  : true  // parse XFBML
		});
		
		FB.Event.subscribe('auth.sessionChange', function(response) {
			if (response.session) {
				// A user has logged in, and a new cookie has been saved
				//window.location.reload();
			} else {
				//window.location.reload();
				// alert('logged out of Facebook.com, push ok to continue.')
				// The user has logged out, and the cookie has been cleared
			}
		});
	},

	/*
	 * Is login?
	 * 
	 * @return response
	 */
	isLogin : function(callback) {
		FB.getLoginStatus(callback);
	},
	
	/*
	 * Login.
	 */
	login : function() {
		FB.login(function(response) {
			if (response.session) {
				if (response.perms) {
					// user is logged in and granted some permissions.
					// perms is a comma separated list of granted permissions
					window.location.reload();
				} else {
					// user is logged in, but did not grant any permissions
					window.location.reload();
				}
			} else {
				// user is not logged in
			}
		}, {
			perms : 'email,friends_about_me,friends_activities,friends_likes,friends_photos,friends_status,friends_videos,offline_access,publish_stream,read_friendlists,read_stream,user_about_me,user_activities,user_likes,user_photos,user_status'
		});	
	},
	
	/*
	 * Gets me. 
	 * 
	 * @return response
	 */
	getMe : function(callback) {
		FB.api('/me?fields=id,name,email,picture', callback);
	},

	/*
	 * Gets my friends. 
	 * 
	 * @return response
	 */
	getMyFriends : function(callback) {
		FB.api('/me/friends?fields=id,name,email,picture', callback);
	},
	
	/*
	 * Gets mutual friends. 
	 * 
	 * @return response
	 */
	getMutualFriends : function(callback) {
		FB.api(
			{
				method: 'fql.query',
				query: 'SELECT uid1, uid2 FROM friend WHERE uid1 IN (SELECT uid2 FROM friend WHERE uid1=me()) AND uid2 IN (SELECT uid2 FROM friend WHERE uid1=me())'
			},
			callback);
	}
};