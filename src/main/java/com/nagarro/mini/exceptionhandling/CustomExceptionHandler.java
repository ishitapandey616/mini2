package com.nagarro.mini.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(PageNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePageNotFoundException(PageNotFoundException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("code", 404);
        errorDetails.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
        
        
    }
    @ExceptionHandler(IllegalArgumentException .class)
    public ResponseEntity<Map<String, Object>> handlePageNotFoundException(IllegalArgumentException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("code", 400);
        errorDetails.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
        
        
    }


}
