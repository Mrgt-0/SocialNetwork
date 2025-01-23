package org.example.socialnetwork.Service;

import jakarta.transaction.SystemException;
import jakarta.transaction.Transactional;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO findUserById(Long userId) {
        User user = userRepository.getById(userId);
        return convertToDTO(user);
    }

    public Optional<UserDTO> findUserByIdAsOptional(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(this::convertToDTO);
    }

    public UserDTO findByUserName(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + userName));
        return convertToDTO(user);
    }

    public Optional<UserDTO> findUserByUserNameAsOptional(String userName){
        Optional<User> user = userRepository.findByUserName(userName);
        return user.map(this::convertToDTO);
    }

    public UserDTO findByEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + email));
        return convertToDTO(user);
    }

    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public void updateUser(String userName, UserDTO updatedUser) throws SystemException {
        userRepository.findByUserName(userName)
                .map(user -> {
                    user.setUserName(updatedUser.getUserName());
                    user.setFirstName(updatedUser.getFirstName());
                    user.setLastName(updatedUser.getLastName());
                    user.setEmail(updatedUser.getEmail());
                    user.setProfilePicture(updatedUser.getProfilePicture());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    @Transactional
    public UserDTO registerUser(UserDTO userDTO) throws SystemException {
        logger.info("Регистрация пользователя с email: {}", userDTO.getEmail());

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent())
            throw new RuntimeException("Пользователь с таким email уже существует: " + userDTO.getEmail());

        if (userRepository.findByUserName(userDTO.getUserName()).isPresent()) {
            logger.error("Пользователь с таким именем пользователя уже существует.");
            return null;
        }
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);

        if (userDTO.getRole() == null || userDTO.getRole().isEmpty())
            userDTO.setRole(Collections.singleton("ADMIN"));

        User registredUser = convertToEntity(userDTO);
        registredUser = userRepository.save(registredUser);
        logger.info("Пользователь успешно зарегистрирован: {}", registredUser.getEmail());
        return convertToDTO(registredUser);
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
        logger.info("Пользователь с ID {} успешно удален.", userId);
    }

    public void changeUserRole(Long userId, Set<String> newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        user.setRole(newRole);
        userRepository.save(user);
        logger.info("Роль пользователя с ID {} изменена на {}", userId, newRole);
    }

    private UserDTO convertToDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setBirthdate(user.getBirthdate());
        userDTO.setProfilePicture(user.getProfilePicture());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    private User convertToEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setUserName(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setBirthdate(userDTO.getBirthdate());
        user.setProfilePicture(userDTO.getProfilePicture());
        user.setRole(userDTO.getRole());
        return user;
    }
}
