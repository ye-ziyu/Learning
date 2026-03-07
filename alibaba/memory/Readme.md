# 🧠 对话记忆功能实现

## 📋 概述

本文档介绍如何使用 Redis 实现大模型对话的记忆功能，让 AI 能够记住历史对话上下文。

---

## 🏗️ 数据结构设计

### Redis 存储方案

```
┌─────────────────────────────────────────┐
│  Redis Key: user:{userId}               │
├─────────────────────────────────────────┤
│  Value: List<Conversation>              │
│                                         │
│  ┌─────────────────────────────────┐    │
│  │  {                              │    │
│  │    "timestamp": 1704067200,     │    │
│  │    "role": "user",              │    │
│  │    "content": "你好"            │    │
│  │  },                             │    │
│  │  {                              │    │
│  │    "timestamp": 1704067205,     │    │
│  │    "role": "assistant",         │    │
│  │    "content": "你好！有什么可...│    │
│  │  }                              │    │
│  └─────────────────────────────────┘    │
└─────────────────────────────────────────┘
```

---

## 🔄 工作流程

```
┌──────────┐     ┌──────────────┐     ┌──────────┐
│  用户输入  │────▶│  查询历史记忆  │────▶│ 拼接上下文 │
└──────────┘     └──────────────┘     └────┬─────┘
                                           │
                                           ▼
┌──────────┐     ┌──────────────┐     ┌──────────┐
│ 返回给用户 │◀────│ 保存到 Redis │◀────│  调用模型  │
└──────────┘     └──────────────┘     └──────────┘
```

---

## 💻 核心逻辑

### 1. 获取历史记忆

```java
public List<Conversation> getMemory(String userId, int limit) {
    // 从 Redis 获取用户对话列表
    List<Conversation> history = redisTemplate.opsForList()
        .range("user:" + userId, 0, -1);
    
    // 按时间戳排序
    history.sort(Comparator.comparing(Conversation::getTimestamp));
    
    // 取最近的 n 条
    return history.stream()
        .skip(Math.max(0, history.size() - limit))
        .collect(Collectors.toList());
}
```

### 2. 拼接上下文

```java
public String buildContext(List<Conversation> history, String newMessage) {
    StringBuilder context = new StringBuilder();
    
    // 添加历史对话
    for (Conversation conv : history) {
        context.append(conv.getRole())
               .append(": ")
               .append(conv.getContent())
               .append("\n");
    }
    
    // 添加新消息
    context.append("user: ").append(newMessage);
    
    return context.toString();
}
```

### 3. 保存新记忆

```java
public void saveMemory(String userId, String userMsg, String aiResponse) {
    String key = "user:" + userId;
    
    // 保存用户消息
    redisTemplate.opsForList().rightPush(key, 
        new Conversation("user", userMsg, System.currentTimeMillis()));
    
    // 保存 AI 回复
    redisTemplate.opsForList().rightPush(key,
        new Conversation("assistant", aiResponse, System.currentTimeMillis()));
    
    // 限制记忆条数
    trimMemory(key, MAX_MEMORY_SIZE);
}
```

---

## ⚙️ 内存管理

### 限制策略

| 策略 | 说明 | 配置 |
|------|------|------|
| 🕐 时间限制 | 只保留最近 N 天的对话 | `memory.days=7` |
| 📊 数量限制 | 只保留最近 N 条记录 | `memory.max-size=20` |
| 🗑️ 自动清理 | 超出限制时删除最旧记录 | 自动执行 |

### 清理代码

```java
private void trimMemory(String key, int maxSize) {
    Long currentSize = redisTemplate.opsForList().size(key);
    if (currentSize != null && currentSize > maxSize) {
        // 删除最旧的记录
        redisTemplate.opsForList().trim(key, currentSize - maxSize, -1);
    }
}
```

---

## ✅ 总结

| 优点 | 说明 |
|------|------|
| 🚀 高性能 | Redis 读写速度快 |
| 🔄 持久化 | 支持数据持久化存储 |
| ⚡ 可扩展 | 易于扩展和集群部署 |
| 🎯 灵活性 | 支持多种内存管理策略 |

> 💡 **提示**：合理设置最大记忆条数，避免过多历史记录影响性能和成本。

