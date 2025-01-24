package org.example.socialnetwork.Controller;

import org.example.socialnetwork.DTO.PostDTO;
import org.example.socialnetwork.Model.Post;
import org.example.socialnetwork.Service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @RequestMapping("/createPost")
    public ResponseEntity<String> publicationPost(@ModelAttribute("post") PostDTO post,
                                                  @RequestParam("image") MultipartFile file,
                                                  RedirectAttributes redirectAttributes) {
        logger.info("текст поста: {}", post.getText());
        logger.info("файл изображения: {}", file.getOriginalFilename());
//
//        if (!file.isEmpty()) {
//            String fileName = saveImage(file);
//            post.setImage(fileName);
//        }
        postService.publicationPost(post);
        redirectAttributes.addFlashAttribute("successMessage", "Пост опубликован успешно!");
        return ResponseEntity.ok("Пост опубликован успешно!");
    }

//    @GetMapping("/createPost")
//    public ResponseEntity<String> showPostCreateForm() {
//        logger.info("Отображение формы создания постов.");
//        return "createPost";
//    }

    @GetMapping("/allPosts")
    public ResponseEntity<List<PostDTO>> showAllPosts() {
        List<PostDTO> posts = postService.getAllPosts();
        for (PostDTO post : posts)
            logger.info("Пост: {}, Автор: {}", post.getText(), post.getUser() != null ? post.getUser().getUserName() : "Не указано");

        logger.info("Отображение страницы постов.");
        return ResponseEntity.ok(posts);
    }

//    public String saveImage(MultipartFile file) {
//        if (file.isEmpty()) {
//            logger.error("Загруженный файл пуст");
//            throw new RuntimeException("Загруженный файл пуст");
//        }
//        try {
//            String uploadDir = "src/main/resources/static/posts/";
//            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//            File imageFile = new File(uploadDir + fileName);
//            if (!imageFile.getParentFile().exists())
//                imageFile.getParentFile().mkdirs();
//
//            file.transferTo(imageFile);
//            logger.info("Файл успешно сохранен: {}", imageFile.getAbsolutePath());
//            return fileName;
//        } catch (IOException e) {
//            logger.error("Ошибка при сохранении файла: {}", e.getMessage());
//            throw new RuntimeException("Ошибка при сохранении файла", e);
//        } catch (Exception e) {
//            logger.error("Общая ошибка: {}", e.getMessage());
//            throw new RuntimeException("Ошибка при сохранении файла", e);
//        }
//    }
}
