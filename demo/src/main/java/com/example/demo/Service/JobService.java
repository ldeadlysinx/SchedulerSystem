package com.example.demo.Service;

import com.example.demo.Job.BackupDatabase;
import com.example.demo.Job.JobScheduler;
import com.example.demo.Job.SampleJob;
import com.example.demo.Job.SendMail;
import com.example.demo.Repository.JobInfoRepository;
import com.example.demo.entity.JobInfo;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobInfoRepository jobInfoRepository;

    private final JobScheduler jobScheduler;

    public JobInfo createJob(JobInfo jobInfo) throws SchedulerException {
        jobInfoRepository.save(jobInfo);
        switch (jobInfo.getJobType()) {
            case "BackupDatabase":
                jobScheduler.scheduleJob(
                        jobInfo.getJobName(),
                        jobInfo.getJobGroup(),
                        jobInfo.getCronExpression(),
                        BackupDatabase.class);
                break;
            case "SampleJob":
                jobScheduler.scheduleJob(
                        jobInfo.getJobName(),
                        jobInfo.getJobGroup(),
                        jobInfo.getCronExpression(),
                        SampleJob.class);
                break;
            case "SendMail":
                jobScheduler.scheduleJob(
                        jobInfo.getJobName(),
                        jobInfo.getJobGroup(),
                        jobInfo.getCronExpression(),
                        SendMail.class);
                break;
            default:
                throw new IllegalArgumentException("Invalid job type: " + jobInfo.getJobType());
        }
        return jobInfo;
    }

    public void deleteJob(Long jobId) throws SchedulerException {
        JobInfo jobInfo = jobInfoRepository.findById(jobId).orElseThrow();
        jobScheduler.deleteJob(jobInfo.getJobName(), jobInfo.getJobGroup());
        System.out.println(jobInfo.getJobName()+"중지");
    }

    public List<JobInfo> getAllJobs() {
        return jobInfoRepository.findAll();
    }

    public void pauseJob(Long jobId) throws SchedulerException {
        JobInfo jobInfo = jobInfoRepository.findById(jobId).orElseThrow();
        jobScheduler.pauseJob(jobInfo.getJobName(), jobInfo.getJobGroup());
        System.out.println(jobInfo.getJobName()+"paused(일시중지)");
    }

    public void resumeJob(Long jobId) throws SchedulerException {
        JobInfo jobInfo = jobInfoRepository.findById(jobId).orElseThrow();
        jobScheduler.resumeJob(jobInfo.getJobName(), jobInfo.getJobGroup());
        System.out.println(jobInfo.getJobName()+"resume(재가동)");
    }

    public boolean updateScheduleJob(JobInfo jobInfo) throws SchedulerException{
        System.out.println(jobInfo.getJobName()+"스케줄 변경");
        return jobScheduler.updateJob(jobInfo);

    }

    public String getScheduleState(Long jobId) throws SchedulerException{
        JobInfo jobInfo = jobInfoRepository.findById(jobId).orElseThrow();
        return jobScheduler.getJobState(jobInfo.getJobName(),jobInfo.getJobGroup());
    }

    public boolean isJobRunning(Long jobId) throws SchedulerException {
        JobInfo jobInfo = jobInfoRepository.findById(jobId).orElseThrow();
        return jobScheduler.isJobRunning(jobInfo.getJobName(), jobInfo.getJobGroup());
    }

    public boolean isJobExists(JobInfo jobInfo) throws SchedulerException {
        return jobScheduler.isJobExists(jobInfo.getJobName(), jobInfo.getJobGroup());
    }

    public boolean immediatelyJob(Long jobId) throws SchedulerException{
        JobInfo jobInfo = jobInfoRepository.findById(jobId).orElseThrow();
        switch (jobInfo.getJobType()) {
            case "BackupDatabase":
                return jobScheduler.triggerJobNow(
                        jobInfo.getJobName(),
                        jobInfo.getJobGroup(),
                        BackupDatabase.class);
            case "SampleJob":
                return jobScheduler.triggerJobNow(
                        jobInfo.getJobName(),
                        jobInfo.getJobGroup(),
                        SampleJob.class);
            default:
                throw new IllegalArgumentException("Invalid job type: " + jobInfo.getJobType());
        }
    }


}
