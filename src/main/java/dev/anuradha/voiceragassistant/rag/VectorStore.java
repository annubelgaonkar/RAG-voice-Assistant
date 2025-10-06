package dev.anuradha.voiceragassistant.rag;

import dev.anuradha.voiceragassistant.model.dto.VectorRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface VectorStore {
    void upsert(List<VectorRecord> records);
    List<VectorRecord> topK(float[] queryEmbedding, int k);
    long count();
    void clear();
}
