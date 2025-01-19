package test.Service;

import org.example.socialnetwork.Model.Message;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.MessageRepository;
import org.example.socialnetwork.Service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageServiceTest {
    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    private User sender;
    private User recipient;
    private Message message;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        sender = new User();
        sender.setUserId(1L);
        sender.setUserName("sender");

        recipient = new User();
        recipient.setUserId(2L);
        recipient.setUserName("recipient");
        message = new Message(sender, recipient, LocalDateTime.now(), LocalDateTime.now(), "Hello!");
    }

    @Test
    public void testSendMessage() {
        messageService.sendMessage(sender, recipient, "Hello!");

        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    public void testGetMessages() {
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        when(messageRepository.findBySenderAndRecipient(sender, recipient)).thenReturn(messages);

        List<Message> resultMessages = messageService.getMessages(sender, recipient);
        assertEquals(1, resultMessages.size());
        assertEquals("Hello!", resultMessages.get(0).getText());
    }

    @Test
    public void testGetMessageById() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        Message foundMessage = messageService.getMessageById(1L);
        assertNotNull(foundMessage);
        assertEquals("Hello!", foundMessage.getText());
    }

    @Test
    public void testDeleteMessage() {
        messageService.deleteMessage(1L);
        verify(messageRepository, times(1)).deleteById(1L);
    }
}
