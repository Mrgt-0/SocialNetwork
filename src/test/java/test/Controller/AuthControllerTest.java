package test.Controller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.example.socialnetwork.Controller.AuthController;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;

public class AuthControllerTest {
    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        UserDTO user = new UserDTO();
        user.setUserName("testUser");
        user.setFirstName("Имя");
        user.setLastName("Фамилия");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setBirthdate(LocalDate.parse("1990-01-01")); // если необходимо
        user.setProfilePicture(null);
        user.setRole(new HashSet<>(Collections.singletonList("USER")));


        when(userService.registerUser(any(UserDTO.class))).thenReturn(null);

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json;charset=UTF-8")
                        .content("{\"userName\":\"testUser\", \"firstName\":\"Имя\", \"lastName\":\"Фамилия\", \"password\":\"password123\", \"email\":\"test@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Регистрация прошла успешно! Пожалуйста, войдите."));

        verify(userService, times(1)).registerUser(any(UserDTO.class));
    }

    @Test
    public void testRegisterUser_Failure() throws Exception {
        UserDTO user = new UserDTO();
        user.setUserName("testUser");
        user.setFirstName("Имя");
        user.setLastName("Фамилия");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setBirthdate(LocalDate.parse("1990-01-01")); // если необходимо
        user.setProfilePicture(null);
        user.setRole(new HashSet<>(Collections.singletonList("USER")));


        doThrow(new RuntimeException("Ошибка регистрации")).when(userService).registerUser(any(UserDTO.class));

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json;charset=UTF-8")
                        .content("{\"userName\":\"testUser\", \"firstName\":\"Имя\", \"lastName\":\"Фамилия\", \"password\":\"password123\", \"email\":\"test@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Регистрация прошла успешно! Пожалуйста, войдите."));

        verify(userService, times(1)).registerUser(any(UserDTO.class));
    }

    @Test
    public void testLogin_Success() throws Exception {
        LoginResponseDTO loginRequest = new LoginResponseDTO();
        loginRequest.setUserName("Tessy");
        loginRequest.setPassword("Tessy_Sammy28*");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json;charset=UTF-8")
                        .content("{\"userName\":\"testUser\", \"firstName\":\"Имя\", \"lastName\":\"Фамилия\", \"password\":\"password123\", \"email\":\"test@example.com\"}"))
                .andExpect(status().isOk());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testLogin_BadCredentials() throws Exception {
        LoginResponseDTO loginRequest = new LoginResponseDTO();
        loginRequest.setUserName("Sammy");
        loginRequest.setPassword("Tessy_Sammy28*");

        doThrow(new BadCredentialsException("Неверный логин или пароль.")).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("{\"userName\":\"testUser\",\"password\":\"incorrectPassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Неверный логин или пароль."));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testLogin_InternalError() throws Exception {
        LoginResponseDTO loginRequest = new LoginResponseDTO();
        loginRequest.setUserName("testUser");
        loginRequest.setPassword("password123");

        doThrow(new RuntimeException("Произошла ошибка при аутентификации.")).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json;charset=UTF-8")
                        .content("{\"userName\":\"testUser\",\"password\":\"password123\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Произошла ошибка при аутентификации."));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
