package org.example.socialnetwork.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

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
    private String passwordHash;

    @Column(name ="email", nullable = false)
    @Email(message = "Некорректный формат email")
    private String email;
    @Column(name ="birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "profile_picture")
    private String profilePicture;
    @Column(name = "user_role", nullable = false)
    private String role;

    public User() {}

    public User(String userName, String firstName, String lastName, String passwordHash, String email, LocalDate birthdate, String profilePicture, String role) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.email = email;
        this.birthdate = birthdate;
        this.profilePicture = profilePicture;
        this.role = role;
    }

    public int getUserId() { return userId; }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
}
