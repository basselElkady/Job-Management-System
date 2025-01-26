package com.example.JobManagementSystem.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.LOCKED)
public class JobStatusIsRuning extends RuntimeException{

    public JobStatusIsRuning(String message) {
        super(message);
    }
}
