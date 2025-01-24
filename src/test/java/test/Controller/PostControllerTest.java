package test.Controller;
import org.example.socialnetwork.Controller.PostController;
import org.example.socialnetwork.DTO.PostDTO;
import org.example.socialnetwork.Service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PostControllerTest {
    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    @Mock
    private RedirectAttributes redirectAttributes;

    private PostDTO post;
    private String postText;
    private String imageUrl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        postText = "Это тестовый пост.";
        imageUrl = "http://example.com/testImage.jpg";
        PostDTO post = new PostDTO();
        post.setText(postText);
        post.setImage(imageUrl);
        post.setUpdated_at(LocalDateTime.now());
    }

    @Test
    public void testPublicationPost() {
        ResponseEntity<String> response = postController.publicationPost(postText, imageUrl, redirectAttributes);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Пост опубликован успешно!", response.getBody());
        PostDTO expectedPost = new PostDTO(postText, imageUrl);
        verify(postService, times(1)).publicationPost(expectedPost);
        verify(redirectAttributes, times(1)).addFlashAttribute("successMessage", "Пост опубликован успешно!");
    }

    @Test
    public void testShowAllPosts() {
        PostDTO post1 = new PostDTO();
        post1.setText("Первый пост");
        PostDTO post2 = new PostDTO();
        post2.setText("Второй пост");
        when(postService.getAllPosts()).thenReturn(Arrays.asList(post1, post2));
        ResponseEntity<List<PostDTO>> response = postController.showAllPosts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(postService, times(1)).getAllPosts();
    }
}