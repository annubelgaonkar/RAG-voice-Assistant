package dev.anuradha.voiceragassistant.rag;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.anuradha.voiceragassistant.config.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.StringJoiner;

@Component
@Primary
@RequiredArgsConstructor
public class OpenAiLlmClient implements LlmClient {

    private final AppProperties props;
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    @Override
    public String answer(String userQuery, List<String> contexts) {
        try {
            String systemPrompt = "You are a helpful customer-care assistant. " +
                    "Answer based on the provided context. Include a brief, factual answer.";

            StringJoiner ctxJoin = new StringJoiner("\n\n");
            contexts.forEach(ctxJoin::add);
            String fullContext = ctxJoin.toString();

            String body = """
                {
                  "model": "%s",
                  "messages": [
                    {"role": "system", "content": "%s"},
                    {"role": "user", "content": "Context:\\n%s\\n\\nQuestion: %s"}
                  ]
                }
                """.formatted(
                    props.getLlm().getModel(),
                    escape(systemPrompt),
                    escape(fullContext),
                    escape(userQuery)
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(props.getLlm().getBaseUrl() + "/chat/completions"))
                    .header("Authorization", "Bearer " + props.getLlm().getApiKey())
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(60))
                    .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() / 100 != 2) {
                throw new RuntimeException("Bad response: " + resp.statusCode() + " -> "
                        + resp.body());
            }
            JsonNode root = mapper.readTree(resp.body());
            return root.get("choices").get(0).get("message").get("content").asText();
        } catch (Exception e) {
            throw new RuntimeException("LLM API failed", e);
        }
    }

    private static String escape(String s) {
        return s.replace("\"", "\\\"");
    }

}