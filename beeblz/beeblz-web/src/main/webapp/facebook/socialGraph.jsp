<html>
<head>
<title>Beeblz - Social Graph for Facebook</title>
<meta charset="utf-8">
<link rel="stylesheet" type="text/css" href="/css/main.css"/>
<link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css"/>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>
<script type="text/javascript" src="../js/protovis/protovis-r3.3.js"></script>
<script type="text/javascript" src="../js/graph/graph.js"></script>
<script type="text/javascript" src="../js/facebook/facebook.js"></script>
<script type="text/javascript" src="../js/db/webdb.js"></script>
</head>
<body>

<div id="fb-root"></div>
<script src="http://connect.facebook.net/en_US/all.js"></script>
<script>
	var graph = new Graph();

	var facebook = new Facebook();
	// init facebook api.
	facebook.init('174260895927181');
	
	var supportDb = false;
	if (window.openDatabase) {
		supportDb = true;
		// create a web database.
		var db = new webdb.DBManager().openDatabase();
		var vertexManager = new webdb.VertexManager(db);
		vertexManager.createTable();
		
		var edgeManager = new webdb.EdgeManager(db);
		edgeManager.createTable();
	} else {
		//alert("This browser does not support Web SQL Database.");
	}	
		
	// check if login status.
	facebook.isLogin(function(response) {
		if (!response.session) {
			facebook.login();
		} else {
			var myId = null;
			// get me.
			facebook.getMe(function(response) {
				//console.log("me.id == " + response.id);
				//graph.addVertex(response.id, response.name, response.picture, 1);
				myId = response.id;
				if (supportDb) {
					var vertexVO = new webdb.Vertex();
					vertexVO.setId(response.id);
					vertexVO.setName(response.name);
					vertexVO.setEmail(response.email);
					vertexVO.setPicture(response.picture);
					vertexVO.setCluster(1);
					vertexVO.setIsMe(true);
					vertexVO.setIsMyFriend(false);
					//vertexVO.setMutualFriendCount();
					
					vertexManager.insert(vertexVO);			
				}				
			});
			
			// get my friends.
			facebook.getMyFriends(function(response) {
				var numFriends = response.data.length;
				//console.log("numFriends == " + numFriends);	    
				if (numFriends > 0) {
					for (var i = 0; i < numFriends; i++) {
						var user = response.data[i];
						//console.log("friend == " + user.name);	  
						graph.addVertex(user.id, user.name, user.picture, 1);
						
						if (supportDb) {
							var vertexVO = new webdb.Vertex();
							vertexVO.setId(user.id);
							vertexVO.setName(user.name);
							vertexVO.setEmail(user.email);
							vertexVO.setPicture(user.picture);
							vertexVO.setCluster(1);
							vertexVO.setIsMe(false);
							vertexVO.setIsMyFriend(true);
							//vertexVO.setMutualFriendCount();
							
							vertexManager.insert(vertexVO);	
							
							var edgeVO1 = new webdb.Edge();
							edgeVO1.setId(myId + ":"+ user.id);
							edgeVO1.setId1(myId);
							edgeVO1.setId2(user.id);
							edgeVO1.setIsMe(true);
							edgeVO1.setIsMyFriend(true);
							//edgeVO1.setCommentCount(0);
							//edgeVO1.setLikeCount(0);
							//edgeVO1.setWeight(0);
							
							var edgeVO2 = new webdb.Edge();
							edgeVO2.setId(user.id + ":"+ myId);
							edgeVO2.setId1(user.id);
							edgeVO2.setId2(myId);
							edgeVO2.setIsMe(true);
							edgeVO2.setIsMyFriend(true);
							//edgeVO2.setCommentCount(0);
							//edgeVO2.setLikeCount(0);
							//edgeVO2.setWeight(0);
							
							edgeManager.insert(edgeVO1);
							edgeManager.insert(edgeVO2);
						}						
					}
				}
				
				// get mutual friends.
				facebook.getMutualFriends(function(response) {
					var numMutualFriends = response.length;
					//console.log("numMutualFriends == " + numMutualFriends);	  
					if (numMutualFriends > 0) {
						for ( var i = 0; i < numMutualFriends; i++) {
							var mutualFriend = response[i];
							//console.log("uid1 == " + mutualFriend.uid1);
							//console.log("uid2 == " + mutualFriend.uid2);
							graph.addEdge(mutualFriend.uid1, mutualFriend.uid2, 1);
							//graph.addEdge(mutualFriend.uid2, mutualFriend.uid1, 1);
							
							if (supportDb) {
								var edgeVO = new webdb.Edge();
								edgeVO.setId(mutualFriend.uid1 + ":"+ mutualFriend.uid2);
								edgeVO.setId1(mutualFriend.uid1);
								edgeVO.setId2(mutualFriend.uid2);
								edgeVO.setIsMe(false);
								edgeVO.setIsMyFriend(true);
								//edgeVO.setCommentCount(0);
								//edgeVO.setLikeCount(0);
								//edgeVO.setWeight(0);
								
								edgeManager.insert(edgeVO);	
							}							
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

</body>
</html>
