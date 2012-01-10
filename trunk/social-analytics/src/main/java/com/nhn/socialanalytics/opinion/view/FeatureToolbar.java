package com.nhn.socialanalytics.opinion.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.nhn.socialanalytics.nlp.sentiment.SentimentAnalyzer;
import com.nhn.socialanalytics.opinion.common.FieldConstants;
import com.nhn.socialanalytics.opinion.lucene.DocIndexSearcher;
import com.nhn.socialanalytics.opinion.search.FeatureResultSet;
import com.nhn.socialanalytics.opinion.search.OpinionFilter;
import com.nhn.socialanalytics.opinion.search.OpinionSearcher;
import com.toedter.calendar.JDateChooser;

@SuppressWarnings("serial")
public class FeatureToolbar extends JPanel {
	
	OpinionViewerApplet parent;
	
	JComboBox comboSite;
	JComboBox comboLanguage;
	JTextField tfldObject;
	JCheckBox chkboxIncludeEtc;
	JDateChooser calStartDate;
	JDateChooser calEndDate;		
	
	public FeatureToolbar(OpinionViewerApplet parent) {
		this.parent = parent;
		UIHandler.setResourceBundle("label");
		
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setupFields();
		setupUI();
	}
	
	private void setupFields() {
		comboSite = new JComboBox();
		comboSite.addItem(new ObjectModel("App Store", "appstore"));
		comboSite.addItem(new ObjectModel("Android Market", "androidmarket"));
		comboSite.addItem(new ObjectModel("Twitter", "twitter"));
		comboSite.addItem(new ObjectModel("me2DAY", "me2day"));		
		comboLanguage = new JComboBox();
		comboLanguage.addItem(new ObjectModel("Korean", "Korean"));
		comboLanguage.addItem(new ObjectModel("Japanese", "Japanese"));		
		tfldObject = new JTextField(10);
		
		calStartDate = new JDateChooser("yyyy/MM/dd", "####/##/##", '_');
		calStartDate.setDate(new Date());
		calEndDate = new JDateChooser("yyyy/MM/dd", "####/##/##", '_');	
		calEndDate.setDate(new Date());
		chkboxIncludeEtc = new JCheckBox();
	}
	
	private void setupUI() {
		FormLayout layout = new FormLayout(
				"left:p",
				"25dlu");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.add(makeSearchOptionPanel(), cc.xy(1, 1));
		
		add(builder.getPanel());
		builder.getPanel().setBorder(BorderFactory.createTitledBorder(""));
	}
	
	private JComponent makeSearchOptionPanel() {
		FormLayout layout = new FormLayout(
				"left:p, 4dlu, p, 4dlu, p, 4dlu, p, 4dlu, p, 4dlu, p, 4dlu, "	+
				"p, 4dlu, p, 4dlu, p, 4dlu, p, 4dlu, p, 4dlu, p, 4dlu, p",  // columns
				"p, " +	// row1: seprator row
				"2dlu, " +	// row2: gap
				"p, 4dlu, p, 4dlu, p, 4dlu, p, 4dlu, p, 4dlu, p, 4dlu, " + // row3: content
				"p, 4dlu, p, 4dlu, p, 4dlu, p, 4dlu, p, 4dlu, p, 4dlu, p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		// row
		//builder.addSeparator(UIHandler.getText("label.search.option"), cc.xyw(1, 1, 23));
		builder.addLabel(UIHandler.getText("label.site"), cc.xy(1, 3));
		builder.add(comboSite, cc.xy(3, 3));
		builder.addLabel(UIHandler.getText("label.language"), cc.xy(5, 3));
		builder.add(comboLanguage, cc.xy(7, 3));
		builder.addLabel(UIHandler.getText("label.object"), cc.xy(9, 3));
		builder.add(tfldObject, cc.xy(11, 3));
		builder.addLabel(UIHandler.getText("label.collect.duration"), cc.xy(13, 3));
		builder.add(calStartDate, cc.xy(15, 3));
		builder.addLabel(" ~ ", cc.xy(17, 3));
		builder.add(calEndDate, cc.xy(19, 3));
		builder.addLabel(UIHandler.getText("label.include.etc"), cc.xy(21, 3));
		builder.add(chkboxIncludeEtc, cc.xy(23, 3));
		
		JButton btnOk = new JButton(UIHandler.getText("btn.search"));		
		ActionListener okActionListener = (ActionListener)(GenericListener.create(
		        ActionListener.class,
				"actionPerformed",
				this,
				"okAction"));		
		btnOk.addActionListener(okActionListener);
				
		builder.add(btnOk, cc.xy(25, 3));
		
		return builder.getPanel();
	}
	
	
	//////////////////////////////////////////
	/*       Defines event actions.         */
	//////////////////////////////////////////
	
	public void okAction(ActionEvent e) {
		
		try {
			/////////////////////////////////
			/*   base term ==> SUBJECT     */
			/////////////////////////////////
			
			String site = ((ObjectModel) comboSite.getSelectedItem()).getValue();
			String language = ((ObjectModel) comboLanguage.getSelectedItem()).getValue();
			String object = tfldObject.getText();	
			Date startDate = calStartDate.getDate();
			Date endDate = calEndDate.getDate();
			boolean includeEtc = chkboxIncludeEtc.isSelected();			
			boolean excludeStopwords = true;
			
			if (object == null || object.equals("")) {
				JOptionPane.showMessageDialog(parent, UIHandler.getText("msg.input.object.name"), "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			String indexBaseDir = "./bin/data/" + site + File.separator + "index";
			File[] indexDirs = DocIndexSearcher.getIndexDirectories(indexBaseDir, startDate, endDate);
			
			Set<String> customStopwords = new HashSet<String>();
			customStopwords.add("좋은데");
			customStopwords.add("바이버보");
			customStopwords.add("그누구");
			customStopwords.add("이건");
			customStopwords.add("역시");
			customStopwords.add("아직");
			customStopwords.add("메세지는");
			customStopwords.add("한가지");

			DocIndexSearcher indexSearcher = new DocIndexSearcher(indexDirs);
			indexSearcher.putStopwordFile(new File("./conf/stopword_ko.txt"));
			indexSearcher.putStopwordFile(new File("./conf/stopword_ja.txt"));
			indexSearcher.putCustomStopwords(customStopwords);
			indexSearcher.putSentimentAnalyzer(FieldConstants.LANG_KOREAN, SentimentAnalyzer.getInstance(new File("./bin/liwc/LIWC_ko.txt")));
			indexSearcher.putSentimentAnalyzer(FieldConstants.LANG_JAPANESE, SentimentAnalyzer.getInstance(new File("./bin/liwc/LIWC_ja.txt")));
			System.out.println("stopwords == " + indexSearcher.getStopwords());			
			
			OpinionFilter filter = new OpinionFilter();
			filter.setObject(object);
			filter.setLanguage(language);				
			
			OpinionSearcher opinionSearcher = new OpinionSearcher(indexSearcher);
			FeatureResultSet featureResultSet = opinionSearcher.getFeatureResultSet(filter);	

			parent.showFeatureChart(featureResultSet, site, includeEtc);

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(parent, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
}
