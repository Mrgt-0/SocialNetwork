package org.example.socialnetwork.Controller;

import jakarta.transaction.SystemException;
import jakarta.validation.Valid;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDTO user) throws SystemException {
        logger.info("Попытка регистрации нового пользователя: {}", user.getUserName());

        try {
            userService.registerUser(user);
            return ResponseEntity.ok("Регистрация прошла успешно! Пожалуйста, войдите.");
        } catch (SystemException e) {
            logger.error("Ошибка при регистрации: {}", e.getMessage());
            throw new RuntimeException("Ошибка при регистрации: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String userName, @RequestParam String password) {
        logger.info("Имя пользователя: {}", userName);
        logger.info("Пароль: {}", password);

        if (userName == null || userName.isEmpty()) {
            logger.error("Имя пользователя не указано!");
            return ResponseEntity.badRequest().body("Имя пользователя не может быть пустым");
        }

        if (password == null || password.isEmpty()) {
            logger.error("Пароль не был предоставлен!");
            return ResponseEntity.badRequest().body("Пароль не может быть пустым");
        }

        // Аутентификация пользователя
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        logger.info("Пользователь {} успешно вошел в систему.", userName);
        return ResponseEntity.ok("Аутентификация успешна!");
    }
}
