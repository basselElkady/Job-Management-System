package com.example.JobManagementSystem.Configuration;
import com.example.JobManagementSystem.Model.MyJob;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.*;

@Component
public class JobScheduler {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void scheduleJob(MyJob job, Runnable jobTask) {
        long delay = Duration.between(LocalDateTime.now(), job.getScheduledTime()).toMillis();

        if (delay > 0) {
            System.out.println("Job scheduled for execution at: " + job.getScheduledTime());

            // Run scheduling asynchronously
            CompletableFuture.runAsync(() -> {
                scheduler.schedule(() -> {
                    System.out.println("Executing scheduled job at: " + LocalDateTime.now());
                    jobTask.run(); // Run the job logic (e.g., send to RabbitMQ)
                }, delay, TimeUnit.MILLISECONDS);
            });
        } else {
            System.out.println("Scheduled time already passed, executing job immediately.");
            jobTask.run(); // Execute immediately
        }
    }
}
