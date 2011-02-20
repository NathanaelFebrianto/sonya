/**
 * Copyright Beeblz.com.
 * webdb.js
 *
 */

//var db;

/**
 * DB Manager. 
 */
db.DBManager = function() {
	this.db = null;
	this.dbName = "beeblz";
	this.version = "1.0";
	this.displayName = "beeblz social graph data";
};

db.DBManager.prototype = {
	
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
db.Vertex = function() {
	this.id;
	this.name;
	this.email;
	this.picture;
	this.cluster;
	this.isMe;
	this.imMyFriend;
	this.mutualFriendCount;
};

db.Vertex.prototype = {
	// getter
	getId : function() { return this.id },
	getName : function() { return this.name },
	getEmail : function() { return this.email },
	getPicture : function() { return this.picture },
	getCluster : function() { return this.cluster },
	isMe : function() { return this.isMe },
	isMyFriend : function() { return this.imMyFriend },
	getMutualFriendCount : function() { return this.mutualFriendCount },
	
	// setter
	setId : function(id) { this.id = id },
	setName : function(name) { this.name = name },
	setEmail : function(email) { this.email = email },
	setPicture : function(picture) { this.picture = picture },
	setCluster : function(cluster) { this.cluster = cluster },
	setIsMe : function(isMe) { this.isMe = isMe },
	setIsMyFriend : function(imMyFriend) { this.imMyFriend = imMyFriend },
	setMutualFriendCount : function(mutualFriendCount) { this.mutualFriendCount = mutualFriendCount }
};

/**
 * Vertex DB Manager.
 */
db.VertexManager = function(db) {
	this.db = db;
};

db.VertexManager.prototype = {
	
	/*
	 * Creates a table for vertex.
	 * 
	 */
	createTable : function() {
		this.db.transaction(function(tx) {
			tx.executeSql("CREATE TABLE vertex(id, name, email, picture, cluster, is_me, is_my_friend, mutual_friend_count)");
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

