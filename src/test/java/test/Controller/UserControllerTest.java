package test.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import jakarta.transaction.SystemException;
import org.example.socialnetwork.Controller.UserController;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Service.UserDetailsServiceImpl;
import org.example.socialnetwork.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.util.Optional;


public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private Authentication authentication;

    @Mock
    private Model model;

    private User currentUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        currentUser = new User();
        currentUser.setUserName("currentUser");
        currentUser.setUserId(1L);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn(currentUser.getUserName());
    }

    @Test
    public void testShowProfileForm() {
        when(userService.findByUserName(currentUser.getUserName())).thenReturn(Optional.of(currentUser));

        String viewName = userController.showProfileForm(model);

        assertEquals("profile", viewName);
        verify(model).addAttribute("user", currentUser);
    }

    @Test
    public void testUpdateProfile() throws SystemException {
        User updatedUser = new User();
        updatedUser.setUserName("newUser");
        updatedUser.setUserId(1L);

        when(authentication.getName()).thenReturn(currentUser.getUserName());
        when(userDetailsService.loadUserByUsername(updatedUser.getUserName())).thenReturn(mock(UserDetails.class));

        String redirectViewName = userController.updateProfile(updatedUser);

        verify(userService).updateUser(currentUser.getUserName(), updatedUser);
        assertEquals("redirect:/users/profile", redirectViewName);
    }
}
