package com.example.taskmanagement.api;

import com.example.taskmanagement.domain.Task;
import com.example.taskmanagement.domain.TaskEnums.Priority;
import com.example.taskmanagement.domain.TaskEnums.TaskStatus;
import com.example.taskmanagement.domain.User;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class TaskResponses {
    public record UserResponse(UUID id,String displayName,String email) {
        public static UserResponse from(User u){return u==null?null:new UserResponse(u.getId(),u.getDisplayName(),u.getEmail());}
    }
    public record TaskResponse(UUID id,String title,String description,TaskStatus status,Priority priority,
        LocalDate dueDate,UserResponse assignee,Instant createdAt,Instant updatedAt) {
        public static TaskResponse from(Task t){return new TaskResponse(t.getId(),t.getTitle(),t.getDescription(),
            t.getStatus(),t.getPriority(),t.getDueDate(),UserResponse.from(t.getAssignee()),
            t.getCreatedAt(),t.getUpdatedAt());}
    }
}
