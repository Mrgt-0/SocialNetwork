package org.example.socialnetwork.DTO;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

public class UserDTO {
    private Long userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private LocalDate birthdate;
    private String profilePicture;
    private Set<String> role;

    public UserDTO(Long userId, String userName, String firstName, String lastName, String password, String email, LocalDate birthdate, String profilePicture, Set<String> role){
        this.userId=userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.birthdate = birthdate;
        this.profilePicture = profilePicture;
        this.role = role;
    }
    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId=userId; }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName){ this.userName=userName; }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) { this.email=email; }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) { this.birthdate = birthdate; }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public Set<String> getRole() {
        return role != null ? role : Collections.emptySet();
    }

    public void setRole(Set<String> role) { this.role=role; }
}
