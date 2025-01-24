package org.example.socialnetwork.Controller;

import org.example.socialnetwork.Service.CommunityService;
import org.example.socialnetwork.Service.PostService;
import org.example.socialnetwork.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommunityService communityService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @PostMapping("/change-role")
    public ResponseEntity<String> changeUserRole(@RequestParam("userId") Long userId,
                                                 @RequestParam("newRole") Set<String> newRole,
                                                 RedirectAttributes redirectAttributes) {
        userService.changeUserRole(userId, newRole);
        redirectAttributes.addFlashAttribute("success", "Роль пользователя изменена успешно!");
        logger.info("Роль пользователя с ID {} изменена на {}", userId, newRole);
        return ResponseEntity.ok("Роль пользователя изменена успешно!");
    }

    @PostMapping("/delete-user")
    public ResponseEntity<String> deleteUser(@RequestParam("userId") Long userId,
                                             RedirectAttributes redirectAttributes) {
        userService.deleteUserById(userId);
        redirectAttributes.addFlashAttribute("success", "Пользователь успешно удален!");
        logger.info("Пользователь с ID {} успешно удален.", userId);
        return ResponseEntity.ok("Пользователь успешно удален!");
    }

    @PostMapping("/delete-post")
    public ResponseEntity<String> deletePost(@RequestParam("postId") Long postId,
                                             RedirectAttributes redirectAttributes) {
        postService.deletePostById(postId);
        redirectAttributes.addFlashAttribute("success", "Пост успешно удален!");
        logger.info("Пост с ID {} успешно удален.", postId);
        return ResponseEntity.ok("Пост успешно удален!");
    }

    @PostMapping("/delete-community")
    public ResponseEntity<String> deleteCommunity(@RequestParam("communityId") Long communityId,
                                                  RedirectAttributes redirectAttributes) {
        communityService.deleteCommunity(communityId);
        redirectAttributes.addFlashAttribute("success", "Группа успешно удалена!");
        logger.info("Группа с ID {} успешно удалена.", communityId);
        return ResponseEntity.ok("Группа успешно удалена!");
    }
}