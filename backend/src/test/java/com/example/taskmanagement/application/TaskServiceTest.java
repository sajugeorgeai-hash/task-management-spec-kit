package com.example.taskmanagement.application;

import com.example.taskmanagement.api.TaskRequests.CreateTaskRequest;
import com.example.taskmanagement.domain.Task;
import com.example.taskmanagement.domain.TaskEnums.Priority;
import com.example.taskmanagement.domain.TaskEnums.TaskStatus;
import com.example.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.example.taskmanagement.domain.User;
import com.example.taskmanagement.api.TaskRequests.UpdateTaskRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock TaskRepository tasks;
    @Mock UserService users;

    @Test void createsTaskWithSuppliedValues(){
        TaskService service=new TaskService(tasks,users);
        when(tasks.save(any(Task.class))).thenAnswer(inv->inv.getArgument(0));
        var result=service.create(new CreateTaskRequest("Test","Description",TaskStatus.TODO,Priority.HIGH,null,null));
        assertEquals("Test",result.title());assertEquals(Priority.HIGH,result.priority());
        verify(tasks).save(any(Task.class));
    }

    @Test void rejectsUnknownTask(){
        UUID id=UUID.randomUUID();
        when(tasks.findById(id)).thenReturn(Optional.empty());
        TaskService service=new TaskService(tasks,users);
        assertThrows(com.example.taskmanagement.exception.ResourceNotFoundException.class,()->service.get(id));
    }

    @Test void rejectsInvalidPaging(){
        TaskService service=new TaskService(tasks,users);
        assertThrows(IllegalArgumentException.class,()->service.list(null,null,null,"createdAt","desc",-1,10));
        assertThrows(IllegalArgumentException.class,()->service.list(null,null,null,"createdAt","desc",0,101));
        assertThrows(IllegalArgumentException.class,()->service.list(null,null,null,"bad","desc",0,10));
        assertThrows(IllegalArgumentException.class,()->service.list(null,null,null,"createdAt","sideways",0,10));
    }

    @Test void listsAndSortsTasksAcrossSearchFilterCombinations(){
        TaskService service=new TaskService(tasks,users);
        Task low=new Task();low.setTitle("Low");low.setStatus(TaskStatus.TODO);low.setPriority(Priority.LOW);
        Task high=new Task();high.setTitle("High");high.setStatus(TaskStatus.DONE);high.setPriority(Priority.HIGH);
        var page=new PageImpl<Task>(List.of(low,high),org.springframework.data.domain.PageRequest.of(0,10),2);
        when(tasks.findAll(any(Pageable.class))).thenReturn(page);
        when(tasks.findByTitleContainingIgnoreCase(anyString(),any(Pageable.class))).thenReturn(page);
        when(tasks.findByStatus(any(TaskStatus.class),any(Pageable.class))).thenReturn(page);
        when(tasks.findByPriorityIn(anyList(),any(Pageable.class))).thenReturn(page);
        when(tasks.findByTitleContainingIgnoreCaseAndStatus(anyString(),any(TaskStatus.class),any(Pageable.class))).thenReturn(page);
        when(tasks.findByTitleContainingIgnoreCaseAndPriorityIn(anyString(),anyList(),any(Pageable.class))).thenReturn(page);
        when(tasks.findByStatusAndPriorityIn(any(TaskStatus.class),anyList(),any(Pageable.class))).thenReturn(page);
        when(tasks.findByTitleContainingIgnoreCaseAndStatusAndPriorityIn(anyString(),any(TaskStatus.class),anyList(),any(Pageable.class))).thenReturn(page);
        assertEquals(2,service.list(null,null,null,"createdAt","asc",0,10).content().size());
        assertEquals(2,service.list("task",null,null,"dueDate","desc",0,10).content().size());
        assertEquals(2,service.list(null,TaskStatus.TODO,null,"priority","asc",0,10).content().size());
        assertEquals(2,service.list(null,null,List.of(Priority.HIGH),"priority","desc",0,10).content().size());
        assertEquals(2,service.list("task",TaskStatus.TODO,null,"createdAt","desc",0,10).content().size());
        assertEquals(2,service.list("task",null,List.of(Priority.HIGH),"createdAt","asc",0,10).content().size());
        assertEquals(2,service.list(null,TaskStatus.TODO,List.of(Priority.HIGH),"createdAt","desc",0,10).content().size());
        assertEquals(2,service.list("task",TaskStatus.TODO,List.of(Priority.HIGH),"priority","asc",0,10).content().size());
    }

    @Test void updatesAssignsAndDeletesTask(){
        UUID id=UUID.randomUUID(),userId=UUID.randomUUID();
        Task existing=new Task();existing.setTitle("Before");existing.setStatus(TaskStatus.TODO);existing.setPriority(Priority.LOW);
        User user=new User("Alex","alex@example.com");user.setId(userId);
        when(tasks.findById(id)).thenReturn(Optional.of(existing));
        when(users.require(userId)).thenReturn(user);
        when(tasks.save(existing)).thenReturn(existing);
        TaskService service=new TaskService(tasks,users);
        var updated=service.update(id,new UpdateTaskRequest("After","Details",TaskStatus.DONE,Priority.HIGH,null,userId));
        assertEquals("After",updated.title());assertEquals(user,existing.getAssignee());
        service.delete(id);
        verify(tasks).delete(existing);
    }
}
