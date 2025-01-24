package org.example.socialnetwork.Service;

import jakarta.transaction.Transactional;
import org.example.socialnetwork.DTO.CommunityDTO;
import org.example.socialnetwork.DTO.CommunityMemberDTO;
import org.example.socialnetwork.DTO.UserDTO;
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
import java.util.stream.Collectors;

@Service
public class CommunityService {
    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private CommunityMemberRepository communityMemberRepository;

    private static final Logger logger = LoggerFactory.getLogger(CommunityService.class);

    @Transactional
    public CommunityDTO createCommunity(String communityName, String description, UserDTO admin) {
        logger.info("Создание сообщества: {}, Администратор: {}", communityName, admin.getUserName());
        CommunityDTO community = new CommunityDTO();
        community.setCommunityName(communityName);
        community.setDescription(description);
        community.setCreated_at(LocalDateTime.now());
        community.setAdmin(convertToEntity(admin));
        CommunityDTO savedCommunity = convertToDTO(communityRepository.save(convertToEntity(community)));
        logger.info("Сообщество успешно создано: {}", savedCommunity.getCommunityName());
        return savedCommunity;
    }

    public List<CommunityDTO> getAllCommunities() {
        List<Community> communities = communityRepository.findAll();
        List<CommunityDTO> communitiesDTO = communities.stream()
                        .map(this::convertToDTO)
                                .collect(Collectors.toList());
        logger.info("Количество сообществ: {}", communities.size());
        return communitiesDTO;
    }

    @Transactional
    public CommunityMemberDTO joinCommunity(Long communityId, UserDTO user) {
        logger.info("Пользователь {} пытается присоединиться к сообществу с ID: {}", user.getUserName(), communityId);
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> {
                    logger.error("Сообщество с ID {} не найдено.", communityId);
                    return new RuntimeException("Сообщество не найдено");
                });
        CommunityDTO communityDTO = convertToDTO(community);

        CommunityMemberDTO member = new CommunityMemberDTO();
        member.setCommunity(convertToEntity(communityDTO));
        member.setUser(convertToEntity(user));
        member.setJoined_at(LocalDateTime.now());

        CommunityMemberDTO savedMember = convertToDTO(communityMemberRepository.save(convertToEntity(member)));
        logger.info("Пользователь {} успешно присоединился к сообществу {}", user.getUserName(), community.getCommunityName());
        return savedMember;
    }

    public List<CommunityMemberDTO> getMembers(Long communityId) {
        logger.info("Получение участников сообщества с ID: {}", communityId);
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> {
                    logger.error("Сообщество с ID {} не найдено.", communityId);
                    return new RuntimeException("Сообщество не найдено");
                });
        CommunityDTO communityDTO = convertToDTO(community);
        List<CommunityMember> members = communityMemberRepository.findByCommunity(convertToEntity(communityDTO));
        List<CommunityMemberDTO> membersDTO = members.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        logger.info("Количество участников сообщества с ID {}: {}", communityId, membersDTO.size());
        return membersDTO;
    }

    public void deleteCommunity(Long communityId){
        communityRepository.deleteById(communityId);
        logger.info("Группа с id {} успешно удалены.", communityId);
    }

    private User convertToEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setUserName(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setBirthdate(userDTO.getBirthdate());
        user.setProfilePicture(userDTO.getProfilePicture());
        user.setRole(userDTO.getRole());
        return user;
    }

    private Community convertToEntity(CommunityDTO communityDTO) {
        if (communityDTO == null) {
            return null;
        }
        Community community = new Community();
        community.setId(communityDTO.getId());
        community.setCommunityName(communityDTO.getCommunityName());
        community.setDescription(communityDTO.getDescription());
        community.setCreated_at(communityDTO.getCreated_at());
        community.setAdmin(communityDTO.getAdmin());
        return community;
    }

    private CommunityMember convertToEntity(CommunityMemberDTO communityMemberDTO) {
        if (communityMemberDTO == null) {
            return null;
        }
        CommunityMember communityMember = new CommunityMember();
        communityMember.setId(communityMemberDTO.getId());
        communityMember.setCommunity(communityMemberDTO.getCommunity());
        communityMember.setUser(communityMemberDTO.getUser());
        communityMember.setJoined_at(communityMemberDTO.getJoined_at());
        return communityMember;
    }

    private CommunityMemberDTO convertToDTO(CommunityMember communityMember) {
        if (communityMember == null) {
            return null;
        }
        CommunityMemberDTO communityMemberDTO = new CommunityMemberDTO();
        communityMemberDTO.setId(communityMember.getId());
        communityMemberDTO.setCommunity(communityMember.getCommunity());
        communityMemberDTO.setUser(communityMember.getUser());
        communityMemberDTO.setJoined_at(communityMember.getJoined_at());
        return communityMemberDTO;
    }

    private CommunityDTO convertToDTO(Community community) {
        if (community == null) {
            return null;
        }
        CommunityDTO communityDTO = new CommunityDTO();
        communityDTO.setId(community.getId());
        communityDTO.setCommunityName(community.getCommunityName());
        communityDTO.setDescription(community.getDescription());
        communityDTO.setCreated_at(community.getCreated_at());
        communityDTO.setAdmin(community.getAdmin());
        return communityDTO;
    }
}
