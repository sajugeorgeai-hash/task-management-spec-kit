package com.example.taskmanagement.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "app_user")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable=false, length=100)
    private String displayName;
    @Column(nullable=false, unique=true, length=254)
    private String email;

    public User() {}
    public User(String displayName, String email) { this.displayName=displayName; this.email=email; }
    public UUID getId(){return id;} public void setId(UUID id){this.id=id;}
    public String getDisplayName(){return displayName;} public void setDisplayName(String v){displayName=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
}
