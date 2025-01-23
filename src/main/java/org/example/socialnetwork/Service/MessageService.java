package org.example.socialnetwork.Service;

import org.example.socialnetwork.DTO.MessageDTO;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.Message;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.MessageRepository;
import org.example.socialnetwork.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    public void sendMessage(UserDTO senderDTO, UserDTO recipientDTO, String content) {
        User sender = userRepository.findByUserName(senderDTO.getUserName())
                .orElseThrow(() -> new RuntimeException("Отправитель не найден"));

        User recipient = userRepository.findByUserName(recipientDTO.getUserName())
                .orElseThrow(() -> new RuntimeException("Получатель не найден"));

        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setText(content);

        messageRepository.save(message);
    }

    public void saveMessage(MessageDTO messageDTO){
        Message message=convertToEntity(messageDTO);
        messageRepository.save(message);
    }

    public List<MessageDTO> getMessages(UserDTO senderDTO, UserDTO recipientDTO) {
        User sender = userRepository.findByUserName(senderDTO.getUserName())
                .orElseThrow(() -> new RuntimeException("Отправитель не найден"));

        User recipient = userRepository.findByUserName(recipientDTO.getUserName())
                .orElseThrow(() -> new RuntimeException("Получатель не найден"));

        List<Message> messages = messageRepository.findMessagesBySenderAndRecipient(sender, recipient);
        return messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MessageDTO getMessageById(Long id) {
        MessageDTO messageDTO = convertToDTO(messageRepository.findById(id).orElse(null));
        return messageDTO;
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    private MessageDTO convertToDTO(Message message) {
        if (message == null) {
            return null;
        }
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessage_id(message.getMessage_id());
        messageDTO.setSender(message.getSender());
        messageDTO.setRecipient(message.getRecipient());
        messageDTO.setCreated_at(message.getCreated_at());
        messageDTO.setCreated_at(message.getCreated_at());
        messageDTO.setText(message.getText());
        return messageDTO;
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
}
