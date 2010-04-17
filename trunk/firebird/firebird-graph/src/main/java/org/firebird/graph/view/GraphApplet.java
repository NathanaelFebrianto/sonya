/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view;

import java.applet.AppletContext;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.firebird.common.http.HttpCommunicateClient;

/**
 * Graph applet.
 * 
 * @author Young-Gue Bae
 */
public class GraphApplet extends JApplet {

	private static final long serialVersionUID = 3218419792123634938L;
	
	public void init() {
		initHttpClient();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceCremeLookAndFeel");
					//UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceRavenGraphiteLookAndFeel");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				getContentPane().add(new GraphPanel(getAppletContext()));
			}
		});
		
		//System.out.println("Look & Feel == " + UIManager.getLookAndFeel());
	}

	private void initHttpClient() {
		try {
			String strUrl = getCodeBase() + "communicate"; // "communicate" is servlet url
			System.out.println("Servlet URL == " + strUrl);
			HttpCommunicateClient.init(new URL(strUrl));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void destroy() {
	}
}
