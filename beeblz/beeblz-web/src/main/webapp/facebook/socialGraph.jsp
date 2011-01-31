<html>
<head>
<title>Force-Directed Layout</title>
<script	src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script type="text/javascript" src="../js/protovis-3.2/protovis-d3.2.js"></script>
<script type="text/javascript+protovis" src="../js/graph/graph.js"></script>
<script type="text/javascript" src="../js/miserables.js"></script>
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

/*
	var vis = new pv.Panel()
	    .width(width)
	    .height(height)
	    .fillStyle("white")
	    .event("mousedown", pv.Behavior.pan())
	    .event("mousewheel", pv.Behavior.zoom());

	var force = vis.add(pv.Layout.Force)
	    .nodes(graph.vertices)
	    .links(graph.edges)
	    .springLength(50) 
	    .chargeConstant(-1750)
	    .bound(true);

	force.link.add(pv.Line)
	    .lineWidth(0.5);

	force.node.add(pv.Dot)
	    .size(500)
	    .fillStyle(function(d) d.fix ? "brown" : colors(d.group))
	    .strokeStyle(function() this.fillStyle().darker())
	    .lineWidth(1)
	    .title(function(d) d.nodeName)
	    .event("mousedown", pv.Behavior.drag())
	    .event("drag", force)
	    .add(pv.Label)
	    	.text(function(d) d.nodeName)
	    	.font("11px tahoma")
			.textAlign("right")
	  	.add(pv.Image)
	    	.left(function(d) d.x - (30/2))
	    	.top(function(d) d.y - (30/2))
	    	.url(function(d) d.picture)
	    	.imageWidth(30)
	    	.imageHeight(30)
	    	.width(30)
	    	.height(30)
			.fillStyle(null)
	    	.strokeStyle(null);

	vis.render();
*/


var renderer = new Graph.Renderer.Protovis(graph, width, height);

</script>

<!--  
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

	var g = new Graph();
	
	FB.getLoginStatus(function(response) {
		if (response.session) {
			
			FB.api('/me', function(response) {
				alert(response.name);
			});
			
			var user = collector.getMyFriends();
			alert('user == ' + user);
			alert('user name == ' + user.name);
			alert('user picture == ' + user.pic_square);
			
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
			
		} else {
			// not logged into facebook
		}
	});
	
</script>
<fb:login-button autologoutlink='true' \
	show-faces='false' \
	perms='email,friends_about_me,friends_activities,friends_likes,friends_photos,friends_status,friends_videos,offline_access,publish_stream,read_friendlists,read_stream,user_about_me,user_activities,user_likes,user_photos,user_status'>
</fb:login-button>
-->
</body>
</html>
