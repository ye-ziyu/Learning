# 🛠️ Tool Calling（工具调用）功能实现

## 📋 概述

Tool Calling 是为了解决大模型实际操作局限问题而引入的功能。

### 解决的问题

| 问题 | 说明 |
|------|------|
| 📅 数据时效性 | 由 RAG 技术解决 |
| 🔒 私域隐秘性 | 由 RAG 技术解决 |
| ⚙️ 实际操作局限 | 由 Tool Calling 解决 |

---

## 🎯 核心概念

### 什么是 Tool Calling？

简单来说，是在本地编写工具函数的工具类，提供给大模型调用。

### 工作原理

```
┌──────────┐     ┌──────────────┐     ┌──────────────┐
│  用户输入  │────▶│   大模型分析  │────▶│  判断需要工具  │
└──────────┘     └──────────────┘     └──────┬───────┘
                                            │
                                            ▼
┌──────────┐     ┌──────────────┐     ┌──────────────┐
│  返回结果  │◀────│   执行工具    │◀────│  调用本地工具  │
└──────────┘     │  (Java 方法)  │     │  (Tool Class) │
                 └──────────────┘     └──────────────┘
```

---

## 💻 代码实现

### 1️⃣ 创建工具类

```java
@Component
public class TimeTool {
    
    public String getCurrentTime() {
        return LocalDateTime.now().toString();
    }
    
    public String getWeather(String city) {
        return city + " 的天气：晴朗";
    }
}
```

### 2️⃣ 使用 ChatModel 调用工具

```java
@Service
public class ToolService {
    
    @Autowired
    private ChatModel chatModel;
    
    public String chatWithTool(String message) {
        return chatModel.call(new Prompt(
            message,
            ChatOptions.builder()
                .function("getCurrentTime", "获取当前时间")
                .function("getWeather", "获取指定城市的天气", 
                    Map.of("city", "城市名称"))
                .build()
        ));
    }
}
```

### 3️⃣ 使用 ChatClient 调用工具（推荐）

```java
@Service
public class ToolClientService {
    
    @Autowired
    private ChatClient chatClient;
    
    public String chatWithTool(String message) {
        return chatClient.prompt()
            .user(message)
            .functions("getCurrentTime", "getWeather")
            .call()
            .content();
    }
}
```

---

## 🔍 Model vs Client 对比

| 特性 | ChatModel | ChatClient |
|:----:|:---------:|:----------:|
| 样板代码 | ⚠️ 较多 | ✅ 简洁 |
| 流式调用 | ❌ 不支持 | ✅ 支持 |
| 工具调用 | ✅ 支持 | ✅ 支持 |
| 代码可读性 | ⚠️ 一般 | ✅ 优秀 |

> 💡 **推荐**：使用 **ChatClient** 进行工具调用，代码更简洁，支持流式响应。

---

## 📝 工具类最佳实践

### 1. 方法命名清晰

```java
public String getCurrentTime() { }      // ✅ 清晰
public String getWeather(String city) { } // ✅ 清晰
public String doSomething() { }          // ❌ 不清晰
```

### 2. 添加方法描述

```java
@FunctionInfo(description = "获取当前系统时间")
public String getCurrentTime() {
    return LocalDateTime.now().toString();
}
```

### 3. 参数说明

```java
@FunctionInfo(
    description = "获取指定城市的天气信息",
    parameters = {
        @ParameterInfo(name = "city", description = "城市名称", required = true)
    }
)
public String getWeather(String city) {
    return city + " 的天气：晴朗";
}
```

---

## ✅ 使用场景

| 场景 | 说明 |
|------|------|
| 🕐 时间查询 | 获取当前时间、日期等 |
| 🌤️ 天气查询 | 获取实时天气信息 |
| 📊 数据查询 | 查询数据库、API 等 |
| 📧 邮件发送 | 发送邮件通知 |
| 📁 文件操作 | 读取、写入文件 |
| 🔍 网络请求 | 调用外部 API |

---

## 🎯 总结

| 优点 | 说明 |
|------|------|
| 🚀 扩展能力 | 让大模型具备实际操作能力 |
| 🎯 精准控制 | 通过工具类精确控制行为 |
| 🔄 灵活集成 | 易于集成现有系统 |
| 📝 代码简洁 | ChatClient 避免大量样板代码 |

