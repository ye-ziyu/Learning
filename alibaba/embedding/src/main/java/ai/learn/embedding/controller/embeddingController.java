package ai.learn.embedding.controller;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmbeddingController {

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private VectorStore vectorStore;

    @GetMapping("/embedding")
    public EmbeddingResponse embedding(@RequestParam(name = "input" , required = true, defaultValue = "hello") String input) {
        EmbeddingResponse embeddingResponse = embeddingModel.call(new EmbeddingRequest(List.of(input),
//                第二个参数填写模型名称等参数，可以此处写null使用yaml全局定义
                DashScopeEmbeddingOptions.builder().withModel("text-embedding-v4").build()));
        float[] output = embeddingResponse.getResult().getOutput();
        return embeddingResponse;
    }

    @GetMapping("/add")
    public String add(@RequestParam(name = "input" , required = true, defaultValue = "hello") String input) {
        List<Document> documents = List.of(
                new Document(input),
                new Document("what happened?"),
                new Document("who are you?")
        );
        vectorStore.add(documents);
        return "success";
    }

    @GetMapping("/query")
    public List<Document> query(@RequestParam(name = "input" , required = true, defaultValue = "me") String input) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(input)
                .topK(3)
                .build();
        return vectorStore.similaritySearch(searchRequest);
    }
}
