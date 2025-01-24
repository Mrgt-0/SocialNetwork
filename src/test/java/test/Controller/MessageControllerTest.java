package test.Controller;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.example.socialnetwork.Config.MyUserDetails;
import org.example.socialnetwork.Controller.MessageController;
import org.example.socialnetwork.DTO.MessageDTO;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Service.MessageService;
import org.example.socialnetwork.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageControllerTest {
    @InjectMocks
    private MessageController messageController;

    @Mock
    private MessageService messageService;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    private UserDTO currentUser;
    private UserDTO recipient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        currentUser = new UserDTO();
        currentUser.setUserId(1L);
        currentUser.setUserName("currentUser");

        recipient = new UserDTO();
        recipient.setUserId(2L);
        recipient.setUserName("recipient");

        MessageDTO message = new MessageDTO(convertToEntity(currentUser), convertToEntity(recipient), LocalDateTime.now(), LocalDateTime.now(), "Hello!");
    }

    @Test
    public void testShowMessages_Success() {
        when(authentication.getPrincipal()).thenReturn(new MyUserDetails(currentUser));
        when(userService.findByUserName("recipient")).thenReturn(new UserDTO());
        when(messageService.getMessages(any(), any())).thenReturn(new ArrayList<>());
        ResponseEntity<?> response = messageController.showMessages("recipient", authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
    }

    @Test
    public void testShowMessages_RecipientNotFound() {
        when(authentication.getPrincipal()).thenReturn(new MyUserDetails(currentUser));
        when(userService.findByUserName("nonexistent")).thenReturn(null);
        ResponseEntity<?> response = messageController.showMessages("nonexistent", authentication);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Пользователь не найден.", response.getBody());
    }

    @Test
    public void testShowMessages_NoRecipient() {
        ResponseEntity<?> responseEntity = messageController.showMessages(null, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Параметр recipient не был передан.", responseEntity.getBody());
    }

    @Test
    public void testSendMessage_Success() {
        when(authentication.getPrincipal()).thenReturn(new MyUserDetails(currentUser));
        when(userService.findByUserName(recipient.getUserName())).thenReturn(recipient);
        ResponseEntity<String> responseEntity = messageController.sendMessage(recipient.getUserName(), "Hello", authentication);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Сообщение успешно отправлено.", responseEntity.getBody());
        verify(messageService).sendMessage(any(UserDTO.class), any(UserDTO.class), any(String.class));
    }

    @Test
    public void testSendMessage_RecipientNotFound() {
        when(authentication.getPrincipal()).thenReturn(new MyUserDetails(currentUser));
        when(userService.findByUserName(recipient.getUserName())).thenReturn(null);
        ResponseEntity<String> responseEntity = messageController.sendMessage(recipient.getUserName(), "Hello", authentication);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Получатель не найден.", responseEntity.getBody());
        verify(messageService, never()).sendMessage(any(UserDTO.class), any(UserDTO.class), any(String.class));
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
