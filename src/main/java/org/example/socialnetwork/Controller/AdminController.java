package org.example.socialnetwork.Controller;

import org.example.socialnetwork.Service.CommunityService;
import org.example.socialnetwork.Service.PostService;
import org.example.socialnetwork.Service.UserDetailsServiceImpl;
import org.example.socialnetwork.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommunityService communityService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/index";
    }

    @PostMapping("/change-role")
    public String changeUserRole(@RequestParam("userId") Long userId,
                                 @RequestParam("newRole") Set<String> newRole,
                                 RedirectAttributes redirectAttributes) {
        try {
            userService.changeUserRole(userId, newRole);
            redirectAttributes.addFlashAttribute("success", "Роль пользователя изменена успешно!");
            logger.info("Роль пользователя с ID {} изменена на {}", userId, newRole);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Не удалось изменить роль пользователя.");
            logger.error("Ошибка при изменении роли пользователя с ID {}: {}", userId, e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam("userId") Long userId,
                             RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUserById(userId);
            redirectAttributes.addFlashAttribute("success", "Пользователь успешно удален!");
            logger.info("Пользователь с ID {} успешно удален.", userId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Не удалось удалить пользователя.");
            logger.error("Ошибка при удалении пользователя с ID {}: {}", userId, e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/delete-post")
    public String deletePost(@RequestParam("postId") Long postId,
                             RedirectAttributes redirectAttributes) {
        try {
            postService.deletePostById(postId);
            redirectAttributes.addFlashAttribute("success", "Пост успешно удален!");
            logger.info("Пост с ID {} успешно удален.", postId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Не удалось удалить пост.");
            logger.error("Ошибка при удалении поста с ID {}: {}", postId, e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/delete-community")
    public String deleteGroup(@RequestParam("communityId") Long communityId,
                              RedirectAttributes redirectAttributes) {
        try {
            communityService.deleteCommunity(communityId);
            redirectAttributes.addFlashAttribute("success", "Группа успешно удалена!");
            logger.info("Группа с ID {} успешно удалена.", communityId);
        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("error", "Не удалось удалить группу.");
            logger.error("Ошибка при удалении группы с ID {}: {}", communityId, e.getMessage());
        }
        return "redirect:/admin";
    }
}
