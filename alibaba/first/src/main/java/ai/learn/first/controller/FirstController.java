package ai.learn.first.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/first")
public class FirstController {

    @Autowired
    private  ChatModel chatModel;

    @GetMapping("/dochat")
    public String doChat(@RequestParam(name = "question" , required = true, defaultValue = "hello") String question) {
        return chatModel.call( question);
    }

    @GetMapping("/dochat2")
    public Flux<String> stream(@RequestParam(name = "question" , required = true, defaultValue = "hello") String question) {
        return chatModel.stream(question);
    }
}
