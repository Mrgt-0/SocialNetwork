package org.example.socialnetwork.Controller;

import org.example.socialnetwork.Config.MyUserDetails;
import org.example.socialnetwork.DTO.FriendDTO;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Service.FriendService;
import org.example.socialnetwork.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController {
    @Autowired
    private FriendService friendService;
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(FriendController.class);

    @GetMapping
    public ResponseEntity<List<FriendDTO>> showFriends(Authentication authentication) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        UserDTO currentUser = userDetails.getUser();
        List<FriendDTO> friends = friendService.getFriends(currentUser);
        return ResponseEntity.ok(friends);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addFriend(@RequestParam String userName, Authentication authentication) {
        logger.info("Запрос на добавление в друзья для пользователя: {}", userName); // Логируем входящие данные
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        UserDTO currentUser = userDetails.getUser();

        UserDTO friend = userService.findByUserName(userName);

        if (friend == null) {
            logger.warn("Пользователь не найден: {}", userName);
            return ResponseEntity.badRequest().body("Ошибка: Пользователь не найден: " + userName);
        }

        logger.info("Current user id: {}", currentUser.getUserId());
        logger.info("Friend user id: {}", friend.getUserId());

        friendService.addFriend(currentUser, friend);
        logger.info("Пользователь {} успешно добавлен в друзья!", friend.getUserName());

        return ResponseEntity.ok("Пользователь успешно добавлен в друзья!");
    }
}
