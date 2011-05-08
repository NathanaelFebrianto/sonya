package com.beeblz.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	
	private static Properties props = new Properties();
	
	private static Config instance = null; 
	
	public Config() {
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("config.properties");
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
