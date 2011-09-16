package com.nhn.socialbuzz.twitter.collector;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.nhn.socialbuzz.common.CommonUtil;
import com.nhn.socialbuzz.me2day.model.TvProgram;
import com.nhn.socialbuzz.me2day.service.TvProgramManager;
import com.nhn.socialbuzz.me2day.service.TvProgramManagerImpl;

public class TwitterCollectorJob implements Job {
	
	public TwitterCollectorJob() { }
	
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
			
			String createStartDate = CommonUtil.convertDateToString("yyyy-MM-dd", CommonUtil.addDay(new Date(), -1));

			TwitterDataCollector collector = new TwitterDataCollector();

			TvProgramManager programManager = new TvProgramManagerImpl();
			TvProgram param = new TvProgram();
			param.setStatus("open");
			List<TvProgram> programs = programManager.getPrograms(param);			
			
			for (int i = 0; i <programs.size(); i++) {
				collector.collectTweets(programs.get(i), createStartDate, "");
			}			
	    	
			// end time
			Date endTime = new Date();
			System.out.println("Quartz says: " + jobName + " finished at "	+ endTime);
			System.out.println("Collecting for tweet data from : " + startTime + " to: " + endTime);
			
	 	} catch (Exception e) {
	    	e.printStackTrace();
	    } 
	}	
}
