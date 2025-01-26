package com.example.JobManagementSystem.UnitTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.within;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.JobManagementSystem.DTO.Request.JobRequestDto;
import com.example.JobManagementSystem.Exception.InCorrectScheduledTime;
import com.example.JobManagementSystem.Exception.JobNotFound;
import com.example.JobManagementSystem.Exception.JobTypeNotFound;
import com.example.JobManagementSystem.Mapper.JobMapper;
import com.example.JobManagementSystem.Model.JobType;
import com.example.JobManagementSystem.Model.MyJob;
import com.example.JobManagementSystem.Repository.JobTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class JobMapperTest {

    @Mock
    private JobTypeRepository jobTypeRepository;

    @InjectMocks
    private JobMapper jobMapper;

    private JobRequestDto jobRequest;
    private JobType jobType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        jobType = new JobType();
        jobType.setName("Software Engineer");

        jobRequest = new JobRequestDto();
        jobRequest.setName("Test Job");
        jobRequest.setJobType("Software Engineer");
    }

    @Test
    void testValidJobRequest() {
        when(jobTypeRepository.findByName("Software Engineer")).thenReturn(Optional.of(jobType));

        MyJob myJob = jobMapper.jobRequestToJob(jobRequest);

        assertThat(myJob).isNotNull();
        assertThat(myJob.getName()).isEqualTo("Test Job");
        assertThat(myJob.getJobType()).isEqualTo(jobType);
        assertThat(myJob.getScheduledTime()).isNotNull();
    }

    @Test
    void testJobTypeNotFound() {

        Exception exception = assertThrows(JobTypeNotFound.class, () -> {
            jobMapper.jobRequestToJob(jobRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("Job type does not exist: Software Engineer");
    }

    @Test
    void testInvalidPastSchedule() {
        jobRequest.setSchedule(LocalDateTime.now().minusDays(1));

        when(jobTypeRepository.findByName("Software Engineer")).thenReturn(Optional.of(jobType));

        Exception exception = assertThrows(InCorrectScheduledTime.class, () -> {
            jobMapper.jobRequestToJob(jobRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("in correct schedule Time");
    }

    @Test
    void testNullScheduleUsesCurrentTime() {
        jobRequest.setSchedule(null); // No schedule provided

        when(jobTypeRepository.findByName("Software Engineer")).thenReturn(Optional.of(jobType));

        MyJob myJob = jobMapper.jobRequestToJob(jobRequest);

        assertThat(myJob.getScheduledTime()).isNotNull();
        assertThat(myJob.getScheduledTime()).isBeforeOrEqualTo(LocalDateTime.now());
    }

}
