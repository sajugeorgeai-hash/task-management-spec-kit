package com.example.taskmanagement.configuration;

import com.example.taskmanagement.domain.Task;
import com.example.taskmanagement.domain.TaskEnums.Priority;
import com.example.taskmanagement.domain.TaskEnums.TaskStatus;
import com.example.taskmanagement.domain.User;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TaskDataSeeder implements CommandLineRunner {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskDataSeeder(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (taskRepository.count() > 0) {
            return;
        }

        List<User> users = userRepository.findAll();
        User alex = users.stream()
            .filter(user -> "alex@example.com".equals(user.getEmail()))
            .findFirst()
            .orElseGet(() -> userRepository.save(new User("Alex Morgan", "alex@example.com")));
        User jordan = users.stream()
            .filter(user -> "jordan@example.com".equals(user.getEmail()))
            .findFirst()
            .orElseGet(() -> userRepository.save(new User("Jordan Lee", "jordan@example.com")));

        saveTask("Prepare quarterly report", "Compile the quarterly metrics and team highlights.", TaskStatus.IN_PROGRESS, Priority.HIGH, LocalDate.now().plusDays(2), alex);
        saveTask("Review pull requests", "Review and provide feedback on open pull requests.", TaskStatus.TODO, Priority.MEDIUM, LocalDate.now().plusDays(3), jordan);
        saveTask("Update documentation", "Refresh setup notes and feature documentation.", TaskStatus.TODO, Priority.LOW, LocalDate.now().plusDays(7), alex);
        saveTask("Fix login validation", "Resolve validation failures on unsupported credentials.", TaskStatus.IN_PROGRESS, Priority.HIGH, LocalDate.now().minusDays(1), jordan);
        saveTask("Plan sprint retrospective", "Collect discussion topics and prepare the sprint agenda.", TaskStatus.DONE, Priority.MEDIUM, LocalDate.now().minusDays(2), alex);
    }

    private void saveTask(String title, String description, TaskStatus status, Priority priority,
                          LocalDate dueDate, User assignee) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setPriority(priority);
        task.setDueDate(dueDate);
        task.setAssignee(assignee);
        taskRepository.save(task);
    }
}
