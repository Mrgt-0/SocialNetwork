package org.example.socialnetwork.Model;

import ch.qos.logback.core.model.INamedModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.time.LocalDate;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int post_id;

    @Column(name = "user_id", nullable = false)
    private int user_id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "image", nullable = true)
    private String image;

    @Column(name = "created_at", nullable = false)
    private LocalDate created_at;

    @Column(name = "updated_at", nullable = false)
    private LocalDate updated_at;

    public Post(int user_id, String text, String image, LocalDate created_at, LocalDate updated_at){
        this.user_id=user_id;
        this.text=text;
        this.image=image;
        this.created_at=created_at;
        this.updated_at=updated_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) { this.text = text; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public LocalDate getCreated_at() { return created_at; }

    public void setCreated_at(LocalDate created_at) { this.created_at = created_at; }

    public LocalDate getUpdated_at() { return updated_at; }

    public void setUpdated_at(LocalDate updated_at) { this.updated_at = updated_at; }
}
