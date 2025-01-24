package test.Service;

import jakarta.transaction.SystemException;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.UserRepository;
import org.example.socialnetwork.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO();
        userDTO.setUserId(1L);
        userDTO.setUserName("testUser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
    }

    @Test
    void testFindUserById() {
        User user = new User();
        user.setUserId(1L);
        user.setUserName("testUser");
        when(userRepository.getById(1L)).thenReturn(user);
        UserDTO result = userService.findUserById(1L);
        assertNotNull(result);
        assertEquals("testUser", result.getUserName());
    }

    @Test
    void testFindUserByIdAsOptional() {
        User user = new User();
        user.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Optional<UserDTO> result = userService.findUserByIdAsOptional(1L);
        assertTrue(result.isPresent());
        assertEquals(user.getUserId(), result.get().getUserId());
    }

    @Test
    void testFindByUserName() {
        User user = new User();
        user.setUserName("testUser");
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));
        UserDTO result = userService.findByUserName("testUser");
        assertNotNull(result);
        assertEquals("testUser", result.getUserName());
    }

    @Test
    void testFindByUserNameWhenNotFound() {
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.findByUserName("testUser");
        });
        assertEquals("Пользователь не найден: testUser", exception.getMessage());
    }

    @Test
    void testRegisterUserWhenAlreadyExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUser(userDTO));
        assertEquals("Пользователь с таким email уже существует: test@example.com", exception.getMessage());
    }

    @Test
    void testDeleteUserByIdWhenUserExists() {
        User user = new User();
        user.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.deleteUserById(1L);
        verify(userRepository).delete(any());
    }

    @Test
    void testDeleteUserByIdWhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        userService.deleteUserById(1L);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void testChangeUserRole() {
        User user = new User();
        user.setUserId(1L);
        user.setRole(Set.of("USER"));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Set<String> newRoles = Set.of("ADMIN");
        userService.changeUserRole(1L, newRoles);
        assertEquals(newRoles, user.getRole());
        verify(userRepository).save(user);
    }

    @Test
    void testChangeUserRoleWhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.changeUserRole(1L, Set.of("ADMIN"));
        });
        assertEquals("Пользователь не найден", exception.getMessage());
    }
}
