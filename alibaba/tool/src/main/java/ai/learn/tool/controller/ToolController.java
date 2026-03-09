package ai.learn.tool.controller;

import ai.learn.tool.util.TimeTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/first")
public class ToolController {

    @Autowired
    private ChatClient chatClient;
    @Autowired
    private ChatModel chatModel;

    @GetMapping("/dochat")
    public String doChat(@RequestParam(name = "question" , required = true, defaultValue = "hello") String question) {
        return chatClient.prompt().user(question).call().content();
    }

    @GetMapping("/dochat2")
    public Flux<String> stream(@RequestParam(name = "question" , required = true, defaultValue = "hello") String question) {
        return chatClient.prompt().user(question).stream().content();
    }

    @GetMapping("/tool/client")
    public String  tool(@RequestParam(name = "question" , required = true, defaultValue = "hello") String question) {
        ToolCallback[] tools = ToolCallbacks.from(new TimeTool());
        ChatOptions options = ToolCallingChatOptions.builder().toolCallbacks( tools)
                .build();
        return chatModel.call(new Prompt(question, options)).getResult().getOutput().getText();
    }

    @GetMapping("/tool/client2")
    public Flux<String> tool2(@RequestParam(name = "question" , required = true, defaultValue = "hello") String question) {
        return chatClient.prompt().user( question).tools(new TimeTool()).stream().content();
    }
}
