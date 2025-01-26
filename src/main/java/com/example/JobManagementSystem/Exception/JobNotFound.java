package com.example.JobManagementSystem.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class JobNotFound extends RuntimeException{

    public JobNotFound(String message) {
        super(message);
    }
}
