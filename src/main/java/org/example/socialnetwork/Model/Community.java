package org.example.socialnetwork.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "communities")
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "community_name", nullable = false)
    private String communityName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    @ManyToOne(optional = false)
    @JoinColumn(name = "admin_user_id", nullable = false)
    private User admin;

    public Community() {}

    public Community(String communityName, String description, LocalDateTime created_at, User admin){
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
