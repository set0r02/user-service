package com.innowise.microservice;



import com.innowise.microservice.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BookstoreApplication {
    static void main() {
        var context = SpringApplication.run(BookstoreApplication.class);
    }
}
