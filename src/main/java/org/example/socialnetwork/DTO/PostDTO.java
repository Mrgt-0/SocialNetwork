package org.example.socialnetwork.DTO;

import org.example.socialnetwork.Model.User;
import java.time.LocalDateTime;

public class PostDTO {
    private Long id;
    private User user;
    private String image;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String text;

    public PostDTO() {}

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
