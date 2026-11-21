package com.example.taskmanagement.api;

import java.util.List;
import com.example.taskmanagement.api.TaskResponses.TaskResponse;

public record PagedTaskResponse(
    List<TaskResponse> content,int page,int size,long totalElements,int totalPages,boolean first,boolean last) {}
