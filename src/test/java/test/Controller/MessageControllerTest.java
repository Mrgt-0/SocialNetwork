package test.Controller;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.example.socialnetwork.Config.MyUserDetails;
import org.example.socialnetwork.Controller.MessageController;
import org.example.socialnetwork.Model.Message;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.UserRepository;
import org.example.socialnetwork.Service.MessageService;
import org.example.socialnetwork.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
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

    private User currentUser;
    private User recipient;
    private Message message;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Настройка текущего пользователя
        currentUser = new User();
        currentUser.setUserId(1L);
        currentUser.setUserName("currentUser");

        // Настройка получателя
        recipient = new User();
        recipient.setUserId(2L);
        recipient.setUserName("recipient");

        // Настройка сообщения
        message = new Message(currentUser, recipient, LocalDateTime.now(), LocalDateTime.now(), "Hello!");
    }

    @Test
    public void testShowMessages_Success() {
        when(authentication.getPrincipal()).thenReturn(new MyUserDetails(currentUser));
        when(userService.findByUserName("recipient")).thenReturn(Optional.of(recipient));
        when(messageService.getMessages(currentUser, recipient)).thenReturn(List.of(message));

        String viewName = messageController.showMessages("recipient", model, authentication);

        assertEquals("messages", viewName);
        verify(model).addAttribute("messages", List.of(message));
        verify(model).addAttribute("recipient", recipient);
    }

    @Test
    public void testShowMessages_RecipientNotFound() {
        when(authentication.getPrincipal()).thenReturn(new MyUserDetails(currentUser));
        when(userService.findByUserName("nonexistent")).thenReturn(Optional.empty());

        String viewName = messageController.showMessages("nonexistent", model, authentication);

        assertEquals("error", viewName);
        verify(model).addAttribute("error", "Пользователь не найден.");
    }

    @Test
    public void testShowMessages_NoRecipient() {
        String viewName = messageController.showMessages(null, model, authentication);

        assertEquals("error", viewName);
        verify(model).addAttribute("error", "Параметр recipient не был передан.");
    }

    @Test
    public void testSendMessage_Success() {
        when(authentication.getPrincipal()).thenReturn(new MyUserDetails(currentUser));
        when(userService.findByUserName(recipient.getUserName())).thenReturn(Optional.of(recipient));

        String viewName = messageController.sendMessage(recipient.getUserName(), "Hello", authentication);

        assertEquals("redirect:/messages?recipient=recipient", viewName);
        verify(messageService).saveMessage(any(Message.class));
    }

    @Test
    public void testSendMessage_RecipientNotFound() {
        when(authentication.getPrincipal()).thenReturn(new MyUserDetails(currentUser));
        when(userService.findByUserName(recipient.getUserName())).thenReturn(Optional.empty());

        String viewName = messageController.sendMessage(recipient.getUserName(), "Hello", authentication);

        assertEquals("error", viewName);
    }
}
