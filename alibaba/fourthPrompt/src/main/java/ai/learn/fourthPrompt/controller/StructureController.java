package ai.learn.fourthPrompt.controller;

import ai.learn.fourthPrompt.record.Book;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.function.Consumer;

@RestController
public class StructureController {

    @Qualifier("deepseakChatClient")
    @Autowired
    private ChatClient deepseakChatClient;

    @GetMapping("/structure/client/deepsek")
    public Book deepsekClient(
        @RequestParam(name = "name" , required = true, defaultValue = "小明") String name,
        @RequestParam(name = "author" , required = true, defaultValue = "作者") String author,
        @RequestParam(name = "year" , required = true, defaultValue = "91") Integer  year) {

        return deepseakChatClient.prompt()
            .system("你正在编写一本书")
            .user(new Consumer<ChatClient.PromptUserSpec>() {
                @Override
                public void accept(ChatClient.PromptUserSpec promptUserSpec) {
                    promptUserSpec.text("请编写一本书名为{name}的作者为{author}的{year}年的书")
                            .params(Map.of("name", name, "author", author, "year", year));
                }
            })
            .call()
            .entity(Book.class);
    }

    @GetMapping("/structure/new/client/deepsek")
    public Book deepsekClientNew(
            @RequestParam(name = "name" , required = true, defaultValue = "小明") String name,
            @RequestParam(name = "author" , required = true, defaultValue = "作者") String author,
            @RequestParam(name = "year" , required = true, defaultValue = "91") Integer  year) {

        return deepseakChatClient.prompt()
                .system("你正在编写一本书")
                .user(
                        promptUserSpec -> promptUserSpec.text("请编写一本书名为{name}的作者为{author}的{year}年的书")
                                .params(Map.of("name", name, "author", author, "year", year))
                )
                .call()
                .entity(Book.class);
    }



}
