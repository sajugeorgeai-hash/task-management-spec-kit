package com.example.taskmanagement.application;

import com.example.taskmanagement.domain.User;
import com.example.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock UserRepository repository;
    @Test void listsAndRequiresUsers(){
        UUID id=UUID.randomUUID();
        User user=new User("Alex","alex@example.com");user.setId(id);
        when(repository.findAll()).thenReturn(List.of(user));
        when(repository.findById(id)).thenReturn(Optional.of(user));
        UserService service=new UserService(repository);
        assertEquals(1,service.list().size());assertEquals("Alex",service.require(id).getDisplayName());
        assertThrows(com.example.taskmanagement.exception.ResourceNotFoundException.class,
            ()->service.require(UUID.randomUUID()));
    }
}
