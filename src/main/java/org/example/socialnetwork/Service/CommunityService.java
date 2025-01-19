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
        Community community = new Community();
        community.setCommunityName(communityName);
        community.setDescription(description);
        community.setCreated_at(LocalDateTime.now());
        community.setAdmin(admin);
        return communityRepository.save(community);
    }

    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    @Transactional
    public CommunityMember joinCommunity(Long communityId, User user) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Сообщество не найдено"));

        CommunityMember member = new CommunityMember();
        member.setCommunity(community);
        member.setUser(user);
        member.setJoined_at(LocalDateTime.now());
        return communityMemberRepository.save(member);
    }

    public List<CommunityMember> getMembers(Long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Сообщество не найдено"));
        return communityMemberRepository.findByCommunity(community);
    }
}
