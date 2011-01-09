/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.view;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Graph applet.
 * 
 * @author YoungGue Bae
 */
public class GraphApplet extends JApplet {

	private static final long serialVersionUID = 3218419792123634938L;
	
	private static JApplet _instance = null;
	
	public void init() {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					//UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel");
					UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceGraphiteGlassLookAndFeel");
					//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					//UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.Plastic3DLookAndFeel");
					//UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
				getContentPane().add(new GraphPanel(getAppletContext()));
			}
		});

		_instance = this;
	}

	public void destroy() {
	}
	
	public static JApplet getInstance() {
		return _instance;
	}
}
