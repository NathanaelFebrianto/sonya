package com.nhn.socialanalytics.opinion.view;

import java.util.List;
import java.util.Vector;

import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.nhn.socialanalytics.opinion.common.OpinionDocument;


/**
 * Table for detail document.
 * 
 * @author Younggue Bae
 */
@SuppressWarnings("serial")
public class DetailDocTable extends DefaultTable {
	
	private DefaultTableModel tableModel;    
	
	public DetailDocTable(List<OpinionDocument> docs) {
        this();
        setRowData(docs, false);
    }
	
	/**
	 * Constructor.
	 * 
	 */
	public DetailDocTable() {
        super();
        
        UIHandler.setResourceBundle("label");        
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
		
		setColumnWidth(0, 20);
		setColumnWidth(7, 200);
		setColumnWidth(8, 200);
		
		setLineWrapCellRenderer(7);
		setLineWrapCellRenderer(8);
		
		setColumnHidden(6, true);
		//setColumnHidden(8, true);	
    }
	
	private Object[] columnNames() {
		Object[] colNames = new Object[] {
				UIHandler.getText("col.no"),
				//UIHandler.getText("col.doc.id"),
				//UIHandler.getText("col.collect.date"),
				UIHandler.getText("col.date"),
				//UIHandler.getText("col.user.id"),
				UIHandler.getText("col.user.name"),
				//UIHandler.getText("col.feature"),
				UIHandler.getText("col.main.feature"),
				//UIHandler.getText("col.clause.feature"),
				UIHandler.getText("col.clause.main.feature"),
				UIHandler.getText("col.subject.predicate"),	
				UIHandler.getText("col.subject.predicate.translate"),	
				//UIHandler.getText("col.predicate"),
				//UIHandler.getText("col.attribute"),
				UIHandler.getText("col.text"),
				UIHandler.getText("col.text.translate"),
				UIHandler.getText("col.polarity"),
				//UIHandler.getText("col.polarity.strength"),
				UIHandler.getText("col.clause.polarity"),
				//UIHandler.getText("col.clause.polarity.strength"),
		};
		
		return colNames;
	}
	
	/*
	private void setCellRenders() {
		setNumberCellRenderer(0, "######");
		setNumberCellRenderer(1, "###,###");
		setNumberCellRenderer(2, "0.00");
		setDateCellRenderer(3, "yyyy-MM-dd");
	}
	*/

	public void setRowData(List<OpinionDocument> docs, boolean translate) {
		if (translate) {
			//setColumnHidden(6, false);
			//setColumnHidden(8, false);
			//setColumnWidth(8, 200);
		}
		else {
			//setColumnHidden(6, true);
			//setColumnHidden(8, true);		
		}
		
	    removeAllRow();	            
        for (int row = 0; row < docs.size(); row++) {
        	OpinionDocument doc = (OpinionDocument) docs.get(row);	        
	        Vector<Object> rowData = new Vector<Object>();
            
	        rowData.add(row + 1);
	        //rowData.add(doc.getDocId());
	        //rowData.add(doc.getCollectDate());
	        rowData.add(doc.getDate());
	        //rowData.add(doc.getAuthorId());
	        rowData.add(doc.getAuthorName());
	        //rowData.add(doc.getDocFeature());
	        rowData.add(doc.getDocMainFeature());
	        //rowData.add(doc.getClauseFeature());
	        rowData.add(doc.getClauseMainFeature());
	        rowData.add(doc.getSubject() + " " + doc.getPredicate());
	        if (translate) {
	        	//rowData.add(Translator.translate(doc.getSubject()) + " " + Translator.translate(doc.getPredicate()));
	        	rowData.add("");
	        }
	        else {
	        	rowData.add("");
	        }
	        //rowData.add(doc.getPredicate());
	        //rowData.add(doc.getAttribute());
	        rowData.add(doc.getText());
	        if (translate) {
	        	rowData.add(Translator.translate(doc.getText()));
	        }
	        else {
	        	rowData.add("");
	        }
	        rowData.add(convertScale(doc.getDocPolarity() * doc.getDocPolarityStrength(), 1));
	        //rowData.add(convertScale(doc.getPolarityStrength(), 1));
	        rowData.add(convertScale(doc.getClausePolarity() * doc.getClausePolarityStrength(), 1));
	        //rowData.add(convertScale(doc.getClausePolarityStrength(), 1));             
	        
	        tableModel.insertRow(row, rowData);
	    }		
	}

}
