/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view.topic;

import java.net.URL;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.event.HyperlinkEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.firebird.graph.view.DefaultTable;
import org.firebird.graph.view.UIHandler;
import org.firebird.io.model.TopicUserCluster;


/**
 * Table for topic users cluster.
 * 
 * @author Young-Gue Bae
 */
public class TopicUserClusterTable extends DefaultTable {

	private static final long serialVersionUID = 2920137218958755822L;
	private DefaultTableModel tableModel;    
	
	/**
	 * Constructor.
	 * 
	 */
	public TopicUserClusterTable() {
        super();
        
        UIHandler.setResourceBundle("graph");        
        tableModel = new DefaultTableModel(columnNames(), 0) {
			
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
        
        // set hidden colum
        setColumnHidden(1, true);
        setColumnHidden(2, true);
        setColumnHidden(5, true);
        setColumnHidden(6, true);
        
        // set sorter
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
		setRowSorter(sorter);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
	
	private Object[] columnNames() {
		Object[] colNames = new Object[] {
				UIHandler.getText("col.no"),
				UIHandler.getText("col.topic.user.cluster.websiteId"),
				UIHandler.getText("col.topic.user.cluster.topicId"),
				UIHandler.getText("col.topic.user.cluster.userId"),
				UIHandler.getText("col.topic.user.cluster.userName"),
				UIHandler.getText("col.topic.user.cluster.userProfileImage"),
				UIHandler.getText("col.topic.user.cluster.userUrl"),
				UIHandler.getText("col.topic.user.cluster.isTopicUser"),
				UIHandler.getText("col.topic.user.cluster.cluster"),
				UIHandler.getText("col.topic.user.cluster.authority"),
				UIHandler.getText("col.topic.user.cluster.topicScore"),
				UIHandler.getText("col.topic.user.cluster.authorityTopicScore"),
		};
		
		return colNames;
	}

	/**
	 * Sets the row data.
	 * 
	 * @param list the list data to convert to row data
	 */
	public void setRowData(List<TopicUserCluster> list) {
	    removeAllRow();	            
        for (int row = 0; row < list.size(); row++) {
        	TopicUserCluster topicUser = (TopicUserCluster) list.get(row);	        
	        Vector<Object> rowData = new Vector<Object>();
            
	        // user profile image
	        ImageIcon profileImage = null;
	        try {
	        	profileImage = new ImageIcon(new URL(topicUser.getUserProfileImage()));
	        } catch (Exception ex) {
	        	ex.printStackTrace();
	        }
	        
	        // user hyperlink url
	        HyperlinkEvent hyperlink = null;
	        try {
	        	hyperlink = new HyperlinkEvent(this, HyperlinkEvent.EventType.ACTIVATED, new URL(topicUser.getUserUrl()));
	        } catch (Exception ex) {
	        	ex.printStackTrace();
	        }
	        
	        rowData.add(row + 1);
	        rowData.add(topicUser.getWebsiteId());
	        rowData.add(topicUser.getTopicId());
	        rowData.add(topicUser.getUserId());
	        rowData.add(topicUser.getUserName());
	        rowData.add(profileImage);
	        rowData.add(topicUser.getUserUrl()); //rowData.add(hyperlink);	
	        rowData.add(topicUser.isTopicUser());
	        rowData.add(topicUser.getCluster());
	        rowData.add(convertScale(topicUser.getAuthority(), 2));
	        rowData.add(convertScale(topicUser.getTopicScore(), 2));
	        rowData.add(convertScale(topicUser.getAuthorityTopicScore(), 2));
	        tableModel.insertRow(row, rowData);
	    }		
	}

}
