package com.example.taskmanagement.configuration;

import com.example.taskmanagement.domain.Task;
import com.example.taskmanagement.domain.User;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskDataSeederTest {
    @Mock TaskRepository taskRepository;
    @Mock UserRepository userRepository;

    @Test
    void seedsFiveTasksWhenTaskTableIsEmpty() throws Exception {
        User alex=new User("Alex Morgan","alex@example.com");
        when(taskRepository.count()).thenReturn(0L);
        when(userRepository.findAll()).thenReturn(List.of(alex));

        new TaskDataSeeder(taskRepository,userRepository).run();

        verify(taskRepository,times(5)).save(any(Task.class));
    }

    @Test
    void doesNotSeedTasksWhenTasksAlreadyExist() throws Exception {
        when(taskRepository.count()).thenReturn(5L);

        new TaskDataSeeder(taskRepository,userRepository).run();

        verify(taskRepository,never()).save(any(Task.class));
        verify(userRepository,never()).findAll();
    }
}
