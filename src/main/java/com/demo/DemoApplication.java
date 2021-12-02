package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class DemoApplication {

    @RequestMapping("/world")
    public String hello(){
        return "hello";
    }

    public static void main(String[] args) {
        System.out.println("hello world!");
        SpringApplication.run(DemoApplication.class, args);
    }

}
