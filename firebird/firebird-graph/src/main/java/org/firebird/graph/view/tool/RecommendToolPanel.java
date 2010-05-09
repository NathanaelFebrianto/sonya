/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view.tool;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.firebird.common.model.ObjectModel;
import org.firebird.graph.bean.GraphClientHandler;
import org.firebird.graph.view.DefaultTable;
import org.firebird.graph.view.GenericListener;
import org.firebird.graph.view.GraphPanel;
import org.firebird.graph.view.UIHandler;
import org.firebird.io.model.TopicUser;
import org.firebird.io.model.TopicUserCluster;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A tool panel for recommendation.
 * 
 * @author Young-Gue Bae
 */
public class RecommendToolPanel extends JPanel {
	private static final long serialVersionUID = 368331540142049161L;

	/** graph client handler */
	GraphClientHandler handler;
	
	/** grpah panel */
	GraphPanel panelGraph;

	/** controls */
	JComboBox comboWebsite;
	JTextField tfldUsername;
	JComboBox comboRecoUserOrderBy;
	JSpinner spinRecoUserTopN;

	/**
	 * Constructor.
	 * 
	 */
	public RecommendToolPanel(GraphPanel panelGraph) {
		this.panelGraph = panelGraph;
		UIHandler.setResourceBundle("graph");
		this.handler = new GraphClientHandler();
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setupFields();
		setupUI();
	}

	private void setupFields() {
		comboWebsite = new JComboBox();
		comboWebsite.addItem(new ObjectModel("Twitter", "1"));
		comboWebsite.addItem(new ObjectModel("me2DAY", "2"));
		
		tfldUsername = new JTextField();
		
		comboRecoUserOrderBy = new JComboBox();
		comboRecoUserOrderBy.addItem("TOPIC_SCORE");
		comboRecoUserOrderBy.addItem("AUTHORITY_TOPIC_SCORE");
		
		spinRecoUserTopN = new JSpinner(new SpinnerNumberModel(20, 1, 999999999, 1));
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
				"left:max(50dlu;p), 4dlu, 75dlu",
				"p, 2dlu, p, 3dlu, " +
				"p, 2dlu, p, 2dlu, p, 3dlu, p, 3dlu");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator(UIHandler.getText("recommend.website.select"), cc.xyw(1, 1, 3));
		builder.addLabel(UIHandler.getText("recommend.website"), cc.xy(1, 3));
		builder.add(comboWebsite, cc.xy(3, 3));			
				
		builder.addSeparator(UIHandler.getText("recommend.option"), cc.xyw(1, 5, 3));		
		builder.addLabel(UIHandler.getText("recommend.username"), cc.xy(1, 7));
		builder.add(tfldUsername, cc.xy(3, 7));	
		builder.addLabel(UIHandler.getText("recommend.orderBy"), cc.xy(1, 9));
		builder.add(comboRecoUserOrderBy, cc.xy(3, 9));
		builder.addLabel(UIHandler.getText("recommend.topn"), cc.xy(1, 11));
		builder.add(spinRecoUserTopN, cc.xy(3, 11));
		
		return builder.getPanel();
	}
	
	private JPanel setupCommandPanel() {
		FormLayout layout = new FormLayout(
				"p, 2dlu, p",
				"p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		JButton btnClear = new JButton(UIHandler.getText("recommend.clear"));		
		ActionListener clearActionListener = (ActionListener)(GenericListener.create(
		        ActionListener.class,
				"actionPerformed",
				this,
				"clearAction"));		
		btnClear.addActionListener(clearActionListener);
		
		JButton btnRecommend = new JButton(UIHandler.getText("recommend.recommend"));		
		ActionListener recommendActionListener = (ActionListener)(GenericListener.create(
		        ActionListener.class,
				"actionPerformed",
				this,
				"recommendAction"));		
		btnRecommend.addActionListener(recommendActionListener);
		
		builder.add(btnClear, cc.xy(1, 1));
		builder.add(btnRecommend, cc.xy(3, 1));
		
		return builder.getPanel();
	}

	//////////////////////////////////////////
	/*       Defines event actions.         */
	//////////////////////////////////////////
	
	public void clearAction(ActionEvent e) {
		this.clearFields();
	}
	
	public void recommendAction(ActionEvent e) {
		try {
			ObjectModel objWebsite = (ObjectModel)comboWebsite.getSelectedItem();
			int website = Integer.parseInt(objWebsite.getValue());
			
			String username = tfldUsername.getText();			
			String orderByColumn = (String) comboRecoUserOrderBy.getSelectedItem();
			Integer topUserNum = (Integer) spinRecoUserTopN.getValue();
			
			panelGraph.getRightTabbedPanel().setSelectedIndex(2);
			panelGraph.getRightRecommendPanel().updateTables(website, topUserNum, username, orderByColumn);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void clearFields() {
		comboWebsite.setSelectedIndex(0);
		tfldUsername.setText("");
		comboRecoUserOrderBy.setSelectedIndex(0);
		spinRecoUserTopN.setValue(new Integer(20));
	}
}
