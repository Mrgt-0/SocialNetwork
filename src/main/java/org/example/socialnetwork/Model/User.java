package org.example.socialnetwork.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name ="first_name", nullable = false)
    private String firstName;

    @Column(name ="last_name", nullable = false)
    private String lastName;

    @Column(name ="password_hash", nullable = false)
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String password;

    @Column(name ="email", nullable = false)
    @Email(message = "Некорректный формат email")
    private String email;

    @Column(name ="birthdate", nullable = true)
    private LocalDate birthdate;

    @Column(name = "profile_picture", nullable = true)
    private String profilePicture;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "user_role")
    private Set<String> role = new HashSet<>();

    public User() {}

    public User(String userName, String firstName, String lastName, String password, String email, LocalDate birthdate, String profilePicture, Set<String> role) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.birthdate = birthdate;
        this.profilePicture = profilePicture;
        this.role = role;
    }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId=userId; }

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

    public Set<String> getRole() { return role; }

    public void setRole(Set<String> role) { this.role=role; }
}
