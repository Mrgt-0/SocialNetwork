package test.Controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.example.socialnetwork.Config.MyUserDetails;
import org.example.socialnetwork.Controller.FriendController;
import org.example.socialnetwork.DTO.FriendDTO;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Service.FriendService;
import org.example.socialnetwork.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FriendControllerTest {
    @InjectMocks
    private FriendController friendController;

    @Mock
    private FriendService friendService;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private MyUserDetails userDetails;

    private UserDTO currentUser;

    @BeforeEach
    public void setup() {
        currentUser = new UserDTO();
        currentUser.setUserId(1L);
        currentUser.setUserName("CurrentUser");

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUser()).thenReturn(currentUser);
    }

    @Test
    public void testShowFriends() {
        FriendDTO friend1 = new FriendDTO();
        FriendDTO friend2 = new FriendDTO();
        when(friendService.getFriends(currentUser)).thenReturn(Arrays.asList(friend1, friend2));
        ResponseEntity<List<FriendDTO>> response = friendController.showFriends(authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(friendService, times(1)).getFriends(currentUser);
    }

    @Test
    public void testAddFriend() {
        String friendUserName = "FriendUser";
        UserDTO friendUser = new UserDTO();
        friendUser.setUserId(2L);
        friendUser.setUserName(friendUserName);
        when(userService.findByUserName(friendUserName)).thenReturn(friendUser);
        ResponseEntity<String> response = friendController.addFriend(friendUserName, authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Пользователь успешно добавлен в друзья!", response.getBody());
        verify(friendService, times(1)).addFriend(currentUser, friendUser);
        verify(userService, times(1)).findByUserName(friendUserName);
    }
}
