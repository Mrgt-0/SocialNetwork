package org.example.socialnetwork.Service;

import jakarta.transaction.Transactional;
import org.example.socialnetwork.DTO.FriendDTO;
import org.example.socialnetwork.DTO.PostDTO;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.Friend;
import org.example.socialnetwork.Model.Post;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.FriendRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {
    @Autowired
    private FriendRepository friendRepository;

    private static final Logger logger = LoggerFactory.getLogger(FriendService.class);

    @Transactional
    public void addFriend(UserDTO user, UserDTO friend) {
        if (friendRepository.findByUserAndFriend(convertToEntity(user), convertToEntity(friend)) != null)
            throw new RuntimeException("Уже в друзьях");

        FriendDTO friendship1 = new FriendDTO();
        friendship1.setUser(convertToEntity(user));
        friendship1.setFriend(convertToEntity(friend));
        friendship1.setCreated_at(LocalDateTime.now());
        friendRepository.save(convertToEntity(friendship1));

        FriendDTO friendship2 = new FriendDTO();
        friendship2.setUser(convertToEntity(friend));
        friendship2.setFriend(convertToEntity(user));
        friendship2.setCreated_at(LocalDateTime.now());
        friendRepository.save(convertToEntity(friendship2));
    }

    public List<FriendDTO> getFriends(UserDTO user) {
        List<Friend> friends = friendRepository.findByUser(convertToEntity(user));
        List<FriendDTO> friendDTOs = friends.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return friendDTOs;
    }

    @Transactional
    public void deleteFriend(FriendDTO friend){
        if(friend!=null){
            friendRepository.delete(convertToEntity(friend));
            logger.info("Пользователь {} успешно удален.", friend.getUser().getUserName());
        }else
            logger.error("Пользователь {} не найден.", friend.getUser().getUserName());
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

    private Friend convertToEntity(FriendDTO friendDTO) {
        if (friendDTO == null) {
            return null;
        }
        Friend friend = new Friend();
        friend.setFriend_id(friendDTO.getFriend_id());
        friend.setUser(friendDTO.getUser());
        friend.setFriend(friendDTO.getFriend());
        friend.setCreated_at(friendDTO.getCreated_at());
        return friend;
    }

    private FriendDTO convertToDTO(Friend friend) {
        if (friend == null) {
            return null;
        }
        FriendDTO friendDTO = new FriendDTO();
        friendDTO.setFriend_id(friend.getFriend_id());
        friendDTO.setUser(friend.getUser());
        friendDTO.setFriend(friend.getFriend());
        friendDTO.setCreated_at(friend.getCreated_at());
        return friendDTO;
    }
}
