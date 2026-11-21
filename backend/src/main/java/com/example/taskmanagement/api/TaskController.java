package com.example.taskmanagement.api;

import com.example.taskmanagement.api.TaskRequests.*;
import com.example.taskmanagement.api.TaskResponses.*;
import com.example.taskmanagement.application.*;
import com.example.taskmanagement.domain.TaskEnums.Priority;
import com.example.taskmanagement.domain.TaskEnums.TaskStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
@Tag(name="Task Management")
public class TaskController {
    private final TaskService tasks;
    private final UserService users;
    public TaskController(TaskService tasks,UserService users){this.tasks=tasks;this.users=users;}
    @GetMapping("/tasks")
    @Operation(summary="List, search, filter, sort, and page tasks")
    public PagedTaskResponse list(@RequestParam(required=false) String search,
        @RequestParam(required=false) TaskStatus status,@RequestParam(required=false) List<Priority> priority,
        @RequestParam(defaultValue="createdAt") String sort,@RequestParam(defaultValue="desc") String direction,
        @RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="6") int size){
        return tasks.list(search,status,priority,sort,direction,page,size);}
    @PostMapping("/tasks")
    @Operation(summary="Create a task")
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody CreateTaskRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(tasks.create(request));}
    @GetMapping("/tasks/{id}")
    public TaskResponse get(@PathVariable UUID id){return tasks.get(id);}
    @PutMapping("/tasks/{id}")
    public TaskResponse update(@PathVariable UUID id,@Valid @RequestBody UpdateTaskRequest request){return tasks.update(id,request);}
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){tasks.delete(id);return ResponseEntity.noContent().build();}
    @GetMapping("/users")
    public List<UserResponse> users(){return users.list().stream().map(UserResponse::from).toList();}
}
