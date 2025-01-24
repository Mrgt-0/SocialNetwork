package org.example.socialnetwork.Controller;

import org.example.socialnetwork.Config.MyUserDetails;
import org.example.socialnetwork.DTO.FriendDTO;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.Friend;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Service.FriendService;
import org.example.socialnetwork.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        UserDTO currentUser = userDetails.getUser();
        UserDTO friend = userService.findByUserName(userName);

        logger.info("Current user id: {}", currentUser.getUserId());
        logger.info("Friend user id: {}", friend.getUserId());
        try {
            friendService.addFriend(currentUser, friend);
            logger.info("Пользователь успешно добавлен в друзья!");
        } catch (RuntimeException e) {
            logger.error("Не удалось добавить пользователя в друзья.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Не удалось добавить пользователя в друзья.");
        }
        return ResponseEntity.ok("Пользователь успешно добавлен в друзья!");
    }
}
