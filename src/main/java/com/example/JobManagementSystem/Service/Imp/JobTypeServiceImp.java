package com.example.JobManagementSystem.Service.Imp;


import com.example.JobManagementSystem.Model.JobType;
import com.example.JobManagementSystem.Repository.JobTypeRepository;
import com.example.JobManagementSystem.Service.JobTypeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class JobTypeServiceImp implements JobTypeService {

    @Autowired
    private JobTypeRepository jobTypeRepository;

    @Override
    public List<JobType> getAllJobTypes(int pageNumber) {

        Page<JobType> jobTypes = jobTypeRepository.findAll(PageRequest.of(pageNumber, 10));  // i made it fixed here on purpose
        return jobTypes.stream().toList();


    }


}
