package org.example.socialnetwork.Controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        ex.printStackTrace(); // Посмотреть полное исключение
        return "error"; // Вернуться на страницу с ошибкой
    }
}
