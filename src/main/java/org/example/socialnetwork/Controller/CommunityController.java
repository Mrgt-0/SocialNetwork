package org.example.socialnetwork.Controller;

import org.example.socialnetwork.Model.Community;
import org.example.socialnetwork.Model.CommunityMember;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Service.CommunityService;
import org.example.socialnetwork.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/communities")
public class CommunityController {
    @Autowired
    private CommunityService communityService;
    @Autowired
    private UserService userService;

    @PostMapping
    public String createCommunity(@RequestParam String name, @RequestParam String description, @RequestParam Long adminId) {
        User admin = userService.findUserByIdAsOptional(adminId)
                .orElseThrow(() -> new RuntimeException("Администратор не найден"));
        Community community = communityService.createCommunity(name, description, admin);
        return "Сообщество создано: " + community.getCommunityName();
    }

    @GetMapping
    public List<Community> getAllCommunities() {
        return communityService.getAllCommunities();
    }

    @PostMapping("/{communityId}/join")
    public String joinCommunity(@PathVariable Long communityId, @RequestParam Long userId) {
        User user = userService.findUserByIdAsOptional(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден")); // Используйте метод сервиса для поиска пользователя
        CommunityMember member = communityService.joinCommunity(communityId, user);
        return "Вы успешно присоединились к сообществу!";
    }

    @GetMapping("/{communityId}/members")
    public List<CommunityMember> getMembers(@PathVariable Long communityId) {
        return communityService.getMembers(communityId);
    }
}
