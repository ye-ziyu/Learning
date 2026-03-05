package ai.learn.thirdMuLtiAI.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class MultiController {

    @Qualifier("deepseakChatModel")
    @Autowired
    private ChatModel deepseakChatModel;
    @Qualifier("qwenChatModel")
    @Autowired
    private ChatModel qwenChatModel;
    @Qualifier("deepseakChatClient")
    @Autowired
    private ChatClient deepseakChatClient;
    @Qualifier("qwenChatClient")
    @Autowired
    private ChatClient qwenChatClient;

    @GetMapping("/model/deepsek")
    public String deepsek(@RequestParam(name = "question" , required = true, defaultValue = "hello") String question) {
        return deepseakChatModel.call( question);
    }

    @GetMapping("/model/qwen")
    public Flux<String> qwen(@RequestParam(name = "question" , required = true, defaultValue = "hello") String question) {
        return qwenChatModel.stream( question);
    }

    @GetMapping("/client/deepsek")
    public String deepsekClient(@RequestParam(name = "question" , required = true, defaultValue = "hello") String question) {
        return deepseakChatClient.prompt().user(question).call().content();
    }

    @GetMapping("/client/qwen")
    public Flux<String> qwenClient(@RequestParam(name = "question" , required = true, defaultValue = "hello") String question) {
        return qwenChatClient.prompt().user(question).stream().content();
    }
}
