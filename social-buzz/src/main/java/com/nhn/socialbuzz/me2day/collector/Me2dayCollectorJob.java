package com.nhn.socialbuzz.me2day.collector;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nhn.socialbuzz.common.CommonUtil;
import com.nhn.socialbuzz.common.JobLogger;
import com.nhn.socialbuzz.me2day.model.TvProgram;
import com.nhn.socialbuzz.me2day.service.TvProgramManager;
import com.nhn.socialbuzz.me2day.service.TvProgramManagerImpl;
import com.nhn.socialbuzz.twitter.collector.TwitterCollectorJob;

public class Me2dayCollectorJob implements Job {	
	// logger
	private static JobLogger logger = JobLogger.getLogger(Me2dayDataCollector.class, "me2day-collect.log");
	
	public Me2dayCollectorJob() { }
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			// get job data map
			//JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();	        
	        //logger.info("@PARAM[???] == " + something);
			
			// start time
    		Date startTime = new Date();
			
			// This job simply prints out its job name and the
			// date and time that it is running
			String jobName = context.getJobDetail().getFullName();
			System.out.println("Quartz says: " + jobName + " executing at " + startTime);
			logger.info("Quartz says: " + jobName + " executing at " + startTime);
			
			String publishStartDate = CommonUtil.convertDateToString("yyyy.MM.dd", CommonUtil.addDay(new Date(), -1));
			String publishEndDate = CommonUtil.convertDateToString("yyyy.MM.dd", CommonUtil.addDay(new Date(), +1));
			
			Me2dayDataCollector collector = new Me2dayDataCollector();
			
			TvProgramManager programManager = new TvProgramManagerImpl();
			TvProgram param = new TvProgram();
			param.setStatus("open");
			param.setNation("KO");
			List<TvProgram> programs = programManager.getPrograms(param);
			
			for (int i = 0; i <programs.size(); i++) {
				collector.collect(programs.get(i), publishStartDate, publishEndDate);
			}			
	    	
			// end time
			Date endTime = new Date();
			System.out.println("Quartz says: " + jobName + " finished at "	+ endTime);
			System.out.println("Collecting for me2day data from : " + startTime + " ~ to: " + endTime);
			
			logger.info("Quartz says: " + jobName + " finished at "	+ endTime);
			logger.jobSummary("Collecting for me2day data", startTime, endTime);
			
	 	} catch (Exception e) {
	    	e.printStackTrace();
	    } 
	}	
}
