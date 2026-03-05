package ai.learn.second.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/first")
public class SecondController {

    @Autowired
    private ChatClient chatClient;

    @GetMapping("/dochat")
    public String doChat(@RequestParam(name = "question" , required = true, defaultValue = "hello") String question) {
        return chatClient.prompt().user(question).call().content();
    }

    @GetMapping("/dochat2")
    public Flux<String> stream(@RequestParam(name = "question" , required = true, defaultValue = "hello") String question) {
        return chatClient.prompt().user(question).stream().content();
    }
}
