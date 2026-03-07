# 🔄 多模型调用指南

## 📋 概述

本文档介绍如何在 Spring Boot 项目中配置和使用多个 AI 模型。

---

## 🚀 配置步骤

### 1️⃣ 定义多个 Bean

在配置类中注入多个 `ChatModel` 或 `ChatClient`，使用 `@Bean(name)` 标注不同名称：

```java
@Configuration
public class MultiModelConfig {
    
    @Bean(name = "qwenModel")
    public ChatModel qwenModel(@Value("${ai.qwen.api-key}") String apiKey) {
        return new DashScopeChatModel(apiKey, "qwen-turbo");
    }
    
    @Bean(name = "gptModel")
    public ChatModel gptModel(@Value("${ai.gpt.api-key}") String apiKey) {
        return new OpenAiChatModel(apiKey, "gpt-3.5-turbo");
    }
}
```

### 2️⃣ 使用指定模型

通过 `@Qualifier` 注解选择要使用的模型：

```java
@Service
public class MultiModelService {
    
    @Autowired
    @Qualifier("qwenModel")
    private ChatModel qwenModel;
    
    @Autowired
    @Qualifier("gptModel")
    private ChatModel gptModel;
    
    public String chatWithQwen(String message) {
        return qwenModel.call(message);
    }
    
    public String chatWithGpt(String message) {
        return gptModel.call(message);
    }
}
```

---

## ⚠️ 注意事项

| 注意点 | 说明 |
|--------|------|
| 🔑 独立配置 | 每个 Bean 都需要独立设置 `model` 和 `api-key` |
| 🌐 URL 自动获取 | `url` 会自动从 `yaml` 配置文件中获取 |
| 📝 命名规范 | 建议使用有意义的 Bean 名称，便于识别 |

---

## 📁 配置示例

```yaml
ai:
  qwen:
    api-key: ${QWEN_API_KEY}
    model: qwen-turbo
  gpt:
    api-key: ${GPT_API_KEY}
    model: gpt-3.5-turbo
```

---

## ✅ 总结

通过 `@Bean(name)` 和 `@Qualifier` 的组合，可以灵活地在多个模型之间切换使用。

