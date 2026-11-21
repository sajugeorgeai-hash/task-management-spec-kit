package com.example.taskmanagement.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.time.Instant;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(ResourceNotFoundException ex,HttpServletRequest req){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error(404,ex.getMessage(),req,null));}
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalid(MethodArgumentNotValidException ex,HttpServletRequest req){
        Map<String,String> fields=new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e->fields.put(e.getField(),e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(error(400,"Validation failed",req,fields));}
    @ExceptionHandler({ConstraintViolationException.class,MethodArgumentTypeMismatchException.class,IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> badRequest(Exception ex,HttpServletRequest req){
        return ResponseEntity.badRequest().body(error(400,ex.getMessage(),req,null));}
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> unreadable(HttpMessageNotReadableException ex,HttpServletRequest req){
        return ResponseEntity.badRequest().body(error(400,"Malformed or invalid request body",req,null));}
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unexpected(Exception ex,HttpServletRequest req){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error(500,"Unexpected server error",req,null));}
    private ErrorResponse error(int status,String message,HttpServletRequest req,Map<String,String> fields){
        return new ErrorResponse(status,message,req.getRequestURI(),Instant.now(),fields==null?Map.of():fields);}
}
