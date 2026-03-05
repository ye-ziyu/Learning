package ai.learn.fourthPrompt.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Objects;

@RestController
public class PromptController {

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

    @GetMapping("/client/deepsek")
    public String deepsekClient(@RequestParam(name = "question" , required = true, defaultValue = "hello") String question) {
        return deepseakChatClient.prompt()
                .system("你是一名医生,只知道医疗医学知识.")
                .user(question)
                .call()
                .content();
    }

    @GetMapping("/model/qwen")
    public Flux<ChatResponse>qwen(@RequestParam(name = "question" , required = true, defaultValue = "hello") String question) {
        SystemMessage systemMessage = new SystemMessage("你是一名医生,只知道医疗医学知识.");
        UserMessage userMessage = new UserMessage(question);
        Prompt prompt = new Prompt(systemMessage, userMessage);
        return qwenChatModel.stream(prompt);
    }

    @GetMapping("/model/deepsek")
    public Flux<String> deepsek(@RequestParam(name = "question" , required = true, defaultValue = "hello") String question) {
        SystemMessage systemMessage = new SystemMessage("你是一名医生,只知道医疗医学知识.");
        UserMessage userMessage = new UserMessage(question);
        Prompt prompt = new Prompt(systemMessage, userMessage);
        return deepseakChatModel.stream(prompt)
                .mapNotNull(response -> response.getResults().get(0).getOutput().getText());
    }

    @GetMapping("/client/qwen")
    public String qwenClient(@RequestParam(name = "question" , required = true, defaultValue = "hello") String question) {
        return Objects.requireNonNull(qwenChatClient.prompt()
                        .user(question)
                        .call()
                        .chatResponse())
                .getResult()
                .getOutput()
                .getText();
    }

    @GetMapping("/client/template/qwen")
    public String qwenTemplateClient(
            @RequestParam(name = "job" , required = true, defaultValue = "hello") String job,
            @RequestParam(name = "occupation" , required = true, defaultValue = "hello") String occupation,
            @RequestParam(name = "question" , required = true, defaultValue = "hello") String question) {
        PromptTemplate promptTemplate = new PromptTemplate("你是一个{job}，身处{occupation}，请帮我解决{question}");
        Prompt prompt = promptTemplate.create(Map.of("job", job, "occupation", occupation, "question", question));
        return Objects.requireNonNull(qwenChatClient.prompt(prompt)
                        .user(question)
                        .call()
                        .chatResponse())
                .getResult()
                .getOutput()
                .getText();
    }
}
