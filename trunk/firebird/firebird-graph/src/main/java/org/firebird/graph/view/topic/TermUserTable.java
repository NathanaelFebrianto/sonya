/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view.topic;

import java.util.List;
import java.util.Vector;

import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.firebird.graph.view.DefaultTable;
import org.firebird.graph.view.UIHandler;
import org.firebird.io.model.UserTerm;


/**
 * Table for term users.
 * 
 * @author Young-Gue Bae
 */
public class TermUserTable extends DefaultTable {

	private static final long serialVersionUID = -9046630922429008594L;
	private DefaultTableModel tableModel;    
	
	/**
	 * Constructor.
	 * 
	 */
	public TermUserTable() {
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
        //setColumnHidden(2, true);
        
        // set sorter
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
		setRowSorter(sorter);
		//setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
	
	private Object[] columnNames() {
		Object[] colNames = new Object[] {
				UIHandler.getText("col.no"),
				UIHandler.getText("col.term.user.websiteId"),
				UIHandler.getText("col.term.user.term"),
				UIHandler.getText("col.term.user.userId"),
				UIHandler.getText("col.term.user.userName"),
				UIHandler.getText("col.term.user.termFreq"),
				UIHandler.getText("col.term.user.tf"),
				UIHandler.getText("col.term.user.idf"),

		};
		
		return colNames;
	}

	/**
	 * Sets the row data.
	 * 
	 * @param list the list data to convert to row data
	 */
	public void setRowData(List<UserTerm> list) {
	    removeAllRow();	            
        for (int row = 0; row < list.size(); row++) {
        	UserTerm userTerm = (UserTerm) list.get(row);	        
	        Vector<Object> rowData = new Vector<Object>();
            
	        rowData.add(row + 1);
	        rowData.add(userTerm.getWebsiteId());
	        rowData.add(userTerm.getTerm());
	        rowData.add(userTerm.getUserId());
	        rowData.add(userTerm.getUserName());
	        rowData.add(userTerm.getTermFreq());
	        rowData.add(convertScale(userTerm.getTF(), 2));
	        rowData.add(convertScale(userTerm.getIDF(), 2));
	        tableModel.insertRow(row, rowData);
	    }		
	}

}
