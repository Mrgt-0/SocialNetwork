package org.example.socialnetwork.Service;

import jakarta.transaction.Transactional;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public Post publicationPost(Post post){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.findByUserName(userDetails.getUsername());
            post.setUser(currentUser); // Установка текущего пользователя
        } else {
            logger.warn("Пользователь не аутентифицирован, пост не будет опубликован.");
            throw new RuntimeException("Пользователь не аутентифицирован");
        }

        post.setCreated_at(LocalDateTime.now());
        post.setUpdated_at(LocalDateTime.now());
        logger.info("Проверка полей перед сохранением: username = {}, text = {}, image = {}", post.getUser().getUserName(), post.getText(), post.getImage());
        Post publishedPost = postRepository.save(post);
        logger.info("Пост успешно зарегистрирован.");
        return publishedPost;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Transactional
    public Post updatedPost(Long post_id, Post updatedPost){
        return postRepository.findPostById(post_id)
                .map(post->{
                    post.setText(updatedPost.getText());
                    post.setImage(updatedPost.getImage());
                    post.setUpdated_at(LocalDateTime.now());
                    return postRepository.save(post);
                })
                .orElseThrow(() -> new RuntimeException("Пост не найден."));
    }

    @Transactional
    public void deletePost(Post post){
        if(post!=null){
            postRepository.deleteById(post.getPostId());
            logger.info("Пост успешно удален.");
        }else
            logger.error("Пост не найден.");
    }
}
