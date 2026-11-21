package com.example.taskmanagement.repository;

import com.example.taskmanagement.domain.Task;
import com.example.taskmanagement.domain.TaskEnums.Priority;
import com.example.taskmanagement.domain.TaskEnums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    Page<Task> findByTitleContainingIgnoreCaseAndStatusAndPriorityIn(
        String title, TaskStatus status, Collection<Priority> priorities, Pageable pageable);
    Page<Task> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
    Page<Task> findByPriorityIn(Collection<Priority> priorities, Pageable pageable);
    Page<Task> findByTitleContainingIgnoreCaseAndStatus(String title, TaskStatus status, Pageable pageable);
    Page<Task> findByTitleContainingIgnoreCaseAndPriorityIn(String title, Collection<Priority> priorities, Pageable pageable);
    Page<Task> findByStatusAndPriorityIn(TaskStatus status, Collection<Priority> priorities, Pageable pageable);
}
