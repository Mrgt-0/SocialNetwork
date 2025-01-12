package org.example.socialnetwork.Service;

import jakarta.transaction.SystemException;
import jakarta.transaction.Transactional;
import org.example.socialnetwork.Model.Post;
import org.example.socialnetwork.Repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

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
        post.setCreated_at(LocalDate.now());
        post.setUpdated_at(LocalDate.now());
        Post publishedPost = postRepository.save(post);
        logger.info("Сообщение успешно зарегистрировано.");
        return publishedPost;
    }

    @Transactional
    public Post updatedPost(int post_id, Post updatedPost){
        return postRepository.findPostById(post_id)
                .map(post->{
                    post.setText(updatedPost.getText());
                    post.setImage(updatedPost.getImage());
                    post.setUpdated_at(LocalDate.now());
                    return postRepository.save(post);
                })
                .orElseThrow(() -> new RuntimeException("Пост не найден."));
    }
}
