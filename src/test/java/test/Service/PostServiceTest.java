package test.Service;

import org.example.socialnetwork.Model.Post;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.PostRepository;
import org.example.socialnetwork.Service.PostService;
import org.example.socialnetwork.Service.UserService;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PostServiceTest {
    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testSavePost() {
        Post post = new Post();
        post.setText("Test post");

        postService.savePost(post);

        verify(postRepository).save(post);
    }

    @Test
    void testPublicationPostWhenUserIsAuthenticated() {
        Post post = new Post();
        post.setText("Test post");

        User currentUser = new User();
        currentUser.setUserName("testUser");

        when(authentication.isAuthenticated()).thenReturn(true);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userService.findByUserName("testUser")).thenReturn(currentUser);
        when(postRepository.save(post)).thenReturn(post);

        Post publishedPost = postService.publicationPost(post);

        assertNotNull(publishedPost);
        assertEquals("testUser", publishedPost.getUser().getUserName());
        verify(postRepository).save(post);
    }

    @Test
    void testPublicationPostWhenUserIsNotAuthenticated() {
        Post post = new Post();

        when(authentication.isAuthenticated()).thenReturn(false);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            postService.publicationPost(post);
        });

        assertEquals("Пользователь не аутентифицирован", thrown.getMessage());
    }

    @Test
    void testGetAllPosts() {
        Post post = new Post();
        post.setText("Test post");
        when(postRepository.findAll()).thenReturn(Collections.singletonList(post));

        List<Post> posts = postService.getAllPosts();

        assertEquals(1, posts.size());
        assertEquals("Test post", posts.get(0).getText());
    }

    @Test
    void testUpdatedPostWhenPostExists() {
        Long postId = 1L;
        Post existingPost = new Post();
        existingPost.setPostId(postId);
        existingPost.setText("Old text");

        Post updatedPostDetails = new Post();
        updatedPostDetails.setText("Updated text");

        when(postRepository.findPostById(postId)).thenReturn(Optional.of(existingPost));
        when(postRepository.save(existingPost)).thenReturn(existingPost);

        Post updatedPost = postService.updatedPost(postId, updatedPostDetails);
        assertEquals("Updated text", updatedPost.getText());
        assertEquals(LocalDateTime.now().getMinute(), updatedPost.getUpdated_at().getMinute());
        verify(postRepository).save(existingPost);
    }

    @Test
    void testUpdatedPostWhenPostDoesNotExist() {
        Long postId = 999L; // ID that doesn't exist
        Post updatedPostDetails = new Post();

        when(postRepository.findPostById(postId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postService.updatedPost(postId, updatedPostDetails);
        });

        assertEquals("Пост не найден.", exception.getMessage());
    }

    @Test
    void testDeletePostWhenPostExists() {
        Post post = new Post();
        post.setPostId(1L);

        postService.deletePost(post);

        verify(postRepository).deleteById(post.getPostId());
    }

    @Test
    void testDeletePostWhenPostIsNull() {
        Post post = null;

        postService.deletePost(post);

        verify(postRepository, never()).deleteById(any());
    }
}
