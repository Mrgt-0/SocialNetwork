package test.Service;

import org.example.socialnetwork.DTO.PostDTO;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.Post;
import org.example.socialnetwork.Repository.PostRepository;
import org.example.socialnetwork.Service.PostService;
import org.example.socialnetwork.Service.UserService;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
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
        Post postEntity = convertToEntity(post);
        postService.savePost(postEntity);
        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(postCaptor.capture());
        assertEquals(postEntity.getText(), postCaptor.getValue().getText());
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
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post savedPost = invocation.getArgument(0);
            return savedPost;
        });
        Post publishedPost = postService.publicationPost(post);
        assertNotNull(publishedPost);
        assertEquals("testUser", publishedPost.getUser().getUserName());
        verify(postRepository).save(any(Post.class));
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
        PostDTO existingPostDTO = new PostDTO();
        existingPostDTO.setPostId(postId);
        existingPostDTO.setText("Old text");
        PostDTO updatedPostDetails = new PostDTO();
        updatedPostDetails.setText("Updated text");
        Post existingPostEntity = convertToEntity(existingPostDTO);
        when(postRepository.findPostById(postId)).thenReturn(Optional.of(existingPostEntity));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post savedPost = invocation.getArgument(0);
            return savedPost;
        });
        PostDTO updatedPost = postService.updatedPost(postId, updatedPostDetails);
        assertEquals("Updated text", updatedPost.getText());
        assertNotNull(updatedPost.getUpdated_at());
        assertEquals(LocalDateTime.now().getMinute(), updatedPost.getUpdated_at().getMinute());
        verify(postRepository).save(any(Post.class));
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
        Long postId = null;
        assertThrows(IllegalArgumentException.class, () -> {
            postService.deletePostById(postId);
        });
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
