package com.example.trainbookingsystem;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.trainbookingsystem.entity.User;
import com.example.trainbookingsystem.repository.UserRepository;
import com.example.trainbookingsystem.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(user.getName(), result.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(user.getEmail(), result.get().getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldReturnEmpty() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(1L);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void createUser_ShouldSaveAndReturnUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_WhenUserExists_ShouldUpdateAndReturnUser() {
        User updatedUser = new User();
        updatedUser.setName("Jane Doe");
        updatedUser.setEmail("jane.doe@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser(1L, updatedUser);

        assertEquals(updatedUser.getName(), result.getName());
        assertEquals(updatedUser.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> userService.updateUser(1L, user));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}