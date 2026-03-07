package ai.learn.image.controller;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/first")
public class ImageController {

    @Autowired
    private ImageModel imageModel;

    @GetMapping("/doimage")
    public String doImage(@RequestParam(name = "description" , required = true, defaultValue = "飞机") String description) {
        return imageModel.call(new ImagePrompt(description)).getResult().getOutput().getUrl();
    }
}
