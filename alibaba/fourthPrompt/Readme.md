# 📝 提示词（Prompt）使用指南

## 📋 概述

本文档介绍如何在 AI 对话中使用提示词模板，以及如何获取结构化返回结果。

---

## 🎯 提示词类型

### System Prompt（系统提示词）
定义 AI 的角色和行为准则

### User Prompt（用户提示词）
用户的具体输入内容

---

## 🔧 实现方式对比

| 特性 | ChatModel | ChatClient |
|:----:|:---------:|:----------:|
| 样板代码 | ⚠️ 较多 | ✅ 简洁 |
| 代码复杂度 | ⚠️ 较高 | ✅ 低 |
| 可读性 | ⚠️ 一般 | ✅ 优秀 |
| 流式调用 | ❌ 不支持 | ✅ 支持 |

> 💡 **推荐**：使用 **ChatClient** 处理提示词，代码更简洁美观

---

## ✨ 提示词模板

### 基本用法

使用 `{}` 作为占位符：

```java
String template = "你是一个{role}，请用{style}的风格回答：{question}";

String response = chatClient.prompt()
    .system("你是一个专业的助手")
    .user(u -> u.text(template)
        .param("role", "技术专家")
        .param("style", "通俗易懂")
        .param("question", "什么是Spring Boot?"))
    .call()
    .content();
```

### 从文件加载

将提示词保存在文本文件中，便于管理和维护：

```java
// 加载提示词模板
@Value("classpath:prompts/expert.txt")
private Resource promptTemplate;

public String chatWithTemplate(String question) throws IOException {
    String template = Files.readString(promptTemplate.getFile().toPath());
    
    return chatClient.prompt()
        .user(u -> u.text(template).param("question", question))
        .call()
        .content();
}
```

---

## 📊 结构化返回

### 获取结构化数据

使用 `.entity()` 替代 `.content()`：

```java
// 定义返回结构
public record BookInfo(String title, String author, int year) {}

// 获取结构化结果
BookInfo bookInfo = chatClient.prompt()
    .user("请介绍《百年孤独》这本书")
    .call()
    .entity(BookInfo.class);
```

### 对比说明

| 方法 | 返回类型 | 适用场景 |
|------|---------|---------|
| `.content()` | `String` | 普通文本对话 |
| `.entity()` | 指定类型 | 需要结构化数据 |

> 📌 **详细示例**：请参考 `structureController` 文件中的具体实现

---

## ✅ 最佳实践

1. 📝 将复杂提示词保存在独立文件中
2. 🎯 使用 ChatClient 简化代码
3. 📊 需要结构化数据时使用 `.entity()`
4. 🔄 利用流式调用提升用户体验

