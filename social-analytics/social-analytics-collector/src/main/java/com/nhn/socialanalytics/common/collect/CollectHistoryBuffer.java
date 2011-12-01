package com.nhn.socialanalytics.common.collect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class CollectHistoryBuffer {
	
	BufferedWriter br;
	int maxRound = 1;
	int currentRound = 1;
	Set<String> historySet = new HashSet<String>();
	Map<String, Integer> historyMap = new HashMap<String, Integer>();
	private static final String DELIMITER = "\t";
	
	public CollectHistoryBuffer(File file, int maxRound) {
		this.maxRound = maxRound;
		
		try {
			this.loadCollectHistory(file);
			this.br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private void loadCollectHistory(File file) throws IOException {
		historySet = new HashSet<String>();
		historyMap = new HashMap<String, Integer>();
		
		try {			
			InputStream is = new FileInputStream(file);
			BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8")); 
			
			int latestRound = 0;
			String line;
			while ((line = in.readLine()) != null) {
				String[] tokens = Pattern.compile(DELIMITER).split(line);	
				String id = tokens[0];
				int round = Integer.valueOf(tokens[1]);				
				
				historySet.add(id);
				historyMap.put(id, new Integer(round));
				
				if (round > latestRound)
					latestRound = round;
			}
			is.close();				
			currentRound = latestRound + 1;
			System.out.println("current round == " + currentRound);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void close() throws IOException {
		br.close();
	}
	
	public boolean checkDuplicate(String newId) {
		return historySet.contains(newId);
	}
	
	public void writeCollectHistory(Set<String> idSet) throws IOException {			
		this.rewritePrevHistory(historyMap);
		
		int round = 1;
		if(currentRound <= maxRound)
			round = currentRound;
		else
			round = currentRound - 1;
		
		for (Iterator<String> it = idSet.iterator(); it.hasNext();) {
			br.write(it.next() + DELIMITER + round);
			br.newLine();
		}
		
		br.close();		
	}
	
	private void rewritePrevHistory(Map<String, Integer> map) throws IOException {		
		if(currentRound <= maxRound) {
		     for (Map.Entry<String, Integer> entry : map.entrySet()) {
		          String id = entry.getKey();
		          int round = entry.getValue();
		          br.write(id + DELIMITER + round);
		          br.newLine();
		     } 
		} else {
		     for (Map.Entry<String, Integer> entry : map.entrySet()) {
		    	 String id = entry.getKey();
		          int round = entry.getValue();
		          if (round > 1) {
		        	  br.write(id + DELIMITER + (round-1));
		        	  br.newLine();
		          }		          
		     } 
		}
	}
	
}
