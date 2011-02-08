<html>
<head>
<title>Force-Directed Layout</title>
<script	src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script type="text/javascript" src="../js/protovis-3.2/protovis-r3.2.js"></script>
<script type="text/javascript" src="../js/graph/graph.js"></script>
<script type="text/javascript" src="../js/facebook/facebook.js"></script>
<style type="text/css">
body {
	margin: 0;
}
</style>
</head>
<body>

<div id="fb-root"></div>
<script src="http://connect.facebook.net/en_US/all.js"></script>
<script>
	var graph = new Graph();

	var facebook = new Facebook();
	// init facebook api.
	facebook.init('174260895927181');
	
	// check if login status.
	facebook.isLogin(function(response) {
		if (!response.session) {
			facebook.login();
		} else {
			// get me.
			facebook.getMe(function(response) {
				console.log("me.id == " + response.id);
				//graph.addVertex(response.id, response.name, response.picture, 1);
			});
			
			// get my friends.
			facebook.getMyFriends(function(response) {
				var numFriends = response.data.length;
				console.log("numFriends == " + numFriends);	    
				if (numFriends > 0) {
					for (var i = 0; i < numFriends; i++) {
						var user = response.data[i];
						console.log("friend == " + user.name);	  
						graph.addVertex(user.id, user.name, user.picture, 1);
					}
				}
				
				// get mutual friends.
				facebook.getMutualFriends(function(response) {
					var numMutualFriends = response.length;
					console.log("numMutualFriends == " + numMutualFriends);	  
					if (numMutualFriends > 0) {
						for ( var i = 0; i < numMutualFriends; i++) {
							var mutualFriend = response[i];
							//console.log("uid1 == " + mutualFriend.uid1);
							//console.log("uid2 == " + mutualFriend.uid2);
							graph.addEdge(mutualFriend.uid1, mutualFriend.uid2, 1);
							//graph.addEdge(mutualFriend.uid2, mutualFriend.uid1, 1);
						}
					}
					
					// draw a graph.
					//var width = 750;
					//var height = 600;
					var width = document.body.clientWidth;
					var height = document.body.clientHeight;
					var colors = pv.Colors.category19();

					var visualization = new Graph.Visualization.Protovis(graph, width, height, colors);
				});	
			});
		}
	});
	

</script>

<!--  
<fb:login-button autologoutlink='true' \
	show-faces='false' \
	perms='email,friends_about_me,friends_activities,friends_likes,friends_photos,friends_status,friends_videos,offline_access,publish_stream,read_friendlists,read_stream,user_about_me,user_activities,user_likes,user_photos,user_status'>
</fb:login-button>
-->

</body>
</html>
