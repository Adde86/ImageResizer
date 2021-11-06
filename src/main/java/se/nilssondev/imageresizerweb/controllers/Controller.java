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

    @GetMapping("/test-again")
    public String testAgain(){
        return "ECS works, does CodeDeploy application?";
    }

    @GetMapping("/test-three")
    public String testThree(){
        return "Third time the charm";
    }

    @GetMapping("/test-four")
    public String testFour(){
        return "Testing a fourth time";
    }
}
