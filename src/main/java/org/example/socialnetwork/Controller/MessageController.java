package org.example.socialnetwork.Controller;

import org.example.socialnetwork.Config.MyUserDetails;
import org.example.socialnetwork.Model.Message;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
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
            return "error"; // Возврат на страницу ошибки
        }

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        User currentUser = myUserDetails.getUser();

        User recipient = userService.findByUserName(recipientUsername);

        if (recipient == null) {
            model.addAttribute("error", "Пользователь не найден.");
            logger.error("Пользователь не найден.");
            return "error"; // Шаблон для ошибок или возврат на предыдущую страницу
        }
        logger.info("Пользователь найден.");
        // Получаем сообщения между текущим пользователем и получателем
        List<Message> messages = messageService.getMessages(currentUser, recipient);
        model.addAttribute("messages", messages);
        model.addAttribute("recipient", recipient); // Устанавливаем получателя в модель

        return "messages";
    }

    @GetMapping("/selectRecipient")
    public String selectRecipient(Model model) {
        // Здесь вы можете получить список пользователей
        List<User> users = userRepository.findAll(); // ваш метод получения всех пользователей
        model.addAttribute("users", users);
        return "selectRecipient"; // Тут должен быть ваш шаблон выбора пользователя
    }

    @PostMapping("/send-message")
    public String sendMessage(@RequestParam("recipient") String recipientUsername,
                              @RequestParam("content") String content, Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        User currentUser = myUserDetails.getUser();

        User recipient = userService.findByUserName(recipientUsername);

        if (recipient != null) {
            Message message = new Message(currentUser, recipient, LocalDateTime.now(), LocalDateTime.now(), content);
            messageService.saveMessage(message); // Сохраняем сообщение

            return "redirect:/messages?recipient=" + recipientUsername; // Перенаправление на чат
        } else {
            return "error"; // Обработка ошибки
        }
    }

    public ResponseEntity<List<Message>> getMessages(@PathVariable Long recipientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof MyUserDetails myUserDetails) {
            User currentUser = myUserDetails.getUser();
            User recipientUser = userService.findUserById((long) Math.toIntExact(recipientId));
            if (recipientUser == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

            List<Message> messages = messageService.getMessages(currentUser, recipientUser);
            return ResponseEntity.ok(messages);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
