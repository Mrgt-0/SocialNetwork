package org.example.socialnetwork.Service;

import jakarta.transaction.Transactional;
import org.example.socialnetwork.DTO.PostDTO;
import org.example.socialnetwork.DTO.UserDTO;
import org.example.socialnetwork.Model.Post;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    public void savePost(Post post) {
        try {
            postRepository.save(post);
            logger.info("Публичное сообщение успешно сохранено.");
        } catch (Exception e) {
            logger.error("Ощибка при сохранении публичного сообщения.", e.getMessage());
        }
    }

    @Transactional
    public Post publicationPost(PostDTO postDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserDTO currentUser = userService.findByUserName(userDetails.getUsername());
            postDTO.setUser(convertToEntity(currentUser));
        } else {
            logger.warn("Пользователь не аутентифицирован, пост не будет опубликован.");
            throw new RuntimeException("Пользователь не аутентифицирован");
        }

        postDTO.setCreated_at(LocalDateTime.now());
        postDTO.setUpdated_at(LocalDateTime.now());
        logger.info("Проверка полей перед сохранением: username = {}, text = {}, image = {}", postDTO.getUser().getUserName(), postDTO.getText(), postDTO.getImage());
        Post publishedPost = postRepository.save(convertToEntity(postDTO));
        logger.info("Пост успешно зарегистрирован.");
        return publishedPost;
    }

    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostDTO> postDTOs = posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return postDTOs;
    }

    @Transactional
    public PostDTO updatedPost(Long post_id, PostDTO updatedPost){
        Post post1 = postRepository.findPostById(post_id)
                .map(post->{
                    post.setText(updatedPost.getText());
                    post.setImage(updatedPost.getImage());
                    post.setUpdated_at(LocalDateTime.now());
                    return postRepository.save(post);
                })
                .orElseThrow(() -> new RuntimeException("Пост не найден."));
        return convertToDTO(post1);
    }

    @Transactional
    public void deletePostById(Long postId){
        if (postId == null)
            throw new IllegalArgumentException("ID поста не может быть null.");

        postRepository.deleteById(postId);
        logger.info("Пост с ID {} успешно удален.", postId);
    }

    private PostDTO convertToDTO(Post post) {
        if (post == null) {
            return null;
        }
        PostDTO postDTO = new PostDTO();
        postDTO.setPostId(postDTO.getPostId());
        postDTO.setUser(post.getUser());
        postDTO.setImage(post.getImage());
        postDTO.setCreated_at(post.getCreated_at());
        postDTO.setUpdated_at(post.getUpdated_at());
        postDTO.setText(post.getText());
        return postDTO;
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

    private User convertToEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setUserName(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setBirthdate(userDTO.getBirthdate());
        user.setProfilePicture(userDTO.getProfilePicture());
        user.setRole(userDTO.getRole());
        return user;
    }
}
