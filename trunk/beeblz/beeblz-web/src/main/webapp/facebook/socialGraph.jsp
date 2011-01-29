<html>
<head>
<title>Force-Directed Layout</title>
<script	src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script type="text/javascript" src="../js/protovis-3.2/protovis-r3.2.js"></script>
<script type="text/javascript" src="../js/miserables.js"></script>
<script type="text/javascript" src="../js/facebook/collector.js"></script>
<style type="text/css">
body {
	margin: 0;
}
</style>
</head>
<body>
<script type="text/javascript+protovis">

/*
var w = document.body.clientWidth,
    h = document.body.clientHeight,
    colors = pv.Colors.category19(); 
*/

var w = 730,
    h = 600,
    colors = pv.Colors.category19();

var vis = new pv.Panel()
    .width(w)
    .height(h)
    .fillStyle("white")
    .event("mousedown", pv.Behavior.pan())
    .event("mousewheel", pv.Behavior.zoom());

var force = vis.add(pv.Layout.Force)
    .nodes(miserables.nodes)
    .links(miserables.links)
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
    	.url(function(d) "http://profile.ak.fbcdn.net/hprofile-ak-snc4/hs224.ash2/48976_1825235343_2331_q.jpg")
    	.imageWidth(30)
    	.imageHeight(30)
    	.width(30)
    	.height(30)
		.fillStyle(null)
    	.strokeStyle(null);

vis.render();
</script>

<div id="user-info" style="display: none;"></div>
	
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
		if (response.session) {
			alert('uid == ' + FB.getSession().uid);
			var user = getMyFriends(response);
			alert('user == ' + user);
			alert('user picture == ' + user.picture);
		} else {
			// not logged into facebook
		}
	});
	
</script>
<fb:login-button autologoutlink='true' \
	show-faces='false' \
	perms='email,friends_about_me,friends_activities,friends_likes,friends_photos,friends_status,friends_videos,offline_access,publish_stream,read_friendlists,read_stream,user_about_me,user_activities,user_likes,user_photos,user_status'>
</fb:login-button>
</body>
</html>