ChatModel的使用:
    首先需要引入对应的依赖,spring-web,spring-ai-alibaba-dashscope(ai使用协议,可以使用open-ai代替)
    之后要配置好对应的属性,包括baseurl,apiKey,modelName三件套
    对于密钥的注入,需要创建一个DashScopeApi对象,并注入到spring容器中
    而baseurl,model会自动从yaml获取,所以只需要注入apiKey即可
就可以直接注入ChatModel,并调用对应的方法进行使用了;