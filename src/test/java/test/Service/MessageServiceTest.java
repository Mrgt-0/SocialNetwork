package test.Service;

import org.example.socialnetwork.DTO.MessageDTO;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.Message;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.MessageRepository;
import org.example.socialnetwork.Repository.UserRepository;
import org.example.socialnetwork.Service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    private UserDTO senderDTO;
    private UserDTO recipientDTO;
    private MessageDTO messageDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        senderDTO = new UserDTO();
        senderDTO.setUserId(1L);
        senderDTO.setUserName("sender");

        recipientDTO = new UserDTO();
        recipientDTO.setUserId(2L);
        recipientDTO.setUserName("recipient");
        messageDTO = new MessageDTO(convertToEntity(senderDTO), convertToEntity(recipientDTO), LocalDateTime.now(), LocalDateTime.now(), "Hello!");
    }

    @Test
    public void testSendMessage() {
        UserDTO senderDTO = new UserDTO();
        senderDTO.setUserName("user1");
        UserDTO recipientDTO = new UserDTO();
        recipientDTO.setUserName("user2");
        User sender = new User();
        User recipient = new User();

        when(userRepository.findByUserName("user1")).thenReturn(Optional.of(sender));
        when(userRepository.findByUserName("user2")).thenReturn(Optional.of(recipient));
        messageService.sendMessage(senderDTO, recipientDTO, "Hello!");
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    public void testGetMessages() {
        UserDTO senderDTO = new UserDTO();
        senderDTO.setUserName("user1");
        UserDTO recipientDTO = new UserDTO();
        recipientDTO.setUserName("user2");
        User sender = new User();
        sender.setUserName("user1");
        User recipient = new User();
        recipient.setUserName("user2");
        when(userRepository.findByUserName("user1")).thenReturn(Optional.of(sender));
        when(userRepository.findByUserName("user2")).thenReturn(Optional.of(recipient));

        Message message = new Message();
        message.setText("Hello!");
        message.setSender(sender);
        message.setRecipient(recipient);

        List<Message> messages = new ArrayList<>();
        messages.add(message);

        when(messageRepository.findMessagesBySenderAndRecipient(sender, recipient))
                .thenReturn(messages);
        List<MessageDTO> resultMessagesDTO = messageService.getMessages(senderDTO, recipientDTO);

        assertEquals(1, resultMessagesDTO.size());
        assertEquals("Hello!", resultMessagesDTO.get(0).getText());
    }

    @Test
    public void testGetMessageById() {
        Message message = convertToEntity(messageDTO);
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        MessageDTO foundMessage = messageService.getMessageById(1L);
        assertNotNull(foundMessage);
        assertEquals("Hello!", foundMessage.getText());
    }

    @Test
    public void testDeleteMessage() {
        messageService.deleteMessage(1L);
        verify(messageRepository, times(1)).deleteById(1L);
    }

    private Message convertToEntity(MessageDTO messageDTO) {
        if (messageDTO == null) {
            return null;
        }
        Message message=new Message();
        message.setMessage_id(messageDTO.getMessage_id());
        message.setSender(messageDTO.getSender());
        message.setRecipient(messageDTO.getRecipient());
        message.setCreated_at(messageDTO.getCreated_at());
        message.setCreated_at(messageDTO.getCreated_at());
        message.setText(messageDTO.getText());
        return message;
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
