# 🎨 文生图功能实现

## 📋 概述

本文档介绍如何使用 `ImageModel` 进行文生图（Text-to-Image）的模型调用。

---

## 🚀 快速开始

### 1️⃣ 添加依赖

在 `pom.xml` 中引入图像生成依赖：

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-ai-alibaba-dashscope</artifactId>
</dependency>
```

### 2️⃣ 配置文件

在 `application.yaml` 中配置图像模型：

```yaml
spring:
  ai:
    dashscope:
      api-key: ${DASHSCOPE_API_KEY}
      image:
        model: wanx-v1
        size: 1024x1024
```

---

## 💻 代码实现

### 基础调用

```java
@Service
public class ImageService {
    
    @Autowired
    private ImageModel imageModel;
    
    public ImageResponse generateImage(String prompt) {
        return imageModel.call(
            new ImagePrompt(prompt)
        );
    }
}
```

---

## 📤 返回值处理

### 获取图片 URL

```java
public String getImageUrl(String prompt) {
    ImageResponse response = imageModel.call(new ImagePrompt(prompt));
    
    // 获取图片 URL 地址
    return response.getResult().getOutput().getUrl();
}
```

### 获取图片字节流

```java
public byte[] getImageBytes(String prompt) {
    ImageResponse response = imageModel.call(new ImagePrompt(prompt));
    
    // 直接获取 byte[] 数据
    return response.getResult().getOutput().getBytes();
}
```

---

## 🔄 多模型配置

### 配置多个图像模型

```java
@Configuration
public class ImageModelConfig {
    
    @Bean(name = "wanxModel")
    public ImageModel wanxModel(@Value("${ai.wanx.api-key}") String apiKey) {
        return new DashScopeImageModel(apiKey, "wanx-v1");
    }
    
    @Bean(name = "dalleModel")
    public ImageModel dalleModel(@Value("${ai.dalle.api-key}") String apiKey) {
        return new OpenAiImageModel(apiKey, "dall-e-3");
    }
}
```

### 使用指定模型

```java
@Service
public class MultiImageService {
    
    @Autowired
    @Qualifier("wanxModel")
    private ImageModel wanxModel;
    
    public String generateWithWanx(String prompt) {
        return wanxModel.call(new ImagePrompt(prompt))
            .getResult()
            .getOutput()
            .getUrl();
    }
}
```

---

## 📊 功能对比

| 特性 | 文本生成 (Chat) | 图像生成 (Image) |
|:----:|:---------------:|:----------------:|
| 模型类 | `ChatModel` | `ImageModel` |
| 配置方式 | 相同 | 相同 |
| 多模型支持 | ✅ 支持 | ✅ 支持 |
| 返回值 | 文本内容 | URL / byte[] |

---

## ✅ 总结

> 💡 **要点**：
> - 图像生成与文本生成的调用方式相似
> - 多模型配置可参考 ChatModel 的多 Bean 注入方式
> - 返回值可选择 URL 地址或直接获取字节数组

