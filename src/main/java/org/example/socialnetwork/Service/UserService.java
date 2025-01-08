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

import java.beans.Transient;
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

    public User findUser(int userId) {
        return userRepository.getById(userId);
    }

    public void saveUser(User user) throws SystemException {
        try{
            userRepository.save(user);
        }catch (Exception e){
            logger.error("Ошибка при сохранении пользователя.", e.getMessage());
        }

    }

    public User findByEmail(String emain){   //ПЕРЕДЕЛАТЬ!!!
        User user=new User();
        return user;
    }

    @Transactional
    public User updateUser(User user) throws SystemException {
        try {
            logger.info("Информация о пользователи обновлена.");
            return userRepository.save(user);
        }catch (Exception e){
            logger.error("Ошибка при обновлении данных пользователя.", e.getMessage());
            return null;
        }
    }

    @Transactional
    public User registerUser(User user) throws SystemException { //логирование
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
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
