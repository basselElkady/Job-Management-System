package com.example.JobManagementSystem.Controller;

import com.example.JobManagementSystem.DTO.Response.JobTypeNameResponse;
import com.example.JobManagementSystem.Model.JobType;
import com.example.JobManagementSystem.Service.JobTypeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/jobtype")
@AllArgsConstructor
public class JobTypeController {

    @Autowired
    private JobTypeService jobTypeService;



    @GetMapping
    public ResponseEntity<JobTypeNameResponse> getAllJobTypes() {
        List<JobType> jobTypes = jobTypeService.getAllJobTypes();
        List<String> result= jobTypes.stream()
                .map(JobType::getName)
                .toList();
        JobTypeNameResponse jobTypeNameResponse=new JobTypeNameResponse();
        jobTypeNameResponse.setJobTypeName(result);
        return ResponseEntity.ok(jobTypeNameResponse);
    }

}