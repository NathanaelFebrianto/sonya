/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view;

import java.awt.Color;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * Table for vertices.
 * 
 * @author Young-Gue Bae
 */
public class GraphTable extends JTable {

	private static final long serialVersionUID = 7544121316834110980L;

	public GraphTable() {
        super();
        setDefaultStyle();
    }

	public GraphTable(int numRows, int numColumns) {
		super(numRows, numColumns);
		setDefaultStyle();
	}

	public GraphTable(Object[][] rowData, Object[] columnNames) {
		super(rowData, columnNames);
		setDefaultStyle();
	}

	public GraphTable(TableModel dm) {
		super(dm);
		setDefaultStyle();
	}

	public GraphTable(TableModel dm, TableColumnModel cm) {
		super(dm, cm);
		setDefaultStyle();
	}
	
	public GraphTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
		super(dm, cm, sm);
		setDefaultStyle();
	}

	public GraphTable(Vector rowData, Vector columnNames) {
		super(rowData, columnNames);
		setDefaultStyle();
	}
	
	public void removeRow(int row) {
	    TableModel tm = getModel();
	    if (tm instanceof DefaultTableModel) {
	        DefaultTableModel dtm = (DefaultTableModel)tm;
	        dtm.removeRow(row);
	    }
	}
	
	public void setDefaultStyle() {
        //setHeaderBackground(new Color(202, 230, 243));
	    //setHeaderForeground(Color.white);
	    //setSelectionBackground(new Color(235, 235, 235));
		setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	}
	
	public void setSortable(boolean sortable) {
        if (sortable) {
    		setAutoCreateRowSorter(true);
            //TableRowSorter sorter = new TableRowSorter(getModel());
            //setRowSorter(sorter);	       	
        }
        else {
        	setAutoCreateRowSorter(false);
        }	
	}

	public void removeSelectedRow() {
	    TableModel tm = getModel();
	    if (tm instanceof DefaultTableModel) {
	        DefaultTableModel dtm = (DefaultTableModel)tm;
	        dtm.removeRow(getSelectedRow());
	    }
	}

	public void removeAllRow() {
	    TableModel tm = getModel();
        DefaultTableModel dtm = (DefaultTableModel)tm;
        for (int row = dtm.getRowCount() - 1; row >= 0; row--) {
	            dtm.removeRow(row);
        }
	}
	
	public void setHeaderBackground(Color color) {
	    getTableHeader().setBackground(color);
	}

	public void setHeaderForeground(Color color) {
	    getTableHeader().setForeground(color);
	}

	public void setColumnWidth(int column, int size) {
	    getColumnModel().getColumn(column).setPreferredWidth(size);
	    getColumnModel().getColumn(column).setMinWidth(size);
	    getColumnModel().getColumn(column).setResizable(true);
	}

	public void setColumnHidden(int column, boolean hidden) {
	    if (hidden) {
		    getColumnModel().getColumn(column).setPreferredWidth(0);
		    getColumnModel().getColumn(column).setMinWidth(0);
		    getColumnModel().getColumn(column).setMaxWidth(0);
	    }
	}

	public void setCheckBoxCellEditor(int column) {
	    JCheckBox checkbox = new JCheckBox();
	    DefaultCellEditor editor = new DefaultCellEditor(checkbox); 
	    getColumnModel().getColumn(column).setCellEditor(editor); 
	}
	
}
