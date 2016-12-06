package info.androidhive.alphagestion.helper;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;

import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import info.androidhive.alphagestion.activity.MainActivity;

/**
 * Created by Jean Philippe on 31/07/2016.
 */
public class BonSortieCron {

      public static void main(String[] args) throws SchedulerException, InterruptedException {

          SchedulerFactory schfa = new StdSchedulerFactory();
          Scheduler sch = schfa.getScheduler();

        JobDetail jobdetail = JobBuilder.newJob(BonSortieJob.class)
                .withIdentity("cronjob1", "crongroup1")
                .build();
        //Executes after every minute 0 0/1 * 1/1 * ? *
       CronTrigger crontrigger = TriggerBuilder
                .newTrigger()
                .withIdentity("trigger1", "crongroup1")
                .withSchedule( CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *"))
                .build();
        sch.scheduleJob(jobdetail, crontrigger);


        jobdetail = JobBuilder.newJob(BonSortieJob.class)
                .withIdentity("cronjob2", "crongroup1")
                .build();
        //Executes after every 2 minute 0 0/2 * 1/1 * ? *
        crontrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("trigger2", "crongroup1")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0/2 * 1/1 * ? *"))
                    .build();

        sch.start();
          sch.scheduleJob(jobdetail, crontrigger);


        Thread.sleep(100L * 1000L);
        sch.shutdown(true);
    }

}
