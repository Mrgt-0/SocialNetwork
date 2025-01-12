package org.example.socialnetwork.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int comment_id;

    @Column(name = "created_at", nullable = false)
    private LocalDate created_at;

    @Column(name = "updated_at", nullable = false)
    private LocalDate updated_at;

    @Column(name = "text", nullable = false)
    private String text;

    public Comment(LocalDate created_at, LocalDate updated_at, String text){
        this.created_at=created_at;
        this.updated_at=updated_at;
        this.text=text;
    }

    public int getMessage_id() { return comment_id; }

    public void setMessage_id(int comment_id) { this.comment_id = comment_id; }

    public LocalDate getCreated_at() { return created_at; }

    public void setCreated_at(LocalDate created_at) { this.created_at = created_at; }

    public LocalDate getUpdated_at() { return updated_at; }

    public void setUpdated_at(LocalDate updated_at) { this.updated_at = updated_at; }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }
}
