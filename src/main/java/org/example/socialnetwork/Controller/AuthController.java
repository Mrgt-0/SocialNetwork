package org.example.socialnetwork.Controller;

import jakarta.transaction.SystemException;
import jakarta.validation.Valid;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDTO user) {
        logger.info("Попытка регистрации нового пользователя: {}", user.getUserName());
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("Регистрация прошла успешно! Пожалуйста, войдите.");
        } catch (SystemException e) {
            logger.error("Ошибка при регистрации: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при регистрации: " + e.getMessage());
        }
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
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

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        logger.info("Пользователь {} успешно вошел в систему.", userName);
        return ResponseEntity.ok("Аутентификация успешна!");
    }
}
