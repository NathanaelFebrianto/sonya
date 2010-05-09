/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view.recommend;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.firebird.graph.bean.GraphClientHandler;
import org.firebird.graph.view.GenericListener;
import org.firebird.graph.view.GraphPanel;
import org.firebird.graph.view.UIHandler;
import org.firebird.graph.view.topic.TopicTermTable;
import org.firebird.io.model.TopicTerm;
import org.firebird.io.model.TopicUserCluster;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A recommendation panel.
 * 
 * @author Young-Gue Bae
 */
public class RecommendPanel extends JPanel {

	private static final long serialVersionUID = 7988213360092644197L;

	/** graph panel */
	GraphPanel panelGraph;
	
	/** graph client handler */
	GraphClientHandler handler;
	
	/** tables */
	TopicTermTable tblTopicTerms;
	UserTopicsTable tblUserTopics;
	RecommendUsersTable tblRecommendUsers;
	
	/** recommend options */
	int topUserNum = 20;
	String username = "";
	String orderByColumn = "AUTHORITY_TOPIC_SCORE";

	/**
	 * Constructor.
	 * 
	 */
	public RecommendPanel(GraphPanel panelGraph) {
		this.panelGraph = panelGraph;
		UIHandler.setResourceBundle("graph");
		this.handler = new GraphClientHandler();		

		setupUI();
	}

	private void setupUI() {
		setLayout(new BorderLayout());

		// create a content panel
		JComponent panelContent = setupContentPanel();

		add(panelContent, BorderLayout.CENTER);
	}
	
	private JComponent setupContentPanel() {		
		// table for user's topics
		tblUserTopics = new UserTopicsTable();
		
		MouseListener selectTopicActionListener = (MouseListener)(GenericListener.create(
				MouseListener.class,
				"mouseReleased",
				this,
				"selectTopicAction"));			
		tblUserTopics.addMouseListener(selectTopicActionListener);
		
		// table for topic terms
		tblTopicTerms = new TopicTermTable();
		
		FormLayout layout = new FormLayout(
				//"left:max(60dlu;p), 4dlu, left:max(60dlu;p)",
				//"left:180dlu, 4dlu, left:180dlu",
				"left:min(210dlu;p), 4dlu, left:min(210dlu;p)",
				"p, 2dlu, 140dlu, 2dlu, " +
				"p, 2dlu, 140dlu, 2dlu");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator(UIHandler.getText("recommend.title.userTopics"), cc.xy(1, 1));
		builder.add(new JScrollPane(tblUserTopics), cc.xy(1, 3));
		
		builder.addSeparator(UIHandler.getText("recommend.title.topicTerms"), cc.xy(3, 1));
		builder.add(new JScrollPane(tblTopicTerms), cc.xy(3, 3));

		// table for users to recommend
		tblRecommendUsers = new RecommendUsersTable();		

		MouseListener selectRecommendedUserActionListener = (MouseListener)(GenericListener.create(
				MouseListener.class,
				"mouseReleased",
				this,
				"selectRecommendedUserAction"));
		tblRecommendUsers.addMouseListener(selectRecommendedUserActionListener);
		
		builder.addSeparator(UIHandler.getText("recommend.title.recommendedUsers"), cc.xyw(1, 5, 3));
		builder.add(new JScrollPane(tblRecommendUsers), cc.xyw(1, 7, 3));
		
		return builder.getPanel();
	}
	
	/**
	 * Updates the tables.
	 * 
	 * @param websiteId the website id
	 * @param topUserNum the top user number
	 * @param username the user id
	 * @param orderByColumn the orderby column
	 */
	public void updateTables(int websiteId, int topUserNum, String username, String orderByColumn) {
		this.topUserNum = topUserNum;
		this.username = username;
		this.orderByColumn = orderByColumn;
		
		tblUserTopics.removeAllRow();
		tblTopicTerms.removeAllRow();		
		tblRecommendUsers.removeAllRow();
		
		try {			
			List<TopicUserCluster> userTopics = handler.getTopicsByClusteredUser(websiteId, username);
			tblUserTopics.setRowData(userTopics);
			
			if (userTopics != null && userTopics.size() > 0) {
				TopicUserCluster userTopic = (TopicUserCluster) userTopics.get(0);
				int defaultTopicId = userTopic.getTopicId();
				List<TopicTerm> topicTerms = handler.getTermsByTopic(websiteId, defaultTopicId);
				tblTopicTerms.setRowData(topicTerms);
				
				List<TopicUserCluster> recommendUsers = handler.getRecommendClusteredUsers(websiteId, defaultTopicId, 
						topUserNum, username, orderByColumn);
				tblRecommendUsers.setRowData(recommendUsers);
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		

	}
	
	//////////////////////////////////////////
	/*       Defines event actions.         */
	//////////////////////////////////////////
	
	public void selectTopicAction(MouseEvent e) {
		int row = tblUserTopics.getSelectedRow();
		
		if (row < 0) {
			return;
		}
		
		if (e.getClickCount() == 2) {
			int websiteId = (Integer) tblUserTopics.getValueAt(row, 0);
			int topicId = (Integer) tblUserTopics.getValueAt(row, 1);
			
			try {
				List<TopicTerm> topicTerms = handler.getTermsByTopic(websiteId, topicId);
				tblTopicTerms.setRowData(topicTerms);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			try {
				List<TopicUserCluster> recommendUsers = handler.getRecommendClusteredUsers(websiteId, topicId, 
						topUserNum, username, orderByColumn);
				tblRecommendUsers.setRowData(recommendUsers);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void selectRecommendedUserAction(MouseEvent e) {
		if (e.getClickCount() == 2) {
			int row = tblRecommendUsers.getSelectedRow();
			String userUrl = (String) tblRecommendUsers.getValueAt(row, 6);
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
