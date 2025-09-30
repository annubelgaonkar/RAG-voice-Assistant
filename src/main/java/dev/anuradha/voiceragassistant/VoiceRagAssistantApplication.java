package dev.anuradha.voiceragassistant;

import dev.anuradha.voiceragassistant.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class VoiceRagAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoiceRagAssistantApplication.class, args);
    }

}
