package com.example.JobManagementSystem.Producer;

import com.example.JobManagementSystem.Configuration.RabbitMQConfig;
import com.example.JobManagementSystem.Model.MyJob;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.stereotype.Service;

@Service
public class JobQueueProducer {

    private final RabbitTemplate rabbitTemplate;

    public JobQueueProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

//    public void sendJob(MyJob job) {
//        rabbitTemplate.convertAndSend(RabbitMQConfig.JOB_QUEUE, job);
//    }

    public void sendJob(MyJob job) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.JOB_QUEUE, job, message -> {
            message.getMessageProperties().setPriority((int) job.getPriority());
            return message;
        });

        System.out.println("Sent job: " + job + " with priority: " + job.getPriority());
    }


}
