package test.Controller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.example.socialnetwork.Config.MyUserDetails;
import org.example.socialnetwork.Controller.CommunityController;
import org.example.socialnetwork.Model.Community;
import org.example.socialnetwork.Model.CommunityMember;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Service.CommunityService;
import org.example.socialnetwork.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CommunityControllerTest {
    @InjectMocks
    private CommunityController communityController;

    @Mock
    private CommunityService communityService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    private MyUserDetails userDetails;
    private User admin;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        admin = new User();
        admin.setUserId(1L);
        admin.setUserName("Admin");
        userDetails = new MyUserDetails(admin);
    }

    @Test
    public void testCreateCommunity() {
        Community community = new Community();
        community.setId(1L);
        community.setCommunityName("Test Community");

        when(userService.findUserByIdAsOptional(1L)).thenReturn(Optional.of(admin));
        when(communityService.createCommunity(any(String.class), any(String.class), any(User.class))).thenReturn(community);

        String result = communityController.createCommunity("Test Community", "Test Description", userDetails);

        assertEquals("redirect:/communities", result);
        verify(userService, times(1)).findUserByIdAsOptional(1L);
        verify(communityService, times(1)).createCommunity("Test Community", "Test Description", admin);
    }

    @Test
    public void testGetAllCommunities() {
        when(communityService.getAllCommunities()).thenReturn(Collections.singletonList(new Community()));

        String result = communityController.getAllCommunities(model);

        assertEquals("communities", result);
        verify(model, times(1)).addAttribute("communities", Collections.singletonList(new Community()));
    }

    @Test
    public void testJoinCommunity() {
        Community community = new Community();
        community.setId(1L);
        community.setCommunityName("Test Community");

        when(userService.findUserByIdAsOptional(1L)).thenReturn(Optional.of(admin));
        when(communityService.joinCommunity(1L, admin)).thenReturn(new CommunityMember());

        String result = communityController.joinCommunity(1L, userDetails);

        assertEquals("redirect:/communities", result);
        verify(communityService, times(1)).joinCommunity(1L, admin);
    }

    @Test
    public void testGetMembers() {
        Community community = new Community();
        community.setId(1L);
        when(communityService.getMembers(1L)).thenReturn(Collections.singletonList(new CommunityMember()));

        List<CommunityMember> members = communityController.getMembers(1L);

        assertNotNull(members);
        assertEquals(1, members.size());
        verify(communityService, times(1)).getMembers(1L);
    }

    @Test
    public void testCreateCommunity_AdminNotFound() {
        when(userService.findUserByIdAsOptional(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            communityController.createCommunity("Test Community", "Test Description", userDetails);
        });

        assertEquals("Администратор не найден", exception.getMessage());
    }
}
