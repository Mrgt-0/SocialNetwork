package test.Controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.example.socialnetwork.Controller.AdminController;
import org.example.socialnetwork.Service.CommunityService;
import org.example.socialnetwork.Service.PostService;
import org.example.socialnetwork.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Collections;
import java.util.Set;

public class AdminControllerTest {
    @InjectMocks
    private AdminController adminController;

    @Mock
    private UserService userService;

    @Mock
    private PostService postService;

    @Mock
    private CommunityService communityService;

    @Mock
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализируем моки
    }

    @Test
    public void testChangeUserRole() {
        Long userId = 1L;
        Set<String> newRole = Collections.singleton("ADMIN");
        ResponseEntity<String> response = adminController.changeUserRole(userId, newRole, redirectAttributes);
        verify(userService, times(1)).changeUserRole(userId, newRole);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("success"), messageCaptor.capture());
        assertEquals(ResponseEntity.ok("Роль пользователя изменена успешно!"), response);
        assertEquals("Роль пользователя изменена успешно!", messageCaptor.getValue());
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;
        ResponseEntity<String> response = adminController.deleteUser(userId, redirectAttributes);
        verify(userService, times(1)).deleteUserById(userId);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("success"), messageCaptor.capture());
        assertEquals(ResponseEntity.ok("Пользователь успешно удален!"), response);
        assertEquals("Пользователь успешно удален!", messageCaptor.getValue());
    }

    @Test
    public void testDeletePost() {
        Long postId = 1L;
        ResponseEntity<String> response = adminController.deletePost(postId, redirectAttributes);
        verify(postService, times(1)).deletePostById(postId);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("success"), messageCaptor.capture());
        assertEquals(ResponseEntity.ok("Пост успешно удален!"), response);
        assertEquals("Пост успешно удален!", messageCaptor.getValue());
    }

    @Test
    public void testDeleteCommunity() {
        Long communityId = 1L;
        ResponseEntity<String> response = adminController.deleteCommunity(communityId, redirectAttributes);
        verify(communityService, times(1)).deleteCommunity(communityId);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("success"), messageCaptor.capture());
        assertEquals(ResponseEntity.ok("Группа успешно удалена!"), response);
        assertEquals("Группа успешно удалена!", messageCaptor.getValue());
    }
}
