package com.nhn.socialanalytics.common.collect;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.mahout.common.iterator.FileLineIterator;

import com.nhn.socialanalytics.common.util.DateUtil;

public abstract class Collector {

	protected static final String DELIMITER = "\t";
	protected Set<Pattern> spamFilterSet = new HashSet<Pattern>();
	
	public Collector() { }
	
	public Collector(File spamFilterFile) {
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
		
		FileLineIterator it = new FileLineIterator(new FileInputStream(spamFilterFile));
		while (it.hasNext()) {
			String line = it.next();
			line = line.trim();
			if (line.startsWith("#")) {
				continue;
			}			
			if (!line.equals("")) {
				String regex = line.replaceAll("\\*", "[\\\\w']*");			
				spamFilterSet.add(Pattern.compile(regex));
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

}
