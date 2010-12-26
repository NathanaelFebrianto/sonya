/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This is a DB manager for apache derby embedded database.
 * 
 * @author YoungGue Bae
 */
public class DbManager {
	static{
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}
	
	public static Connection getConnection(){
		try {
			Connection conn = DriverManager.getConnection("jdbc:derby:beeblz;create=true");
			//Connection conn = DriverManager.getConnection("jdbc:derby:C:\\Users\\Louie\\MyDB;create=true");
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
