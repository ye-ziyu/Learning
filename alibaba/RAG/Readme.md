# 🔍 RAG（检索增强生成）技术实现

## 📋 概述

RAG（Retrieval Augmented Generation）技术用于解决大模型的两个核心问题：
- 📅 **数据时效性** - 大模型无法获取实时数据
- 🔒 **私域数据** - 大模型无法访问私有数据

---

## 🏗️ RAG 工作原理

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  外部数据源  │────▶│   索引阶段    │────▶│  Redis Stack │
│  (文档/配置) │     │  (向量化)     │     │  (向量数据库) │
└──────────────┘     └──────────────┘     └──────┬───────┘
                                            │
                                            ▼
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  用户提问    │────▶│   检索阶段    │────▶│  相关文档    │
└──────────────┘     │ (相似度查询)  │     └──────┬───────┘
                     └──────────────┘             │
                                                  ▼
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  最终回答    │◀────│   生成阶段    │◀────│  上下文+问题 │
└──────────────┘     │  (大模型)     │     └──────────────┘
                     └──────────────┘
```

---

## 🔄 两个核心步骤

### 1️⃣ 索引阶段

将外部数据记录到文本文档或统一配置中，在项目加载时：
- 以 config 形式读取数据
- 将数据向量化后保存到 Redis Stack
- 形成可检索的索引

### 2️⃣ 检索阶段

当用户提出问题时：
- RAG 先检索相关的外部数据
- 通过向量数据库相似度查询获取结果
- 将检索到的数据与用户问题一起输入大模型
- 大模型生成最终回答

---

## ⚠️ 避免重复加载

### 问题

多次启动项目时，可能多次加载同一份数据到 Redis Stack，导致：
- 数据冗余
- 检索结果不准确
- 性能下降

### 解决方案

使用 Redis 的 `SETNX` 命令（Set if Not eXists）：

```java
public void loadData(String documentName, String content) {
    String key = "rag:doc:" + DigestUtils.md5Hex(documentName);
    
    // 使用 SETNX 判断是否已存储
    Boolean isNew = redisTemplate.opsForValue().setIfAbsent(key, "loaded");
    
    if (Boolean.TRUE.equals(isNew)) {
        // 首次加载，进行向量化存储
        List<Document> documents = List.of(new Document(content));
        vectorStore.add(documents);
    } else {
        // 已存在，可选择跳过或更新
        updateDocument(key, content);
    }
}
```

### 策略选择

| 策略 | 说明 | 适用场景 |
|------|------|---------|
| 🚫 跳过加载 | 存在则不存储 | 数据未变化 |
| 🔄 删除重建 | 删除旧数据再存储 | 数据已更新 |
| ⏭️ 增量更新 | 只更新变化部分 | 大规模数据 |

---

## 💻 核心代码示例

### 初始化向量数据库

```java
@Component
public class InitVectorDatabaseConfig {
    
    @Autowired
    private VectorStore vectorStore;
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @PostConstruct
    public void init() {
        String docKey = "rag:doc:ops";
        
        if (Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(docKey, "loaded"))) {
            List<Document> documents = loadDocuments();
            vectorStore.add(documents);
        }
    }
}
```

### RAG 查询

```java
@GetMapping("/rag/query")
public String ragQuery(@RequestParam String question) {
    // 1. 检索相关文档
    SearchRequest searchRequest = SearchRequest.builder()
            .query(question)
            .topK(3)
            .build();
    
    List<Document> relevantDocs = vectorStore.similaritySearch(searchRequest);
    
    // 2. 构建上下文
    String context = relevantDocs.stream()
            .map(Document::getContent)
            .collect(Collectors.joining("\n"));
    
    // 3. 调用大模型生成回答
    String prompt = "基于以下上下文回答问题：\n\n" + context + "\n\n问题：" + question;
    
    return chatClient.prompt()
            .user(prompt)
            .call()
            .content();
}
```

---

## ✅ 最佳实践

1. 📝 使用文档名称或特征的 MD5 值作为 Redis 键
2. 🔒 使用 SETNX 避免重复加载
3. 🔄 定期更新索引数据
4. 📊 监控向量数据库的存储和查询性能
5. 🎯 合理设置 topK 参数，平衡准确性和性能

