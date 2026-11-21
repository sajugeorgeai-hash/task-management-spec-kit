package com.example.taskmanagement.api;

import com.example.taskmanagement.domain.TaskEnums;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

public class TaskRequests {
    public record CreateTaskRequest(
        @NotBlank @Size(max=200) String title,
        @Size(max=2000) String description,
        @NotNull TaskEnums.TaskStatus status,
        @NotNull TaskEnums.Priority priority,
        LocalDate dueDate,
        UUID assigneeId
    ) {}
    public record UpdateTaskRequest(
        @NotBlank @Size(max=200) String title,
        @Size(max=2000) String description,
        @NotNull TaskEnums.TaskStatus status,
        @NotNull TaskEnums.Priority priority,
        LocalDate dueDate,
        UUID assigneeId
    ) {}
}
