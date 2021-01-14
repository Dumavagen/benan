package com.example.buttetinboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ButtetinBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(ButtetinBoardApplication.class, args);
    }

}
