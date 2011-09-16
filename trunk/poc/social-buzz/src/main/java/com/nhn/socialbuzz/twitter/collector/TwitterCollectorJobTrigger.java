package com.nhn.socialbuzz.twitter.collector;

import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.nhn.socialbuzz.common.Config;
import com.nhn.socialbuzz.common.JobLogger;
import com.nhn.socialbuzz.me2day.collector.Me2dayDataCollector;

public class TwitterCollectorJobTrigger {
	// logger
	private static JobLogger logger = JobLogger.getLogger(Me2dayDataCollector.class, "twitter-collect.log");
	
	public void run() throws Exception {

		System.out.println("------- Initializing -------------------");
		logger.info("------- Initializing -------------------");
		
		// first we must get a reference to a scheduler
		SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        System.out.println("------- Initialization Complete --------");
        System.out.println("------- Scheduling Jobs ----------------");
        
        logger.info("------- Initialization Complete --------");
        logger.info("------- Scheduling Jobs ----------------");
		
		// get a "nice round" time a few seconds in the future...
        //long ts = TriggerUtils.getNextGivenSecondDate(null, 15).getTime();

		// jobs can be scheduled before sched.start() has been called
		// job will run every 60 minutes
        
        String schedule = Config.getProperty("twitterCollectorJob_scheule");
		JobDetail job = new JobDetail("TwitterCollectorJob", "twitter", TwitterCollectorJob.class);
        CronTrigger trigger = new CronTrigger("TwitterCollectorJobTrigger", "twitter", "TwitterCollectorJob",
                "twitter", schedule);	//second, minute, hour, dayOfMonth, month, year
        sched.addJob(job, true);
        Date ft = sched.scheduleJob(trigger);
        
        System.out.println(job.getFullName() + " has been scheduled to run at: " + ft
                + " and repeat based on expression: "
                + trigger.getCronExpression());
        
        System.out.println("------- Starting Scheduler ----------------");        
        
        logger.info(job.getFullName() + " has been scheduled to run at: " + ft
                + " and repeat based on expression: "
                + trigger.getCronExpression());
        
        logger.info("------- Starting Scheduler ----------------");
        
        // All of the jobs have been added to the scheduler, but none of the
        // jobs
        // will run until the scheduler has been started
        sched.start();

        System.out.println("------- Started Scheduler -----------------");	
        logger.info("------- Startged Scheduler -----------------");	
	}

	public static void main(String[] args) throws Exception {
		TwitterCollectorJobTrigger trigger = new TwitterCollectorJobTrigger();
		trigger.run();
	}	
}
