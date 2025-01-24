package test.Controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.example.socialnetwork.Controller.UserController;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Service.UserDetailsServiceImpl;
import org.example.socialnetwork.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private Authentication authentication;

    private UserDTO user;

    @BeforeEach
    public void setup() {
        user = new UserDTO();
        user.setUserId(1L);
        user.setUserName("testuser");

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testGetUserById_UserFound() {
        when(userService.findUserById(anyLong())).thenReturn(user);

        ResponseEntity<UserDTO> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).findUserById(1L);
    }

    @Test
    public void testGetUserById_UserNotFound() {
        when(userService.findUserById(anyLong())).thenReturn(null);
        ResponseEntity<UserDTO> response = userController.getUserById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetUserByUserName_UserFound() {
        when(userService.findByUserName("testuser")).thenReturn(user);
        ResponseEntity<UserDTO> response = userController.getUserByUserName("testuser");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).findByUserName("testuser");
    }

    @Test
    public void testGetUserByUserName_UserNotFound() {
        when(userService.findUserById(anyLong())).thenReturn(null);
        ResponseEntity<UserDTO> response = userController.getUserById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testShowProfileForm_UserFound() {
        when(authentication.getName()).thenReturn("testuser");
        when(userService.findByUserName("testuser")).thenReturn(user);

        ResponseEntity<UserDTO> response = userController.showProfileForm();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testShowProfileForm_UserNotFound() {
        when(authentication.getName()).thenReturn("unknownUser");
        when(userService.findByUserName("unknownUser")).thenReturn(null);
        ResponseEntity<UserDTO> response = userController.showProfileForm();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateProfile_UserNotAuthenticated() throws Exception {
        when(authentication.getName()).thenReturn(null);

        ResponseEntity<String> response = userController.updateProfile("newUserName", "new@example.com", "NewFirstName", "NewLastName");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Пользователь не аутентифицирован.", response.getBody());
    }

    @Test
    public void testUpdateProfile_CurrentUserNotFound() throws Exception {
        when(authentication.getName()).thenReturn("unknownUser");
        when(userService.findByUserName("unknownUser")).thenReturn(null);

        ResponseEntity<String> response = userController.updateProfile("newUserName", "new@example.com", "NewFirstName", "NewLastName");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Пользователь не найден.", response.getBody());
    }
}
