package com.example.JobManagementSystem.UnitTest;

import java.util.Optional;

import com.example.JobManagementSystem.Model.JobType;
import com.example.JobManagementSystem.Repository.JobTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class JobTypeRepositoryTest {

    @Autowired
    private JobTypeRepository jobTypeRepository;

    private JobType jobType;

    @BeforeEach
    void setUp() {
        jobType = new JobType();
        jobType.setId(12L);
        jobType.setName("test");
        jobTypeRepository.save(jobType);
    }

    @Test
    void testFindByName() {
        String typeName = "test";

        Optional<JobType> foundJobType = jobTypeRepository.findByName(typeName);
        assertThat(foundJobType).isPresent();
        assertThat(foundJobType.get().getName()).isEqualTo(typeName);
    }

    @Test
    void testFindByNameNotFound() {
        // Given
        String typeName = "Manager";

        Optional<JobType> foundJobType = jobTypeRepository.findByName(typeName);

        assertThat(foundJobType).isNotPresent();
    }

    @Test
    void testExistsByName() {
        String typeName = "test";

        boolean exists = jobTypeRepository.existsByName(typeName);

        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByNameNotFound() {
        String typeName = "Manager";

        boolean exists = jobTypeRepository.existsByName(typeName);

        assertThat(exists).isFalse();
    }
}