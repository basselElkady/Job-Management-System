package com.example.JobManagementSystem.JobExecutor;

import com.example.JobManagementSystem.JobExecutor.JobExecutor;
import com.example.JobManagementSystem.Model.MyJob;
import org.springframework.stereotype.Service;

@Service
public class DataLoadJobExecutor implements JobExecutor {
    @Override
    public void execute() {
        System.out.println("Executing Data Load Job ");
    }

}
