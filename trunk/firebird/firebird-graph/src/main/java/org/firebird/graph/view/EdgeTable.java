/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.firebird.io.model.Edge;


/**
 * Table for edge data.
 * 
 * @author Young-Gue Bae
 */
public class EdgeTable extends GraphTable {
	
	private static final long serialVersionUID = -7528850847482435852L;
	private DefaultTableModel tableModel;
    
	/**
	 * Constructor.
	 * 
	 * @param edges the edge list
	 */
	public EdgeTable(List<Edge> edges) {
        this();
        setEdges(edges);
    }
	
	/**
	 * Constructor.
	 * 
	 */
	public EdgeTable() {
        super();
        
        UIHandler.setResourceBundle("graph");        
        tableModel = new DefaultTableModel(columnNames(), 0) {
			public Class getColumnClass(int column) {
				Class returnValue;		
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
		};
		
        setModel(tableModel);
        
        // set sorter
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
		setRowSorter(sorter);        
		// set cell renderers
		//setCellRenders();   
	}
	
	private Object[] columnNames() {
		Object[] colNames = new Object[] {
				UIHandler.getText("col.no"),
				//UIHandler.getText("col.edge.websiteId1"),
				//UIHandler.getText("col.edge.websiteId2"),
				UIHandler.getText("col.edge.vertex1"),
				UIHandler.getText("col.edge.vertex2"),
				//UIHandler.getText("col.edge.vertexNo1"),
				//UIHandler.getText("col.edge.vertexNo2"),
				//UIHandler.getText("col.edge.color"),
				//UIHandler.getText("col.edge.width"),
				//UIHandler.getText("col.edge.opacity"),
				//UIHandler.getText("col.edge.directed"),
				//UIHandler.getText("col.edge.relationship"),
				//UIHandler.getText("col.edge.edgeWeight"),
				UIHandler.getText("col.edge.betweennessCentrality"),
				//UIHandler.getText("col.edge.replyCount"),
				//UIHandler.getText("col.edge.dmCount"),
				//UIHandler.getText("col.edge.referCount"),
				//UIHandler.getText("col.edge.lastReplyDate"),
				//UIHandler.getText("col.edge.lastDmDate"),
				//UIHandler.getText("col.edge.lastReferDate"),
				//UIHandler.getText("col.edge.createDate"),
				//UIHandler.getText("col.edge.lastUpdateDate"),
				//UIHandler.getText("col.edge.colCreateDate"),
				//UIHandler.getText("col.edge.colLastUpdateDate")
		};
		
		return colNames;
	}
	
	private void setCellRenders() {
		setNumberCellRenderer(0, "######");
		setNumberCellRenderer(3, "###,###");
		//setDateCellRenderer(4, "yyyy-MM-dd");
	}
	
	/**
	 * Sets the edges data into table.
	 * 
	 * @param edges the edge list
	 */
	public void setEdges(List<Edge> edges) {
	    removeAllRow();	            
        for (int row = 0; row < edges.size(); row++) {
        	Edge edge = (Edge)edges.get(row);	        
	        Vector<Object> rowData = new Vector<Object>();
            
	        rowData.add(row + 1);
	        rowData.add(edge.getVertex1());
	        rowData.add(edge.getVertex2());
	        rowData.add(convertScale(edge.getBetweennessCentrality(), 0));
	        //rowData.add(edge.getLastUpdateDate());
	        
	        tableModel.insertRow(row, rowData);
	    }		
	}
	
	private BigDecimal convertScale(double d, int scale) {
		return new BigDecimal(d).setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
}
