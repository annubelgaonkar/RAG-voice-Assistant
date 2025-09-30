package dev.anuradha.voiceragassistant.rag.embeddings;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import dev.anuradha.voiceragassistant.config.AppProperties;
import dev.anuradha.voiceragassistant.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@Primary
@RequiredArgsConstructor
public class OpenAiEmbeddingClient implements EmbeddingClient{
    private final AppProperties props;
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    @Override
    public float[] embed(String text) {
        try {
            String body = """
              {"model":"%s","input":"%s"}
              """.formatted(props.getEmbeddings().getModel(), escape(text));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(props.getEmbeddings().getBaseUrl() + "/embeddings"))
                    .header("Authorization", "Bearer "
                            + props.getEmbeddings().getApiKey())
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(60))
                    .POST(HttpRequest.BodyPublishers.ofString(body,
                            StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() / 100 != 2) {
                throw new RuntimeException("Bad response: " + resp.statusCode()
                        + " -> " + resp.body());
            }
            JsonNode root = mapper.readTree(resp.body());
            JsonNode arr = root.get("data").get(0).get("embedding");
            List<Float> floats = new ArrayList<>(arr.size());
            for (JsonNode n : arr) floats.add(n.floatValue());
            float[] vec = new float[floats.size()];
            for (int i = 0; i < vec.length; i++) vec[i] = floats.get(i);
            return vec;
        } catch (Exception e) {
            throw new RuntimeException("Embedding API failed", e);
        }
    }

    @Override
    public int dimension() { return 1536; }

    private static String escape(String s) {
        return s.replace("\"", "\\\"");
    }


}
