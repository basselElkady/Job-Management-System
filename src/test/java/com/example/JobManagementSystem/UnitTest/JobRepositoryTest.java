package com.example.JobManagementSystem.UnitTest;

import com.example.JobManagementSystem.Model.Enum.JobStatus;
import com.example.JobManagementSystem.Model.MyJob;
import com.example.JobManagementSystem.Repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class JobRepositoryTest {


    @Autowired
    private JobRepository jobRepository;

    private MyJob job;

    @BeforeEach
    void setUp() {
        job = new MyJob();
        job.setName("Test Job");
        job.setStatus(JobStatus.QUEUED);
        jobRepository.save(job);
    }

    @Test
    void testExistsByName() {
        // Given
        String name = "Test Job";

        // When
        boolean exists = jobRepository.existsByName(name);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void testFindByName() {
        // Given
        String name = "Test Job";

        // When
        MyJob foundJob = jobRepository.findByName(name);

        // Then
        assertThat(foundJob).isNotNull();
        assertThat(foundJob.getName()).isEqualTo(name);
        assertThat(foundJob.getStatus()).isEqualTo(JobStatus.QUEUED);

    }

    @Test
    void testFindByNameNotFound() {
        // Given
        String name = "Non-existent Job";

        // When
        MyJob foundJob = jobRepository.findByName(name);

        // Then
        assertThat(foundJob).isNull();
    }




}
