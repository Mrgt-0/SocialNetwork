package org.example.socialnetwork.Controller;

import org.example.socialnetwork.Model.Post;
import org.example.socialnetwork.Service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @RequestMapping("/createPost")
    public String publicationPost(@ModelAttribute("post") Post post,
                                  BindingResult bindingResult,
                                  @RequestParam("image") MultipartFile file,
                                  RedirectAttributes redirectAttributes) {
        logger.info("текст поста: {}", post.getText());
        logger.info("файл изображения: {}", file.getOriginalFilename());

        if (!file.isEmpty()) {
            String fileName = saveImage(file);
            post.setImage(fileName);
        }
        postService.publicationPost(post);
        redirectAttributes.addFlashAttribute("successMessage", "Пост опубликован успешно!");
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
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        for (Post post : posts)
            logger.info("Пост: {}, Автор: {}", post.getText(), post.getUser() != null ? post.getUser().getUserName() : "Не указано");

        logger.info("Отображение страницы постов.");
        return "allPosts";
    }

    public String saveImage(MultipartFile file) {
        if (file.isEmpty()) {
            logger.error("Загруженный файл пуст");
            throw new RuntimeException("Загруженный файл пуст");
        }
        try {
            String uploadDir = "src/main/resources/static/posts/";
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
}
