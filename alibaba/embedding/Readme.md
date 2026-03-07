# 🔢 文本向量化与向量数据库

## 📋 概述

本文档介绍如何使用文本向量化模型和向量数据库进行文本相似度查询和分析。

---

## 🎯 核心概念

### 向量化（Embedding）

将文本、图片等数据转换为对应的浮点数数组（向量），用于表示数据的语义特征。

### 向量数据库

专门用于存储和检索向量数据的数据库，支持高效的相似度查询。

---

## 💾 存储方案

| 方案 | 说明 | 适用场景 |
|------|------|---------|
| 🧠 内存存储 | 存储在内存中 | 小规模数据、快速原型 |
| 💻 代码存储 | 存储在代码中 | 静态数据、测试环境 |
| 🗄️ Redis Stack | 推荐方案 | 生产环境、大规模数据 |

> 💡 **推荐**：使用 **Redis Stack** 作为向量数据库，性能优秀且易于集成。

---

## ⚙️ 配置说明

### 1️⃣ 向量化模型配置

在 `application.yaml` 中配置：

```yaml
spring:
  ai:
    embedding:
      model: text-embedding-v2
```

或在代码中直接指定：

```java
@Bean
public EmbeddingModel embeddingModel() {
    return new DashScopeEmbeddingModel("text-embedding-v2");
}
```

### 2️⃣ Redis Stack 配置

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: ${REDIS_PASSWORD}
  ai:
    vectorstore:
      redis:
        prefix: "doc:"
        index-name: "doc-index"
```

> ⚠️ **前置条件**：使用前需安装 Redis Stack 并配置好连接信息。

---

## 💻 代码实现

### 方式一：主动获取向量化数据

```java
@Service
public class EmbeddingService {
    
    @Autowired
    private EmbeddingModel embeddingModel;
    
    public float[] getEmbedding(String text) {
        return embeddingModel.embed(text);
    }
}
```

### 方式二：自动存储到向量数据库

```java
@Service
public class VectorStoreService {
    
    @Autowired
    private VectorStore vectorStore;
    
    public void storeDocument(String text, Map<String, Object> metadata) {
        Document document = new Document(text, metadata);
        vectorStore.add(List.of(document));
    }
}
```

---

## 🔄 工作流程

```
┌──────────┐     ┌──────────────┐     ┌──────────────┐
│  输入文本  │────▶│  向量化模型    │────▶│  生成向量    │
└──────────┘     └──────────────┘     └──────┬───────┘
                                            │
                                            ▼
┌──────────┐     ┌──────────────┐     ┌──────────────┐
│  查询结果  │◀────│  相似度计算    │◀────│  存入数据库  │
└──────────┘     └──────────────┘     └──────────────┘
```

---

## 📊 数据存储格式

存储在 Redis Stack 中的数据为 JSON 格式：

```json
{
  "text": "原始文本内容",
  "embedding": [0.1234, 0.5678, ...],
  "metadata": {
    "source": "document.pdf",
    "timestamp": 1704067200
  }
}
```

> 📌 **自动转换**：Vector 内部会自动注入项目中的向量化模型，完成文本到向量的转换，并将原始文本和向量数据一起存储。

---

## 🔍 相似度查询

```java
public List<Document> searchSimilar(String query, int topK) {
    return vectorStore.similaritySearch(
        SearchRequest.query(query)
            .withTopK(topK)
    );
}
```

---

## ✅ 总结

| 特性 | 说明 |
|------|------|
| 🚀 自动化 | Vector 自动完成文本向量化 |
| 📦 一体化 | 原始文本和向量数据一起存储 |
| 🔍 高效查询 | 支持快速的相似度检索 |
| 🎯 灵活配置 | 支持多种向量化模型和数据库 |

