/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.view;

import java.applet.AppletContext;
import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.beeblz.graph.GraphView;

/**
 * A Social network graph main panel.
 * 
 * @author YoungGue Bae
 */
public class GraphPanel extends JPanel {
	
	private static final long serialVersionUID = -5520707258678283156L;

	// applet context
	AppletContext context = null;
	
	// split panel for content
	JSplitPane spaneContent;
	
	// right split panel
	JSplitPane spaneTable;
	
	// tab panel for graph
	JTabbedPane tpaneGraph;
	
	// tab panel for top table
	JTabbedPane tpaneTopTable;
	
	// tab panel for bottom table
	JTabbedPane tpaneBottomTable;
	
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
		setLayout(new BorderLayout());

		// create a content panel
		spaneContent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JComponent panelLeft = setupLeftPanel();
		JComponent panelRight = setupRightPanel();

		spaneContent.setOneTouchExpandable(true);
		spaneContent.setDividerLocation(panelLeft.getPreferredSize().width);
		spaneContent.setLeftComponent(panelLeft);
		spaneContent.setRightComponent(panelRight);

		// create a graph toolbar
		JPanel toolbar = new JPanel();

		setLayout(new BorderLayout());
		add(toolbar, BorderLayout.NORTH);
		add(spaneContent, BorderLayout.CENTER);
	}

	private JComponent setupLeftPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		GraphView graph = new GraphView();
		
		tpaneGraph = new JTabbedPane();
		
		tpaneGraph.addTab(UIHandler.getText("tab.social.graph"),
				UIHandler.getImageIcon("/info.png"), graph);
		
		tpaneGraph.addTab(UIHandler.getText("tab.closeness.graph"),
				UIHandler.getImageIcon("/info.png"), graph);

		panel.add(tpaneGraph, BorderLayout.CENTER);

		return panel;
	}

	private JComponent setupRightPanel() {
		spaneTable = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		spaneTable.setOneTouchExpandable(true);
		spaneTable.setDividerLocation(200);
		spaneTable.setTopComponent(setupTopMyFriendsPanel());
		spaneTable.setBottomComponent(setupBottomFriendsOfFriendPanel());
		
		return spaneTable;
	}
	
	private JComponent setupTopMyFriendsPanel() {
		tpaneTopTable = new JTabbedPane(SwingConstants.TOP);
		
		tpaneTopTable.addTab(
				UIHandler.getText("tab.my.friends"), 
				UIHandler.getImageIcon("/bug.gif"), 
				new JTable());
		
		return tpaneTopTable;
	}
	
	private JComponent setupBottomFriendsOfFriendPanel() {
		tpaneTopTable = new JTabbedPane(SwingConstants.TOP);
		
		tpaneTopTable.addTab(
				UIHandler.getText("tab.friends.of.friend"), 
				UIHandler.getImageIcon("/bug.gif"), 
				new JTable());
		
		tpaneTopTable.addTab(
				UIHandler.getText("tab.friend.may.know"), 
				UIHandler.getImageIcon("/bug.gif"), 
				new JTable());
		
		return tpaneTopTable;
	}

}
