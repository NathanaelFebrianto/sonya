/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a DB manager for vertex.
 * 
 * @author YoungGue Bae
 */
public class VertexManager {
	
	private static final String CREATE_TABLE_SQL = 
			"CREATE TABLE vertex ("
			+ "id integer not null, "
			+ "name varchar(100), "
			+ "email varchar(100), "
			+ "picture varchar(100), "
			+ "is_me char(1), "
			+ "is_my_friend char(1), "
			+ "mutual_friend_count integer, "
			+ "cluster integer, "
			+ "post_status_count integer, "
			+ "post_link_count integer, "
			+ "post_photo_count integer, "
			+ "post_video_count integer, "
			+ "PRIMARY KEY (id)"
			+ ")";
	private static final String SELECT_SQL = 
			"SELECT id, name, email, picture, is_me, is_my_friend, mutual_friend_count, "
			+ "cluster, post_status_count, post_link_count, post_photo_count, post_video_count "
			+ "FROM vertex "
			+ "WHERE 1=1 ";	
	private static final String INSERT_SQL = 
			"INSERT INTO vertex (" 
			+ "id, name, email, picture, is_me, is_my_friend, mutual_friend_count, "
			+ "cluster, post_status_count, post_link_count, post_photo_count, post_video_count) " 
			+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * This guarantees that the table needed for this class already exists by
	 * the time the class is loaded. This eats any SQL exception caused by
	 * trying to create the table if it already exists.
	 */
	static {
		Connection conn = DbManager.getConnection();
		try {
			//conn.createStatement().execute("DROP TABLE vertex");
			conn.createStatement().execute(CREATE_TABLE_SQL);
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("Table already existed");
		}
	}
	
	/**
	 * Retrieves all of the vertices satisfying a SQL where clause. The clause
	 * can also include things like order and limit decorators.
	 * 내 친구 가져오기(본인제외), 친구의 친구 가져오기(본인제외, 나 포함), 같은 클러스터에 속한 친구아닌 사람 가져오기
	 * 
	 * @param clause a SQL where clause
	 * @return List<Vertex> a List of all contacts satisfying the where clause
	 */
	public static List<Vertex> getVertices(String clause) {
		if (clause == null)
			clause = "";
		
		String sql = SELECT_SQL	+ clause;
		
		Connection conn = DbManager.getConnection();
		List<Vertex> vertices = new ArrayList<Vertex>();
		
		try {
			ResultSet cursor = conn.createStatement().executeQuery(sql);
			while (cursor.next()) {
				Vertex vertex = new Vertex();
				vertex.setId(cursor.getString(1));
				vertex.setName(cursor.getString(2));
				vertex.setEmail(cursor.getString(3));
				vertex.setPicture(cursor.getString(4));
				vertex.setIsMe(cursor.getBoolean(5));
				vertex.setIsMyFriend(cursor.getBoolean(6));
				vertex.setMutualFriendCount(cursor.getInt(7));
				vertex.setCluster(cursor.getInt(8));
				vertex.setPostStatusCount(cursor.getInt(9));
				vertex.setPostLinkCount(cursor.getInt(10));
				vertex.setPostPhotoCount(cursor.getInt(11));
				vertex.setPostVideoCount(cursor.getInt(12));
				vertices.add(vertex);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vertices;
	}
	
	/**
	 * Inserts a vertex to the database.
	 * 
	 * @param vertex a vertex model
	 */
	public static void insert(Vertex vertex) {		
		
		Connection conn = DbManager.getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(INSERT_SQL);
			ps.setString(1, vertex.getId());
			ps.setString(2, vertex.getName());
			ps.setString(3, vertex.getEmail());
			ps.setString(4, vertex.getPicture());
			ps.setBoolean(5, vertex.getIsMe());
			ps.setBoolean(6, vertex.getIsMyFriend());
			ps.setInt(7, vertex.getMutualFriendCount());
			ps.setInt(8, vertex.getCluster());
			ps.setInt(9, vertex.getPostStatusCount());
			ps.setInt(10, vertex.getPostLinkCount());
			ps.setInt(11, vertex.getPostPhotoCount());
			ps.setInt(12, vertex.getPostVideoCount());
			ps.executeUpdate();
			
			System.out.println("Vertex is saved. new id = " + vertex.getId());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes all the vertices from the database.
	 */
	public static void deleteAll() {
		Connection conn = DbManager.getConnection();
		String sql = "DELETE FROM vertex";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			System.out.println("All vertices are deleted.");
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes a vertex from the database.
	 * 
	 * @param id the vertex id
	 */
	public static void delete(String id) {
		Connection conn = DbManager.getConnection();
		String sql = "DELETE FROM vertex WHERE id=?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.executeUpdate();
			System.out.println("Vertex is deleted. id = " + id);
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * Updates a vertex from the database.
	 * 
	 * @param vertex the vertex model
	 */
	public static void update(Vertex vertex) {
		Connection conn = DbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE vertex SET ");
		if (vertex.getName() != null)	
			sql.append("name='").append(vertex.getName()).append("'");
		if (vertex.getEmail() != null)	
			sql.append(", ").append("email='").append(vertex.getEmail()).append("'");
		if (vertex.getPicture() != null)	
			sql.append(", ").append("picture='").append(vertex.getPicture()).append("'");
		if (vertex.getIsMe() == true)	
			sql.append(", ").append("is_me=").append("'1'");
		if (vertex.getIsMyFriend() == true)	
			sql.append(", ").append("is_my_friend=").append("'1'");
		if (vertex.getMutualFriendCount() >= 0)	
			sql.append(", ").append("mutual_friend_count=").append(vertex.getMutualFriendCount());
		if (vertex.getCluster() >= 0)	
			sql.append(", ").append("cluster=").append(vertex.getCluster());
		if (vertex.getPostStatusCount() >= 0)	
			sql.append(", ").append("post_status_count=").append(vertex.getPostStatusCount());
		if (vertex.getPostLinkCount() >= 0)	
			sql.append(", ").append("post_link_count=").append(vertex.getPostLinkCount());
		if (vertex.getPostPhotoCount() >= 0)	
			sql.append(", ").append("post_photo_count=").append(vertex.getPostPhotoCount());
		if (vertex.getPostVideoCount() >= 0)	
			sql.append(", ").append("post_video_count=").append(vertex.getPostVideoCount());
		sql.append(" WHERE id=?");
		
		System.out.println(sql.toString());

		try {
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ps.setString(1, vertex.getId());
			ps.executeUpdate();
			System.out.println("Vertex is updated. id = " + vertex.getId());
		} catch (SQLException e){
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {
		Vertex vertex = new Vertex();
		vertex.setId("2222");
		vertex.setName("Test");
		vertex.setEmail("");
		vertex.setPicture("picture");
		vertex.setIsMyFriend(true);
		vertex.setMutualFriendCount(10);
		
		//VertexManager.insert(vertex);
		List<Vertex> vertices = VertexManager.getVertices("");
		vertex.setPostLinkCount(55);
		VertexManager.update(vertex);
		System.out.println("vertices == " + vertices.size());
	}

}
