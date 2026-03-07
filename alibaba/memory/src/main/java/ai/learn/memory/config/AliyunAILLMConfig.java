package ai.learn.memory.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunAILLMConfig {

    @Value("${spring.ai.dashscope.api-key}")
    public String key;

//    常量形式标注模型名称
    public final static String DEEPSEAK_MODEL= "deepseek-v3";
    public final static String QWEN_MODEL= "qwen3-max";

    @Bean(name = "deepseakChatModel")
    public ChatModel deepseakChatModel() {
//       注入 ChatModel实现类, 这里使用DashScopeChatModel
        return DashScopeChatModel.builder()
                .dashScopeApi(DashScopeApi.builder().apiKey( key).build())
//                参数形式配置Model
                .defaultOptions(DashScopeChatOptions.builder().withModel(DEEPSEAK_MODEL).build())
                .build();
    }

    @Bean("qwenChatModel")
    public ChatModel qwenChatModel() {
        return DashScopeChatModel.builder()
                .dashScopeApi(DashScopeApi.builder().apiKey( key).build())
                .defaultOptions(DashScopeChatOptions.builder().withModel(QWEN_MODEL).build())
                .build();
    }

    @Bean("deepseakChatClient")
    public ChatClient chatClient(@Qualifier("deepseakChatModel") ChatModel chatModel,
                                            RedisChatMemoryRepository redisChatMemoryRepository) {
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(redisChatMemoryRepository)
                .maxMessages(10)
                .build();
        return ChatClient.builder(chatModel)
//                由于注入的model已经配置好model ,  这里可以注释掉下面这行 , 不需要再配置model
                .defaultOptions(DashScopeChatOptions.builder().withModel(DEEPSEAK_MODEL).build())
//                以顾问·形式配置Memory，在Memory中保存对话记录
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Bean("qwenChatClient")
    public ChatClient qwenChatClient(@Qualifier("qwenChatModel") ChatModel chatModel,
                                            RedisChatMemoryRepository redisChatMemoryRepository) {
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(redisChatMemoryRepository)
                .maxMessages(10)
                .build();
        return ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()
                ).build();
    }
}
