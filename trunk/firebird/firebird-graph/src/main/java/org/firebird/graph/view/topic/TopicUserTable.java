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
import org.firebird.io.model.TopicUser;


/**
 * Table for topic users.
 * 
 * @author Young-Gue Bae
 */
public class TopicUserTable extends DefaultTable {

	private static final long serialVersionUID = 5385849251499917691L;
	private DefaultTableModel tableModel;    
	
	/**
	 * Constructor.
	 * 
	 */
	public TopicUserTable() {
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
        //setColumnHidden(5, true);	// profile image
        setColumnHidden(6, true);
        setColumnHidden(9, true);
        
        // set sorter
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
		setRowSorter(sorter);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		this.setIconCellRenderer(5);
		this.setRowHeight(55);
    }
	
	private Object[] columnNames() {
		Object[] colNames = new Object[] {
				UIHandler.getText("col.no"),
				UIHandler.getText("col.topic.user.websiteId"),
				UIHandler.getText("col.topic.user.topicId"),
				UIHandler.getText("col.topic.user.userId"),
				UIHandler.getText("col.topic.user.userName"),
				UIHandler.getText("col.topic.user.userProfileImage"),
				UIHandler.getText("col.topic.user.userUrl"),
				UIHandler.getText("col.topic.user.matchTermsCount"),
				UIHandler.getText("col.topic.user.score"),
				UIHandler.getText("col.topic.user.userMatchTerms"),
		};
		
		return colNames;
	}

	/**
	 * Sets the row data.
	 * 
	 * @param list the list data to convert to row data
	 */
	public void setRowData(List<TopicUser> list) {
	    removeAllRow();	            
        for (int row = 0; row < list.size(); row++) {
        	TopicUser topicUser = (TopicUser) list.get(row);	        
	        Vector<Object> rowData = new Vector<Object>();
	        
	        // user profile image
	        ImageIcon profileImage = null;
	        try {
	        	profileImage = new ImageIcon(new URL(topicUser.getUserProfileImage()));
	        } catch (Exception ex) {
	        	ex.printStackTrace();
	        }
	        
	        rowData.add(row + 1);
	        rowData.add(topicUser.getWebsiteId());
	        rowData.add(topicUser.getTopicId());
	        rowData.add(topicUser.getUserId());
	        rowData.add(topicUser.getUserName());
	        if (profileImage == null)	rowData.add("");
	        else	rowData.add(profileImage);
	        rowData.add(topicUser.getUserUrl());
	        rowData.add(topicUser.getUserMatchTermsCount());	        
	        rowData.add(convertScale(topicUser.getScore(), 2));
	        rowData.add(topicUser.getUserMatchTerms());
	        tableModel.insertRow(row, rowData);
	    }		
	}

}
