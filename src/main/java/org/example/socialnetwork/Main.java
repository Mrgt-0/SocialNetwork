package org.example.socialnetwork;

import jakarta.transaction.SystemException;
import org.example.socialnetwork.Model.User;
import org.example.socialnetwork.Service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws SystemException {
        SpringApplication.run(Main.class);
    }
}
