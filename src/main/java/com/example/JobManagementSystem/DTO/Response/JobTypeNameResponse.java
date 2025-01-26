package com.example.JobManagementSystem.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class JobTypeNameResponse {

    List<String> jobTypeName;

    public List<String> getJobTypeName() {
        return jobTypeName;
    }

    public void setJobTypeName(List<String> jobTypeName) {
        this.jobTypeName = jobTypeName;
    }
}
