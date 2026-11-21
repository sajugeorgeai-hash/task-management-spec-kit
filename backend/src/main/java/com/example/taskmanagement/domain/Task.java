package com.example.taskmanagement.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import com.example.taskmanagement.domain.TaskEnums.Priority;
import com.example.taskmanagement.domain.TaskEnums.TaskStatus;

@Entity
@Table(name="task")
public class Task {
    @Id @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;
    @Column(nullable=false,length=200) private String title;
    @Column(length=2000) private String description;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private TaskStatus status;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Priority priority;
    private LocalDate dueDate;
    @ManyToOne(fetch=FetchType.EAGER) private User assignee;
    @Column(nullable=false,updatable=false) private Instant createdAt;
    @Column(nullable=false) private Instant updatedAt;

    @PrePersist void onCreate(){createdAt=Instant.now();updatedAt=createdAt;}
    @PreUpdate void onUpdate(){updatedAt=Instant.now();}

    public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public TaskStatus getStatus(){return status;} public void setStatus(TaskStatus v){status=v;}
    public Priority getPriority(){return priority;} public void setPriority(Priority v){priority=v;}
    public LocalDate getDueDate(){return dueDate;} public void setDueDate(LocalDate v){dueDate=v;}
    public User getAssignee(){return assignee;} public void setAssignee(User v){assignee=v;}
    public Instant getCreatedAt(){return createdAt;} public void setCreatedAt(Instant v){createdAt=v;}
    public Instant getUpdatedAt(){return updatedAt;} public void setUpdatedAt(Instant v){updatedAt=v;}
}
