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
import org.springframework.http.HttpStatus;
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

@RestController
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
        logger.info("Пользователь с именем: {} найден. ID: {}",userName,  user.get().getUserId());
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> showProfileForm() {
        logger.info("Отображение формы профиля.");
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO user = userService.findByUserName(currentUserName);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            logger.warn("Пользователь не найден: {}", currentUserName);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody UserDTO updatedUser) throws SystemException {
        logger.info("Открыта форма редактирования данных профиля.");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null || auth.getName() == null) {
            logger.warn("Клиент не аутентифицирован.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Пользователь не аутентифицирован.");
        }

        String currentUserName = auth.getName();
        UserDTO currentUser = userService.findByUserName(currentUserName);

        if (currentUser == null) {
            logger.warn("Текущий пользователь не найден: {}", currentUserName);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Пользователь не найден.");
        }

        logger.info("Обновление данных пользователя: {}", currentUserName);

        userService.updateUser(currentUserName, updatedUser);

        if (!currentUserName.equals(updatedUser.getUserName())) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(updatedUser.getUserName());
            Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
        logger.info("Данные о пользователе {} обновлены.", updatedUser.getUserName());
        return ResponseEntity.ok("Данные о пользователе обновлены.");
    }
}
