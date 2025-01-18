package org.example.socialnetwork.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import org.example.socialnetwork.Model.User;

import java.time.LocalDateTime;

public class MessageDTO {
    private Long message_id;
    private User sender;
    private User recipient;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String text;

    public MessageDTO(Long message_id, User sender, User recipient, LocalDateTime created_at, LocalDateTime updated_at, String text){
        this.message_id=message_id;
        this.sender=sender;
        this.recipient=recipient;
        this.created_at=created_at;
        this.updated_at=updated_at;
        this.text=text;
    }

    public Long getMessage_id() { return message_id; }

    public void setMessage_id(Long message_id) { this.message_id = message_id; }

    public User getSender() { return sender; }

    public void setUser_id(User sender) { this.sender = sender; }

    public User getRecipient() { return recipient; }

    public void setRecipient(User recipient) { this.recipient = recipient; }

    public LocalDateTime getCreated_at() { return created_at; }

    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }

    public LocalDateTime getUpdated_at() { return updated_at; }

    public void setUpdated_at(LocalDateTime updated_at) { this.updated_at = updated_at; }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }
}
