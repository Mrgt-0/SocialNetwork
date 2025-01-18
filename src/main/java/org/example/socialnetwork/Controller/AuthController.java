package org.example.socialnetwork.Controller;

import jakarta.transaction.SystemException;
import jakarta.validation.Valid;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Service.UserDetailsServiceImpl;
import org.example.socialnetwork.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

//    @GetMapping("/{id}")
//    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
//        Optional<User> user = Optional.ofNullable(userService.findUserById(id));
//        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Получение пользователя по имени пользователя
//    @GetMapping("/username/{username}")
//    public ResponseEntity<User> getUserByUserName(@PathVariable("username") String userName) {
//        Optional<User> user = userService.findByUserName(userName);
//        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Шаблон для регистрации
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) throws SystemException {
        if (bindingResult.hasErrors()) {
            logger.error("Ошибки валидации: " + bindingResult.getAllErrors());
            return "register"; // Вернуться к форме, если есть ошибки
        }

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("emailExists", "Email уже используется.");
            return "register"; // Вернуться к форме, если email уже существует
        }
        if (userService.findByUserName(user.getUserName()).isPresent()) {
            model.addAttribute("usernameExists", "Имя уже используется.");
            return "register";
        }

        userService.registerUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "Регистрация прошла успешно! Пожалуйста, войдите.");
        return "redirect:/auth/login"; // Перенаправление пользователя на страницу входа
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        logger.info("Пользователь открывает страницу логина.");
        model.addAttribute("user", new User());
        return "login"; // Шаблон для входа
    }
}
