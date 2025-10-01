package dev.anuradha.voiceragassistant.ingest;

import dev.anuradha.voiceragassistant.rag.TextChunker;
import dev.anuradha.voiceragassistant.rag.embeddings.EmbeddingClient;
import dev.anuradha.voiceragassistant.model.dto.VectorRecord;
import dev.anuradha.voiceragassistant.rag.VectorStore;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IngestionService {

    private final TextChunker chunker;
    private final EmbeddingClient embedding;
    private final VectorStore store;

    public int ingestClasspathFaqs() {
        try {
            PathMatchingResourcePatternResolver r = new PathMatchingResourcePatternResolver();
            Resource[] files = r.getResources("classpath*:sample-data/faqs/*.*");
            List<VectorRecord> all = new ArrayList<>();

            for (Resource res : files) {
                String filename = res.getFilename();
                if (filename == null) continue;
                String lower = filename.toLowerCase();
                if (!(lower.endsWith(".md") || lower.endsWith(".txt") ||
                        lower.endsWith(".json"))) continue;

                String docId = filename;
                String source = humanizeSourceName(filename);

                String text = new String(res.getInputStream().readAllBytes(),
                        StandardCharsets.UTF_8);
                for (String chunk : chunker.chunk(text)) {
                    float[] vec = embedding.embed(chunk);
                    all.add(VectorRecord.builder()
                            .id(UUID.randomUUID().toString())
                            .docId(docId)
                            .source(source)
                            .text(chunk)
                            .embedding(vec)
                            .build());
                }
            }
            store.clear();
            store.upsert(all);
            return all.size();
        } catch (Exception e) {
            throw new RuntimeException("Failed to ingest FAQs", e);
        }
    }
    private static String humanizeSourceName(String filename) {
        String base = filename.replaceAll("\\.[^.]+$", "");
        return base.replace('-', ' ').replace('_', ' ');
    }
}
