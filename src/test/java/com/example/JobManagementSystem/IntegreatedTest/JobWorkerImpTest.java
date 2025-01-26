package com.example.JobManagementSystem.IntegreatedTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.JobManagementSystem.Factory.JobExecutorFactory;
import com.example.JobManagementSystem.JobExecutor.JobExecutor;
import com.example.JobManagementSystem.Model.JobType;
import com.example.JobManagementSystem.Model.MyJob;
import com.example.JobManagementSystem.Model.Enum.JobStatus;
import com.example.JobManagementSystem.Repository.JobRepository;
import com.example.JobManagementSystem.Service.Imp.JobWorkerImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

public class JobWorkerImpTest {

    @Mock
    private JobExecutorFactory jobExecutorFactory;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private JobExecutor jobExecutor;

    private JobWorkerImp jobWorkerImp;
    private MyJob myJob;
    private JobType jobType;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jobWorkerImp = new JobWorkerImp(jobExecutorFactory, jobRepository);

        // Create a test job
        myJob = new MyJob();
        myJob.setId(1L);
        myJob.setName("Test Job");
        myJob.setStatus(JobStatus.QUEUED);

        jobType = new JobType();
        jobType.setName("JobType1");  // Mock JobType
        myJob.setJobType(jobType);
    }

    @Test
    public void processJob_ShouldUpdateStatusToSuccess_AndExecuteJob() {
        // Arrange
        when(jobExecutorFactory.getExecutor(anyString())).thenReturn(jobExecutor);  // Mock executor

        // Act
        jobWorkerImp.processJob(myJob);

        // Assert
        assertEquals(JobStatus.SUCCESS, myJob.getStatus());
    }


    @Test
    public void recover_ShouldSetStatusToFailed_WhenJobFailsAfterMaxRetries() {
        // Arrange
        when(jobExecutorFactory.getExecutor(anyString())).thenReturn(jobExecutor);
        doThrow(new RuntimeException("Execution failed")).when(jobExecutor).execute();  // Simulate failure in execution

        // Act
        jobWorkerImp.recover(new RuntimeException("Execution failed"), myJob);

        // Assert
        assertEquals(JobStatus.FAILED, myJob.getStatus());  // Verify job status is set to FAILED
        verify(jobRepository, times(1)).save(myJob);  // Ensure job status is saved once after failure
    }
}
