package com.nhn.socialbuzz.twitter.collector;

import java.util.Date;
import java.util.Map;

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
        
        String schedule1 = Config.getProperty("twitterCollectorJob_scheule_KO");
		JobDetail job1 = new JobDetail("TwitterCollectorJob_KO", "twitter", TwitterCollectorJob.class);
		
		Map m1 = job1.getJobDataMap();
		m1.put("tv.program.status", "open");
		m1.put("tv.program.nation", "KO");
		
        CronTrigger trigger1 = new CronTrigger("TwitterCollectorJobTrigger1", "twitter", "TwitterCollectorJob_KO",
                "twitter", schedule1);	//second, minute, hour, dayOfMonth, month, year
        sched.addJob(job1, true);
        Date ft1 = sched.scheduleJob(trigger1);
        
        System.out.println(job1.getFullName() + " has been scheduled to run at: " + ft1
                + " and repeat based on expression: "
                + trigger1.getCronExpression());
        
        logger.info(job1.getFullName() + " has been scheduled to run at: " + ft1
                + " and repeat based on expression: "
                + trigger1.getCronExpression());

        ////////////////////////////////////////////////
        String schedule2 = Config.getProperty("twitterCollectorJob_scheule_US");
		JobDetail job2 = new JobDetail("TwitterCollectorJob_US", "twitter", TwitterCollectorJob.class);
		
		Map m2 = job2.getJobDataMap();
		m2.put("tv.program.status", "open");
		m2.put("tv.program.nation", "US");
		
        CronTrigger trigger2 = new CronTrigger("TwitterCollectorJobTrigger2", "twitter", "TwitterCollectorJob_US",
                "twitter", schedule2);	//second, minute, hour, dayOfMonth, month, year
        sched.addJob(job2, true);
        Date ft2 = sched.scheduleJob(trigger2);
        
        System.out.println(job2.getFullName() + " has been scheduled to run at: " + ft2
                + " and repeat based on expression: "
                + trigger2.getCronExpression());
        
       
        
        logger.info(job2.getFullName() + " has been scheduled to run at: " + ft2
                + " and repeat based on expression: "
                + trigger2.getCronExpression());
        
        System.out.println("------- Starting Scheduler ----------------");        
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
