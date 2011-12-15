package com.nhn.socialanalytics.miner.view;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class OpinionToolbar extends JPanel {
	
	JComboBox comboSite;
	JComboBox comboLanguage;
	JTextField tfldObject;
	JCheckBox chkboxFeature;
	JSpinner spinSubjectTF;
	JSpinner spinSubjectLinkTF;
	JSpinner spinPredicateTF;
	JSpinner spinPredicateLinkTF;
	JSpinner spinAttributeTF;
	JSpinner spinAttributeLinkTF;	
	
	public OpinionToolbar() {
		UIHandler.setResourceBundle("label");
	}
	
	
	
	
}
