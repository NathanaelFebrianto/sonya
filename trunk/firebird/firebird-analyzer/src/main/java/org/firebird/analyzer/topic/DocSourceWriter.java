/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.analyzer.topic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.firebird.analyzer.util.JobLogger;
import org.firebird.common.util.ConvertUtil;
import org.firebird.io.model.UserBlogEntry;

/**
 * This class extracts documents from the source data.
 * 
 * @author Young-Gue Bae
 */
public class DocSourceWriter {
	/** logger */
	private static JobLogger logger = JobLogger.getLogger(DocSourceWriter.class);

	private File fileDir;	
	
	/**
	 * Constructor.
	 * 
	 * @param dir the file directory
	 */
	public DocSourceWriter(String dir) {
		this.fileDir = new File(dir);

		if (fileDir.exists())	fileDir.delete();
		fileDir.mkdir();
	}

	/**
	 * Writes the user's blog entries to a file.
	 * 
	 * @param userId the user id
	 * @param blogEntris the list of user blog entry
	 */
	public void write(String userId, List<UserBlogEntry> blogEntries) {        
		try {
			StringBuffer outBuffer = new StringBuffer(1024);			
			outBuffer.append(userId).append("\n\n");
			
			Calendar cal = Calendar.getInstance();
			cal.set(1900, 1, 1);
			Date lastDate = cal.getTime();
			
			for (int i = 0; i < blogEntries.size(); i++) {
				UserBlogEntry blogEntry = (UserBlogEntry) blogEntries.get(i);
				Date createDate = blogEntry.getCreateDate();
				if (lastDate.before(createDate)) {
					lastDate = createDate;
				}
					
				String body = blogEntry.getBody();				
				String content = extractContent(body);				
				outBuffer.append(content).append("\n");
			}
	        
	        String out = outBuffer.toString();
	        String timeline = ConvertUtil.convertDateToString("yyyyMMdd", lastDate);

	        File outFile = new File(fileDir, "doc-" + userId + ".txt");
	        logger.info("writing to file = " + outFile);
	        
	        FileWriter writer = new FileWriter(outFile);
	        writer.write(timeline + "\n");
	        writer.write(out);
	        writer.close();
	        outBuffer.setLength(0);	 		
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}	
	
	private String extractContent(String body) {
		Pattern EXTRACTION_PATTERN = Pattern.compile("@(.*?)\\s|@(.*?)\\Z|RT\\s@(.*?)\\s|http://(.*?)\\s|http://(.*?)\\Z");
		
		StringBuffer buffer = new StringBuffer(body);
		String out = body;
		
		Matcher matcher = EXTRACTION_PATTERN.matcher(buffer);
		
		logger.info("source == " + body);
		
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                if (matcher.group(i) != null) {
                	logger.info(i + " == " + matcher.group(i));
                	if (i == 1)
                		try { out = out.replaceAll("@"+matcher.group(i)+"\\s", ""); } catch (Exception e) { e.printStackTrace(); }
                		
                	if (i == 2)
                    	try { out = out.replaceAll("@"+matcher.group(i)+"\\Z", ""); } catch (Exception e) { e.printStackTrace(); }
                		
                	if (i == 3)
                		try { out = out.replaceAll("RT @"+matcher.group(i)+"\\s", ""); } catch (Exception e) { e.printStackTrace(); }

                	if (i == 4)
                		try { out = out.replaceAll("http://"+matcher.group(i)+"\\s", ""); } catch (Exception e) { e.printStackTrace(); }
                		
                	if (i == 5)
                		try { out = out.replaceAll("http://"+matcher.group(i)+"\\Z", ""); } catch (Exception e) { e.printStackTrace(); }
                }
            }            
        }
        logger.info("output == " + out);
        
        return out;
	}

}
