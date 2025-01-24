package org.example.socialnetwork.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "community_members")
public class CommunityMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "joined_at")
    private LocalDateTime joined_at;

    public CommunityMember() {}

    public CommunityMember(Community community, User user, LocalDateTime joined_at){
        this.community=community;
        this.user=user;
        this.joined_at=joined_at;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Community getCommunity() { return community; }

    public void setCommunity(Community community) { this.community = community; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public LocalDateTime getJoined_at() { return joined_at; }

    public void setJoined_at(LocalDateTime joined_at) { this.joined_at = joined_at; }
}
