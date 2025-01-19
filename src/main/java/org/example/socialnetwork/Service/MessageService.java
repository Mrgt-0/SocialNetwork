package org.example.socialnetwork.Service;

import org.example.socialnetwork.Model.Message;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public void sendMessage(User sender, User recipient, String content) {
        Message message = new Message(sender, recipient, LocalDateTime.now(), LocalDateTime.now(), content);
        messageRepository.save(message);
    }

    public void saveMessage(Message message){
        messageRepository.save(message);
    }
    public List<Message> getMessages(User sender, User recipient) {
        return messageRepository.findBySenderAndRecipient(sender, recipient);
    }

    public Message getMessageById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}
