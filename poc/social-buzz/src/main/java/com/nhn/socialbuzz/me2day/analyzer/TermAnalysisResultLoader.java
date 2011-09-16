package com.nhn.socialbuzz.me2day.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import com.nhn.socialbuzz.common.Config;
import com.nhn.socialbuzz.me2day.model.Term;
import com.nhn.socialbuzz.me2day.service.TermManager;
import com.nhn.socialbuzz.me2day.service.TermManagerImpl;

public class TermAnalysisResultLoader {
	
	private String dataDir;
	
	private TermManager termManager;

	/**
	 * Creates a term loader.
	 * 
	 * @param dataDir
	 */
	public TermAnalysisResultLoader(String dataDir) {
		this.dataDir = dataDir;
		termManager = new TermManagerImpl();
	}
	
	public void execute(String startDate, String endDate) {
		try {			
			String subDir = startDate + "-" +  endDate;
			File f = new File(dataDir + File.separator + subDir);
			
			// tf
			File[] tfFiles = f.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.startsWith("tf_"); 
				}
			});	
			
			for (int i = 0; i < tfFiles.length; i++) {
				File tfFile = tfFiles[i];
				String fileName = tfFile.getName();
				String programId = fileName.replaceAll("tf_", "").replaceAll(".csv", "");				
				String type = "TERM";
				
				System.out.println("....loading to database for program id == " + programId);
				loadToDatabase(tfFile, programId, startDate, endDate, type);
			}
			
			// stf
			File[] stfFiles = f.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.startsWith("stf_"); 
				}
			});	
			
			for (int i = 0; i < stfFiles.length; i++) {
				File stfFile = stfFiles[i];
				String fileName = stfFile.getName();
				String programId = fileName.replaceAll("stf_", "").replaceAll(".csv", "");				
				String type = "SENTIMENT";
				
				System.out.println("....loading to database for program id == " + programId);
				loadToDatabase(stfFile, programId, startDate, endDate, type);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private void loadToDatabase(File file, String programId, 
			String startDate, String endDate, String type) throws Exception {
		
		InputStream is = new FileInputStream(file);
		BufferedReader in = new BufferedReader(new InputStreamReader(is, "EUC-KR")); 
		
		Term cond = new Term();
		cond.setProgramId(programId);
		cond.setStartDate(startDate);
		cond.setEndDate(endDate);
		cond.setType(type);
		
		termManager.deleteTerms(cond);
		
		int row = 0;
		String line;
		while ((line = in.readLine()) != null) {

			if (line.startsWith("#")) {
				continue;
			}
			
			if (row > 0) {
				String[] tokens = Pattern.compile(",").split(line);
				
				String strTerm = tokens[0];
				strTerm = strTerm.replaceAll("\"", "");
				double tf = Double.parseDouble(tokens[1]);
				
				Term term = new Term();
				term.setProgramId(programId);
				term.setStartDate(startDate);
				term.setEndDate(endDate);
				term.setType(type);
				term.setTerm(strTerm);
				term.setTf(tf);	
				
				System.out.println("program == " + programId + ", type == " + type + ", term == " + strTerm + ", tf == " + tf);
				
				termManager.addTerm(term);
			}
			
			row++;
		}
		is.close();
	}

	public static void main(String[] args) {
		String dataDir = Config.getProperty("dataDir");
		TermAnalysisResultLoader loader = new TermAnalysisResultLoader(dataDir);
		
		try {
			String[] date1 = { "20110815", "20110821" };
			String[] date2 = { "20110822", "20110828" };
						
//			loader.execute(date1[0], date1[1]);
//			loader.execute(date2[0], date2[1]);		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
