package test.Service;

import org.example.socialnetwork.DTO.FriendDTO;
import org.example.socialnetwork.DTO.PostDTO;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.Friend;
import org.example.socialnetwork.Model.Post;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.FriendRepository;
import org.example.socialnetwork.Service.FriendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FriendServiceTest {
    @InjectMocks
    private FriendService friendService;

    @Mock
    private FriendRepository friendRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddFriend() {
        UserDTO user = new UserDTO();
        UserDTO friend = new UserDTO();

        when(friendRepository.findByUserAndFriend(convertToEntity(user), convertToEntity(friend))).thenReturn(null);
        friendService.addFriend(user, friend);
        verify(friendRepository, times(2)).save(any(Friend.class));
    }

    @Test
    public void testAddFriend_AlreadyFriends() {
        UserDTO user = new UserDTO();
        UserDTO friend = new UserDTO();
        FriendDTO existingFriendship = new FriendDTO();
        when(friendRepository.findByUserAndFriend(convertToEntity(user), convertToEntity(friend))).thenReturn(convertToEntity(existingFriendship));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendService.addFriend(user, friend);
        });

        assertEquals("Уже в друзьях", exception.getMessage());
    }

    @Test
    public void testGetFriends() {
        User user = new User();
        user.setUserId(1L);

        Friend friendEntity1 = new Friend();
        friendEntity1.setFriend_id(1L);

        User friendUser1 = new User();
        friendUser1.setUserId(2L);
        friendEntity1.setFriend(friendUser1);

        Friend friendEntity2 = new Friend();
        friendEntity2.setFriend_id(2L);

        User friendUser2 = new User();
        friendUser2.setUserId(3L);
        friendEntity2.setFriend(friendUser2);

        List<Friend> friends = List.of(friendEntity1, friendEntity2);
        when(friendRepository.findByUser(user)).thenReturn(friends);
        List<FriendDTO> expectedFriends = friends.stream()
                .map(friend -> {
                    FriendDTO friendDTO = new FriendDTO();
                    friendDTO.setFriend_id(friend.getFriend_id());
                    friendDTO.setFriend_id(friend.getFriend().getUserId());
                    return friendDTO;
                })
                .collect(Collectors.toList());
        List<FriendDTO> result = friendService.getFriends(convertToDTO(user));
        assertEquals(expectedFriends, result);
    }

    @Test
    public void testDeleteFriend() {
        FriendDTO friend = new FriendDTO();
        when(friendRepository.findById(friend.getFriend_id())).thenReturn(Optional.of(convertToEntity(friend)));
        friendService.deleteFriend(friend);
        verify(friendRepository).delete(convertToEntity(friend));
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

    private UserDTO convertToDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setBirthdate(user.getBirthdate());
        userDTO.setProfilePicture(user.getProfilePicture());
        userDTO.setRole(user.getRole());
        return userDTO;
    }
}
