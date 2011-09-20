package com.nhn.socialbuzz.twitter.collector;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nhn.socialbuzz.common.CommonUtil;
import com.nhn.socialbuzz.common.JobLogger;
import com.nhn.socialbuzz.me2day.collector.Me2dayDataCollector;
import com.nhn.socialbuzz.me2day.model.TvProgram;
import com.nhn.socialbuzz.me2day.service.TvProgramManager;
import com.nhn.socialbuzz.me2day.service.TvProgramManagerImpl;

public class TwitterCollectorJob implements Job {
	// logger
	private static JobLogger logger = JobLogger.getLogger(Me2dayDataCollector.class, "twitter-collect.log");
	
	public TwitterCollectorJob() { }
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			// get job data map
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();	
			String status = jobDataMap.getString("tv.program.status");
			String nation = jobDataMap.getString("tv.program.nation");
			
			System.out.println("@PARAM[tv.program.status] == " + status);
			System.out.println("@PARAM[tv.program.nation] == " + nation);
			
			logger.info("@PARAM[tv.program.status] == " + status);
			logger.info("@PARAM[tv.program.nation] == " + nation);
			
			// start time
    		Date startTime = new Date();
			
			// This job simply prints out its job name and the
			// date and time that it is running
			String jobName = context.getJobDetail().getFullName();
			System.out.println("Quartz says: " + jobName + " executing at " + startTime);
			logger.info("Quartz says: " + jobName + " executing at " + startTime);
			
			String createStartDate = CommonUtil.convertDateToString("yyyy-MM-dd", CommonUtil.addDay(new Date(), -1));

			TwitterDataCollector collector = new TwitterDataCollector();

			TvProgramManager programManager = new TvProgramManagerImpl();
			TvProgram param = new TvProgram();
			param.setStatus(status);
			param.setNation(nation);
			List<TvProgram> programs = programManager.getPrograms(param);			
			
			for (int i = 0; i <programs.size(); i++) {
				collector.collectTweets(programs.get(i), createStartDate, "");
			}			
	    	
			// end time
			Date endTime = new Date();
			System.out.println("Quartz says: " + jobName + " finished at "	+ endTime);
			System.out.println("Collecting for twitter data from : " + startTime + " to: " + endTime);
			
			logger.info("Quartz says: " + jobName + " finished at "	+ endTime);
			logger.jobSummary("Collecting for twitter data", startTime, endTime);
			
	 	} catch (Exception e) {
	    	e.printStackTrace();
	    } 
	}	
}
