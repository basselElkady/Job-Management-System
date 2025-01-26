package com.example.JobManagementSystem.Repository;

import com.example.JobManagementSystem.Model.MyJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<MyJob,Long> {
    boolean existsByName(String name);

    MyJob findByName(String name);

    boolean existsById(Long id);

    @Query("SELECT j.status FROM MyJob j WHERE j.id = :jobId")
    String findJobStatusById(Long jobId);
}
