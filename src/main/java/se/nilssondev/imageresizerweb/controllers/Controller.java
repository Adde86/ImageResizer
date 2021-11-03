package se.nilssondev.imageresizerweb.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/")
    public String index(){
        return "Hello aws";
    }

    @GetMapping("/test")
    public String test(){
        return "yes better believe it";
    }
}
