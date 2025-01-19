package org.example.socialnetwork.Controller;

import org.example.socialnetwork.Config.MyUserDetails;
import org.example.socialnetwork.Model.Friend;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Service.FriendService;
import org.example.socialnetwork.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/friends")
public class FriendController {
    @Autowired
    private FriendService friendService;
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(FriendController.class);

    @GetMapping
    public String showFriends(Model model, Authentication authentication) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User currentUser = userDetails.getUser();
        List<Friend> friends = friendService.getFriends(currentUser);
        model.addAttribute("friends", friends);
        return "friends";
    }

    @PostMapping("/add")
    public String addFriend(@RequestParam String userName, Model model, Authentication authentication) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        User currentUser = userDetails.getUser();
        User friend = userService.findByUserName(userName);

        logger.info("Current user id: {}", currentUser.getUserId());
        logger.info("Friend user id: {}", friend.getUserId());
        try {
            friendService.addFriend(currentUser, friend);
            model.addAttribute("successMessage", "Друг добавлен успешно!");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        List<Friend> friends = friendService.getFriends(currentUser);
        model.addAttribute("friends", friends);

        return "friends";
    }
}
