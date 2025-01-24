package test.Controller;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.SystemException;
import org.example.socialnetwork.Controller.AuthController;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class AuthControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        UserDTO userDto = new UserDTO();
        userDto.setUserName("testUser");
        userDto.setPassword("testPassword");
        userDto.setEmail("testuser@example.com");
        when(userService.registerUser(any(UserDTO.class))).thenReturn(userDto);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(userService, authenticationManager)).build();
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Регистрация прошла успешно! Пожалуйста, войдите."));
    }

    @Test
    void testRegisterUser_Failure() throws Exception {
        UserDTO userDto = new UserDTO();
        userDto.setUserName("testUser");
        userDto.setPassword("testPassword");
        userDto.setEmail("test@example.com");
        when(userService.registerUser(any(UserDTO.class))).thenThrow(new SystemException("Registration error"));
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(userService, authenticationManager)).build();
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Ошибка при регистрации: Registration error"));
    }

    @Test
    void testLogin_Success() throws Exception {
        String userName = "testUser";
        String password = "testPassword";

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password)))
                .thenReturn(mock(Authentication.class));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(userService, authenticationManager)).build();

        mockMvc.perform(post("/auth/login")
                        .param("userName", userName)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().string("Аутентификация успешна!"));
    }

    @Test
    void testLogin_UserNameEmpty() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(userService, authenticationManager)).build();

        mockMvc.perform(post("/auth/login")
                        .param("userName", "")
                        .param("password", "password"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Имя пользователя не может быть пустым"));
    }

    @Test
    void testLogin_PasswordEmpty() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(userService, authenticationManager)).build();

        mockMvc.perform(post("/auth/login")
                        .param("userName", "user")
                        .param("password", ""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Пароль не может быть пустым"));
    }
}
