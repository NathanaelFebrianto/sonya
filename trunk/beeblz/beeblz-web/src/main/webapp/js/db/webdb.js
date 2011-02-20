/**
 * Copyright Beeblz.com.
 * webdb.js
 *
 */

/**
 * db base class.
 */
webdb = {};

/**
 * DB Manager. 
 */
webdb.DBManager = function() {
	this.db = null;
	this.dbName = "beeblz";
	this.version = "1.0";
	this.displayName = "beeblz social graph data";
};

webdb.DBManager.prototype = {
	
	/*
	 * Opens a new database. 
	 * 
	 * @return database the database
	 */
	openDatabase : function() {
		this.db = window.openDatabase(this.dbName, this.version, this.displayName, 10*1024*1024);		 
		return this.db;
	}
};

/**
 * Vertex.
 */
webdb.Vertex = function() {
	this.id;
	this.name;
	this.email;
	this.picture;
	this.cluster;
	this.isMe;
	this.isMyFriend;
	this.mutualFriendCount;
};

webdb.Vertex.prototype = {
	// getter
	getId : function() { return this.id },
	getName : function() { return this.name },
	getEmail : function() { return this.email },
	getPicture : function() { return this.picture },
	getCluster : function() { return this.cluster },
	getIsMe : function() { return this.isMe },
	getIsMyFriend : function() { return this.isMyFriend },
	getMutualFriendCount : function() { return this.mutualFriendCount },
	
	// setter
	setId : function(id) { this.id = id },
	setName : function(name) { this.name = name },
	setEmail : function(email) { this.email = email },
	setPicture : function(picture) { this.picture = picture },
	setCluster : function(cluster) { this.cluster = cluster },
	setIsMe : function(isMe) { this.isMe = isMe },
	setIsMyFriend : function(isMyFriend) { this.isMyFriend = isMyFriend },
	setMutualFriendCount : function(mutualFriendCount) { this.mutualFriendCount = mutualFriendCount }
};

/**
 * Vertex DB Manager.
 */
webdb.VertexManager = function(db) {
	this.db = db;
};

webdb.VertexManager.prototype = {
	
	/*
	 * Creates a table for vertex.
	 * 
	 */
	createTable : function() {
		this.db.transaction(function(tx) {
			tx.executeSql("CREATE TABLE vertex(id UNIQUE, name, email, picture, cluster, is_me, is_my_friend, mutual_friend_count)");
		});
	},
	
	/*
	 * Inserts a vertex.
	 * 
	 * @param vertex the vertex object
	 */
	insert: function(vertex) {
		db.transaction(function(tx) {
			tx.executeSql("INSERT INTO vertex values(?,?,?,?,?,?,?,?)", 
				[vertex.id, vertex.name, vertex.email, vertex.picture, vertex.cluster, vertex.isMe, vertex.isMyFriend, vertex.mutualFriendCount]);
		});
	}
	
};

/**
 * Edge.
 */
webdb.Edge = function() {
	this.id;
	this.id1;
	this.id2;
	this.isMe;
	this.isMyFriend;
	this.commentCount;
	this.likeCount;
	this.weight;
};

webdb.Edge.prototype = {
	// getter
	getId : function() { return this.id },
	getId1 : function() { return this.id1 },
	getId2 : function() { return this.id2 },
	getIsMe : function() { return this.isMe },
	getIsMyFriend : function() { return this.isMyFriend },
	getCommentCount : function() { return this.commentCount },
	getLikeCount : function() { return this.likeCount },
	getWeight : function() { return this.weight },
	
	// setter
	setId : function(id) { this.id = id },
	setId1 : function(id1) { this.id1 = id1 },
	setId2 : function(id2) { this.id2 = id2 },
	setIsMe : function(isMe) { this.isMe = isMe },
	setIsMyFriend : function(isMyFriend) { this.isMyFriend = isMyFriend },
	setCommentCount : function(commentCount) { this.commentCount = commentCount },
	setLikeCount : function(likeCount) { this.likeCount = likeCount },
	setWeight : function(weight) { this.weight = weight }
};

/**
 * Edge DB Manager.
 */
webdb.EdgeManager = function(db) {
	this.db = db;
};

webdb.EdgeManager.prototype = {
	
	/*
	 * Creates a table for vertex.
	 * 
	 */
	createTable : function() {
		this.db.transaction(function(tx) {
			tx.executeSql("CREATE TABLE edge(id UNIQUE, id1, id2, is_me, is_my_friend, comment_count, like_count, weight)");
		});
	},
	
	/*
	 * Inserts a edge.
	 * 
	 * @param edge the edge object
	 */
	insert: function(edge) {
		db.transaction(function(tx) {
			tx.executeSql("INSERT INTO edge values(?,?,?,?,?,?,?,?)", 
				[edge.id, edge.id1, edge.id2, edge.isMe, edge.isMyFriend, edge.commentCount, edge.likeCount, edge.weight]);
		});
	}
	
};



