package com.example.JobManagementSystem.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class InCorrectScheduledTime extends RuntimeException{

    public InCorrectScheduledTime(String message) {
        super(message);
    }
}
