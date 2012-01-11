package com.nhn.socialanalytics.common.collect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nhn.socialanalytics.common.util.DateUtil;
import com.nhn.socialanalytics.nlp.feature.FeatureClassifier;
import com.nhn.socialanalytics.nlp.morpheme.MorphemeAnalyzer;
import com.nhn.socialanalytics.nlp.semantic.SemanticAnalyzer;
import com.nhn.socialanalytics.nlp.semantic.SemanticClause;
import com.nhn.socialanalytics.nlp.sentiment.SentimentAnalyzer;
import com.nhn.socialanalytics.opinion.common.DetailDoc;

public abstract class Collector {

	public static final String DELIMITER = "\t";
	public static final String LANG_KOREAN = "Korean";
	public static final String LANG_JAPANESE = "Japanese";
	public static final String LANG_ENGLISH = "English";
	public Set<Pattern> spamFilterSet = new HashSet<Pattern>();
	
	public Map<String, MorphemeAnalyzer> morphemeAnalyzers = new HashMap<String, MorphemeAnalyzer>();
	public Map<String, SemanticAnalyzer> semanticAnalyzers = new HashMap<String, SemanticAnalyzer>();
	public Map<String, SentimentAnalyzer> sentimentAnalyzers = new HashMap<String, SentimentAnalyzer>();
	public Map<String, Map<String, FeatureClassifier>> featureClassifiers = new HashMap<String, Map<String, FeatureClassifier>>();
	
	public Collector() { }
	
	public void setSpamFilter(File spamFilterFile) {
		try {
			spamFilterSet = this.loadSpamFilterSet(spamFilterFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Set<Pattern> loadSpamFilterSet(File spamFilterFile) throws IOException {
		Set<Pattern> spamFilterSet = new HashSet<Pattern>();
		
		if (spamFilterFile == null)
			return spamFilterSet;
		
		BufferedReader in = null;
		
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(spamFilterFile), "utf-8"));
			String line = "";
			while((line = in.readLine()) != null) {
				line = line.trim();
				
				if (line.startsWith("#")) {
					continue;
				}
				if (!line.equals("")) {
					String regex = line.replaceAll("\\*", "[\\\\w']*");			
					spamFilterSet.add(Pattern.compile(regex));
				}
			}
		}catch(IOException e){
			e.printStackTrace();
			throw e;
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
		}
		
		return spamFilterSet;
	}
	
	protected boolean isSpam(String text) {
		if (text == null)
			return false;
		
		text = text.toLowerCase();			
		for (Iterator<Pattern> it = spamFilterSet.iterator(); it.hasNext();) {
			Pattern pattern = it.next();
			Matcher m = pattern.matcher(text);
			while (m.find()) {
				return true;
			}			
		}		
		return false;		
	}
	
	protected File[] getDocumentIndexDirsToSearch(String parentIndexDir, Date collectDate) {		
		File currentDocIndexDir = this.getDocIndexDir(parentIndexDir, collectDate);
		File beforeDocIndexDir = this.getDocIndexDir(parentIndexDir,  DateUtil.addDay(collectDate, -1));
		
		if (currentDocIndexDir.exists() && currentDocIndexDir.listFiles().length > 0) {
			File[] indexDirs = new File[1];
			indexDirs[0] = currentDocIndexDir;
			
			return indexDirs;
		} else {
			File[] indexDirs = new File[2];
			indexDirs[0] = beforeDocIndexDir;
			indexDirs[1] = currentDocIndexDir;
			return indexDirs;
		}
	}
	
	protected File getDataFile(String dataDir, String objectId, Date collectDate) {
		File dir = new File(dataDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		String strDate = DateUtil.convertDateToString("yyyyMMdd", collectDate);	
		return new File(dataDir + File.separator + objectId + "_" + strDate + ".txt");
	}
	
	protected File getDocIndexDir(String parentIndexDir, Date collectDate) {
		File dir = new File(parentIndexDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		String strDate = DateUtil.convertDateToString("yyyyMMdd", collectDate);		
		return new File(parentIndexDir + File.separator + strDate);
	}
	
	protected File getCollectHistoryFile(String dataDir, String objectId) {
		return new File(dataDir + File.separator + objectId + ".txt");	
	}
	
	public void putMorphemeAnalyzer(String language, MorphemeAnalyzer analyzer) {
		morphemeAnalyzers.put(language, analyzer);
	}
	
	public void putSemanticAnalyzer(String language, SemanticAnalyzer analyzer) {
		semanticAnalyzers.put(language, analyzer);
	}
	
	public void putSentimentAnalyzer(String language, SentimentAnalyzer analyzer) {
		sentimentAnalyzers.put(language, analyzer);
	}
	
	public void putFeatureClassifier(String object, String language, FeatureClassifier objectClassifier) {
		Map<String, FeatureClassifier> objectClassifiers = (Map<String, FeatureClassifier>) featureClassifiers.get(object);
		
		if (objectClassifiers != null) {
			objectClassifiers.put(language, objectClassifier);
			//featureClassifiers.put(object, objectClassifiers);
		}
		else {
			Map<String, FeatureClassifier> newObjectClassifiers = new HashMap<String, FeatureClassifier>();
			newObjectClassifiers.put(language, objectClassifier);
			featureClassifiers.put(object, newObjectClassifiers);			
		}		
	}
	
	public MorphemeAnalyzer getMorphemeAnalyzer(String language) {
		return (MorphemeAnalyzer) morphemeAnalyzers.get(language);
	}
	
	public SemanticAnalyzer getSemanticAnalyzer(String language) {
		return (SemanticAnalyzer) semanticAnalyzers.get(language);
	}
	
	public SentimentAnalyzer getSentimentAnalyzer(String language) {
		return (SentimentAnalyzer) sentimentAnalyzers.get(language);
	}
	
	public FeatureClassifier getFeatureClassifier(String object, String language) {
		Map<String, FeatureClassifier> objectClassifiers = (Map<String, FeatureClassifier>) featureClassifiers.get(object);
		
		FeatureClassifier classifier = (FeatureClassifier) objectClassifiers.get(language);
		if (classifier == null) {
			classifier = (FeatureClassifier) objectClassifiers.get(LANG_KOREAN);
		}
		
		return classifier;
	}
	
	protected DetailDoc setClauseFeatureToDocument(String object, String language, SemanticClause clause, DetailDoc doc) {
		/*
		String clauseStandardLabels = clause.getStandardSubject() + " " +
				clause.getStandardPredicate() + " " +
				clause.getStandardAttributes();
		*/
		String clauseStandardLabels = clause.makeStandardLabel(" ", true, false, false);
		
		FeatureClassifier featureClassifier = this.getFeatureClassifier(object, language);
		
		Map<String, Double> clauseFeatureCounts = featureClassifier.getFeatureCounts(clauseStandardLabels, true);
		String clauseFeature = featureClassifier.getFeatureLabel(clauseFeatureCounts);
		String clauseMainFeature = featureClassifier.getMainFeatureLabel(clauseFeatureCounts);
		
		doc.setClauseFeature(clauseFeature);
		doc.setClauseMainFeature(clauseMainFeature);
		
		return doc;
	}

}
