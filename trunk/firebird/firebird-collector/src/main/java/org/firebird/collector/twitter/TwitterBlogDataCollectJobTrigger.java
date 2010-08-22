/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.collector.twitter;

import java.util.Date;

import org.firebird.collector.util.CollectJobLogger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

/**
 * This class is a scheduling trigger for colleting tweet data.
 * 
 * @author Young-Gue Bae
 */
public class TwitterBlogDataCollectJobTrigger {

	/** logger */
	private static CollectJobLogger logger = CollectJobLogger.getLogger(TwitterBlogDataCollectJobTrigger.class);
		
	public void run() throws Exception {

		logger.info("------- Initializing -------------------");

		// first we must get a reference to a scheduler
		SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

		logger.info("------- Initialization Complete --------");

		logger.info("------- Scheduling Jobs ----------------");
		
		// get a "nice round" time a few seconds in the future...
        long ts = TriggerUtils.getNextGivenSecondDate(null, 15).getTime();

		// jobs can be scheduled before sched.start() has been called
		// job will run every 30 minutes
        /*
		JobDetail job = new JobDetail("TwitterBlogDataCollectJob", "firebird", TwitterBlogDataCollectJob.class);
        CronTrigger trigger = new CronTrigger("TwitterBlogDataCollectJobTrigger", "firebird", "TwitterBlogDataCollectJob",
                "twitter", "0 0/30 * * * ?");
        sched.addJob(job, true);
        Date ft = sched.scheduleJob(trigger);
        logger.info(job.getFullName() + " has been scheduled to run at: " + ft
                + " and repeat based on expression: "
                + trigger.getCronExpression());
        */
		
        // jobs can be scheduled before sched.start() has been called
        // job7 will repeat 20 times, repeat every 10 minutes(1 second = 1000L)
        /*
        JobDetail job = new JobDetail("TwitterBlogDataCollectJob", "firebird", TwitterBlogDataCollectJob.class);
        SimpleTrigger trigger = new SimpleTrigger("TwitterBlogDataCollectJobTrigger", "firebird", "TwitterBlogDataCollectJob", "firebird",
                new Date(ts), null, 1, 3660000L);
        Date ft = sched.scheduleJob(job, trigger);
        logger.info(job.getFullName() +
                " will run at: " + ft +  
                " and repeat: " + trigger.getRepeatCount() + 
                " times, every " + trigger.getRepeatInterval() / 1000 + " seconds");
		*/
        
        int startPage = 61;
        int endPage = 70;
        
        JobDetail job = null;
        JobDataMap jobDataMap = null;
        long dt;
        SimpleTrigger trigger = null;
        Date ft = null;
                
        // job 1        
        job = new JobDetail("TwitterBlogDataCollectJob1", "firebird", TwitterBlogDataCollectJob.class);
        jobDataMap = new JobDataMap();
        jobDataMap.put("startPage", startPage);
        jobDataMap.put("endPage", endPage);
        jobDataMap.put("userStartIndex", 0);
        jobDataMap.put("userEndIndex", 30);
        job.setJobDataMap(jobDataMap);
        
        dt = TriggerUtils.getDateOf(0, 50, 21, 21, 8, 2010).getTime();	//second, minute, hour, dayOfMonth, month, year

        trigger = new SimpleTrigger("TwitterBlogDataCollectJobTrigger1", "firebird", new Date(dt));
        ft = sched.scheduleJob(job, trigger);
        logger.info(job.getFullName() +
                " will run at: " + ft +  
                " and repeat: " + trigger.getRepeatCount() + 
                " times, every " + trigger.getRepeatInterval() / 1000 + " seconds");
        
        // job 2
        job = new JobDetail("TwitterBlogDataCollectJob2", "firebird", TwitterBlogDataCollectJob.class);
        jobDataMap = new JobDataMap();
        jobDataMap.put("startPage", startPage);
        jobDataMap.put("endPage", endPage);
        jobDataMap.put("userStartIndex", 30);
        jobDataMap.put("userEndIndex", 60);
        job.setJobDataMap(jobDataMap);
        
        dt = TriggerUtils.getDateOf(0, 0, 23, 21, 8, 2010).getTime();	//second, minute, hour, dayOfMonth, month, year
        
        trigger = new SimpleTrigger("TwitterBlogDataCollectJobTrigger2", "firebird", new Date(dt));
        ft = sched.scheduleJob(job, trigger);
        logger.info(job.getFullName() +
                " will run at: " + ft +  
                " and repeat: " + trigger.getRepeatCount() + 
                " times, every " + trigger.getRepeatInterval() / 1000 + " seconds");
        
        
        // job 3
        job = new JobDetail("TwitterBlogDataCollectJob3", "firebird", TwitterBlogDataCollectJob.class);
        jobDataMap = new JobDataMap();
        jobDataMap.put("startPage", startPage);
        jobDataMap.put("endPage", endPage);
        jobDataMap.put("userStartIndex", 60);
        jobDataMap.put("userEndIndex", 90);
        job.setJobDataMap(jobDataMap);
        
        dt = TriggerUtils.getDateOf(0, 10, 0, 22, 8, 2010).getTime();	//second, minute, hour, dayOfMonth, month, year
        
        trigger = new SimpleTrigger("TwitterBlogDataCollectJobTrigger3", "firebird", new Date(dt));
        ft = sched.scheduleJob(job, trigger);
        logger.info(job.getFullName() +
                " will run at: " + ft +  
                " and repeat: " + trigger.getRepeatCount() + 
                " times, every " + trigger.getRepeatInterval() / 1000 + " seconds");
        
        
        // job 4
        job = new JobDetail("TwitterBlogDataCollectJob4", "firebird", TwitterBlogDataCollectJob.class);
        jobDataMap = new JobDataMap();
        jobDataMap.put("startPage", startPage);
        jobDataMap.put("endPage", endPage);
        jobDataMap.put("userStartIndex", 90);
        jobDataMap.put("userEndIndex", 120);
        job.setJobDataMap(jobDataMap);
        
        dt = TriggerUtils.getDateOf(0, 20, 1, 22, 8, 2010).getTime();	//second, minute, hour, dayOfMonth, month, year
        
        trigger = new SimpleTrigger("TwitterBlogDataCollectJobTrigger4", "firebird", new Date(dt));
        ft = sched.scheduleJob(job, trigger);
        logger.info(job.getFullName() +
                " will run at: " + ft +  
                " and repeat: " + trigger.getRepeatCount() + 
                " times, every " + trigger.getRepeatInterval() / 1000 + " seconds");
        
        logger.info("------- Starting Scheduler ----------------");
        
        
        // All of the jobs have been added to the scheduler, but none of the
        // jobs
        // will run until the scheduler has been started
        sched.start();

        logger.info("------- Started Scheduler -----------------");	
	}

	public static void main(String[] args) throws Exception {
		TwitterBlogDataCollectJobTrigger trigger = new TwitterBlogDataCollectJobTrigger();
		trigger.run();
	}

}
