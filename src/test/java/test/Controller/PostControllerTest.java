package test.Controller;

import org.example.socialnetwork.Controller.PostController;
import org.example.socialnetwork.Model.Post;
import org.example.socialnetwork.Service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostControllerTest {
    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private MultipartFile file;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowPostCreateForm() {
        String viewName = postController.showPostCreateForm(model);
        assertEquals("createPost", viewName);
        verify(model).addAttribute("post", new Post());
    }

    @Test
    void testShowAllPosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post());
        when(postService.getAllPosts()).thenReturn(posts);

        String viewName = postController.showAllPosts(model);
        assertEquals("allPosts", viewName);
        verify(model).addAttribute("posts", posts);
        for (Post post : posts) {
            assertNotNull(post.getText());
        }
    }

    @Test
    void testPublicationPostWithFile() throws IOException {
        Post post = new Post();
        post.setText("Test post");
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("test.png");

        String result = postController.publicationPost(post, bindingResult, file, redirectAttributes);

        assertEquals("redirect:/posts/allPosts", result);
        verify(postService).publicationPost(post);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Пост опубликован успешно!");
        verify(file).transferTo(any(File.class));
    }

    @Test
    void testPublicationPostWithEmptyFile() {
        Post post = new Post();

        when(file.isEmpty()).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postController.publicationPost(post, bindingResult, file, redirectAttributes);
        });

        assertEquals("Загруженный файл пуст", exception.getMessage());
        verify(postService, never()).publicationPost(any());
    }
}