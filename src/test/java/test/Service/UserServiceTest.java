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
        userDTO.setUserName("Mark");
        userDTO.setEmail("zuckerberg@example.com");
        userDTO.setPassword("password123");
        userDTO.setRole(Collections.singleton("USER"));
    }

    @Test
    public void testRegisterUser_Success() throws SystemException {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUserName(userDTO.getUserName())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("hashedpassword");
        when(userRepository.save(any(User.class))).thenReturn(convertToEntity(userDTO));

        UserDTO registeredUser = userService.registerUser(userDTO);

        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUserName());
        verify(passwordEncoder).encode(anyString());
        verify(userRepository).save(any(User.class));
        verify(userRepository).findByEmail(userDTO.getEmail());
        verify(userRepository).findByUserName(userDTO.getUserName());
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() throws SystemException {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(convertToEntity(userDTO)));

        UserDTO registeredUser = userService.registerUser(userDTO);

        assertNull(registeredUser, "Пользователь не должен быть зарегистрирован.");
        verify(userRepository).findByEmail(userDTO.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUser_UsernameAlreadyExists() throws SystemException {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUserName(userDTO.getUserName())).thenReturn(Optional.of(convertToEntity(userDTO)));

        UserDTO registeredUser = userService.registerUser(userDTO);

        assertNull(registeredUser, "Пользователь не должен быть зарегистрирован.");
        verify(userRepository).findByUserName(userDTO.getUserName());
        verify(userRepository, never()).save(any(User.class));
    }

    private User convertToEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setUserName(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setBirthdate(userDTO.getBirthdate());
        user.setProfilePicture(userDTO.getProfilePicture());
        user.setRole(userDTO.getRole());
        return user;
    }
}
