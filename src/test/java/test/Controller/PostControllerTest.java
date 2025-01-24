package test.Controller;

import org.example.socialnetwork.Controller.PostController;
import org.example.socialnetwork.DTO.PostDTO;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.Post;
import org.example.socialnetwork.Model.User;
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
    void testShowAllPosts() {
        List<PostDTO> posts = new ArrayList<>();
        posts.add(new PostDTO());
        when(postService.getAllPosts()).thenReturn(posts);

        String viewName = String.valueOf(postController.showAllPosts());
        assertEquals("allPosts", viewName);
        verify(model).addAttribute("posts", posts);
        for (PostDTO post : posts) {
            assertNotNull(post.getText());
        }
    }

    @Test
    void testPublicationPostWithFile() throws IOException {
        PostDTO post = new PostDTO();
        post.setText("Test post");
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("test.png");

        String result = String.valueOf(postController.publicationPost(post, file, redirectAttributes));

        assertEquals("redirect:/posts/allPosts", result);
        verify(postService).publicationPost(post);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Пост опубликован успешно!");
        verify(file).transferTo(any(File.class));
    }

    @Test
    void testPublicationPostWithEmptyFile() {
        PostDTO post = new PostDTO();

        when(file.isEmpty()).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postController.publicationPost(post, file, redirectAttributes);
        });

        assertEquals("Загруженный файл пуст", exception.getMessage());
        verify(postService, never()).publicationPost(any());
    }
}