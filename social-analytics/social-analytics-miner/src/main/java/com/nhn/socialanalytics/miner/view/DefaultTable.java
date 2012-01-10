package com.nhn.socialanalytics.miner.view;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Default table.
 * 
 * @author Younggue Bae
 */
@SuppressWarnings("serial")
public class DefaultTable extends JTable {

	public DefaultTable() {
        super();
        setDefaultStyle();
    }

	public DefaultTable(int numRows, int numColumns) {
		super(numRows, numColumns);
		setDefaultStyle();
	}

	public DefaultTable(Object[][] rowData, Object[] columnNames) {
		super(rowData, columnNames);
		setDefaultStyle();
	}

	public DefaultTable(TableModel dm) {
		super(dm);
		setDefaultStyle();
	}

	public DefaultTable(TableModel dm, TableColumnModel cm) {
		super(dm, cm);
		setDefaultStyle();
	}
	
	public DefaultTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
		super(dm, cm, sm);
		setDefaultStyle();
	}

	public DefaultTable(Vector rowData, Vector columnNames) {
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
	    else {
	    	int size = 55;
		    getColumnModel().getColumn(column).setPreferredWidth(size);
		    getColumnModel().getColumn(column).setMinWidth(size);
		    getColumnModel().getColumn(column).setMaxWidth(size);
	    }
	}

	public void setCheckBoxCellEditor(int column) {
	    JCheckBox checkbox = new JCheckBox();
	    DefaultCellEditor editor = new DefaultCellEditor(checkbox); 
	    getColumnModel().getColumn(column).setCellEditor(editor); 
	}
	
	public void setNumberCellRenderer(int column, String format) {
		TableColumnModel tcm = this.getColumnModel();
		TableColumn tc = tcm.getColumn(column);
		tc.setCellRenderer(new NumberRenderer(format));
	}
	
	public void setDateCellRenderer(int column, String format) {
		TableColumnModel tcm = this.getColumnModel();
		TableColumn tc = tcm.getColumn(column);
		tc.setCellRenderer(new DateRenderer(format));
	}
	
	public void setIconCellRenderer(int column) {
		TableColumnModel tcm = this.getColumnModel();
		TableColumn tc = tcm.getColumn(column);
		tc.setCellRenderer(new IconCellRenderer());
	}
	
	public void setLineWrapCellRenderer(int column) {
		TableColumnModel tcm = this.getColumnModel();
		TableColumn tc = tcm.getColumn(column);
		tc.setCellRenderer(new LineWrapCellRenderer());
	}
	
	protected BigDecimal convertScale(double d, int scale) {
		return new BigDecimal(d).setScale(scale, BigDecimal.ROUND_HALF_UP);
	}	
	
	public class NumberRenderer extends DefaultTableCellRenderer {
		private String format;

		public NumberRenderer(String format) {
			super();
			setHorizontalAlignment(SwingConstants.RIGHT);
			this.format = format;
		}

		public void setValue(Object value) {
			if (value != null) {
				DecimalFormat formatter = new DecimalFormat(format);
				value = formatter.format(value);
			}
			super.setValue(value);
		}
	}
	
	public class DateRenderer extends DefaultTableCellRenderer {
		private String format;

		public DateRenderer(String format) {
			super();
			setHorizontalAlignment(SwingConstants.CENTER);
			this.format = format;
		}

		public void setValue(Object value) {
			if (value != null) {
				SimpleDateFormat formatter = new SimpleDateFormat(format);
				value = formatter.format(value);
			}
			super.setValue(value);
		}
	}
	
	public class IconCellRenderer extends DefaultTableCellRenderer { 
		private static final long serialVersionUID = -4820370669578276713L;

		public void setValue(Object value) { 
	        if(value instanceof ImageIcon) { 
	            ImageIcon icon = (ImageIcon)value; 
	            setIcon(icon); 
	            setText("");
	            //setText(icon.getDescription()); 
	        } 
	        else 
	            super.setValue(value); 
	    } 
	} 
	
	
	
	public class LineWrapCellRenderer extends JTextArea implements
			TableCellRenderer {

		private final DefaultTableCellRenderer adaptee = new DefaultTableCellRenderer();
		// map from table to map of rows to map of column heights 
		private final Map cellSizes = new HashMap();

		public LineWrapCellRenderer() {
			setLineWrap(true);
			setWrapStyleWord(true);
		}

		public Component getTableCellRendererComponent(
			//
			JTable table, Object obj, boolean isSelected, boolean hasFocus,
				int row, int column) {
			// set the colours, etc. using the standard for that platform
			adaptee.getTableCellRendererComponent(table, obj, isSelected,
					hasFocus, row, column);
			setForeground(adaptee.getForeground());
			setBackground(adaptee.getBackground());
			setBorder(adaptee.getBorder());
			setFont(adaptee.getFont());
			setText(adaptee.getText());

			// This line was very important to get it working with JDK1.4
			TableColumnModel columnModel = table.getColumnModel();
			setSize(columnModel.getColumn(column).getWidth(), 100000);
			int height_wanted = (int) getPreferredSize().getHeight();
			addSize(table, row, column, height_wanted);
			height_wanted = findTotalMaximumRowSize(table, row);
			if (height_wanted != table.getRowHeight(row)) {
				table.setRowHeight(row, height_wanted);
			}
			return this;
		}

		private void addSize(JTable table, int row, int column, int height) {
			Map rows = (Map) cellSizes.get(table);
			if (rows == null) {
				cellSizes.put(table, rows = new HashMap());
			}
			Map rowheights = (Map) rows.get(new Integer(row));
			if (rowheights == null) {
				rows.put(new Integer(row), rowheights = new HashMap());
			}
			rowheights.put(new Integer(column), new Integer(height));
		}

		/**
		 * Look through all columns and get the renderer. If it is also a
		 * TextAreaRenderer, we look at the maximum height in its hash table for
		 * this row.
		 */
		private int findTotalMaximumRowSize(JTable table, int row) {
			int maximum_height = 0;
			Enumeration columns = table.getColumnModel().getColumns();
			while (columns.hasMoreElements()) {
				TableColumn tc = (TableColumn) columns.nextElement();
				TableCellRenderer cellRenderer = tc.getCellRenderer();
				if (cellRenderer instanceof LineWrapCellRenderer) {
					LineWrapCellRenderer tar = (LineWrapCellRenderer) cellRenderer;
					maximum_height = Math.max(maximum_height,
							tar.findMaximumRowSize(table, row));
				}
			}
			return maximum_height;
		}

		private int findMaximumRowSize(JTable table, int row) {
			Map rows = (Map) cellSizes.get(table);
			if (rows == null)
				return 0;
			Map rowheights = (Map) rows.get(new Integer(row));
			if (rowheights == null)
				return 0;
			int maximum_height = 0;
			for (Iterator it = rowheights.entrySet().iterator(); it.hasNext();) {
				Map.Entry entry = (Map.Entry) it.next();
				int cellHeight = ((Integer) entry.getValue()).intValue();
				maximum_height = Math.max(maximum_height, cellHeight);
			}
			return maximum_height;
		}
	}
	 
}
