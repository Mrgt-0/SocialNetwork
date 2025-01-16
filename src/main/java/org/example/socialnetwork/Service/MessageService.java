package org.example.socialnetwork.Service;

import org.example.socialnetwork.Model.Message;
import org.example.socialnetwork.Repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message sendMessage(Message message) {
        message.setCreated_at(LocalDate.now());
        message.setUpdated_at(LocalDate.now());
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(int id) {
        return messageRepository.findById(id).orElse(null);
    }

    public void deleteMessage(int id) {
        messageRepository.deleteById(id);
    }
}
