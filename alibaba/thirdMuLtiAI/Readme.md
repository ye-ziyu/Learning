对于多模型的调用
    在注入容器时注入多个ChatModel、ChatClient的容器，分别用不同的@Bean(name)标注
之后使用时@Qualifier("beanName")即可任意调用

需要注意，每个注入的Bean都需要独立设置model和key，url则会自动从yaml获取