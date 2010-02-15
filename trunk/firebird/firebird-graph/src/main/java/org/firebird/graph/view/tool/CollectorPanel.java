/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view.tool;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import org.firebird.collector.CollectorConfig;
import org.firebird.common.model.ObjectModel;
import org.firebird.graph.bean.GraphClientHandler;
import org.firebird.graph.view.GenericListener;
import org.firebird.graph.view.GraphPanel;
import org.firebird.graph.view.UIHandler;
import org.firebird.io.model.Edge;
import org.firebird.io.model.Vertex;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import edu.uci.ics.jung.algorithms.layout.FRLayout;

/**
 * A panel for a collector.
 * 
 * @author Young-Gue Bae
 */
public class CollectorPanel extends JPanel {

	private static final long serialVersionUID = 8969977474679240657L;
	
	/** graph client handler */
	GraphClientHandler handler;
	
	/** grpah panel */
	GraphPanel panelGraph;
	
	/** database storage */
	boolean dbStorage = false;
	
	/** controls */
	JComboBox comboWebsite;
	JTextField tfldUsername;
	JCheckBox chkboxDbstore;
	JCheckBox chkboxCollectFriend;
	JCheckBox chkboxCollectFollower;
	JCheckBox chkboxCollectBlog;
	JSpinner spinLevelLimit;
	JSpinner spinDegreeLimit;
	JSpinner spinPeopleLimit;

	/**
	 * Constructor.
	 * 
	 */
	public CollectorPanel(GraphPanel panelGraph) {
		this.panelGraph = panelGraph;
		UIHandler.setResourceBundle("graph");
		this.handler = new GraphClientHandler();
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setupFields();
		setupUI();
	}
	
	/**
	 * Gets the database storage mode.
	 * 
	 * @return true if db storage
	 */
	public boolean isDBStorage() {
		return dbStorage;
	}
	
	/**
	 * Sets the database storage mode.
	 * 
	 * @param dbStorage true if db storage
	 */
	public void setDBStorage(boolean dbStorage) {
		this.dbStorage = dbStorage;
		if (dbStorage) {
			chkboxDbstore.setEnabled(true);
			chkboxDbstore.setSelected(true);
		}
		else
			chkboxDbstore.setEnabled(false);
	}

	private void setupFields() {
		comboWebsite = new JComboBox();
		comboWebsite.addItem(new ObjectModel("Twitter", "1"));
		comboWebsite.addItem(new ObjectModel("me2DAY", "2"));
		
		tfldUsername = new JTextField();
		chkboxDbstore = new JCheckBox();
		chkboxCollectFriend = new JCheckBox();
		chkboxCollectFollower = new JCheckBox();
		chkboxCollectBlog = new JCheckBox();
		spinLevelLimit = new JSpinner(new SpinnerNumberModel(2, 1, 5, 1));
		spinDegreeLimit = new JSpinner(new SpinnerNumberModel(5, 1, 1000, 1));
		spinPeopleLimit = new JSpinner(new SpinnerNumberModel(50, 1, 2000, 1));
	}
	
	private void setupUI() {
		FormLayout layout = new FormLayout(
				"right:pref:grow",
				"p, 2dlu, p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.add(setupContentUI(), cc.xy(1, 1));
		builder.add(setupCommandPanel(), cc.xy(1, 3));
		
		add(builder.getPanel());
		
		builder.getPanel().setBorder(new EmptyBorder(0,0,0,0));
	}
	
	private JComponent setupContentUI() {
		FormLayout layout = new FormLayout(
				"left:max(20dlu;p), 4dlu, 75dlu",
				"p, 2dlu, p, 3dlu, p, 7dlu, " +
				"p, 2dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 7dlu, " + 
				"p, 2dlu, p, 3dlu, p, 3dlu, p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator(UIHandler.getText("collector.datasource"), cc.xyw(1, 1, 3));
		builder.addLabel(UIHandler.getText("collector.website"), cc.xy(1, 3));
		builder.add(comboWebsite, cc.xy(3, 3));
		builder.addLabel(UIHandler.getText("collector.username"), cc.xy(1, 5));
		builder.add(tfldUsername, cc.xy(3, 5));
		
		builder.addSeparator(UIHandler.getText("collector.option"), cc.xyw(1, 7, 3));
		builder.addLabel(UIHandler.getText("collector.dbstore"), cc.xy(1, 9));
		builder.add(chkboxDbstore, cc.xy(3, 9));
		builder.addLabel(UIHandler.getText("collector.collect.friend"), cc.xy(1, 11));
		builder.add(chkboxCollectFriend, cc.xy(3, 11));
		builder.addLabel(UIHandler.getText("collector.collect.follower"), cc.xy(1, 13));
		builder.add(chkboxCollectFollower, cc.xy(3, 13));
		builder.addLabel(UIHandler.getText("collector.collect.blogentry"), cc.xy(1, 15));
		builder.add(chkboxCollectBlog, cc.xy(3, 15));
		
		builder.addSeparator(UIHandler.getText("collector.scope"), cc.xyw(1, 17, 3));
		builder.addLabel(UIHandler.getText("collector.level.limit"), cc.xy(1, 19));
		builder.add(spinLevelLimit, cc.xy(3, 19));
		builder.addLabel(UIHandler.getText("collector.degree.limit"), cc.xy(1, 21));
		builder.add(spinDegreeLimit, cc.xy(3, 21));
		builder.addLabel(UIHandler.getText("collector.people.limit"), cc.xy(1, 23));
		builder.add(spinPeopleLimit, cc.xy(3, 23));
		
		//builder.getPanel().setBorder(new LineBorder(Color.red));
				
		return builder.getPanel();
	}
	
	private JPanel setupCommandPanel() {
		FormLayout layout = new FormLayout(
				"p, 2dlu, p",
				"p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		JButton btnClear = new JButton(UIHandler.getText("collector.clear"));		
		ActionListener clearActionListener = (ActionListener)(GenericListener.create(
		        ActionListener.class,
				"actionPerformed",
				this,
				"clearAction"));		
		btnClear.addActionListener(clearActionListener);
		
		JButton btnOk = new JButton(UIHandler.getText("collector.ok"));		
		ActionListener okActionListener = (ActionListener)(GenericListener.create(
		        ActionListener.class,
				"actionPerformed",
				this,
				"okAction"));		
		btnOk.addActionListener(okActionListener);
		
		builder.add(btnClear, cc.xy(1, 1));
		builder.add(btnOk, cc.xy(3, 1));		
		
		return builder.getPanel();
	}

	//////////////////////////////////////////
	/*       Defines event actions.         */
	//////////////////////////////////////////
	
	public void clearAction(ActionEvent e) {
		this.clearFields();
	}
	
	public void okAction(ActionEvent e) {
		try {
			CollectorConfig config = this.createCollectorConfig();
			String username = tfldUsername.getText();
			if (username != null)	username.trim();
			
			if (dbStorage) {
				handler.collectTwitter(config, username);
			}
			else {	
				HashMap data = handler.collectRealtimeTwitter(config, username);    	
				List<Vertex> vertices = (List<Vertex>)data.get("vertices");
				List<Edge> edges = (List<Edge>)data.get("edges");
				
				panelGraph.showGraph(vertices, edges);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void clearFields() {
		comboWebsite.setSelectedIndex(0);
		tfldUsername.setText(null);
		chkboxDbstore.setSelected(false);
		chkboxCollectFriend.setSelected(false);
		chkboxCollectFollower.setSelected(false);
		chkboxCollectBlog.setSelected(false);
		spinLevelLimit.setValue(new Integer(2));
		spinDegreeLimit.setValue(new Integer(5));
		spinPeopleLimit.setValue(new Integer(50));
	}
	
	private CollectorConfig createCollectorConfig() {
		CollectorConfig config = new CollectorConfig();
		config.setDBStorage(chkboxDbstore.isSelected());
		config.setCollectFriend(chkboxCollectFriend.isSelected());
		config.setCollectFollower(chkboxCollectFollower.isSelected());
		config.setCollectUserBlogEntry(chkboxCollectBlog.isSelected());
		config.setLevelLimit((Integer)spinLevelLimit.getValue());
		config.setDegreeLimit((Integer)spinDegreeLimit.getValue());
		config.setPeopleLimit((Integer)spinPeopleLimit.getValue());
		
		return config;
	}
}
