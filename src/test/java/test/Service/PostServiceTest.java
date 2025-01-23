package test.Service;

import org.example.socialnetwork.DTO.PostDTO;
import org.example.socialnetwork.DTO.UserDTO;
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
        PostDTO post = new PostDTO();
        post.setText("Test post");

        postService.savePost(convertToEntity(post));

        verify(postRepository).save(convertToEntity(post));
    }

    @Test
    void testPublicationPostWhenUserIsAuthenticated() {
        PostDTO post = new PostDTO();
        post.setText("Test post");

        UserDTO currentUser = new UserDTO();
        currentUser.setUserName("testUser");

        when(authentication.isAuthenticated()).thenReturn(true);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userService.findByUserName("testUser")).thenReturn(currentUser);
        when(postRepository.save(convertToEntity(post))).thenReturn(convertToEntity(post));

        Post publishedPost = postService.publicationPost(post);

        assertNotNull(publishedPost);
        assertEquals("testUser", publishedPost.getUser().getUserName());
        verify(postRepository).save(convertToEntity(post));
    }

    @Test
    void testPublicationPostWhenUserIsNotAuthenticated() {
        PostDTO post = new PostDTO();

        when(authentication.isAuthenticated()).thenReturn(false);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            postService.publicationPost(post);
        });

        assertEquals("Пользователь не аутентифицирован", thrown.getMessage());
    }

    @Test
    void testGetAllPosts() {
        PostDTO post = new PostDTO();
        post.setText("Test post");
        when(postRepository.findAll()).thenReturn(Collections.singletonList(convertToEntity(post)));

        List<PostDTO> posts = postService.getAllPosts();

        assertEquals(1, posts.size());
        assertEquals("Test post", posts.get(0).getText());
    }

    @Test
    void testUpdatedPostWhenPostExists() {
        Long postId = 1L;
        PostDTO existingPost = new PostDTO();
        existingPost.setPostId(postId);
        existingPost.setText("Old text");

        PostDTO updatedPostDetails = new PostDTO();
        updatedPostDetails.setText("Updated text");

        when(postRepository.findPostById(postId)).thenReturn(Optional.of(convertToEntity(existingPost)));
        when(postRepository.save(convertToEntity(existingPost))).thenReturn(convertToEntity(existingPost));

        PostDTO updatedPost = postService.updatedPost(postId, updatedPostDetails);
        assertEquals("Updated text", updatedPost.getText());
        assertEquals(LocalDateTime.now().getMinute(), updatedPost.getUpdated_at().getMinute());
        verify(postRepository).save(convertToEntity(existingPost));
    }

    @Test
    void testUpdatedPostWhenPostDoesNotExist() {
        Long postId = 999L;
        PostDTO updatedPostDetails = new PostDTO();

        when(postRepository.findPostById(postId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postService.updatedPost(postId, updatedPostDetails);
        });

        assertEquals("Пост не найден.", exception.getMessage());
    }

    @Test
    void testDeletePostWhenPostExists() {
        PostDTO post = new PostDTO();
        post.setPostId(1L);

        postService.deletePostById(post.getPostId());

        verify(postRepository).deleteById(post.getPostId());
    }

    @Test
    void testDeletePostWhenPostIsNull() {
        PostDTO post = null;

        postService.deletePostById(post.getPostId());

        verify(postRepository, never()).deleteById(any());
    }

    private Post convertToEntity(PostDTO postDTO) {
        if (postDTO == null) {
            return null;
        }
        Post post = new Post();
        post.setUser(postDTO.getUser());
        post.setImage(postDTO.getImage());
        post.setCreated_at(postDTO.getCreated_at());
        post.setUpdated_at(postDTO.getUpdated_at());
        post.setText(postDTO.getText());
        return post;
    }
}
