# 🤖 ChatModel 使用指南

## 📋 概述

本文档介绍如何在 Spring Boot 项目中使用 `ChatModel` 进行 AI 对话。

---

## 🚀 快速开始

### 1️⃣ 添加依赖

在 `pom.xml` 中引入以下依赖：

| 依赖 | 说明 |
|------|------|
| `spring-web` | Spring Web 基础框架 |
| `spring-ai-alibaba-dashscope` | AI 使用协议（可用 OpenAI 替代） |

### 2️⃣ 配置属性

在 `application.yaml` 中配置以下三项：

```yaml
ai:
  base-url: ${AI_BASE_URL}
  api-key: ${AI_API_KEY}
  model-name: ${AI_MODEL_NAME}
```

> 💡 **配置说明**：
> - `base-url` - API 基础地址
> - `api-key` - 访问密钥
> - `model-name` - 模型名称

### 3️⃣ 注入密钥

创建 `DashScopeApi` 对象并注入 Spring 容器：

```java
@Bean
public DashScopeApi dashScopeApi(@Value("${ai.api-key}") String apiKey) {
    return new DashScopeApi(apiKey);
}
```

> 🔑 **注意**：`base-url` 和 `model` 会自动从 YAML 获取，只需注入 `api-key` 即可。

### 4️⃣ 使用 ChatModel

```java
@Service
public class ChatService {
    
    @Autowired
    private ChatModel chatModel;
    
    public String chat(String message) {
        return chatModel.call(message);
    }
}
```

---

## ✅ 总结

完成以上步骤后，即可直接注入 `ChatModel` 并调用对应方法进行使用。

