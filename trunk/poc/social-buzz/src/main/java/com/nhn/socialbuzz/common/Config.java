package com.nhn.socialbuzz.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	
	/**
	 * Configuration file.
	 */
	public static final File DEFAULT_CONFIG_FILE = new File("config.properties");
	
	private static Properties props = new Properties();	
	private static Config instance = null; 
	
	
	
	public Config() {
		try {
			InputStream is = null;
			
			if (DEFAULT_CONFIG_FILE.exists()) {
				is = new FileInputStream(DEFAULT_CONFIG_FILE);
			}
			else {
				is = this.getClass().getClassLoader().getResourceAsStream("config.properties");
			}
			props.load(is);	
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}		
		return instance;
	} 
	
	public static final String getProperty(String key) {
		Config.getInstance();
		return props.getProperty(key);
	}

}
