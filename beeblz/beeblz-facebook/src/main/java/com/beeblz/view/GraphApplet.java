/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.view;

import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.beeblz.db.DbManager;
import com.beeblz.db.EdgeManager;
import com.beeblz.db.VertexManager;

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
				//accessToken = "174260895927181|840f2a247cfde3146badd2fc-1480697938|37Ov4jPcGJrh6e1RJXUSBDc0LXM";	// louie
				//accessToken = "174260895927181|1aa4354dcc3866c06f5cc712-708185302|6ablX5Oibz8_9XJGJf5gHesceR0";		// sonya
				 
				panelGraph = new GraphPanel(getAppletContext(), accessToken, alignment);
				getContentPane().add(panelGraph);
				panelGraph.init();	
			}
		});
		
		_instance = this;
	}

	public void destroy() {
		VertexManager.dropTable();
		EdgeManager.dropTable();
		
		DbManager.close();
		DbManager.shutdown();
	}
	
	public static JApplet getInstance() {
		return _instance;
	}
}
