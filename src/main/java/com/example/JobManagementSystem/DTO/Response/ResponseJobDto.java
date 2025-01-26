package com.example.JobManagementSystem.DTO.Response;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResponseJobDto {

    private Long id;
    private String name;
    private String jobType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }
}
