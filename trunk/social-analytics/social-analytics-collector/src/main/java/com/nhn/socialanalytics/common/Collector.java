package com.nhn.socialanalytics.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import com.nhn.socialanalytics.common.util.DateUtil;

public abstract class Collector {

	protected static final String DELIMITER = "\t";
	
	protected Set<String> loadPrevCollectedHashSet(String dataDir, String objectId) throws IOException, UnsupportedEncodingException {
		Set<String> idSet = new HashSet<String>();
		
		try {			
			File prevColIdSetFile = this.getIDSetFile(dataDir, objectId);
			
			InputStream is = new FileInputStream(prevColIdSetFile);
			BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8")); 
			
			String line;
			while ((line = in.readLine()) != null) {
				String[] tokens = Pattern.compile(DELIMITER).split(line);				
				String tweetId = tokens[0];
				idSet.add(tweetId);
			}
			is.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			return idSet;
		}
		
		return idSet;
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
	
	protected File getIDSetFile(String dataDir, String objectId) {
		return new File(dataDir + File.separator + objectId + ".txt");	
	}

}
