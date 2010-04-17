/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view.tool;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.RowSorter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.firebird.analyzer.graph.clustering.Clusterer;
import org.firebird.common.model.ObjectModel;
import org.firebird.graph.bean.GraphClientHandler;
import org.firebird.graph.view.DefaultTable;
import org.firebird.graph.view.GenericListener;
import org.firebird.graph.view.GraphPanel;
import org.firebird.graph.view.UIHandler;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A tool panel for clustering.
 * 
 * @author Young-Gue Bae
 */
public class ClusteringToolPanel extends JPanel {

	private static final long serialVersionUID = 2193168249407035150L;

	/** graph client handler */
	GraphClientHandler handler;
	
	/** grpah panel */
	GraphPanel panelGraph;

	/** controls */
	JComboBox comboWebsite;
	JComboBox comboClusterType;
	
	/** cluster table */
	ClusterTable tblCluster;

	/**
	 * Constructor.
	 * 
	 */
	public ClusteringToolPanel(GraphPanel panelGraph) {
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
		
		comboClusterType = new JComboBox();
		comboClusterType.addItem(new ObjectModel("EdgeBetweenness", Clusterer.EDGE_BETWEENNESS_CLUSTER));
		comboClusterType.addItem(new ObjectModel("Voltage", Clusterer.VOLTAGE_CLUSTER));
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
				"p, 2dlu, p, 3dlu, 120dlu");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator(UIHandler.getText("clustering.website.select"), cc.xyw(1, 1, 3));
		builder.addLabel(UIHandler.getText("clustering.website"), cc.xy(1, 3));
		builder.add(comboWebsite, cc.xy(3, 3));	
		
			
		builder.addSeparator(UIHandler.getText("clustering.clusters"), cc.xyw(1, 5, 3));

		builder.addLabel(UIHandler.getText("clustering.cluster.type"), cc.xy(1, 7));
		builder.add(comboClusterType, cc.xy(3, 7));
		
		tblCluster = new ClusterTable();	
		builder.add(new JScrollPane(tblCluster), cc.xyw(1, 9, 3));
				
		return builder.getPanel();
	}
	
	private JPanel setupCommandPanel() {
		FormLayout layout = new FormLayout(
				//"p, 2dlu, p, 2dlu, p",
				"p, 2dlu, p",
				"p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		JButton btnClear = new JButton(UIHandler.getText("clustering.clear"));		
		ActionListener clearActionListener = (ActionListener)(GenericListener.create(
		        ActionListener.class,
				"actionPerformed",
				this,
				"clearAction"));		
		btnClear.addActionListener(clearActionListener);
		
		JButton btnView = new JButton(UIHandler.getText("clustering.view"));		
		ActionListener viewClusterActionListener = (ActionListener)(GenericListener.create(
		        ActionListener.class,
				"actionPerformed",
				this,
				"viewClusterAction"));		
		btnView.addActionListener(viewClusterActionListener);
		
		JButton btnColor = new JButton(UIHandler.getText("clustering.coloring"));		
		ActionListener colorClusterActionListener = (ActionListener)(GenericListener.create(
		        ActionListener.class,
				"actionPerformed",
				this,
				"colorClusterAction"));		
		btnColor.addActionListener(colorClusterActionListener);
		
		//builder.add(btnClear, cc.xy(1, 1));
		//builder.add(btnView, cc.xy(3, 1));
		//builder.add(btnColor, cc.xy(5, 1));
		
		builder.add(btnView, cc.xy(1, 1));
		builder.add(btnColor, cc.xy(3, 1));
		
		return builder.getPanel();
	}

	//////////////////////////////////////////
	/*       Defines event actions.         */
	//////////////////////////////////////////
	
	public void clearAction(ActionEvent e) {
		this.clearFields();
	}
	
	public void viewClusterAction(ActionEvent e) {
		try {
			ObjectModel objWebsite = (ObjectModel)comboWebsite.getSelectedItem();
			int website = Integer.parseInt(objWebsite.getValue());
			
			ObjectModel objClusterType = (ObjectModel)comboClusterType.getSelectedItem();
			String clusterType = objClusterType.getValue();
			
			Map<Integer, Set<String>> clusterMap = handler.getClusterMap(clusterType, website);
			tblCluster.setClusters(clusterMap);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void colorClusterAction(ActionEvent e) {
		int[] rows = tblCluster.getSelectedRows();
		
		for (int row : rows) {
			Set<String> verticesSet = (Set<String>) tblCluster.getValueAt(row, 2);
			Color color = panelGraph.getGraphViewer().colorVertices(verticesSet);
			panelGraph.getSatelliteGraphViewer().colorVertices(verticesSet, color);
		}		
	}
	
	private void clearFields() {
		comboWebsite.setSelectedIndex(0);
		comboClusterType.setSelectedIndex(0);
		tblCluster.removeAllRow();
	}
	
	/**
	 * This class is a cluster table.
	 * 
	 * @author Young-Gue Bae
	 */
	public class ClusterTable extends DefaultTable {
		private static final long serialVersionUID = -3186982433514089428L;
		private DefaultTableModel tableModel;

		/**
		 * Constructor.
		 */
		public ClusterTable() {
			super();
			UIHandler.setResourceBundle("graph");
			tableModel = new DefaultTableModel(columnNames(), 3) {
				
				public Class<?> getColumnClass(int column) {
					Class<?> returnValue;		
					if ((column >= 0) && (column < getColumnCount())) {
						if (getValueAt(0, column) != null)
							returnValue = getValueAt(0, column).getClass();
						else
							returnValue = Object.class;
					} else {
						returnValue = Object.class;
					}
					return returnValue;
				}
				
				public boolean isCellEditable(int row, int col) { 
			        return false; 
			    }
			};
			setModel(tableModel);
			
	        // set sorter
	        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
			setRowSorter(sorter); 

			setColumnHidden(2, true);
		}

		private Object[] columnNames() {
			Object[] colNames = new Object[] { 
					UIHandler.getText("col.cluster.no"),
					UIHandler.getText("col.cluster.count"), 
					"Vertices Set", };
			return colNames;
		}

		/**
		 * Sets the clusters.
		 * 
		 * @param clusterMap the cluster map
		 */
		public void setClusters(Map<Integer, Set<String>> clusterMap) {
			removeAllRow();
			
			if (clusterMap == null) {
				System.out.println("Cluster map is null!");
				return;
			}

			int row = 0;
			
			for (Iterator<Integer> it = clusterMap.keySet().iterator(); it.hasNext();) {
				Integer cluster = (Integer) it.next();
				Set<String> verticesSet = (Set<String>) clusterMap.get(cluster);
				Vector<Object> rowData = new Vector<Object>();
				rowData.add(cluster);
				rowData.add(verticesSet.size());
				rowData.add(verticesSet);

				tableModel.insertRow(row, rowData);
				row++;
				cluster++;
			}
		}
	}
	
}
