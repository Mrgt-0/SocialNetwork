package test.Service;

import org.example.socialnetwork.DTO.FriendDTO;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.Friend;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.FriendRepository;
import org.example.socialnetwork.Service.FriendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {
    @InjectMocks
    private FriendService friendService;

    @Mock
    private FriendRepository friendRepository;

    private UserDTO user;
    private UserDTO friend;
    private FriendDTO friendDTO;
    private Friend existingFriendship;

    @BeforeEach
    public void setUp() {
        user = new UserDTO();
        user.setUserId(1L);
        user.setUserName("user1");

        friend = new UserDTO();
        friend.setUserId(2L);
        friend.setUserName("user2");

        friendDTO = new FriendDTO();
        friendDTO.setFriend_id(1L);
        friendDTO.setUser(convertToEntity(user));
        friendDTO.setFriend(convertToEntity(friend));
        friendDTO.setCreated_at(LocalDateTime.now());

        existingFriendship = new Friend();
        existingFriendship.setFriend_id(1L);
        existingFriendship.setUser(convertToEntity(user));
        existingFriendship.setFriend(convertToEntity(friend));
        existingFriendship.setCreated_at(LocalDateTime.now());
    }

    @Test
    public void testAddFriend_ShouldAddFriendship() {
        when(friendRepository.findByUserAndFriend(any(), any())).thenReturn(null);
        friendService.addFriend(user, friend);
        verify(friendRepository, times(2)).save(any(Friend.class));
    }

    @Test
    public void testAddFriend_AlreadyFriends_ShouldThrowException() {
        when(friendRepository.findByUserAndFriend(any(), any())).thenReturn(existingFriendship);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendService.addFriend(user, friend);
        });
        assertEquals("Уже в друзьях", exception.getMessage());
    }

    @Test
    public void testGetFriends_ShouldReturnListOfFriends() {
        when(friendRepository.findByUser(any())).thenReturn(Arrays.asList(existingFriendship));
        List<FriendDTO> friends = friendService.getFriends(user);
        assertNotNull(friends);
        assertEquals(1, friends.size());
        assertEquals(friendDTO.getFriend_id(), friends.get(0).getFriend_id());
    }

    @Test
    public void testDeleteFriend_ShouldCallDeleteOnFriendRepository() {
        when(friendRepository.findByUserAndFriend(any(), any())).thenReturn(existingFriendship); // это может быть ненужным

        // Act
        friendService.deleteFriend(friendDTO);

        // Assert
        verify(friendRepository).delete(any(Friend.class));
    }

    @Test
    public void testDeleteFriend_NullFriend_ShouldNotThrowException() {
        assertDoesNotThrow(() -> {
            friendService.deleteFriend(null);
        });
        verify(friendRepository, never()).delete(any());
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
}
