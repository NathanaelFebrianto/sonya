/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view;

import java.util.List;
import java.util.Vector;

import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.firebird.io.model.Vertex;


/**
 * Table for a graph data.
 * 
 * @author Young-Gue Bae
 */
public class VertexTable extends GraphTable {

	private static final long serialVersionUID = -6203182900465356334L;
	private DefaultTableModel tableModel;
    
	/**
	 * Constructor.
	 * 
	 */
	public VertexTable() {
        super();
        
        UIHandler.setResourceBundle("graph");        
        tableModel = new DefaultTableModel(columnNames(), 0) {
			public Class getColumnClass(int column) {
				Class returnValue;		
				if ((column >= 0) && (column < getColumnCount())) {					
					returnValue = getValueAt(0, column).getClass();
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
		setCellRenders();
    }
	
	private Object[] columnNames() {
		Object[] colNames = new Object[] {
				UIHandler.getText("col.no"),
				//UIHandler.getText("col.vertex.websiteid"),
				UIHandler.getText("col.vertex.id"),
				UIHandler.getText("col.vertex.name"),
				//UIHandler.getText("col.vertex.imageFile"),
				UIHandler.getText("col.vertex.degree"),
				UIHandler.getText("col.vertex.inDegree"),
				UIHandler.getText("col.vertex.outDegree"),
				UIHandler.getText("col.vertex.authority"),
				UIHandler.getText("col.vertex.hub"),
				UIHandler.getText("col.vertex.betweennessCentrality"),
				UIHandler.getText("col.vertex.closenessCentrality"),
				UIHandler.getText("col.vertex.eigenvectorCentrality"),
				UIHandler.getText("col.vertex.clusteringCoefficient"),
				UIHandler.getText("col.vertex.friendsCount"),
				UIHandler.getText("col.vertex.followersCount"),
				//UIHandler.getText("col.vertex.userNo"),
				//UIHandler.getText("col.vertex.userName"),
				UIHandler.getText("col.vertex.userUrl"),
				UIHandler.getText("col.vertex.blogEntryCount"),
				//UIHandler.getText("col.vertex.lastBlogEntryId"),
				UIHandler.getText("col.vertex.lastBlogEntryBody"),
				//UIHandler.getText("col.vertex.lastBlogEntryType"),
				UIHandler.getText("col.vertex.lastBlogEntryCreateDate"),
				//UIHandler.getText("col.vertex.lastBlogEntryReplyTo"),
				//UIHandler.getText("col.vertex.lastBlogEntryReferFrom"),
				//UIHandler.getText("col.vertex.createDate"),
				//UIHandler.getText("col.vertex.lastUpdateDate")		
		};
		
		return colNames;
	}
	
	private void setCellRenders() {
		setNumberCellRenderer(1, "######");
		setNumberCellRenderer(3, "###,###");
		setNumberCellRenderer(4, "###,###");
		setNumberCellRenderer(5, "###,###");
		setNumberCellRenderer(6, "0.00");
		setNumberCellRenderer(7, "0.00");
		setNumberCellRenderer(8, "###,###");
		setNumberCellRenderer(9, "###,###");
		setNumberCellRenderer(10, "###,###");
		setNumberCellRenderer(11, "0.00");
		setNumberCellRenderer(12, "###,###");
		setNumberCellRenderer(13, "###,###");
		setNumberCellRenderer(15, "###,###");
		setDateCellRenderer(17, "yyyy-MM-dd");
	}
	
	/**
	 * Constructor.
	 * 
	 * @param vertices the vertex list
	 */
	public VertexTable(List<Vertex> vertices) {
        this();
        setVertices(vertices);
    }

	/**
	 * Sets the vetices data into table.
	 * 
	 * @param vertices the vertex list
	 */
	public void setVertices(List<Vertex> vertices) {
	    removeAllRow();	            
        for (int row = 0; row < vertices.size(); row++) {
	        Vertex vertex = (Vertex)vertices.get(row);	        
	        Vector<Object> rowData = new Vector<Object>();
            
	        rowData.add(String.valueOf(row + 1));
	        //rowData.add(vertex.getWebsiteId());
	        rowData.add(vertex.getId());
	        //rowData.add(vertex.getNo());
	        rowData.add(vertex.getName());
	        //rowData.add(vertex.getImageFile());
	        rowData.add(vertex.getDegree());
	        rowData.add(vertex.getInDegree());
	        rowData.add(vertex.getOutDegree());
	        rowData.add(vertex.getAuthority());
	        rowData.add(vertex.getHub());
	        rowData.add(vertex.getBetweennessCentrality());
	        rowData.add(vertex.getClosenessCentrality());
	        rowData.add(vertex.getEigenvectorCentrality());
	        rowData.add(vertex.getClusteringCoefficient());
	        rowData.add(vertex.getFriendsCount());
	        rowData.add(vertex.getFollowersCount());
	        //rowData.add(vertex.getUserNo());
	        //rowData.add(vertex.getUserId());
	        //rowData.add(vertex.getUserName());
	        rowData.add(vertex.getUserUrl());
	        rowData.add(vertex.getBlogEntryCount());
	        //rowData.add(vertex.getLastBlogEntryId());
	        rowData.add(vertex.getLastBlogEntryBody());
	        //rowData.add(vertex.getLastBlogEntryType());
	        rowData.add(vertex.getLastBlogEntryCreateDate());
	        //rowData.add(vertex.getLastBlogEntryReplyTo());
	        //rowData.add(vertex.getLastBlogEntryDmTo());
	        //rowData.add(vertex.getLastBlogEntryReferFrom());
	        //rowData.add(vertex.getCreateDate());	             
	        
	        tableModel.insertRow(row, rowData);
	    }		
	}
	
	/*
	private BigDecimal convertScale(double d, int scale) {
		return new BigDecimal(d).setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
	*/
	
}
