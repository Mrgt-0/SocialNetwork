package org.example.socialnetwork.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.example.socialnetwork.Model.Community;
import org.example.socialnetwork.Model.User;

import java.time.LocalDateTime;

public class CommunityMemberDTO {
    private Long id;
    private Community community;
    private User user;
    private LocalDateTime joined_at;

    public CommunityMemberDTO() {}

    public CommunityMemberDTO(Community community, User user, LocalDateTime joined_at){
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
