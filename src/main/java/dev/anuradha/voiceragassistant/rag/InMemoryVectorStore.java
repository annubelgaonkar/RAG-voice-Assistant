package dev.anuradha.voiceragassistant.rag;

import dev.anuradha.voiceragassistant.util.Cosine;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryVectorStore implements VectorStore{
    private final Map<String, VectorRecord> store = new ConcurrentHashMap<>();

    @Override
    public void upsert(List<VectorRecord> records) {
        for (VectorRecord r : records) store.put(r.getId(), r);
    }

    @Override
    public List<VectorRecord> topK(float[] queryEmbedding, int k) {
        return store.values().stream()
                .map(r -> new AbstractMap.SimpleEntry<>(r,
                        Cosine.similarity(queryEmbedding, r.getEmbedding())))
                .sorted((a,
                         b) ->
                        Double.compare(b.getValue(), a.getValue()))
                .limit(k)
                .map(Map.Entry::getKey)
                .toList();
    }

    @Override public long count() { return store.size(); }
    @Override public void clear() { store.clear(); }
}
