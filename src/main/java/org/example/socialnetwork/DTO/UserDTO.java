package org.example.socialnetwork.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

public class UserDTO {
    private Long userId;
    @NotEmpty(message = "Имя пользователя не должно быть пустым")
    private String userName;
    private String firstName;
    private String lastName;

    @NotEmpty(message = "Пароль не должен быть пустым")
    @Size(min = 8, message = "Пароль должен содержать как минимум 8 символов")
    private String password;

    @NotEmpty(message = "Email не должен быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    private LocalDate birthdate;

    private String profilePicture;
    private Set<String> role;

    public UserDTO() {}

    public UserDTO(String userName, String email, String firstName, String lastName){
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
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
