package test.Service;

import org.example.socialnetwork.Model.Friend;
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
        User user = new User();
        User friend = new User();

        when(friendRepository.findByUserAndFriend(user, friend)).thenReturn(null);
        friendService.addFriend(user, friend);
        verify(friendRepository, times(2)).save(any(Friend.class));
    }

    @Test
    public void testAddFriend_AlreadyFriends() {
        User user = new User();
        User friend = new User();
        Friend existingFriendship = new Friend();
        when(friendRepository.findByUserAndFriend(user, friend)).thenReturn(existingFriendship);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendService.addFriend(user, friend);
        });

        assertEquals("Уже в друзьях", exception.getMessage());
    }

    @Test
    public void testGetFriends() {
        User user = new User();
        List<Friend> friends = new ArrayList<>();
        when(friendRepository.findByUser(user)).thenReturn(friends);
        List<Friend> result = friendService.getFriends(user);
        assertEquals(friends, result);
    }

    @Test
    public void testDeleteFriend() {
        Friend friend = new Friend();
        when(friendRepository.findById(friend.getFriend_id())).thenReturn(Optional.of(friend));
        friendService.deleteFriend(friend);
        verify(friendRepository).delete(friend);
    }
}
