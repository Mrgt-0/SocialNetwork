package org.example.socialnetwork.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int message_id;

    @Column(name = "created_at", nullable = false)
    private LocalDate created_at;

    @Column(name = "updated_at", nullable = false)
    private LocalDate updated_at;

    @Column(name = "text", nullable = false)
    private String text;

    public Message(LocalDate created_at, LocalDate updated_at, String text){
        this.created_at=created_at;
        this.updated_at=updated_at;
        this.text=text;
    }

    public int getMessage_id() { return message_id; }

    public void setMessage_id(int message_id) { this.message_id = message_id; }

    public LocalDate getCreated_at() { return created_at; }

    public void setCreated_at(LocalDate created_at) { this.created_at = created_at; }

    public LocalDate getUpdated_at() { return updated_at; }

    public void setUpdated_at(LocalDate updated_at) { this.updated_at = updated_at; }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }
}
