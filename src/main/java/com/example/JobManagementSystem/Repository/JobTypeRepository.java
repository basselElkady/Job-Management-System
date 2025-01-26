package com.example.JobManagementSystem.Repository;

import com.example.JobManagementSystem.Model.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobTypeRepository extends JpaRepository<JobType, Long> {
    Optional<JobType> findByName(String typeName);
    boolean existsByName(String typeName);

}