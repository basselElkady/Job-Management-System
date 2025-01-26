package com.example.JobManagementSystem.Mapper;


import com.example.JobManagementSystem.DTO.Request.JobRequestDto;
import com.example.JobManagementSystem.DTO.Response.ResponseJobDto;
import com.example.JobManagementSystem.Exception.InCorrectScheduledTime;
import com.example.JobManagementSystem.Exception.JobTypeNotFound;
import com.example.JobManagementSystem.Model.MyJob;
import com.example.JobManagementSystem.Repository.JobTypeRepository;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class JobMapper {

    @Autowired
    private JobTypeRepository jobTypeRepository;

    
    public MyJob jobRequestToJob(JobRequestDto jobRequest) {

        MyJob myJob = new MyJob();
        myJob.setName(jobRequest.getName());
        if(jobTypeRepository.findByName(jobRequest.getJobType()).isEmpty()){
            //throw new IllegalArgumentException("Job type does not exist: " + jobRequest.getJobType());
            throw new JobTypeNotFound("Job type does not exist: " + jobRequest.getJobType());
        }
        myJob.setJobType(jobTypeRepository.findByName(jobRequest.getJobType()).get());
        if(jobRequest.getschedule() == null)
            myJob.setScheduledTime(LocalDateTime.now());
        else if(jobRequest.getschedule().equals(LocalDateTime.now()))
            myJob.setScheduledTime(LocalDateTime.now());
        else if (jobRequest.getschedule().isBefore(LocalDateTime.now()))
            //throw new IllegalArgumentException("time is not correct");
            throw new InCorrectScheduledTime("in correct schedule Time");
        else
            myJob.setScheduledTime(jobRequest.getschedule());
        return myJob;
    }



    public ResponseJobDto jobToJobResponseDto(MyJob job){
        ResponseJobDto responseJobDto= new ResponseJobDto();
        responseJobDto.setId(job.getId());
        responseJobDto.setName(job.getName());
        responseJobDto.setJobType(job.getJobType().getName());
        return responseJobDto;
    }

}
