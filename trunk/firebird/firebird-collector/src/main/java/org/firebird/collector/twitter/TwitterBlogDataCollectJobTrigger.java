/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.collector.twitter;

import java.util.Date;

import org.firebird.collector.util.CollectJobLogger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
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

		// jobs can be scheduled before sched.start() has been called
		// job will run every 30 minutes
        JobDetail job = new JobDetail("job1", "group1", TwitterBlogDataCollectJob.class);
        CronTrigger trigger = new CronTrigger("trigger1", "group1", "job1",
                "group1", "0 0/30 * * * ?");
        sched.addJob(job, true);
        Date ft = sched.scheduleJob(trigger);
        logger.info(job.getFullName() + " has been scheduled to run at: " + ft
                + " and repeat based on expression: "
                + trigger.getCronExpression());

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
