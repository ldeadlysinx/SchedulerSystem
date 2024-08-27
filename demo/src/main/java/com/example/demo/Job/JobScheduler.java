package com.example.demo.Job;

import com.example.demo.entity.JobInfo;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JobScheduler {

    private final Scheduler scheduler;

    public void scheduleJob(String jobName, String jobGroup, String cronExpression, Class<? extends Job> jobClass) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, jobGroup)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName + "Trigger", jobGroup)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void deleteJob(String jobName, String jobGroup) throws SchedulerException {
        scheduler.deleteJob(new JobKey(jobName, jobGroup));
    }

    public void pauseJob(String jobName, String jobGroup) throws SchedulerException {
        scheduler.pauseJob(new JobKey(jobName, jobGroup));
    }

    public void resumeJob(String jobName, String jobGroup) throws SchedulerException{
        scheduler.resumeJob(new JobKey(jobName, jobGroup));

    }

    public boolean updateJob(JobInfo jobInfo) throws SchedulerException{
        JobKey jobKey = JobKey.jobKey(jobInfo.getJobName(), jobInfo.getJobGroup());
        TriggerKey triggerKey = TriggerKey.triggerKey(jobInfo.getJobName() + "Trigger", jobInfo.getJobGroup());

        Trigger newTrigger = TriggerBuilder.newTrigger()
                .withIdentity(jobInfo.getJobName() + "Trigger", jobInfo.getJobGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(jobInfo.getCronExpression()))
                .build();

        Date rescheduleDate = scheduler.rescheduleJob(triggerKey,newTrigger);

        return rescheduleDate!=null;
    }

    public String getJobState(String jobName, String jobGroup) throws SchedulerException{
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);

        JobDetail jobDetail = scheduler.getJobDetail(jobKey);

        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobDetail.getKey());

        if (triggers.isEmpty()) {
            return "NO_TRIGGERS";
        }

        for (Trigger trigger : triggers) {
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            switch (triggerState) {
                case NORMAL:
                    return "SCHEDULED";
                case PAUSED:
                    return "PAUSED";
                case COMPLETE:
                    return "COMPLETE";
                case ERROR:
                    return "ERROR";
                case BLOCKED:
                    return "BLOCKED";
                default:
                    return "UNKNOWN";
            }
        }

        return "UNKNOWN";
    }

    public boolean isJobRunning(String jobName, String jobGroup) throws SchedulerException{
        List<JobExecutionContext> currentlyExecutingJobs = scheduler.getCurrentlyExecutingJobs(); // 현재 실행 중인 모든 작업을 가져옵니다.

        for (JobExecutionContext jobCtx : currentlyExecutingJobs) {
            JobKey jobKey = jobCtx.getJobDetail().getKey();
            // 주어진 jobName과 jobGroup과 일치하는 작업이 현재 실행 중인지 확인합니다.
            if (jobKey.getName().equals(jobName) && jobKey.getGroup().equals(jobGroup)) {
                return true; // 일치하는 작업이 실행 중이면 true를 반환합니다.
            }
        }

        return false;
    }

    public boolean triggerJobNow(String jobName, String jobGroup, Class<? extends Job> jobClass) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);

            // JobDataMap을 사용하여 추가 데이터를 전달할 수 있습니다.
            JobDataMap jobDataMap = new JobDataMap();

            scheduler.triggerJob(jobKey, jobDataMap);

            return true;
        }catch (SchedulerException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean isJobExists(String jobName, String jobGroup) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            return scheduler.checkExists(jobKey); // jobKey가 존재하는지 확인
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false; // 예외 발생 시 false 반환
        }
    }
}
