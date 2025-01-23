package org.example.socialnetwork.DTO;
import org.example.socialnetwork.Model.User;

import java.time.LocalDateTime;

public class CommunityDTO {
    private Long id;
    private String communityName;
    private String description;
    private LocalDateTime created_at;
    private User admin;

    public CommunityDTO() {}

    public CommunityDTO(String communityName, String description, LocalDateTime created_at, User admin){
        this.communityName=communityName;
        this.description=description;
        this.created_at=created_at;
        this.admin=admin;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getCommunityName() { return communityName; }

    public void setCommunityName(String communityName) { this.communityName = communityName; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreated_at() { return created_at; }

    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }

    public User getAdmin() { return admin; }

    public void setAdmin(User admin) { this.admin = admin; }
}
