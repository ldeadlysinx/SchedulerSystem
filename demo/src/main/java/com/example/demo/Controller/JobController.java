package com.example.demo.Controller;

import com.example.demo.Service.JobService;
import com.example.demo.entity.JobInfo;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    // 작업 생성
    @PostMapping
    public ResponseEntity<JobInfo> createJob(@RequestBody JobInfo jobInfo) throws SchedulerException {
        JobInfo createdJob = jobService.createJob(jobInfo);
        return ResponseEntity.ok(createdJob);
    }

    @GetMapping
    public ResponseEntity<List<JobInfo>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    //작업 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) throws SchedulerException {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    // 작업 중지
    @PostMapping("/pause/{id}")
    public ResponseEntity<Void> pauseJob(@PathVariable Long id) throws SchedulerException {
        jobService.pauseJob(id);
        return ResponseEntity.noContent().build();
    }

    // 작업 실행
    @PostMapping("/resume/{id}")
    public ResponseEntity<Void> resumeJob(@PathVariable Long id) throws SchedulerException {
        jobService.resumeJob(id);
        return ResponseEntity.noContent().build();
    }

    // 작업 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<JobInfo> updateJob(@PathVariable Long id, @RequestBody JobInfo jobInfo) throws SchedulerException{
        jobInfo.setId(id);
        boolean updated = jobService.updateScheduleJob(jobInfo);
        if (updated) {
            return ResponseEntity.ok(jobInfo);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 작업 즉시 실행
    @PostMapping("/{id}/trigger")
    public ResponseEntity<Void> triggerJob(@PathVariable Long id) throws SchedulerException{
         // JobInfo 조회 로직
        boolean triggered = jobService.immediatelyJob(id);
        if (triggered) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 작업 상태 확인
    @GetMapping("/{id}/status")
    public ResponseEntity<String> getJobStatus(@PathVariable Long id) throws SchedulerException{
         // JobInfo 조회 로직
        String status = jobService.getScheduleState(id);
        return ResponseEntity.ok(status);
    }

    // 작업이 실행 중인지 확인
    @GetMapping("/{id}/running")
    public ResponseEntity<Boolean> isJobRunning(@PathVariable Long id) throws SchedulerException{
        // JobInfo 조회 로직
        boolean isRunning = jobService.isJobRunning(id);
        return ResponseEntity.ok(isRunning);
    }
}
