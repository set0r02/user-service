package com.innowise.microservice;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BookstoreApplication {
    static void main(String[] args) {
        var context = SpringApplication.run(BookstoreApplication.class, args);
    }
}
