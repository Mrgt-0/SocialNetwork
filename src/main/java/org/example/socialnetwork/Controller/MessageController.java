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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/messages")
    public String showMessages(@RequestParam(value = "recipient", required = false) String recipientUsername,
                               Model model,
                               Authentication authentication) {
        logger.info("Открытие формы чата.");
        if (recipientUsername == null || recipientUsername.isEmpty()) {
            model.addAttribute("error", "Параметр recipient не был передан.");
            logger.error("Параметр recipient не был передан.");
            return "error";
        }

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        UserDTO currentUser = myUserDetails.getUser();

        UserDTO recipient = userService.findByUserName(recipientUsername);

        if (recipient == null) {
            model.addAttribute("error", "Пользователь не найден.");
            logger.error("Пользователь не найден.");
            return "error";
        }
        logger.info("Пользователь найден.");
        List<MessageDTO> messages = messageService.getMessages(currentUser, recipient);
        model.addAttribute("messages", messages);
        model.addAttribute("recipient", recipient);

        return "messages";
    }

    @GetMapping("/selectRecipient")
    public String selectRecipient(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "selectRecipient";
    }

    @PostMapping("/send-message")
    public String sendMessage(@RequestParam("recipient") String recipientUsername,
                              @RequestParam("content") String content, Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        UserDTO currentUserDTO = myUserDetails.getUser();

        UserDTO recipientDTO = userService.findByUserName(recipientUsername);
        if (recipientDTO == null) {
            logger.error("Не удалось отправить сообщение. Получатель '{}' не найден.", recipientUsername);
            return "error";
        }
        messageService.sendMessage(currentUserDTO, recipientDTO, content);

        logger.info("Сообщение успешно отправлено от '{}' к '{}'.", currentUserDTO.getUserName(), recipientUsername);
        return "redirect:/messages?recipient=" + recipientUsername;
    }

    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable Long recipientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof MyUserDetails myUserDetails) {
            UserDTO currentUser = myUserDetails.getUser();
            UserDTO recipientUser = userService.findUserById((long) Math.toIntExact(recipientId));
            if (recipientUser == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

            List<MessageDTO> messages = messageService.getMessages(currentUser, recipientUser);
            return ResponseEntity.ok(messages);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
