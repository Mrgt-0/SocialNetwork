package org.example.socialnetwork.Service;

import jakarta.transaction.Transactional;
import org.example.socialnetwork.Model.Community;
import org.example.socialnetwork.Model.CommunityMember;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.CommunityMemberRepository;
import org.example.socialnetwork.Repository.CommunityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommunityService {
    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private CommunityMemberRepository communityMemberRepository;

    private static final Logger logger = LoggerFactory.getLogger(CommunityService.class);

    @Transactional
    public Community createCommunity(String communityName, String description, User admin) {
        logger.info("Создание сообщества: {}, Администратор: {}", communityName, admin.getUserName());
        Community community = new Community();
        community.setCommunityName(communityName);
        community.setDescription(description);
        community.setCreated_at(LocalDateTime.now());
        community.setAdmin(admin);
        Community savedCommunity = communityRepository.save(community);
        logger.info("Сообщество успешно создано: {}", savedCommunity.getCommunityName());
        return communityRepository.save(community);
    }

    public List<Community> getAllCommunities() {
        List<Community> communities = communityRepository.findAll();
        logger.info("Количество сообществ: {}", communities.size());
        return communities;
    }

    @Transactional
    public CommunityMember joinCommunity(Long communityId, User user) {
        logger.info("Пользователь {} пытается присоединиться к сообществу с ID: {}", user.getUserName(), communityId);
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> {
                    logger.error("Сообщество с ID {} не найдено.", communityId);
                    return new RuntimeException("Сообщество не найдено");
                });

        CommunityMember member = new CommunityMember();
        member.setCommunity(community);
        member.setUser(user);
        member.setJoined_at(LocalDateTime.now());

        CommunityMember savedMember = communityMemberRepository.save(member);
        logger.info("Пользователь {} успешно присоединился к сообществу {}", user.getUserName(), community.getCommunityName());
        return savedMember;
    }

    public List<CommunityMember> getMembers(Long communityId) {
        logger.info("Получение участников сообщества с ID: {}", communityId);
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> {
                    logger.error("Сообщество с ID {} не найдено.", communityId);
                    return new RuntimeException("Сообщество не найдено");
                });
        List<CommunityMember> members = communityMemberRepository.findByCommunity(community);
        logger.info("Количество участников сообщества с ID {}: {}", communityId, members.size());
        return members;
    }
}
