package ai.learn.mcp_server.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    @Tool(description = "Get weather information")
    public String getWeather(String city) {
        return "fault : "+city+"'s sunny today!";
    }
}
