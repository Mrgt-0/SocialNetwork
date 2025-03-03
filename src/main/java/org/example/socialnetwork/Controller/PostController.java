package org.example.socialnetwork.Controller;
import org.example.socialnetwork.Model.Post;
import org.example.socialnetwork.Service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @RequestMapping("/createPost")
    public String publicationPost(@ModelAttribute("post") Post post,
                                  @RequestParam("image") MultipartFile file) {
        logger.info("текст поста: {}", post.getText());
        logger.info("файл изображения: {}", file.getOriginalFilename());

        if (!file.isEmpty()) {
            try {
                byte[] imageBytes = file.getBytes();
                post.setImage(imageBytes);
            } catch (IOException e) {
                logger.error("Ошибка при чтении файла: {}", e.getMessage());
            }
        }
        postService.publicationPost(post);
        logger.info("Пост опубликован успешно!");
        return "redirect:/posts/allPosts";
    }

    @GetMapping("/createPost")
    public String showPostCreateForm(Model model) {
        model.addAttribute("post", new Post());
        logger.info("Отображение формы создания постов.");
        return "createPost";
    }

    @GetMapping("/allPosts")
    public String showAllPosts(Model model) {
        try {
            List<Post> posts = postService.getAllPosts();
            model.addAttribute("posts", posts);
            for (Post post : posts) {
                logger.info("Пост: {}, Автор: {}", post.getText(), post.getUser() != null ? post.getUser().getUserName() : "Не указано");
            }
            logger.info("Отображение страницы постов.");
        } catch (Exception e) {
            logger.error("Ошибка при получении постов: {}", e.getMessage());
            model.addAttribute("errorMessage", "Не удалось получить посты. Попробуйте позже.");
            return "error";
        }
        return "allPosts";
    }

    public String saveImage(MultipartFile file) {
        if (file.isEmpty()) {
            logger.error("Загруженный файл пуст");
            throw new RuntimeException("Загруженный файл пуст");
        }
        try {
            String uploadDir = String.valueOf(Paths.get("src/main/resources/static/posts/"));
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File imageFile = new File(uploadDir + fileName);
            if (!imageFile.getParentFile().exists())
                imageFile.getParentFile().mkdirs();

            file.transferTo(imageFile);
            logger.info("Файл успешно сохранен: {}", imageFile.getAbsolutePath());
            return fileName;
        } catch (IOException e) {
            logger.error("Ошибка при сохранении файла: {}", e.getMessage());
            throw new RuntimeException("Ошибка при сохранении файла", e);
        } catch (Exception e) {
            logger.error("Общая ошибка: {}", e.getMessage());
            throw new RuntimeException("Ошибка при сохранении файла", e);
        }
    }

    public String encodeImageToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
