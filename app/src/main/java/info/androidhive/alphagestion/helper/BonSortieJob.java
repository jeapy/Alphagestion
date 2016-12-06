package info.androidhive.alphagestion.helper;

import android.util.Log;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import java.util.Date;

/**
 * Created by Jean Philippe on 31/07/2016.
 */
public class BonSortieJob implements Job {


    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        JobKey key = jobDetail.getKey();
        Log.i("Z","For Job key:" + key + ", Current Date:" + new Date());
    }
}
