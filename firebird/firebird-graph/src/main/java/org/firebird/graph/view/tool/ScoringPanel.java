/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view.tool;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import org.firebird.analyzer.graph.scoring.ScoringConfig;
import org.firebird.common.model.ObjectModel;
import org.firebird.graph.bean.GraphClientHandler;
import org.firebird.graph.view.GenericListener;
import org.firebird.graph.view.GraphPanel;
import org.firebird.graph.view.UIHandler;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A panel for scoring.
 * 
 * @author Young-Gue Bae
 */
public class ScoringPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/** graph client handler */
	GraphClientHandler handler;
	
	/** grpah panel */
	GraphPanel panelGraph;

	/** controls */
	JComboBox comboWebsite;
	JCheckBox chkboxPR;
	JCheckBox chkboxHITS;
	JCheckBox chkboxBC;
	JCheckBox chkboxCC;
	JCheckBox chkboxEC;

	/**
	 * Constructor.
	 * 
	 */
	public ScoringPanel(GraphPanel panelGraph) {
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
		
		chkboxPR = new JCheckBox();
		chkboxHITS = new JCheckBox();
		chkboxBC = new JCheckBox();
		chkboxCC = new JCheckBox();
		chkboxEC = new JCheckBox();
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
				"p, 2dlu, p, 7dlu, " +
				"p, 2dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator(UIHandler.getText("scoring.website.select"), cc.xyw(1, 1, 3));
		builder.addLabel(UIHandler.getText("scoring.website"), cc.xy(1, 3));
		builder.add(comboWebsite, cc.xy(3, 3));
		
		builder.addSeparator(UIHandler.getText("scoring.algorithms"), cc.xyw(1, 5, 3));
		builder.addLabel(UIHandler.getText("scoring.page.rank"), cc.xy(1, 7));
		builder.add(chkboxPR, cc.xy(3, 7));
		builder.addLabel(UIHandler.getText("scoring.hits"), cc.xy(1, 9));
		builder.add(chkboxHITS, cc.xy(3, 9));
		builder.addLabel(UIHandler.getText("scoring.betweenness.centrality"), cc.xy(1, 11));
		builder.add(chkboxBC, cc.xy(3, 11));
		builder.addLabel(UIHandler.getText("scoring.closeness.centrality"), cc.xy(1, 13));
		builder.add(chkboxCC, cc.xy(3, 13));
		builder.addLabel(UIHandler.getText("scoring.eigenvector.centrality"), cc.xy(1, 15));
		builder.add(chkboxEC, cc.xy(3, 15));
				
		return builder.getPanel();
	}
	
	private JPanel setupCommandPanel() {
		FormLayout layout = new FormLayout(
				"p, 2dlu, p",
				"p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		JButton btnClear = new JButton(UIHandler.getText("scoring.clear"));		
		ActionListener clearActionListener = (ActionListener)(GenericListener.create(
		        ActionListener.class,
				"actionPerformed",
				this,
				"clearAction"));		
		btnClear.addActionListener(clearActionListener);
		
		JButton btnOk = new JButton(UIHandler.getText("scoring.ok"));		
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
			ScoringConfig config = this.createScoringConfig();
			ObjectModel objWebsite = (ObjectModel)comboWebsite.getSelectedItem();
			int website = Integer.parseInt(objWebsite.getValue());
			
			handler.scoringGraph(config, website, website);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void clearFields() {
		comboWebsite.setSelectedIndex(0);
		chkboxPR.setSelected(false);
		chkboxHITS.setSelected(false);
		chkboxBC.setSelected(false);
		chkboxCC.setSelected(false);
		chkboxEC.setSelected(false);
	}
	
	private ScoringConfig createScoringConfig() {
		ScoringConfig config = new ScoringConfig();
		config.setEnblePageRank(chkboxPR.isSelected());
		config.setEnbleHITS(chkboxHITS.isSelected());
		config.setEnableBetweennessCentrality(chkboxBC.isSelected());
		config.setEnableClosenessCentrality(chkboxCC.isSelected());
		config.setEnableEigenvectorCentrality(chkboxEC.isSelected());
		
		return config;
	}
}
