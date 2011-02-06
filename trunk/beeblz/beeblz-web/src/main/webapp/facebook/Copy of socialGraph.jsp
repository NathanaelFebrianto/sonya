<html>
<head>
<title>Force-Directed Layout</title>
<script	src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script type="text/javascript" src="../js/protovis-3.2/protovis-d3.2.js"></script>
<script type="text/javascript" src="../js/graph/graph.js"></script>
<script type="text/javascript" src="../js/facebook/collector.js"></script>
<style type="text/css">
body {
	margin: 0;
}
</style>
</head>
<body>

<script type="text/javascript+protovis">

var graph = new Graph();
graph.addVertex(0, "node1", "node1", "http://profile.ak.fbcdn.net/hprofile-ak-snc4/hs224.ash2/48976_1825235343_2331_q.jpg", 1);
graph.addVertex(1, "node2", "node2", "http://profile.ak.fbcdn.net/hprofile-ak-snc4/hs224.ash2/48976_1825235343_2331_q.jpg", 1);
graph.addVertex(2, "node3", "node3", "http://profile.ak.fbcdn.net/hprofile-ak-snc4/hs224.ash2/48976_1825235343_2331_q.jpg", 2);
graph.addVertex(3, "node4", "node4", "http://profile.ak.fbcdn.net/hprofile-ak-snc4/hs224.ash2/48976_1825235343_2331_q.jpg", 2);

graph.addEdge(0, 1, 1);
graph.addEdge(0, 2, 1);
graph.addEdge(1, 3, 1);
graph.addEdge(1, 2, 1);
graph.addEdge(3, 2, 1);

var width = 750;
var height = 600;
var colors = pv.Colors.category19();

var renderer = new Graph.Renderer.Protovis(graph, width, height, colors);

</script>

<div id="fb-root"></div>
<script src="http://connect.facebook.net/en_US/all.js"></script>
<script>
	// initialize the library with the API key
	FB.init({
		appId : '174260895927181', 
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

	function login() {
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
	}
	
	FB.getLoginStatus(function(response) {
		if (!response.session) {
			return;
		}
				
		var facebook = new Facebook();
		me = facebook.getMe(function(result) {
			alert("me.id == " + result.id);
		});	
		
			
		/*
		FB.api('/me?fields=id,name,email,picture', function(response) {
			var user = response;
			//alert("me.id == " + user.id);
			//alert("me.name == " + user.name);
			//alert("me.picture == " + user.picture);
		});
		
		FB.api('/me/friends?fields=id,name,email,picture', function(response) {
			var numFriends = response.data.length;
		    if (numFriends > 0) {
		      for (var i = 0; i < numFriends; i++) {
		    	  var user = response.data[i];
		    	  //alert("id == " + user.id);
		    	  //alert("name == " + user.name);
		    	  //alert("picture == " + user.picture);
		      }
		    }
		});
		*?

		/*
		FB.api(
		{
			method: 'fql.query',
			query: 'SELECT uid, name, email, pic_square FROM user WHERE uid=' + FB.getSession().uid
		},
		function(response) {
			var user = response[0];
			alert('user name == ' + user.name);
			alert('user picture == ' + user.pic_square);
		});
		*/
	});
	
</script>
<fb:login-button autologoutlink='true' \
	show-faces='false' \
	perms='email,friends_about_me,friends_activities,friends_likes,friends_photos,friends_status,friends_videos,offline_access,publish_stream,read_friendlists,read_stream,user_about_me,user_activities,user_likes,user_photos,user_status'>
</fb:login-button>

</body>
</html>
