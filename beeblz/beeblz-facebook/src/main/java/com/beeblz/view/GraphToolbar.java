/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import prefuse.data.Graph;

import com.beeblz.graph.GraphView;

/**
 * A tool bar for graph.
 * 
 * @author YoungGue Bae
 */
public class GraphToolbar extends JPanel {
	
	private static final long serialVersionUID = 2421586141073868812L;

	//the graph main panel
	GraphPanel panelGraph;
	
	// graph view
	GraphView graphView;

	// actions
	AbstractAction clusteringAction;
	AbstractAction rankingAction;
	AbstractAction graphShareAction;
	
	JLabel lblClusterSize;
	
	JSlider sliderClustering;
	
	/**
	 * Constructor.
	 * 
	 * @param panelGraph the graph panel
	 */
	public GraphToolbar(GraphPanel panelGraph) {
		UIHandler.setResourceBundle("graph");
		
		this.panelGraph = panelGraph;
		
		this.graphView = panelGraph.getGraphView();
		
		initActions();
	    setActionMap();
	    createToolbar();
	}
	
	/**
	 * Updates the cluster size label.
	 * 
	 * @param size the cluster size
	 */
	public void updateClusterSizeLabel(int size) {
		lblClusterSize.setText(String.valueOf(size));
	}
	
	/**
	 * Updates the maximum value of the clustering slider bar.
	 * 
	 * @param max the maximum value
	 * @param value the value
	 */
	public void updateClusteringSliderBar(int max, int value) {
		sliderClustering.setMaximum(max);
		sliderClustering.setValue(value);
	}
	
	/**
	 * Creates a tool bar.
	 */
	private void createToolbar() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		// get graph
		Graph graph = graphView.getGraph();
		
		JButton button = null;
		
		// toolbar group - control
		button = new JButton(this.getActionMap().get("clustering"));
		button.setText(UIHandler.getText("toolbar.clustering"));
		//button.setIcon(UIHandler.getImageIcon("/clustering.png"));
		add(button);
		
		lblClusterSize = new JLabel(String.valueOf(graphView.getClusterSize()));
		
		sliderClustering = new JSlider(JSlider.HORIZONTAL);
		//sliderClustering.setBackground(Color.WHITE);
		sliderClustering.setMaximum(100);
		sliderClustering.setMinimum(0);
		sliderClustering.setValue(0);
		sliderClustering.setMajorTickSpacing(1);
		
		sliderClustering.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					int numEdgesToRemove = source.getValue();
					System.out.println("numEdgesToRemove == " + numEdgesToRemove);
					int size = graphView.clusterGraph(numEdgesToRemove, false);
					lblClusterSize.setText(String.valueOf(size));
					panelGraph.updateClusterComboBox();
				}
			}
		});
		
		add(sliderClustering);
		add(lblClusterSize);

		button = new JButton(this.getActionMap().get("ranking"));
		button.setText(UIHandler.getText("toolbar.ranking"));
		//button.setIcon(UIHandler.getImageIcon("/ranking.png"));
		add(button);
		
		button = new JButton(this.getActionMap().get("graph.share"));
		button.setText(UIHandler.getText("toolbar.graph.share"));
		//button.setIcon(UIHandler.getImageIcon("/share.png"));
		add(button);
	}

	/**
	 * Initializes actions.
	 */
	private void initActions() {
		clusteringAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				
			}
		};
		
		rankingAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				
			}
		};
		
		graphShareAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				
			}
		};
	}

	/**
	 * Sets the action map.
	 */
	private void setActionMap() {
		this.getActionMap().put("clustering", clusteringAction);
		this.getActionMap().put("ranking", rankingAction);
		this.getActionMap().put("graph.share", graphShareAction);
	}

}
