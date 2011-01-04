/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.view;

import java.applet.AppletContext;

/**
 * A Social network graph main panel.
 * 
 * @author YoungGue Bae
 */
public class GraphPanel {

	/** applet context */
	AppletContext context = null;
	
	/**
	 * Constructor.
	 * 
	 */
	public GraphPanel() {
		this(null);

		setupUI();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param context the applet context
	 */
	public GraphPanel(AppletContext context) {
		this.context = context;
		UIHandler.setResourceBundle("graph");
		UIHandler.changeAllSwingComponentDefaultFont();

		setupUI();
	}
	
	private void setupUI() {
		
	}
}
