package org.example.socialnetwork.Controller;

import jakarta.transaction.SystemException;
import jakarta.validation.Valid;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.LoginResponse;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Service.UserDetailsServiceImpl;
import org.example.socialnetwork.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

//    @GetMapping("/register")
//    public String showRegistrationForm(Model model) {
//        model.addAttribute("user", new User());
//        return "register";
//    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDTO user) throws SystemException {
        logger.info("Попытка регистрации нового пользователя: {}", user.getUserName());

        try {
            userService.registerUser(user);
            return ResponseEntity.ok("Регистрация прошла успешно! Пожалуйста, войдите.");
        } catch (RuntimeException e) {
            logger.error("Ошибка при регистрации: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Ошибка при регистрации: " + e.getMessage());
        }
    }

//    @GetMapping("/login")
//    public String showLoginForm(Model model) {
//        logger.info("Пользователь открывает страницу логина.");
//        model.addAttribute("user", new User());
//        return "login";
//    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserDTO user) {
        logger.info("Пользователь пытается войти: {}", user.getUserName());

        try {
            Authentication authentication = authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            logger.info("Аутентифицированный пользователь: {}", userDetails.getUsername());
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Роль пользователя не найдена"))
                    .getAuthority();

            return ResponseEntity.ok(new LoginResponse(userDetails.getUsername(), role));
        } catch (BadCredentialsException e) {
            logger.error("Неправильное имя пользователя или пароль.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неправильное имя пользователя или пароль.");
        } catch (Exception e) {
            logger.error("Ошибка при логине: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка при входе.");
        }
    }
}
