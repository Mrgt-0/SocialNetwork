package org.example.socialnetwork.Controller;

import org.example.socialnetwork.Config.MyUserDetails;
import org.example.socialnetwork.DTO.MessageDTO;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.UserRepository;
import org.example.socialnetwork.Service.MessageService;
import org.example.socialnetwork.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/messages")
    public ResponseEntity<?> showMessages(@RequestParam(value = "recipient", required = false) String recipientUsername,
                               Authentication authentication) {
        logger.info("Открытие формы чата.");

        if (recipientUsername == null || recipientUsername.isEmpty()) {
            logger.error("Параметр recipient не был передан.");
            return ResponseEntity.badRequest().body("Параметр recipient не был передан."); // Возвращаем 400
        }
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        UserDTO currentUser = myUserDetails.getUser();
        UserDTO recipient = userService.findByUserName(recipientUsername);

        if (recipient == null) {
            logger.error("Пользователь '{}' не найден.", recipientUsername);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден.");
        }
        List<MessageDTO> messages = messageService.getMessages(currentUser, recipient);
        logger.info("Пользователь '{}' найден. Получение сообщений.", recipient.getUserName());
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/selectRecipient")
    public ResponseEntity<List<User>> selectRecipient() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/send-message")
    public ResponseEntity<String> sendMessage(@RequestParam("recipient") String recipientUsername,
                              @RequestParam("content") String content, Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        UserDTO currentUserDTO = myUserDetails.getUser();

        UserDTO recipientDTO = userService.findByUserName(recipientUsername);
        if (recipientDTO == null) {
            logger.error("Не удалось отправить сообщение. Получатель '{}' не найден.", recipientUsername);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Получатель не найден."); // Вернем 404
        }

        messageService.sendMessage(currentUserDTO, recipientDTO, content);
        logger.info("Сообщение '{}' успешно отправлено от '{}' к '{}'.", content, currentUserDTO.getUserName(), recipientUsername);
        return ResponseEntity.ok("Сообщение успешно отправлено.");
    }

    @GetMapping("/messages/{recipientId}")
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable Long recipientId, Authentication authentication) {
        if (authentication.getPrincipal() instanceof MyUserDetails myUserDetails) {
            UserDTO currentUser = myUserDetails.getUser();
            UserDTO recipientUser = userService.findUserById(recipientId);
            if (recipientUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            List<MessageDTO> messages = messageService.getMessages(currentUser, recipientUser);
            return ResponseEntity.ok(messages);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
