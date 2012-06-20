package org.louie.common.util;

import java.io.File;

/**
 * This class is file utilities.
 * 
 * @author Younggue Bae
 */
public class FileUtils {
	
	/**
	 * Extracts directory path from full filename path.
	 * 
	 * @param filename
	 * @return
	 */
	public static final String extractDirectory(String filename) {
		if (StringUtils.isEmpty(filename))
			return null;
		
		int end = filename.lastIndexOf(File.separator);
		if (end >= 0)
			return filename.substring(0, end);
		else
			return null;
	}
	
	/**
	 * Makes directory from full filename path.
	 * 
	 * @param filename
	 */
	public static final void mkdirs(String filename) {
		String strDir = extractDirectory(filename);
		if (!StringUtils.isEmpty(strDir)) {
			File dir = new File(strDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
	}

}
