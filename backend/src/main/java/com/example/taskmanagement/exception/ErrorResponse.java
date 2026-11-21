package com.example.taskmanagement.exception;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(int status,String message,String path,Instant timestamp,Map<String,String> fieldErrors) {}
