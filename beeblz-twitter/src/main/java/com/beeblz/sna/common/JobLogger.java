/*
 * Copyright (c) 2011, Young-Gue Bae
 * All rights reserved.
 */
package com.beeblz.sna.common;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * This class is a job logger.
 * 
 * @author Young-Gue Bae
 */
public class JobLogger {
	
	static JobLogger instance = null;
	static Logger logger = null;
	String layout = "[%d{HH:mm:ss}]	%m%n";
	String logFilename = "D:/beeblz/log/log";
	String datePattern = ".yyyy-MM-dd";
	PatternLayout patternLayout = new PatternLayout(layout);
	DailyRollingFileAppender appender = null;

	/**
	 * Constructor
	 * 
	 * @param name the logger name
	 */
	public JobLogger(Class<?> name) {
		try {
			logger = Logger.getLogger(name);
			appender = new DailyRollingFileAppender(patternLayout, logFilename, datePattern);
			logger.addAppender(appender);
			logger.setLevel(Level.ALL);
		} catch (IOException ie) {
			ie.printStackTrace();
		}		
	}
	
	/**
	 * Gets the job logger instance.
	 * 
	 * @param name the logger name
	 * @return JobLogger the job logger instance
	 */
	public static JobLogger getLogger(Class<?> name) {
		if (instance == null) {
			instance = new JobLogger(name);
		}		
		return instance;
	}

	/**
	 * Writes the info message.
	 * 
	 * @param message the message
	 */
	public void info(String message) {
		logger.info(message);
	}
	
	/**
	 * Writes the error message.
	 * 
	 * @param message the message
	 * @param t the exception
	 */
	public void error(String message, Throwable t) {
		logger.error(message, t);
	}

	/**
	 * Writes the job summary log.
	 * 
	 * @param message the message
	 * @param startTime the startTime
	 * @param endTime the endTime	 * 
	 */
	public void jobSummary(String message, Date startTime, Date endTime) {
		long time = endTime.getTime() - startTime.getTime();		
		String spendTime = (int)Math.floor(time/1000)/60 + "min" + " " + (int)Math.floor(time/1000) %60 +"sec";
		
		logger.info("******************************************");
		logger.info(message);
		logger.info("Start Time : " + startTime);
		logger.info("End Time : " + endTime);
		logger.info("Spend Time : " + spendTime);
		logger.info("******************************************");		
	}
	
}
