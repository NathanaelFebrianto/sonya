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
 * This is a DB manager for edge.
 * 
 * @author YoungGue Bae
 */
public class EdgeManager {

	private static final String CREATE_TABLE_SQL = 
		"CREATE TABLE edge ("
		+ "id varchar(50) not null, "
		+ "id1 varchar(20), "
		+ "id2 varchar(20), "
		+ "is_me char(1), "
		+ "is_my_friend char(1), "
		+ "comment_id1_to_id2 integer, "
		+ "like_id1_to_id2 integer, "
		+ "comment_id2_to_id1 integer, "
		+ "like_id2_to_id1 integer, "
		+ "closeness_id1_to_id2 integer, "
		+ "closeness_id2_to_id1 integer, "
		+ "mutual_closeness integer, "
		+ "PRIMARY KEY (id, id1, id2)"
		+ ")";
	private static final String SELECT_SQL = 
		"SELECT id, id1, id2, is_me, is_my_friend, comment_id1_to_id2, like_id1_to_id2, "
		+ "comment_id2_to_id1, like_id2_to_id1, closeness_id1_to_id2, closeness_id2_to_id1, mutual_closeness "
		+ "FROM edge ";
	private static final String INSERT_SQL = 
		"INSERT INTO edge (" 
		+ "id, id1, id2, is_me, is_my_friend, comment_id1_to_id2, like_id1_to_id2, "
		+ "comment_id2_to_id1, like_id2_to_id1, closeness_id1_to_id2, closeness_id2_to_id1, mutual_closeness) " 
		+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * This guarantees that the table needed for this class already exists by
	 * the time the class is loaded. This eats any SQL exception caused by
	 * trying to create the table if it already exists.
	 */
	static {
		Connection conn = DbManager.getConnection();
		try {
			//conn.createStatement().execute("DROP TABLE edge");
			conn.createStatement().execute(CREATE_TABLE_SQL);
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("Table already existed");
		}
	}
	
	/**
	 * Retrieves all of the edges satisfying a SQL where clause. The clause
	 * can also include things like order and limit decorators.
	 * 
	 * @param clause a SQL where clause
	 * @return List<Edge> a List of all edges satisfying the where clause
	 */
	public static List<Edge> getEdges(String clause) {
		if (clause == null)
			clause = "";
		
		String sql = SELECT_SQL	+ clause;
		
		System.out.println(sql);
		
		Connection conn = DbManager.getConnection();
		List<Edge> edges = new ArrayList<Edge>();
		
		try {
			ResultSet cursor = conn.createStatement().executeQuery(sql);
			while (cursor.next()) {
				Edge edge = new Edge();
				edge.setId(cursor.getString(1));
				edge.setId1(cursor.getString(2));
				edge.setId2(cursor.getString(3));
				edge.setIsMe(cursor.getBoolean(4));
				edge.setIsMyFriend(cursor.getBoolean(5));
				edge.setCommentId1ToId2(cursor.getInt(6));
				edge.setLikeId1ToId2(cursor.getInt(7));
				edge.setCommentId2ToId1(cursor.getInt(8));
				edge.setLikeId2ToId1(cursor.getInt(9));
				edge.setClosenessId1ToId2(cursor.getInt(10));
				edge.setClosenessId2ToId1(cursor.getInt(11));
				edge.setMutualCloseness(cursor.getInt(12));
				edges.add(edge);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return edges;
	}
	
	/**
	 * Inserts a edge to the database.
	 * 
	 * @param edge a edge model
	 */
	public static void insert(Edge edge) {		
		
		Connection conn = DbManager.getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(INSERT_SQL);
			ps.setString(1, edge.getId());
			ps.setString(2, edge.getId1());
			ps.setString(3, edge.getId2());
			ps.setBoolean(4, edge.getIsMe());
			ps.setBoolean(5, edge.getIsMyFriend());
			ps.setInt(6, edge.getCommentId1ToId2());
			ps.setInt(7, edge.getLikeId1ToId2());
			ps.setInt(8, edge.getCommentId2ToId1());
			ps.setInt(9, edge.getLikeId2ToId1());
			ps.setInt(10, edge.getClosenessId1ToId2());
			ps.setInt(11, edge.getClosenessId2ToId1());
			ps.setInt(12, edge.getMutualCloseness());
			ps.executeUpdate();
			
			System.out.println("Edge is saved. new id = " + edge.getId());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes all the edges from the database.
	 */
	public static void deleteAll() {
		Connection conn = DbManager.getConnection();
		String sql = "DELETE FROM edge";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			System.out.println("All edges are deleted.");
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes a edge from the database.
	 * 
	 * @param id the edge id
	 */
	public static void delete(String id) {
		Connection conn = DbManager.getConnection();
		String sql = "DELETE FROM edge WHERE id=?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, id);
			ps.executeUpdate();
			System.out.println("Edge is deleted. id = " + id);
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * Updates a edge from the database.
	 * 
	 * @param edge the edge model
	 */
	public static void update(Edge edge) {
		Connection conn = DbManager.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE edge SET ");
		sql.append("id=id");		
		if (edge.getIsMe() != null && edge.getIsMe() == true)	
			sql.append(", ").append("is_me=").append("'1'");
		if (edge.getIsMyFriend() != null && edge.getIsMyFriend() == true)	
			sql.append(", ").append("is_my_friend=").append("'1'");
		if (edge.getCommentId1ToId2() >= 0)	
			sql.append(", ").append("comment_id1_to_id2=").append(edge.getCommentId1ToId2());
		if (edge.getLikeId1ToId2() >= 0)	
			sql.append(", ").append("like_id1_to_id2=").append(edge.getLikeId1ToId2());
		if (edge.getCommentId2ToId1() >= 0)	
			sql.append(", ").append("comment_id2_to_id1=").append(edge.getCommentId2ToId1());
		if (edge.getLikeId2ToId1() >= 0)	
			sql.append(", ").append("like_id2_to_id1=").append(edge.getLikeId2ToId1());
		if (edge.getClosenessId1ToId2() >= 0)	
			sql.append(", ").append("closeness_id1_to_id2=").append(edge.getClosenessId1ToId2());
		if (edge.getClosenessId2ToId1() >= 0)	
			sql.append(", ").append("closeness_id2_to_id1=").append(edge.getClosenessId2ToId1());
		if (edge.getMutualCloseness() >= 0)	
			sql.append(", ").append("mutual_closeness=").append(edge.getMutualCloseness());
		sql.append(" WHERE id=?");
		
		System.out.println(sql.toString());

		try {
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			ps.setString(1, edge.getId());
			ps.executeUpdate();
			System.out.println("Edge is updated. id = " + edge.getId());
		} catch (SQLException e){
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args) {

	}

	
}
