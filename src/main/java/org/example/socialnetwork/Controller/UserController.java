package org.example.socialnetwork.Controller;

import jakarta.transaction.SystemException;
import jakarta.validation.Valid;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Service.UserDetailsServiceImpl;
import org.example.socialnetwork.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<UserDTO> user = Optional.ofNullable(userService.findUserById(id));
        logger.info("Пользователь с id: {} найден: {}", id, user.get().getUserName());
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUserName(@PathVariable("username") String userName) {
        Optional<UserDTO> user = Optional.ofNullable(userService.findByUserName(userName));
        logger.info("Пользователь с именем: {} найде. ID: {}",userName,  user.get().getUserId());
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/profile")
    public String showProfileForm(Model model) {
        logger.info("Форма профиля отображается.");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = auth.getName();
        UserDTO user = userService.findByUserName(currentUserName);

        if (user != null) {
            model.addAttribute("user", user);
        } else {
            logger.warn("Пользователь не найден: {}", currentUserName);
            model.addAttribute("errorMessage", "Пользователь не найден.");
        }
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute UserDTO updatedUser) throws SystemException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = auth.getName();
        userService.updateUser(currentUserName, updatedUser);

        if (!currentUserName.equals(updatedUser.getUserName())) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(updatedUser.getUserName());
            Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
        logger.info("Данные о пользователе обновлены.");
        return "redirect:/users/profile";
    }
}
