# 💬 ChatClient 使用指南

## 📋 概述

本文档对比 `ChatModel` 与 `ChatClient` 的差异，帮助您选择合适的 AI 调用方式。

---

## 🔍 特性对比

| 特性 | ChatModel | ChatClient |
|------|:---------:|:----------:|
| 自动注入 | ✅ 支持 | ❌ 需手动配置 Bean |
| 多模型支持 | ✅ 支持运行时切换 | ❌ 单一模型 |
| 流式调用 | ❌ 需额外处理 | ✅ 内置支持 |
| 代码简洁度 | ⚠️ 较多样板代码 | ✅ 简洁美观 |
| SpringBoot 集成 | ⚠️ 一般 | ✅ 更友好 |

---

## ⚡ ChatClient 配置方式

### 方式一：Bean 方式注入

```java
@Configuration
public class ChatClientConfig {
    
    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
            .build();
    }
}
```

### 方式二：构造方法中配置Client

```java
@Service
public class ChatService {
    private final ChatClient chatClient;
    
    public ChatService(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }
}
```

---

## 🎯 选型建议

### 选择 ChatClient 当：
- ✅ 需要更简单的使用方式
- ✅ 希望减少样板代码
- ✅ 需要流式响应
- ✅ 使用单一模型即可满足需求

### 选择 ChatModel 当：
- ✅ 需要灵活的模型切换
- ✅ 需要更底层的接口控制
- ✅ 需要运行时动态选择模型

---

## 💡 总结

> **推荐**：在 SpringBoot 项目中，如果不需要频繁切换模型，优先使用 **ChatClient**，代码更简洁、更易维护。

