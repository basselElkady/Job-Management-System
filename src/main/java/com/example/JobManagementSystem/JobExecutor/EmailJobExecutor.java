package com.example.JobManagementSystem.JobExecutor;

import com.example.JobManagementSystem.JobExecutor.JobExecutor;
import com.example.JobManagementSystem.Model.Enum.JobStatus;
import com.example.JobManagementSystem.Model.MyJob;
import com.example.JobManagementSystem.Repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailJobExecutor implements JobExecutor {


    @Override
    public void execute() {

        try {
            Thread.sleep(10000);
        }
        catch (Exception e){
            throw new RuntimeException("jjjjjjjjjjj");
        }

        throw new RuntimeException("this is your exception");
        //System.out.println("Executing Email Job ");
    }
}