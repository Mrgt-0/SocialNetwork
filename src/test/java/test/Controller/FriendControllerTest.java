package test.Controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.example.socialnetwork.Config.MyUserDetails;
import org.example.socialnetwork.Controller.FriendController;
import org.example.socialnetwork.Model.Friend;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Service.FriendService;
import org.example.socialnetwork.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import java.util.List;


public class FriendControllerTest {
    @InjectMocks
    private FriendController friendController;

    @Mock
    private FriendService friendService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private MyUserDetails myUserDetails;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(friendController).build();
    }

    @Test
    public void testShowFriends() throws Exception {
        User user = new User();
        when(authentication.getPrincipal()).thenReturn(myUserDetails);
        when(myUserDetails.getUser()).thenReturn(user);

        List<Friend> friends = Collections.emptyList();
        when(friendService.getFriends(user)).thenReturn(friends);

        mockMvc.perform(get("/friends").principal(() -> "username"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("friends", friends))
                .andExpect(view().name("friends"));
    }

    @Test
    public void testAddFriend() throws Exception {
        User currentUser = new User();
        User friend = new User();
        String userName = "friendUsername";

        when(authentication.getPrincipal()).thenReturn(myUserDetails);
        when(myUserDetails.getUser()).thenReturn(currentUser);
        when(userService.findByUserName(userName)).thenReturn(friend);

        mockMvc.perform(post("/friends/add")
                        .param("userName", userName)
                        .principal(() -> "username"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("friends"))
                .andExpect(view().name("friends"));

        verify(friendService).addFriend(currentUser, friend);
    }
}
