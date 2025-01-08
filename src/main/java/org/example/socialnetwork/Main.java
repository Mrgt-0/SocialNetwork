package org.example.socialnetwork;

import jakarta.transaction.SystemException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws SystemException {
        SpringApplication.run(Main.class, args);
    }
}
