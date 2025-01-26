package com.example.JobManagementSystem.DTO.Response;

import com.example.JobManagementSystem.DTO.Request.JobRequestDto;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class JobListResponse {

    List<ResponseJobDto> jobRequestDtos;

    public List<ResponseJobDto> getJobRequestDtos() {
        return jobRequestDtos;
    }

    public void setJobRequestDtos(List<ResponseJobDto> jobRequestDtos) {
        this.jobRequestDtos = jobRequestDtos;
    }
}
