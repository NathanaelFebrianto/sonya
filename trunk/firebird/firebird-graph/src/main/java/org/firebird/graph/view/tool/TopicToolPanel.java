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
 * A tool panel for topic analysis.
 * 
 * @author Young-Gue Bae
 */
public class TopicToolPanel extends JPanel {
	private static final long serialVersionUID = -5672634853943684688L;

	/** graph client handler */
	GraphClientHandler handler;
	
	/** grpah panel */
	GraphPanel panelGraph;

	/** controls */
	JComboBox comboWebsite;
	JComboBox comboColorTarget;
	JSpinner spinTopN;
	
	/** topic table */
	TopicTable tblTopic;

	/**
	 * Constructor.
	 * 
	 */
	public TopicToolPanel(GraphPanel panelGraph) {
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
		
		comboColorTarget = new JComboBox();
		comboColorTarget.addItem(UIHandler.getText("topic.coloring.topicUser"));
		comboColorTarget.addItem(UIHandler.getText("topic.coloring.topicClusteredUser"));
		
		spinTopN = new JSpinner(new SpinnerNumberModel(10, 1, 999999999, 1));
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
				"p, 2dlu, p, 2dlu, p, 3dlu, 150dlu");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator(UIHandler.getText("topic.website.select"), cc.xyw(1, 1, 3));
		builder.addLabel(UIHandler.getText("topic.website"), cc.xy(1, 3));
		builder.add(comboWebsite, cc.xy(3, 3));	
		
				
		builder.addSeparator(UIHandler.getText("topic.topics"), cc.xyw(1, 5, 3));		
		builder.addLabel(UIHandler.getText("topic.coloring.target"), cc.xy(1, 7));
		builder.add(comboColorTarget, cc.xy(3, 7));	
		builder.addLabel(UIHandler.getText("topic.coloring.topn"), cc.xy(1, 9));
		builder.add(spinTopN, cc.xy(3, 9));
		
		tblTopic = new TopicTable();
		MouseListener selectTopicActionListener = (MouseListener)(GenericListener.create(
				MouseListener.class,
				"mouseReleased",
				this,
				"selectTopicAction"));
		tblTopic.addMouseListener(selectTopicActionListener);
		
		builder.add(new JScrollPane(tblTopic), cc.xyw(1, 11, 3));
				
		return builder.getPanel();
	}
	
	private JPanel setupCommandPanel() {
		FormLayout layout = new FormLayout(
				"p, 2dlu, p, 2dlu, p",
				"p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		JButton btnClear = new JButton(UIHandler.getText("topic.clear"));		
		ActionListener clearActionListener = (ActionListener)(GenericListener.create(
		        ActionListener.class,
				"actionPerformed",
				this,
				"clearAction"));		
		btnClear.addActionListener(clearActionListener);
		
		JButton btnView = new JButton(UIHandler.getText("topic.view"));		
		ActionListener viewTopicsActionListener = (ActionListener)(GenericListener.create(
		        ActionListener.class,
				"actionPerformed",
				this,
				"viewTopicsAction"));		
		btnView.addActionListener(viewTopicsActionListener);
		
		JButton btnColor = new JButton(UIHandler.getText("topic.coloring"));		
		ActionListener coloringActionListener = (ActionListener)(GenericListener.create(
		        ActionListener.class,
				"actionPerformed",
				this,
				"coloringAction"));		
		btnColor.addActionListener(coloringActionListener);
		
		JButton btnDetail = new JButton(UIHandler.getText("topic.view.detail"));		
		ActionListener viewDetailActionListener = (ActionListener)(GenericListener.create(
		        ActionListener.class,
				"actionPerformed",
				this,
				"viewDetailAction"));		
		btnDetail.addActionListener(viewDetailActionListener);
		
		//builder.add(btnClear, cc.xy(1, 1));
		builder.add(btnView, cc.xy(1, 1));
		builder.add(btnColor, cc.xy(3, 1));
		builder.add(btnDetail, cc.xy(5, 1));
		
		return builder.getPanel();
	}

	//////////////////////////////////////////
	/*       Defines event actions.         */
	//////////////////////////////////////////
	
	public void clearAction(ActionEvent e) {
		this.clearFields();
	}
	
	public void viewTopicsAction(ActionEvent e) {
		try {
			ObjectModel objWebsite = (ObjectModel)comboWebsite.getSelectedItem();
			int website = Integer.parseInt(objWebsite.getValue());
			
			List<Integer> topics = handler.getTopics(website);
			tblTopic.setTopics(website, topics);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void coloringAction(ActionEvent e) {
		try {
			String colorTarget = (String) comboColorTarget.getSelectedItem();
			
			ObjectModel objWebsite = (ObjectModel)comboWebsite.getSelectedItem();
			int websiteId = Integer.parseInt(objWebsite.getValue());
			
			Integer topUserNum = (Integer)spinTopN.getValue();
			
			int[] rows = tblTopic.getSelectedRows();
			
			if (colorTarget.equals(UIHandler.getText("topic.coloring.topicUser"))) {
				for (int row : rows) {
					int topicId = (Integer) tblTopic.getValueAt(row, 0);
					
					List<TopicUser> users = handler.getUsersByTopic(websiteId, topicId, topUserNum);				
					Set<String> verticesSet = this.getVerticesSet(users);
					
					Color color = panelGraph.getGraphViewer().colorVertices(verticesSet);
					panelGraph.getSatelliteGraphViewer().colorVertices(verticesSet, color);
				}				
			}
			else if (colorTarget.equals(UIHandler.getText("topic.coloring.topicClusteredUser"))) {
				for (int row : rows) {
					int topicId = (Integer) tblTopic.getValueAt(row, 0);
					
					List<TopicUserCluster> users = handler.getClusteredUsersByTopic(websiteId, topicId, topUserNum);
					Set<String> verticesSet = this.getVerticesSet(users);

					Color color = panelGraph.getGraphViewer().colorVertices(verticesSet);
					panelGraph.getSatelliteGraphViewer().colorVertices(verticesSet, color);
				}				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void selectTopicAction(MouseEvent e) {
		try {
			if (e.getClickCount() == 2) {
				ObjectModel objWebsite = (ObjectModel)comboWebsite.getSelectedItem();
				int websiteId = Integer.parseInt(objWebsite.getValue());
				
				int row = tblTopic.getSelectedRow();
				if (row < 0) {
					return;
				}
				
				if (tblTopic.getValueAt(row, 0) == null) {
					return;
				}
				
				int topicId = (Integer) tblTopic.getValueAt(row, 0);
				
				Integer topUserNum = (Integer)spinTopN.getValue();
				
				panelGraph.getRightTabbedPanel().setSelectedIndex(1);
				panelGraph.getRightTopicPanel().updateTables(websiteId, topicId, topUserNum);				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void viewDetailAction(ActionEvent e) {
		try {
			ObjectModel objWebsite = (ObjectModel)comboWebsite.getSelectedItem();
			int websiteId = Integer.parseInt(objWebsite.getValue());
			
			int row = tblTopic.getSelectedRow();
			if (row < 0) {
				return;
			}
			
			if (tblTopic.getValueAt(row, 0) == null) {
				return;
			}
			
			int topicId = (Integer) tblTopic.getValueAt(row, 0);
			
			Integer topUserNum = (Integer)spinTopN.getValue();
			
			panelGraph.getRightTabbedPanel().setSelectedIndex(1);
			panelGraph.getRightTopicPanel().updateTables(websiteId, topicId, topUserNum);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private Set<String> getVerticesSet(List users) {
		Set<String> verticesSet = new HashSet<String>();
		
		for (int i = 0; i < users.size(); i++) {
			Object o = users.get(i);
			if (o instanceof TopicUser) {
				TopicUser topicUser = (TopicUser) o;
				verticesSet.add(topicUser.getUserId());				
			}
			else if (o instanceof TopicUserCluster) {
				TopicUserCluster topicUserCluster = (TopicUserCluster) o;
				verticesSet.add(topicUserCluster.getUserId());	
			}
		}		
		return verticesSet;
	}
	
	private void clearFields() {
		comboWebsite.setSelectedIndex(0);
		comboColorTarget.setSelectedIndex(0);
		spinTopN.setValue(new Integer(10));
		tblTopic.removeAllRow();
	}
	
	/**
	 * This class is a topics table.
	 * 
	 * @author Young-Gue Bae
	 */
	public class TopicTable extends DefaultTable {
		private static final long serialVersionUID = 8666704342271697642L;
		private DefaultTableModel tableModel;

		/**
		 * Constructor.
		 */
		public TopicTable() {
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
		}

		private Object[] columnNames() {
			Object[] colNames = new Object[] { 
					UIHandler.getText("col.topic.id"),
					UIHandler.getText("col.topic.user.count"),
					UIHandler.getText("col.topic.clustered.user.count"),};
			return colNames;
		}

		/**
		 * Sets the topics.
		 * 
		 * @param websiteId the website id
		 * @param topics the topics
		 */
		public void setTopics(int websiteId, List<Integer> topics) {
			removeAllRow();

			int row = 0;

			for (Integer topic : topics) {
				Vector<Object> rowData = new Vector<Object>();
				rowData.add(topic);
				try {
					List<TopicUser> topicUsers = handler.getUsersByTopic(websiteId, topic);
					rowData.add(topicUsers.size());
				} catch (Exception ex) {
					rowData.add("error");
				}
				
				try {
					List<TopicUserCluster> topicUserClusters = handler.getClusteredUsersByTopic(websiteId, topic);
					rowData.add(topicUserClusters.size());
				} catch (Exception ex) {
					rowData.add("error");
				}				

				tableModel.insertRow(row, rowData);
				row++;
			}
		}
	}
	
}
