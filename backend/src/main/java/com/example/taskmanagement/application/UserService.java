package com.example.taskmanagement.application;

import com.example.taskmanagement.domain.User;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {
    private final UserRepository users;
    public UserService(UserRepository users){this.users=users;}
    public List<User> list(){return users.findAll();}
    public User require(UUID id){
        return users.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found: "+id));
    }
}
