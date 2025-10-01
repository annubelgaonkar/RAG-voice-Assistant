package dev.anuradha.voiceragassistant.rag;

import dev.anuradha.voiceragassistant.model.dto.VectorRecord;

import java.util.List;

public interface VectorStore {
    void upsert(List<VectorRecord> records);
    List<VectorRecord> topK(float[] queryEmbedding, int k);
    long count();
    void clear();
}
