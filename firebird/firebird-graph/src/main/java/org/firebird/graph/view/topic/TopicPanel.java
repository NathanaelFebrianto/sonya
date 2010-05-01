/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view.topic;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.firebird.graph.bean.GraphClientHandler;
import org.firebird.graph.view.GenericListener;
import org.firebird.graph.view.GraphPanel;
import org.firebird.graph.view.UIHandler;
import org.firebird.io.model.TopicTerm;
import org.firebird.io.model.TopicUser;
import org.firebird.io.model.TopicUserCluster;
import org.firebird.io.model.UserTerm;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A topic panel.
 * 
 * @author Young-Gue Bae
 */
public class TopicPanel extends JPanel {

	private static final long serialVersionUID = -3575297530745036754L;

	/** graph panel */
	GraphPanel panelGraph;
	
	/** graph client handler */
	GraphClientHandler handler;
	
	/** content split panel */
	JSplitPane spaneContent;
	
	/** tables */
	TopicTermTable tblTopicTerms;
	TermUserTable tblTermUsers;
	TopicUserTable tblTopicUsers;
	TopicUserClusterTable tblTopicUserCluster;

	/**
	 * Constructor.
	 * 
	 */
	public TopicPanel(GraphPanel panelGraph) {
		this.panelGraph = panelGraph;
		UIHandler.setResourceBundle("graph");
		this.handler = new GraphClientHandler();		

		setupUI();
	}

	private void setupUI() {
		setLayout(new BorderLayout());

		// create a content panel
		spaneContent = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		JComponent panelTop = setupTopPanel();
		JComponent panelBottom = setupBottomPanel();

		spaneContent.setOneTouchExpandable(true);
		spaneContent.setTopComponent(panelTop);
		spaneContent.setBottomComponent(panelBottom);
		add(spaneContent, BorderLayout.CENTER);
	}
	
	private JComponent setupTopPanel() {		
		// table for topic terms
		tblTopicTerms = new TopicTermTable();
		MouseListener selectTopicTermActionListener = (MouseListener)(GenericListener.create(
				MouseListener.class,
				"mouseReleased",
				this,
				"selectTopicTermAction"));	
		
		tblTopicTerms.addMouseListener(selectTopicTermActionListener);
		
		// table for term users
		tblTermUsers = new TermUserTable();
		
		FormLayout layout = new FormLayout(
				//"left:max(60dlu;p), 4dlu, left:max(60dlu;p)",
				//"left:180dlu, 4dlu, left:180dlu",
				"left:min(200dlu;p), 4dlu, left:min(200dlu;p)",
				"p, 2dlu, 140dlu, 2dlu");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator(UIHandler.getText("topic.title.topicTerms"), cc.xy(1, 1));
		builder.add(new JScrollPane(tblTopicTerms), cc.xy(1, 3));
		
		builder.addSeparator(UIHandler.getText("topic.title.termUsers"), cc.xy(3, 1));
		builder.add(new JScrollPane(tblTermUsers), cc.xy(3, 3));
				
		return builder.getPanel();
	}
	
	private JComponent setupBottomPanel() {		
		// table for topic users
		tblTopicUsers = new TopicUserTable();
		MouseListener selectTopicTermActionListener = (MouseListener)(GenericListener.create(
				MouseListener.class,
				"mouseReleased",
				this,
				"selectTopicUserAction"));	
		
		tblTopicUsers.addMouseListener(selectTopicTermActionListener);	
		
		// table for topic user cluster
		tblTopicUserCluster = new TopicUserClusterTable();
		MouseListener selectTopicClusteredUserActionListener = (MouseListener)(GenericListener.create(
				MouseListener.class,
				"mouseReleased",
				this,
				"selectTopicClusteredUserAction"));
		tblTopicUserCluster.addMouseListener(selectTopicClusteredUserActionListener);
		
		FormLayout layout = new FormLayout(
				//"left:max(60dlu;p), 4dlu, left:max(60dlu;p)",
				//"left:180dlu, 4dlu, left:180dlu",
				"left:min(200dlu;p), 4dlu, left:min(200dlu;p)",
				"p, 2dlu, 140dlu, 2dlu");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator(UIHandler.getText("topic.title.topicUsers"), cc.xy(1, 1));
		builder.add(new JScrollPane(tblTopicUsers), cc.xy(1, 3));
		
		builder.addSeparator(UIHandler.getText("topic.title.topicUserCluster"), cc.xy(3, 1));
		builder.add(new JScrollPane(tblTopicUserCluster), cc.xy(3, 3));
				
		return builder.getPanel();
	}
	
	/**
	 * Updates the tables.
	 * 
	 * @param websiteId the website id
	 * @param topicId the topic id
	 */
	public void updateTables(int websiteId, int topicId, int topUserNum) {
		tblTopicTerms.removeAllRow();
		tblTermUsers.removeAllRow();
		tblTopicUsers.removeAllRow();
		tblTopicUserCluster.removeAllRow();
		
		try {			
			List<TopicTerm> topicTerms = handler.getTermsByTopic(websiteId, topicId);
			tblTopicTerms.setRowData(topicTerms);			
					
			try {
				String term = (String) tblTopicTerms.getValueAt(0, 3);	
				List<UserTerm> userTerms = handler.getUsersByTerm(websiteId, term);
				tblTermUsers.setRowData(userTerms);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			List<TopicUser> topicUsers = handler.getUsersByTopic(websiteId, topicId);
			tblTopicUsers.setRowData(topicUsers);

			// this code is commented just for the performance(speed)
			// List<TopicUserCluster> topicUserClusters = handler.getClusteredUsersByTopic(websiteId, topicId);	
			List<TopicUserCluster> topicUserClusters = handler.getClusteredUsersByTopic(websiteId, topicId, topUserNum);
			tblTopicUserCluster.setRowData(topicUserClusters);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		

	}
	
	//////////////////////////////////////////
	/*       Defines event actions.         */
	//////////////////////////////////////////
	
	public void selectTopicTermAction(MouseEvent e) {
		int row = tblTopicTerms.getSelectedRow();
		
		if (row < 0) {
			return;
		}
		
		int websiteId = (Integer) tblTopicTerms.getValueAt(row, 1);
		String term = (String) tblTopicTerms.getValueAt(row, 3);
		
		try {
			List<UserTerm> userTerms = handler.getUsersByTerm(websiteId, term);
			tblTermUsers.setRowData(userTerms);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void selectTopicUserAction(MouseEvent e) {
		if (e.getClickCount() == 2) {
			int row = tblTopicUsers.getSelectedRow();
			String userUrl = (String) tblTopicUsers.getValueAt(row, 6);
			System.out.println(userUrl);
			
			try {
				URL urlObject = new URL(userUrl);
				panelGraph.getAppletContext().showDocument(urlObject, "_blank");
			} catch (MalformedURLException me) { 
				me.printStackTrace();
			}			
		}
	}
	
	public void selectTopicClusteredUserAction(MouseEvent e) {
		if (e.getClickCount() == 2) {
			int row = tblTopicUserCluster.getSelectedRow();
			String userUrl = (String) tblTopicUserCluster.getValueAt(row, 6);
			System.out.println(userUrl);
			
			try {
				URL urlObject = new URL(userUrl);
				panelGraph.getAppletContext().showDocument(urlObject, "_blank");
			} catch (MalformedURLException me) { 
				me.printStackTrace();
			}
		}
	}
	
}
