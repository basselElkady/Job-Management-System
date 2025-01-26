package com.example.JobManagementSystem.Service.Imp;


import com.example.JobManagementSystem.Configuration.JobScheduler;
import com.example.JobManagementSystem.DTO.Request.JobRequestDto;
import com.example.JobManagementSystem.DTO.Response.JobListResponse;
import com.example.JobManagementSystem.DTO.Response.ResponseJobDto;
import com.example.JobManagementSystem.Exception.JobNotFound;
import com.example.JobManagementSystem.Exception.JobStatusIsRuning;
import com.example.JobManagementSystem.Mapper.JobMapper;
import com.example.JobManagementSystem.Model.Enum.JobStatus;
import com.example.JobManagementSystem.Model.MyJob;
import com.example.JobManagementSystem.Producer.JobQueueProducer;
import com.example.JobManagementSystem.Repository.JobRepository;
import com.example.JobManagementSystem.Service.JobService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class JobServiceImp implements JobService {

    @Value("${pagination.pageSize}")
    private int pageSize;
    private final JobRepository jobRepository;
    private final JobQueueProducer jobQueueProducer;
    private final JobMapper jobMapper;
    private final JobScheduler jobScheduler;


    @Autowired
    public JobServiceImp(JobRepository jobRepository,JobQueueProducer jobQueueProducer, JobMapper jobMapper,JobScheduler jobScheduler){
        this.jobMapper=jobMapper;
        this.jobQueueProducer=jobQueueProducer;
        this.jobRepository=jobRepository;
        this.jobScheduler=jobScheduler;
    }

    @Override
    public boolean createJob(JobRequestDto jobRequestDto) {
        MyJob myJob = jobMapper.jobRequestToJob(jobRequestDto);
//        if(jobRepository.existsByName(myJob.getName()))
//            throw new IllegalArgumentException("Job name already exists: " + myJob.getName()); // name is not unique any more

        myJob.setStatus(JobStatus.QUEUED);
        jobRepository.save(myJob);

        if ( jobRequestDto.getschedule() == null || jobRequestDto.getschedule().equals(LocalDateTime.now())) {
            // If no scheduled time, send it to RabbitMQ immediately
            jobQueueProducer.sendJob(myJob);
        } else {
            // If scheduled time is set, schedule execution asynchronously
            jobScheduler.scheduleJob(myJob, () -> jobQueueProducer.sendJob(myJob));
        }

        return true;
    }


    public boolean retryJob(Long id) {
        MyJob myJob = jobRepository.findById(id).orElseThrow(
                () -> new JobNotFound("job not found with this id"+id)
        );

        if(myJob.getStatus() != JobStatus.FAILED) {
            return false;
        }

        myJob.setStatus(JobStatus.QUEUED);
        myJob.setScheduledTime(LocalDateTime.now());
        jobQueueProducer.sendJob(myJob);
        jobRepository.save(myJob);

        return true;
    }



//    @Override
//    public boolean createBatchJobs(List<JobRequestDto> jobsRequestDto) {
//        jobsRequestDto.forEach(this::createJob);
//        return true;
//    }

    @Override
    public boolean createBatchJobs(List<JobRequestDto> jobsRequestDto) {
        jobsRequestDto.forEach(this::createJob);
        return true;
    }

    @Override
    public boolean deleteJob(Long id) {
        if(jobRepository.existsById(id)) {
            MyJob myJob = jobRepository.findById(id).orElseThrow(
                    () -> new JobNotFound("Job not found with this id " + id)
            );
            if(myJob.getStatus() == JobStatus.RUNNING)
                //throw new IllegalArgumentException("Cannot delete a running job: " + name+ " as it's status is: " + myJob.getStatus());
                throw new JobStatusIsRuning(" job status is Running ");
            jobRepository.delete(myJob);
        }else{
            //throw new IllegalArgumentException("Job name does not exist: " + id);
            throw new JobNotFound("Job not found with this id "+id );
        }
        return true;
    }

    @Override
    public String getJobStatus(Long id) {
        if(id == null)
            throw new IllegalArgumentException("id cannot be null");
        if(!jobRepository.existsById(id))
            //throw new IllegalArgumentException("there is no job with that id");
            throw new JobNotFound("Job not found with this id "+id );

        return jobRepository.findJobStatusById(id);
    }

    @Override
    public JobListResponse finAllJobs(int pageNumber) {

        Page<MyJob> jobList = jobRepository.findAll(PageRequest.of(pageNumber, pageSize)); // i make it fixed here
        List<ResponseJobDto> responseJobDtos= jobList.stream()
                .map(jobMapper::jobToJobResponseDto)
                .toList();
        JobListResponse jobListResponse=new JobListResponse();
        jobListResponse.setJobRequestDtos(responseJobDtos);
        return jobListResponse;

    }


}
