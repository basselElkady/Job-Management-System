package com.example.JobManagementSystem.Service.Imp;

import com.example.JobManagementSystem.Configuration.RabbitMQConfig;
import com.example.JobManagementSystem.Factory.JobExecutorFactory;
import com.example.JobManagementSystem.JobExecutor.JobExecutor;
import com.example.JobManagementSystem.Model.Enum.JobStatus;
import com.example.JobManagementSystem.Model.MyJob;
import com.example.JobManagementSystem.Repository.JobRepository;
import com.example.JobManagementSystem.Service.JobWorker;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//@Transactional
public class JobWorkerImp implements JobWorker {

        private final JobExecutorFactory jobExecutorFactory;
        private final JobRepository jobRepository;

        @Autowired
        public JobWorkerImp(JobExecutorFactory jobExecutorFactory, JobRepository jobRepository) {
            this.jobExecutorFactory = jobExecutorFactory;
            this.jobRepository = jobRepository;
        }

        @RabbitListener(queues = RabbitMQConfig.JOB_QUEUE)
        @Retryable(value = Exception.class,
                maxAttemptsExpression = "#{${retry.maxAttempts}}",
                backoff = @Backoff(delayExpression = "#{${retry.delay}}"))
//                maxAttempts = 3,
//                backoff = @Backoff(delay = 2000))
        public void processJob(MyJob job) {
            job.setStatus(JobStatus.RUNNING);
            jobRepository.save(job);

            JobExecutor executor = jobExecutorFactory.getExecutor(job.getJobType().getName());
            executor.execute();
            job.setStatus(JobStatus.SUCCESS);

            jobRepository.save(job);
        }


        @Recover
        @Transactional
        public void recover(Exception e, MyJob job) {
            job.setStatus(JobStatus.FAILED);
            jobRepository.save(job);
            //System.out.println("Job failed after 3 retries: " + job.getId());
        }

}
