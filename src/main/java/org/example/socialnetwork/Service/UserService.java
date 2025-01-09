package org.example.socialnetwork.Service;

import jakarta.transaction.SystemException;
import jakarta.transaction.Transactional;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    public User findUserById(int userId) {
        return userRepository.getById(userId);
    }

    public Optional<User> findByUserName(String userName) { return userRepository.findByUserName(userName); }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void saveUser(User user) throws SystemException {
        try{
            userRepository.save(user);
        }catch (Exception e){
            logger.error("Ошибка при сохранении пользователя.", e.getMessage());
        }
    }

    @Transactional
    public User updateUser(Integer id, User updatedUser) throws SystemException {
        return userRepository.findById(id)
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
    public User registerUser(User user) throws SystemException {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            logger.error("Пользователь с таким email уже существует.");
            return null; // или выбросьте исключение
        }

        if (userRepository.findByUserName(user.getUserName()).isPresent()) {
            logger.error("Пользователь с таким именем пользователя уже существует.");
            return null; // или выбросьте исключение
        }

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
//        }catch (Exception e){
//            logger.error("Произошла ошибка при регистрации пользователя.", e.getMessage());
//            return null;
//        }
    }

    @Transactional
    public void deleteUser(User user) {
        if (user != null) {
            userRepository.delete(user);
            logger.info("Пользователь успешно удален.");
        }
        else
            logger.error("Пользователь не найден.");
    }
}
