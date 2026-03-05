相较于ChatModel,chatClient:
    其不能够自动注入,需要提前以Bean方式写入,或在使用时构造方法中使用已注入的ChatModel构造ChatClient;
    其不支持多模型,只能使用单一模型,无法在运行时切换模型;
    但是其提供了简便的流失调用方法,使用起来更方便;
    且ChatClient避免了ChatModel的大量样本代码,更适合于在SpringBoot中使用;
因此,如果需要更灵活的模型使用和更底层的接口,可以选择ChatClient;
如果需要更简单的使用方式和自动注入功能,可以选择ChatModel
    