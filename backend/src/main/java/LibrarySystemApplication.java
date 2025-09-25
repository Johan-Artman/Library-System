package main.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"main.java", "controllers", "config"})
public class LibrarySystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibrarySystemApplication.class, args);
    }
}