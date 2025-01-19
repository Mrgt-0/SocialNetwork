package org.example.socialnetwork.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "friends")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "friend_id", nullable = false)
    private User friend;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    public Friend() {}

    public Friend(User user, User friend, LocalDateTime created_at){
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
