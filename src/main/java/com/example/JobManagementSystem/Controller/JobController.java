package com.example.JobManagementSystem.Controller;


import com.example.JobManagementSystem.DTO.Request.JobRequestDto;
import com.example.JobManagementSystem.DTO.Response.JobListResponse;
import com.example.JobManagementSystem.Service.JobService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/jobs")
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService){
        this.jobService=jobService;
    }

    @PostMapping
    public ResponseEntity<Boolean> createJob(@RequestBody @Valid JobRequestDto jobRequestDto) {

        boolean result = jobService.createJob(jobRequestDto);
        return ResponseEntity.ok(result);

    }

    @PostMapping("/batch")
    public ResponseEntity<Boolean> createBatchJobs(@RequestBody @Valid List<JobRequestDto> jobsRequestDto) {
        if (jobsRequestDto.size() > 100) {
            throw new IllegalArgumentException("Batch size too large");
        }

        boolean result = jobService.createBatchJobs(jobsRequestDto);
        return ResponseEntity.ok(result);

    }


    @PutMapping
    public ResponseEntity<Boolean> retryJob(@RequestParam @NotNull Long id){

        boolean result= jobService.retryJob(id);
        return ResponseEntity.ok(result);

    }


    @DeleteMapping
    public ResponseEntity<Boolean> deleteJob(@RequestParam Long id) {

        boolean result = jobService.deleteJob(id);
        return ResponseEntity.ok(result);

    }


    @GetMapping("{id}/status")
    public ResponseEntity<String> getJobStatus(@PathVariable Long id){
        String result= jobService.getJobStatus(id);
        return ResponseEntity.ok(result);
    }


    @GetMapping
    public ResponseEntity<JobListResponse> getAllJobs(@RequestParam int pageNumber){
        JobListResponse jobListResponse= jobService.finAllJobs(pageNumber);
        return ResponseEntity.ok(jobListResponse);
    }


}
