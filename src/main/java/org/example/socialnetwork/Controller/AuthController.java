package org.example.socialnetwork.Controller;

import jakarta.transaction.SystemException;
import jakarta.validation.Valid;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
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

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        logger.info("Пользователь открывает страницу логина.");
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public String login(@RequestParam String userName, @RequestParam String password, Model model) {
        logger.info("Имя пользователя: {}", userName);
        logger.info("Пароль: {}", password);

        if (userName == null || userName.isEmpty()) {
            logger.error("Имя пользователя не указано!");
            model.addAttribute("errorMessage", "Неверное имя пользователя или пароль.");
            return "login";
        }

        if (password == null || password.isEmpty()) {
            logger.error("Пароль не был предоставлен!");
            model.addAttribute("errorMessage", "Пароль не может быть пустым");
            return "login";
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        logger.info("Пользователь {} успешно вошел в систему.", userName);
        ResponseEntity.ok("Аутентификация успешна!");
        return "redirect:/users/profile";
    }
}
