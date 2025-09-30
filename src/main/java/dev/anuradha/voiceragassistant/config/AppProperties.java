package dev.anuradha.voiceragassistant.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private Llm llm = new Llm();
    private Embeddings embeddings = new Embeddings();
    private VectorStore vectorStore = new VectorStore();
    private Cors cors = new Cors();

    @Data
    public static class Llm {
        private String provider = "openai";
        private String apiKey;
        private String baseUrl = "https://api.openai.com/v1";
        private String model = "gpt-4o-mini";
    }
    @Data
    public static class Embeddings {
        private String provider = "openai";
        private String apiKey;
        private String baseUrl = "https://api.openai.com/v1";
        private String model = "text-embedding-3-small";
    }

    @Data
    public static class VectorStore {
        private String type = "INMEMORY"; // INMEMORY | PINECONE | etc.
    }

    @Data
    public static class Cors {
        private String allowedOrigins = "*";
    }
}
