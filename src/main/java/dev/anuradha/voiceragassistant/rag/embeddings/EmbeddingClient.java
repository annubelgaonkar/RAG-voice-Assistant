package dev.anuradha.voiceragassistant.rag.embeddings;

public interface EmbeddingClient {
    float[] embed(String text);
    int dimension();
}
