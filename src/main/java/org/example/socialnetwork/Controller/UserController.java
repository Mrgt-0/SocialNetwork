package org.example.socialnetwork.Controller;

import jakarta.transaction.SystemException;
import jakarta.validation.Valid;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        Optional<User> user = Optional.ofNullable(userService.findUserById(id));
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Получение пользователя по имени пользователя
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUserName(@PathVariable("username") String userName) {
        Optional<User> user = userService.findByUserName(userName);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Шаблон для регистрации
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult, Model model) throws SystemException {
        if (bindingResult.hasErrors()) {
            return "register"; // Вернуться к форме, если есть ошибки
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("emailExists", "Email уже используется.");
            return "register"; // Вернуться к форме, если email уже существует
        }

        if (userService.findByUserName(user.getUserName()) != null){
            model.addAttribute("usernameExists", "Имя уже используется.");
            return "register";
        }

        userService.registerUser(user);
        return "redirect:/login"; // Перенаправление пользователя на страницу входа
    }

    @GetMapping("/profile")
    public String showProfileForm(Model model) {
        // Получите текущего пользователя (например, из session)
        Optional<User> user = userService.findByEmail("user@example.com"); // Пример получения пользователя
        model.addAttribute("user", user);
        return "profile"; // Шаблон для редактирования профиля
    }

    @PostMapping("/profile")
    public ResponseEntity<User> updateProfile(@PathVariable Integer id, @RequestBody User updatedUser) {
        try {
            User user = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(user);
        } catch (RuntimeException | SystemException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
