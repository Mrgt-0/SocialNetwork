package org.example.socialnetwork.Controller;

import jakarta.transaction.SystemException;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Service.UserDetailsServiceImpl;
import org.example.socialnetwork.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        UserDTO user = userService.findUserById(id);
        if (user != null) {
            logger.info("Пользователь с id: {} найден: {}", id, user.getUserName());
            return ResponseEntity.ok(user);
        } else {
            logger.warn("Пользователь с id: {} не найден.", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUserName(@PathVariable("username") String userName) {
        Optional<UserDTO> user = Optional.ofNullable(userService.findByUserName(userName));
        logger.info("Пользователь с именем: {} найден. ID: {}",userName,  user.get().getUserId());
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/profile")
    public String showProfileForm(Model model) {
        logger.info("Отображение формы профиля.");
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
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
    public String updateProfile(@RequestParam("userName") String userName,
                                                @RequestParam("email") String email,
                                                @RequestParam("firstName") String firstName,
                                                @RequestParam("lastName") String lastName) throws SystemException {
        logger.info("Открыта форма редактирования данных профиля.");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null) {
            logger.warn("Клиент не аутентифицирован.");
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Пользователь не аутентифицирован.");
        }

        String currentUserName = auth.getName();
        UserDTO currentUser = userService.findByUserName(currentUserName);

        if (currentUser == null) {
            logger.warn("Текущий пользователь не найден: {}", currentUserName);
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Пользователь не найден.");
        }
        UserDTO updatedUser = new UserDTO(userName, email, firstName, lastName);
        logger.info("Обновление данных пользователя: {}", currentUserName);

        userService.updateUser(currentUserName, updatedUser);

        if (!currentUserName.equals(updatedUser.getUserName())) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(updatedUser.getUserName());
            Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
        logger.info("Данные о пользователе {} обновлены.", updatedUser.getUserName());
        return "redirect:/users/profile";
    }
}
