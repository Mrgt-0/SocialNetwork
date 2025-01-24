package test.Controller;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.example.socialnetwork.Config.MyUserDetails;
import org.example.socialnetwork.Controller.MessageController;
import org.example.socialnetwork.DTO.MessageDTO;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.UserRepository;
import org.example.socialnetwork.Service.MessageService;
import org.example.socialnetwork.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MessageControllerTest {
    @InjectMocks
    private MessageController messageController;

    @Mock
    private MessageService messageService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private Model model;

    private UserDTO currentUser;
    private UserDTO recipient;
    private MessageDTO message;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        currentUser = new UserDTO();
        currentUser.setUserId(1L);
        currentUser.setUserName("currentUser");

        recipient = new UserDTO();
        recipient.setUserId(2L);
        recipient.setUserName("recipient");

        message = new MessageDTO(convertToEntity(currentUser), convertToEntity(recipient), LocalDateTime.now(), LocalDateTime.now(), "Hello!");
    }

    @Test
    public void testShowMessages_Success() {
        when(authentication.getPrincipal()).thenReturn(new MyUserDetails(currentUser));
        when(userService.findUserByUserNameAsOptional("recipient")).thenReturn(Optional.of(recipient));
        when(messageService.getMessages(currentUser, recipient)).thenReturn(List.of(message));

        String viewName = String.valueOf(messageController.showMessages("recipient", authentication));

        assertEquals("messages", viewName);
        verify(model).addAttribute("messages", List.of(message));
        verify(model).addAttribute("recipient", recipient);
    }

    @Test
    public void testShowMessages_RecipientNotFound() {
        when(authentication.getPrincipal()).thenReturn(new MyUserDetails(currentUser));
        when(userService.findUserByUserNameAsOptional("nonexistent")).thenReturn(Optional.empty());

        String viewName = String.valueOf(messageController.showMessages("nonexistent", authentication));

        assertEquals("error", viewName);
        verify(model).addAttribute("error", "Пользователь не найден.");
    }

    @Test
    public void testShowMessages_NoRecipient() {
        String viewName = String.valueOf(messageController.showMessages(null, authentication));

        assertEquals("error", viewName);
        verify(model).addAttribute("error", "Параметр recipient не был передан.");
    }

    @Test
    public void testSendMessage_Success() {
        when(authentication.getPrincipal()).thenReturn(new MyUserDetails(currentUser));
        when(userService.findUserByUserNameAsOptional(recipient.getUserName())).thenReturn(Optional.of(recipient));

        String viewName = String.valueOf(messageController.sendMessage(recipient.getUserName(), "Hello", authentication));

        assertEquals("redirect:/messages?recipient=recipient", viewName);
        verify(messageService).saveMessage(any(MessageDTO.class));
    }

    @Test
    public void testSendMessage_RecipientNotFound() {
        when(authentication.getPrincipal()).thenReturn(new MyUserDetails(currentUser));
        when(userService.findUserByUserNameAsOptional(recipient.getUserName())).thenReturn(Optional.empty());

        String viewName = String.valueOf(messageController.sendMessage(recipient.getUserName(), "Hello", authentication));

        assertEquals("error", viewName);
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
