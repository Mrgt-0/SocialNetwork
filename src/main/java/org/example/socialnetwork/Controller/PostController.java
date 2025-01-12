package org.example.socialnetwork.Controller;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import org.example.socialnetwork.Model.Post;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Service.PostService;
import org.example.socialnetwork.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @RequestMapping("/postCreater")
    public String publicationPost(@Valid @ModelAttribute("post") Post post,
                                  BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            logger.error("Ошибки валидации: " + bindingResult.getAllErrors());
            return "postCreater"; // Вернуться к форме, если есть ошибки
        }
        postService.publicationPost(post);
        redirectAttributes.addFlashAttribute("successMessage", "Пост опубликован успешно успешно! Пожалуйста, войдите.");
        return "redirect:/posts/postCreater"; // должен быть переход на начальную страницу
    }

}
