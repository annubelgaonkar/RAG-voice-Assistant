package dev.anuradha.voiceragassistant.rag;

import java.util.List;

public interface LlmClient {
    String answer(String userQuery, List<String> contexts);
}
