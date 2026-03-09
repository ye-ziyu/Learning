package ai.learn.tool.util;


import org.springframework.ai.tool.annotation.Tool;

public class TimeTool {

    @Tool(description = "获取当前时间", returnDirect = false)
    public String getTime() {
        return "The current time is " + java.time.LocalTime.now();
    }
}
