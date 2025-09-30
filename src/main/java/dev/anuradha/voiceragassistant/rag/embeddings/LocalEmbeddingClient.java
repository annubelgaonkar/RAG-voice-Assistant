package dev.anuradha.voiceragassistant.rag.embeddings;

import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.embeddings.provider", havingValue = "local", matchIfMissing = true)
public class LocalEmbeddingClient implements EmbeddingClient{


}
