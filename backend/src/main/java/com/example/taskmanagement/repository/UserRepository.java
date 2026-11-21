package com.example.taskmanagement.repository;

import com.example.taskmanagement.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {}
