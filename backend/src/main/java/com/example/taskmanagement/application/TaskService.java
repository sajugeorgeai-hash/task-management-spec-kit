package com.example.taskmanagement.application;

import com.example.taskmanagement.api.PagedTaskResponse;
import com.example.taskmanagement.api.TaskRequests.*;
import com.example.taskmanagement.api.TaskResponses.TaskResponse;
import com.example.taskmanagement.domain.Task;
import com.example.taskmanagement.domain.TaskEnums.*;
import com.example.taskmanagement.exception.ResourceNotFoundException;
import com.example.taskmanagement.repository.TaskRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.Comparator;

@Service
public class TaskService {
    private final TaskRepository tasks;
    private final UserService users;
    public TaskService(TaskRepository tasks,UserService users){this.tasks=tasks;this.users=users;}

    public PagedTaskResponse list(String search,TaskStatus status,List<Priority> priorities,String sort,
                                  String direction,int page,int size){
        if(page<0||size<1||size>100) throw new IllegalArgumentException("page must be >= 0 and size must be 1-100");
        if(!List.of("dueDate","priority","createdAt").contains(sort)) throw new IllegalArgumentException("Unsupported sort field");
        if(!List.of("asc","desc").contains(direction)) throw new IllegalArgumentException("Unsupported sort direction");
        String term=search==null?"":search.trim();
        boolean hasStatus=status!=null;
        boolean hasPriority=priorities!=null&&!priorities.isEmpty();
        Sort dbSort=sort.equals("priority")?Sort.unsorted():
            Sort.by(direction.equals("asc")?Sort.Direction.ASC:Sort.Direction.DESC,
                sort.equals("dueDate")?"dueDate":"createdAt");
        Pageable pageable=PageRequest.of(page,size,dbSort);
        Page<Task> result;
        if(term.isEmpty()&&!hasStatus&&!hasPriority) result=tasks.findAll(pageable);
        else if(!term.isEmpty()&&hasStatus&&hasPriority) result=tasks.findByTitleContainingIgnoreCaseAndStatusAndPriorityIn(term,status,priorities,pageable);
        else if(!term.isEmpty()&&hasStatus) result=tasks.findByTitleContainingIgnoreCaseAndStatus(term,status,pageable);
        else if(!term.isEmpty()&&hasPriority) result=tasks.findByTitleContainingIgnoreCaseAndPriorityIn(term,priorities,pageable);
        else if(hasStatus&&hasPriority) result=tasks.findByStatusAndPriorityIn(status,priorities,pageable);
        else if(!term.isEmpty()) result=tasks.findByTitleContainingIgnoreCase(term,pageable);
        else if(hasStatus) result=tasks.findByStatus(status,pageable);
        else result=tasks.findByPriorityIn(priorities,pageable);

        List<Task> content=new ArrayList<>(result.getContent());
        if(sort.equals("priority")){
            Comparator<Task> c=Comparator.comparing(t->List.of(Priority.LOW,Priority.MEDIUM,Priority.HIGH).indexOf(t.getPriority()));
            if(direction.equals("desc")) c=c.reversed();
            content.sort(c);
        }
        return new PagedTaskResponse(content.stream().map(TaskResponse::from).toList(),result.getNumber(),
            result.getSize(),result.getTotalElements(),result.getTotalPages(),result.isFirst(),result.isLast());
    }
    public TaskResponse get(UUID id){return TaskResponse.from(require(id));}
    public TaskResponse create(CreateTaskRequest r){
        Task t=new Task(); apply(t,r.title(),r.description(),r.status(),r.priority(),r.dueDate(),r.assigneeId());return TaskResponse.from(tasks.save(t));}
    public TaskResponse update(UUID id,UpdateTaskRequest r){
        Task t=require(id);apply(t,r.title(),r.description(),r.status(),r.priority(),r.dueDate(),r.assigneeId());return TaskResponse.from(tasks.save(t));}
    public void delete(UUID id){tasks.delete(require(id));}
    private Task require(UUID id){return tasks.findById(id).orElseThrow(()->new ResourceNotFoundException("Task not found: "+id));}
    private void apply(Task t,String title,String description,TaskStatus status,Priority priority,java.time.LocalDate due,UUID assigneeId){
        t.setTitle(title);t.setDescription(description);t.setStatus(status);t.setPriority(priority);t.setDueDate(due);
        t.setAssignee(assigneeId==null?null:users.require(assigneeId));
    }
}
