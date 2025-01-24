package org.example.socialnetwork.Controller;

import org.example.socialnetwork.DTO.PostDTO;
import org.example.socialnetwork.Service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @RequestMapping(value = "/createPost", method = RequestMethod.POST)
    public ResponseEntity<String> publicationPost(@RequestParam("text") String text,
                                                  @RequestParam("imageUrl") String imageUrl,
                                                  RedirectAttributes redirectAttributes) {
        logger.info("Текст поста: {}", text);
        logger.info("URL изображения: {}", imageUrl);
        postService.publicationPost(new PostDTO(text, imageUrl));
        redirectAttributes.addFlashAttribute("successMessage", "Пост опубликован успешно!");
        return ResponseEntity.ok("Пост опубликован успешно!");
    }

    @GetMapping("/allPosts")
    public ResponseEntity<List<PostDTO>> showAllPosts() {
        List<PostDTO> posts = postService.getAllPosts();
        for (PostDTO post : posts)
            logger.info("Пост: {}, Автор: {}", post.getText(), post.getUser() != null ? post.getUser().getUserName() : "Не указано");

        logger.info("Отображение страницы постов.");
        return ResponseEntity.ok(posts);
    }
}
