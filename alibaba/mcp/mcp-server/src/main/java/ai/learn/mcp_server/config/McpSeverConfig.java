package ai.learn.mcp_server.config;

import ai.learn.mcp_server.service.WeatherService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpSeverConfig {
    /**
     * 将工具方法暴露给外部 mcp client 调用
     *
     * @param weatherService
     * @return
     */
    @Bean
    public ToolCallbackProvider weatherTools(WeatherService weatherService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(weatherService)
                .build();
    }
}
