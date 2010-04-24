/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view;

import java.awt.BorderLayout;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
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
	
	private static JApplet _instance = null;
	
	public void init() {
		
		final String mode = getParameter("screenMode");
		final int width = Integer.parseInt(getParameter("frameWidth"));
		final int height = Integer.parseInt(getParameter("frameHeight"));
		
		initHttpClient();

		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceCremeLookAndFeel");
					//UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceRavenGraphiteLookAndFeel");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
				if (mode.equalsIgnoreCase("frame")) {
					setupDefaultUI();
					GraphFrame frame = new GraphFrame(GraphApplet.this);
					frame.setSize(width, height);
					frame.setVisible(true);
				}
				else if (mode.equalsIgnoreCase("applet")) {
					getContentPane().add(new GraphPanel(getAppletContext()));
				}
				else {
					getContentPane().add(new GraphPanel(getAppletContext()));
				}
			}
		});
		
		_instance = this;
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
	
	private void setupDefaultUI() {
		getContentPane().setLayout(new BorderLayout());
		JPanel panel = new JPanel(new BorderLayout());
		
		JTextArea textArea = new JTextArea("Applet is running..Don't close this applet window!");
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		
		panel.add(textArea, BorderLayout.CENTER);
		getContentPane().add(panel, SwingConstants.CENTER);
	}

	public void destroy() {
	}
	
	public static JApplet getInstance() {
		return _instance;
	}
}
