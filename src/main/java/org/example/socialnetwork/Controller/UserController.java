package org.example.socialnetwork.Controller;

import jakarta.transaction.SystemException;
import jakarta.validation.Valid;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        Optional<User> user = Optional.ofNullable(userService.findUserById(id));
        if (user.isPresent())
            return ResponseEntity.ok(user.get());
        else
            return ResponseEntity.notFound().build();
    }

    // Получение пользователя по имени пользователя
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUserName(@PathVariable("username") String userName) {
        Optional<User> user = userService.findByUserName(userName);
        if (user.isPresent())
            return ResponseEntity.ok(user.get());
         else
            return ResponseEntity.notFound().build();
    }

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
        return "redirect:/users/login"; // Перенаправление пользователя на страницу входа
    }

    @PostMapping("/login")
    public String login(@RequestParam(required = false) String userName, @RequestParam(required = false) String password, Model model) {
        logger.info("Пользователь пытается войти: {}", userName);
        logger.info("Метод login вызван");
        boolean isAuthenticated = userService.authenticateUser(userName, password);
        logger.info("Аутентификация прошла: {}", isAuthenticated);
        if (isAuthenticated) {
            // Вход успешный
            logger.info("Пользователь {} успешно вошел в систему.", userName);
            return "redirect:/users/profile"; // или перенаправьте на другую страницу
        } else {
            // Неверные учетные данные
            model.addAttribute("errorMessage", "Неверное имя пользователя или пароль.");
            logger.warn("Неудачная попытка входа для пользователя {}.", userName);
            return "login"; // возврат на страницу входа
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        logger.info("Пользователь открывает страницу логина.");
        model.addAttribute("user", new User());
        return "login"; // Шаблон для входа
    }

    @GetMapping("/profile")
    public String showProfileForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = auth.getName();
        Optional<User> user = userService.findByUserName(currentUserName); // Получаем текущего пользователя
        user.ifPresent(u -> model.addAttribute("user", u)); // Добавляем пользователя в модель
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User updatedUser) throws SystemException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = auth.getName(); // Получаем текущее имя пользователя
        userService.updateUser(currentUserName, updatedUser); // Обновляем пользователя на основе имени
        return "redirect:/profile";
    }
}
