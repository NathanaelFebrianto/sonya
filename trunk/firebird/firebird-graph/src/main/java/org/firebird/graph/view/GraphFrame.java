/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.graph.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A Social network graph frame.
 * 
 * @author Young-Gue Bae
 */
public class GraphFrame extends JFrame {

	private static final long serialVersionUID = 2214294379450099088L;

	/**
	 * Constructor.
	 * 
	 * @param applet the main applet
	 */
	public GraphFrame(JApplet applet) {
	    super("Firebird");
	    this.setLayout(new BorderLayout());
	    GraphPanel panelGraph = new GraphPanel(applet.getAppletContext());
	    this.add(panelGraph, BorderLayout.CENTER);
	    
	    JPanel footer = new JPanel();
	    footer.setLayout(new FlowLayout(FlowLayout.CENTER));
	    footer.add(new JLabel("Copyright(c) Firebird by louiezzang@hotmail.com. All Rights Reserved."));
	    
	    this.add(panelGraph, BorderLayout.CENTER);
	    this.add(footer, BorderLayout.SOUTH);
	    
	    this.setIconImage(UIHandler.getImageIcon("/firebird.png").getImage());
	    this.setSize(new Dimension(1250, 700));
	    this.setLocationRelativeTo(null);
	    this.setResizable(true);
	    
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
