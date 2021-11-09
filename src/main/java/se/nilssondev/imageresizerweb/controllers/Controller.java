package se.nilssondev.imageresizerweb.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {


    @GetMapping("/")
    public String index(){
        return "Index has been changed";
    }

    @GetMapping("/test")
    public String test(){
        return "Testing something else";
    }

}
