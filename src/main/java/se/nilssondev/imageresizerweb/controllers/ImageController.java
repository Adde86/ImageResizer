package se.nilssondev.imageresizerweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.nilssondev.imageresizerweb.services.ImageService;

@Controller
@RequestMapping({"/"})
public class ImageController {

private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/images")
    public String imageIndex(Model model){
        model.addAttribute("images", imageService.getImages());
        return "/images/list";
    }
}
