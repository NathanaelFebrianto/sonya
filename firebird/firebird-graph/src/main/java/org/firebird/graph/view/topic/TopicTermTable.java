/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view.topic;

import java.util.List;
import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.firebird.graph.view.DefaultTable;
import org.firebird.graph.view.UIHandler;
import org.firebird.io.model.TopicTerm;


/**
 * Table for topic terms.
 * 
 * @author Young-Gue Bae
 */
public class TopicTermTable extends DefaultTable {

	private static final long serialVersionUID = 524781026461867511L;
	private DefaultTableModel tableModel;    
	
	/**
	 * Constructor.
	 * 
	 */
	public TopicTermTable() {
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
        
        // set sorter
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
		setRowSorter(sorter);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
	
	private Object[] columnNames() {
		Object[] colNames = new Object[] {
				UIHandler.getText("col.no"),
				UIHandler.getText("col.topic.term.websiteId"),
				UIHandler.getText("col.topic.term.topicId"),
				UIHandler.getText("col.topic.term.term"),
				UIHandler.getText("col.topic.term.score"),
		};
		
		return colNames;
	}

	/**
	 * Sets the row data.
	 * 
	 * @param list the list data to convert to row data
	 */
	public void setRowData(List<TopicTerm> list) {
	    removeAllRow();	            
        for (int row = 0; row < list.size(); row++) {
        	TopicTerm topicTerm = (TopicTerm) list.get(row);	        
	        Vector<Object> rowData = new Vector<Object>();
            
	        rowData.add(row + 1);
	        rowData.add(topicTerm.getWebsiteId());
	        rowData.add(topicTerm.getTopicId());
	        rowData.add(topicTerm.getTerm());
	        rowData.add(convertScale(topicTerm.getScore(), 2));
	        tableModel.insertRow(row, rowData);
	    }		
	}

}
