package org.example.socialnetwork.Controller;

import jakarta.transaction.SystemException;
import jakarta.validation.Valid;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

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

        userService.registerUser(user);
        return "redirect:/login"; // Перенаправление пользователя на страницу входа
    }

    @GetMapping("/profile")
    public String showProfileForm(Model model) {
        // Получите текущего пользователя (например, из session)
        User user = userService.findByEmail("user@example.com"); // Пример получения пользователя
        model.addAttribute("user", user);
        return "profile"; // Шаблон для редактирования профиля
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("user") User user,
                                BindingResult bindingResult) throws SystemException {
        if (bindingResult.hasErrors()) {
            return "profile"; // Вернуться к форме, если есть ошибки
        }

        userService.updateUser(user);
        return "redirect:/profile"; // Перенаправление на страницу профиля
    }
}
