package org.example.socialnetwork.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "image", nullable = true)
    private String image;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updated_at;

    @Column(name = "text", nullable = false)
    private String text;

    public Post() {}

    public Post(User user, String text, String image, LocalDateTime created_at, LocalDateTime updated_at){
        this.user=user;
        this.image=image;
        this.created_at=created_at;
        this.updated_at=updated_at;
        this.text=text;
    }

    public Long getPostId() { return id; }

    public void setPostId(Long postId) { this.id = postId; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) { this.text = text; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public LocalDateTime getCreated_at() { return created_at; }

    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }

    public LocalDateTime getUpdated_at() { return updated_at; }

    public void setUpdated_at(LocalDateTime updated_at) { this.updated_at = updated_at; }
}
