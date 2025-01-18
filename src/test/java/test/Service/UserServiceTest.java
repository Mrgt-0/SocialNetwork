package test.Service;

import jakarta.transaction.SystemException;
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

import java.util.Optional;

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

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserName("Mark");
        user.setEmail("Zucerberg");
        user.setPassword("password123");
    }

    @Test
    public void testRegisterUser_Success() throws SystemException {
        // Подготовка данных
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("hashedpassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.registerUser(user);

        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUserName());
        verify(passwordEncoder).encode(anyString());
        verify(userRepository).save(any(User.class));
        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).findByUserName(user.getUserName());
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() throws SystemException {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));  // Эмуляция существующего пользователя

        User registeredUser = userService.registerUser(user);

        assertNull(registeredUser, "Пользователь не должен быть зарегистрирован.");
        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUser_UsernameAlreadyExists() throws SystemException {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));

        User registeredUser = userService.registerUser(user);

        assertNull(registeredUser, "Пользователь не должен быть зарегистрирован.");
        verify(userRepository).findByUserName(user.getUserName());
        verify(userRepository, never()).save(any(User.class));
    }
}
