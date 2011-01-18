/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * This is a DB manager for apache derby embedded database.
 * 
 * @author YoungGue Bae
 */
public class DbManager {
	
	//public static final String DB_URL = "jdbc:derby:beeblz_" + System.currentTimeMillis() + ";create=true";
	public static final String PROTOCOL = "jdbc:derby:";
	public static final String DBNAME = "../beeblz"; 
	
	
	
	static{
		System.setProperty("derby.stream.error.file", "../derby.log");
		
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 		
	}
	
	public static Connection getConnection(){
		try {
			Connection conn = DriverManager.getConnection(PROTOCOL + DBNAME + ";create=true");
			//Connection conn = DriverManager.getConnection("jdbc:derby:C:\\Users\\Louie\\MyDB;create=true");
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Closes connetion.
	 */
	public static void close() {
		try {
			Connection conn = DriverManager.getConnection(PROTOCOL + DBNAME);
			conn.close();
			System.out.println("DB connection[" + PROTOCOL + DBNAME + "] is closed.");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Shutdowns database.
	 */
	public static void shutdown() {
        try {
            // the shutdown=true attribute shuts down Derby
            DriverManager.getConnection("jdbc:derby:;shutdown=true");

            // To shut down a specific database only, but keep the
            // engine running (for example for connecting to other
            // databases), specify a database in the connection URL:
            //DriverManager.getConnection("jdbc:derby:" + DBNAME + ";shutdown=true");
        } catch (SQLException se) {
            if (( (se.getErrorCode() == 50000)
                    && ("XJ015".equals(se.getSQLState()) ))) {
                // we got the expected exception
                System.out.println("Derby shut down normally");
                // Note that for single database shutdown, the expected
                // SQL state is "08006", and the error code is 45000.
            } else {
                // if the error code or SQLState is different, we have
                // an unexpected exception (shutdown failed)
                System.err.println("Derby did not shut down normally");
                se.printStackTrace();
            }
        }		
	}
}
