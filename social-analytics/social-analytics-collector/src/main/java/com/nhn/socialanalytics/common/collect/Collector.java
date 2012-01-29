package com.nhn.socialanalytics.common.collect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nhn.socialanalytics.common.util.DateUtil;
import com.nhn.socialanalytics.opinion.dao.SourceDocumentGenerator;
import com.nhn.socialanalytics.opinion.dao.file.SourceDocumentFileWriter;

public abstract class Collector {

	public Set<Pattern> spamFilterSet = new HashSet<Pattern>();
	
	protected CollectHistoryBuffer historyBuffer;
	protected SourceDocumentFileWriter docWriter;
	protected SourceDocumentGenerator docGenerator;
	
	public Collector() { }
	
	public void setCollectHistoryBuffer(CollectHistoryBuffer historyBuffer) {
		this.historyBuffer = historyBuffer;
	}
	
	public void setSourceDocumentWriter(SourceDocumentFileWriter docWriter) {
		this.docWriter = docWriter;
	}
	
	public void setSourceDocumentGenerator(SourceDocumentGenerator docGenerator) {
		this.docGenerator = docGenerator;
	}
	
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
	
	public final static File getSourceDocFile(String dataDir, String entityId, Date collectDate) {
		File dir = new File(dataDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		String strDate = DateUtil.convertDateToString("yyyyMMdd", collectDate);	
		return new File(dataDir + File.separator + entityId + "_" + strDate + ".txt");
	}
	
	public final static File getCollectHistoryFile(String dataDir, String entityId) {
		File dir = new File(dataDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		return new File(dataDir + File.separator + entityId + ".txt");	
	}

}
