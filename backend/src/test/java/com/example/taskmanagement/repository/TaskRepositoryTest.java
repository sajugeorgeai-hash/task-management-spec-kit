package com.example.taskmanagement.repository;

import com.example.taskmanagement.domain.Task;
import com.example.taskmanagement.domain.TaskEnums.Priority;
import com.example.taskmanagement.domain.TaskEnums.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaskRepositoryTest {
    @Autowired TaskRepository repository;

    @Test void persistsAndPagesTasks(){
        Task task=new Task();task.setTitle("Repository task");task.setStatus(TaskStatus.TODO);task.setPriority(Priority.MEDIUM);
        repository.save(task);
        var page=repository.findByTitleContainingIgnoreCase("Repository",PageRequest.of(0,10));
        assertEquals(1,page.getTotalElements());assertEquals("Repository task",page.getContent().get(0).getTitle());
    }
}
