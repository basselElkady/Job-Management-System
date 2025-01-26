package com.example.JobManagementSystem.IntegreatedTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import com.example.JobManagementSystem.DTO.Request.JobRequestDto;
import com.example.JobManagementSystem.Exception.JobStatusIsRuning;
import com.example.JobManagementSystem.Model.MyJob;
import com.example.JobManagementSystem.Model.JobType;
import com.example.JobManagementSystem.Model.Enum.JobStatus;
import com.example.JobManagementSystem.Producer.JobQueueProducer;
import com.example.JobManagementSystem.Repository.JobRepository;
import com.example.JobManagementSystem.Repository.JobTypeRepository;  // Added for JobType repository
import com.example.JobManagementSystem.Service.Imp.JobServiceImp;
import com.example.JobManagementSystem.Mapper.JobMapper;
import com.example.JobManagementSystem.Configuration.JobScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

public class JobServiceImpTest {

    @Mock
    private JobRepository jobRepository;
    @Mock
    private JobQueueProducer jobQueueProducer;
    @Mock
    private JobMapper jobMapper;
    @Mock
    private JobScheduler jobScheduler;
    @Mock
    private JobTypeRepository jobTypeRepository;  // Mocking JobType repository

    @InjectMocks
    private JobServiceImp jobService;

    private JobRequestDto jobRequestDto;
    private MyJob myJob;
    private JobType jobType;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jobRequestDto = new JobRequestDto();
        jobRequestDto.setName("Test Job");
        jobRequestDto.setJobType("JobType1");  // Set a valid job type
        jobRequestDto.setSchedule(LocalDateTime.now().plusMinutes(10));  // Set a scheduled time

        myJob = new MyJob();
        myJob.setId(1L);
        myJob.setName("Test Job");
        myJob.setStatus(JobStatus.QUEUED);

        jobType = new JobType();
        jobType.setName("JobType1");  // Mock JobType
        myJob.setJobType(jobType);
    }

    @Test
    public void createJob_ShouldSaveJob_WhenValidRequest() {
        // Arrange
        when(jobMapper.jobRequestToJob(any(JobRequestDto.class))).thenReturn(myJob);
        when(jobRepository.save(any(MyJob.class))).thenReturn(myJob);
        when(jobTypeRepository.findByName("JobType1")).thenReturn(Optional.of(jobType));  // Mock valid JobType

        // Act
        boolean result = jobService.createJob(jobRequestDto);

        // Assert
        assertTrue(result);
        verify(jobRepository, times(1)).save(myJob);
        verify(jobScheduler, times(1)).scheduleJob(any(MyJob.class), any(Runnable.class));
    }


    @Test
    public void createJob_ShouldSendJobToQueue_WhenNoScheduleTime() {
        // Arrange
        jobRequestDto.setSchedule(null);  // No scheduled time, so it should go directly to the queue
        when(jobMapper.jobRequestToJob(any(JobRequestDto.class))).thenReturn(myJob);
        when(jobRepository.save(any(MyJob.class))).thenReturn(myJob);
        when(jobTypeRepository.findByName("JobType1")).thenReturn(Optional.of(jobType));  // Mock valid JobType

        // Act
        boolean result = jobService.createJob(jobRequestDto);

        // Assert
        assertTrue(result);
        verify(jobQueueProducer, times(1)).sendJob(myJob);  // Directly send to the queue
        verify(jobScheduler, times(0)).scheduleJob(any(MyJob.class), any(Runnable.class));  // Should not schedule
    }

    @Test
    public void deleteJob_ShouldDeleteJob_WhenJobExists() {

        when(jobRepository.existsById(1L)).thenReturn(true);
        when(jobRepository.findById(1L)).thenReturn(Optional.of(myJob)); // Use findById mock here

        // Act
        boolean result = jobService.deleteJob(1L);

        // Assert
        assertTrue(result);
        verify(jobRepository, times(1)).delete(myJob);

    }

    @Test
    public void deleteJob_ShouldThrowException_WhenJobIsRunning() {
        // Arrange
        myJob.setStatus(JobStatus.RUNNING);
        when(jobRepository.existsById(1L)).thenReturn(true);
        when(jobRepository.findById(1L)).thenReturn(Optional.of(myJob));

        // Act & Assert
        JobStatusIsRuning exception = assertThrows(JobStatusIsRuning.class, () -> {
            jobService.deleteJob(1L);
        });
        assertEquals(" job status is Running ", exception.getMessage());
    }
}
