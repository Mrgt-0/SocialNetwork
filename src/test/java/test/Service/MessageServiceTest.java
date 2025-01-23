package test.Service;

import org.example.socialnetwork.DTO.MessageDTO;
import org.example.socialnetwork.DTO.UserDTO;
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
        messageService.sendMessage(senderDTO, recipientDTO, "Hello!");

        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    public void testGetMessages() {
        List<Message> messages = new ArrayList<>();
        messages.add(convertToEntity(messageDTO));

        when(messageRepository.findMessagesBySenderAndRecipient(convertToEntity(senderDTO), convertToEntity(recipientDTO)))
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
