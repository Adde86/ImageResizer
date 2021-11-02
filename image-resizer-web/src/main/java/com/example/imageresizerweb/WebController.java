package com.example.imageresizerweb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @GetMapping("/")
    public String sayHello(){
        return "hello";
    }
}
