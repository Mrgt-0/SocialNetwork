package org.example.socialnetwork.Controller;

import org.example.socialnetwork.Config.MyUserDetails;
import org.example.socialnetwork.DTO.CommunityDTO;
import org.example.socialnetwork.DTO.CommunityMemberDTO;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.Community;
import org.example.socialnetwork.Model.CommunityMember;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Service.CommunityService;
import org.example.socialnetwork.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/communities")
public class CommunityController {
    @Autowired
    private CommunityService communityService;
    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(CommunityController.class);

    @PostMapping
    public ResponseEntity<String> createCommunity(@RequestParam("community_name") String communityName,
                                                  @RequestParam("description") String description, @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Проверка данных о сообществе. Название: {}, Описание: {}, Администратор: {}", communityName, description, userDetails.getUsername());
        Long adminId = ((MyUserDetails) userDetails).getUserId();

        UserDTO admin = userService.findUserByIdAsOptional(adminId)
                .orElseThrow(() -> {
                    logger.error("Администратор с ID {} не найден.", adminId);
                    return new RuntimeException("Администратор не найден");
                });

        CommunityDTO community = communityService.createCommunity(communityName, description, admin);
        logger.info("Сообщество успешно создано: {}", community.getCommunityName());
        return ResponseEntity.ok("Сообщество успешно создано.");
    }

    @GetMapping
    public ResponseEntity<List<CommunityDTO>> getAllCommunities() {
        logger.info("Получение списка всех сообществ.");
        List<CommunityDTO> communities = communityService.getAllCommunities();
        logger.info("Количество сообществ получено: {}", communities.size());
        return ResponseEntity.ok(communities);
    }

    @PostMapping("/{communityId}/join")
    public ResponseEntity<String>  joinCommunity(@PathVariable Long communityId, @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((MyUserDetails) userDetails).getUserId();
        UserDTO user = userService.findUserByIdAsOptional(userId)
                .orElseThrow(() -> {
                    logger.error("Пользователь с ID {} не найден.", userId);
                    return new RuntimeException("Пользователь не найден");
                });

        communityService.joinCommunity(communityId, user);
        logger.info("Пользователь {} успешно присоединился к сообществу с ID {}.", user.getUserName(), communityId);
        return ResponseEntity.ok("Вы успешно присоединились к сообществу!");
    }

    @PostMapping("/{communityId}/members")
    public ResponseEntity<CommunityMemberDTO> getMembers(@PathVariable Long communityId) {
        logger.info("Получение участников сообщества с ID: {}", communityId);
        List<CommunityMemberDTO> members = communityService.getMembers(communityId);
        logger.info("Количество участников сообщества с ID {}: {}", communityId, members.size());
        return ResponseEntity.ok((CommunityMemberDTO) members);
    }
}
