package dev.anuradha.voiceragassistant.rag;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocalStubLlmClient implements LlmClient{

    @Override
    public String answer(String userQuery, List<String> contexts) {
        String ctx = contexts.isEmpty() ? "" : contexts.get(0);
        String snippet = ctx.length() > 220 ? ctx.substring(0, 220) + "…" : ctx;
        return "Based on our policy: " + snippet;
    }
}
