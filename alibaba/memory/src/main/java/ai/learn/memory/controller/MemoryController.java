package ai.learn.memory.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
public class MemoryController {

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
        return deepseakChatClient.prompt()
                .user(question)
                .advisors(new Consumer<ChatClient.AdvisorSpec>() {
                    @Override
                    public void accept(ChatClient.AdvisorSpec advisorSpec) {
                    advisorSpec.param(CONVERSATION_ID,"conversation-id-001");
                    }
                })
                .call()
                .content();
    }

    @GetMapping("/client/qwen")
    public Flux<String> qwenClient(@RequestParam(name = "question" , required = true, defaultValue = "hello") String question) {
        return qwenChatClient
                .prompt()
                .user(question)
                .advisors(advisorSpec ->  advisorSpec.param(CONVERSATION_ID,"conversation-id-001"))
                .stream()
                .content();
    }
}
