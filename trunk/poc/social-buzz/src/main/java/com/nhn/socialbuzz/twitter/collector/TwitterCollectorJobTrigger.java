package com.nhn.socialbuzz.twitter.collector;

import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class TwitterCollectorJobTrigger {
	
	public void run() throws Exception {

		System.out.println("------- Initializing -------------------");

		// first we must get a reference to a scheduler
		SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

        System.out.println("------- Initialization Complete --------");

        System.out.println("------- Scheduling Jobs ----------------");
		
		// get a "nice round" time a few seconds in the future...
        //long ts = TriggerUtils.getNextGivenSecondDate(null, 15).getTime();

		// jobs can be scheduled before sched.start() has been called
		// job will run every 60 minutes
		JobDetail job = new JobDetail("TwitterCollectorJob", "twitter", TwitterCollectorJob.class);
        CronTrigger trigger = new CronTrigger("TwitterCollectorJobTrigger", "twitter", "TwitterCollectorJob",
                "twitter", "0 00/60 * * * ?");	//second, minute, hour, dayOfMonth, month, year
        sched.addJob(job, true);
        Date ft = sched.scheduleJob(trigger);
        System.out.println(job.getFullName() + " has been scheduled to run at: " + ft
                + " and repeat based on expression: "
                + trigger.getCronExpression());
        
        System.out.println("------- Starting Scheduler ----------------");
        
        
        // All of the jobs have been added to the scheduler, but none of the
        // jobs
        // will run until the scheduler has been started
        sched.start();

        System.out.println("------- Started Scheduler -----------------");	
	}

	public static void main(String[] args) throws Exception {
		TwitterCollectorJobTrigger trigger = new TwitterCollectorJobTrigger();
		trigger.run();
	}	
}