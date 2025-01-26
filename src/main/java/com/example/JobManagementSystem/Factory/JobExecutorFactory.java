package com.example.JobManagementSystem.Factory;


import com.example.JobManagementSystem.JobExecutor.JobExecutor;
import com.example.JobManagementSystem.Model.JobType;
import com.example.JobManagementSystem.Repository.JobTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JobExecutorFactory {
    private final Map<String, JobExecutor> executors;

    private final JobTypeRepository jobTypeRepository;



    @Autowired
    public JobExecutorFactory(List<JobExecutor> executorList, JobTypeRepository jobTypeRepository) {
        this.jobTypeRepository = jobTypeRepository;
        this.executors = executorList.stream()
                .collect(Collectors.toMap(
                        executor -> executor.getClass().getSimpleName().replace("JobExecutor", ""),
                        executor -> executor
                ));
        for(String jobType : executors.keySet()) {
            if(jobTypeRepository.findByName(jobType).isEmpty()) {
                JobType jobType1 = new JobType();
                jobType1.setName(jobType);
                jobTypeRepository.save(jobType1);
            }
        }



    }


    public JobExecutor getExecutor(String jobType) {
        return executors.getOrDefault(jobType, () -> {
            throw new IllegalArgumentException("Unknown Job Type: " + jobType);
        });
    }

}
