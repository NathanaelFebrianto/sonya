/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.beeblz.db.DbManager;

/**
 * Graph applet.
 * 
 * @author YoungGue Bae
 */
public class GraphApplet extends JApplet {

	private static final long serialVersionUID = 3218419792123634938L;
	
	private static JApplet _instance = null;
	public GraphPanel panelGraph = null; 
	
	public void init() {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel");
					//UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceGraphiteAquaLookAndFeel");
					//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					//UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.Plastic3DLookAndFeel");
					//UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
				String accessToken = getParameter("access_token");
				String alignment = getParameter("alignment");
				
				// just for test
				//accessToken = "174260895927181|1a5694cf627f262374c974bd-1480697938|EYTy5jF8f6ph2cesWyY7jFjICV4";
				 
				panelGraph = new GraphPanel(getAppletContext(), accessToken, alignment);
				getContentPane().add(panelGraph);
				panelGraph.init();	
			}
		});
		
		_instance = this;
	}

	public void destroy() {
		try {
			Connection conn = DriverManager.getConnection(DbManager.DB_URL);
			conn.close();
			System.out.println("DB connection[" + DbManager.DB_URL + "] is closed.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static JApplet getInstance() {
		return _instance;
	}
}
