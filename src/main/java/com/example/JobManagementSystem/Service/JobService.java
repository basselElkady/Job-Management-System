package com.example.JobManagementSystem.Service;

import com.example.JobManagementSystem.DTO.Request.JobRequestDto;
import com.example.JobManagementSystem.DTO.Response.JobListResponse;

import java.util.List;

public interface JobService {

    boolean createJob(JobRequestDto jobRequestDto);

    boolean createBatchJobs(List<JobRequestDto> jobsRequestDto);

    boolean deleteJob(Long id);

    String getJobStatus(Long id);

    JobListResponse finAllJobs(int pageSize);

}