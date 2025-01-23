package org.example.socialnetwork.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.example.socialnetwork.Model.User;

import java.time.LocalDateTime;

public class FriendDTO {
    private Long id;
    private User user;
    private User friend;
    private LocalDateTime created_at;

    public FriendDTO() {}

    public FriendDTO(Long id, User user, User friend, LocalDateTime created_at){
        this.id=id;
        this.user=user;
        this.friend=friend;
        this.created_at=created_at;
    }
    public Long getFriend_id() { return id; }

    public void setFriend_id(Long id) { this.id = id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public User getFriend() { return friend; }

    public void setFriend(User friend) { this.friend = friend; }

    public LocalDateTime getCreated_at() { return created_at; }

    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }
}
