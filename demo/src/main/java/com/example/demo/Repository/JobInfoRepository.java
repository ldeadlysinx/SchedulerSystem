package com.example.demo.Repository;

import com.example.demo.entity.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobInfoRepository extends JpaRepository<JobInfo, Long> {
}
