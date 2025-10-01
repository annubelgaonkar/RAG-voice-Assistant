package dev.anuradha.voiceragassistant.rag;

import dev.anuradha.voiceragassistant.model.dto.AskResponse;
import dev.anuradha.voiceragassistant.model.dto.CitationDTO;
import dev.anuradha.voiceragassistant.model.dto.VectorRecord;
import dev.anuradha.voiceragassistant.rag.embeddings.EmbeddingClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RagService {

    private final VectorStore store;
    private final EmbeddingClient embedding;
    private final LlmClient llm;

    public AskResponse answer(String query) {
        long start = System.currentTimeMillis();

        // 1) Embed query
        float[] q = embedding.embed(query);

        // 2) Retrieve top-k
        List<VectorRecord> hits = store.topK(q, 6);


        // 3) Re-rank retrieved results heuristically
        List<VectorRecord> reranked = rerank(query, hits);


        // 4️⃣ Build the context for LLM (top 3)
        List<String> contexts = reranked.stream()
                .limit(3)
                .map(VectorRecord::getText)
                .toList();

        // 5️⃣ Generate answer
        String response = llm.answer(query, contexts);

        // 6️⃣ Build short citations (unique sources, cleaned)
        List<CitationDTO> cites = reranked.stream()
                .map(r -> new CitationDTO(r.getSource(), r.getId()))
                .distinct()
                .limit(2)
                .toList();

        String citationTag = " (" + reranked.stream()
                .map(VectorRecord::getSource)
                .map(this::cleanSource)
                .distinct()
                .limit(2)
                .collect(Collectors.joining(", ")) + ")";

        return AskResponse.builder()
                .answer(response + citationTag)
                .latencyMs(System.currentTimeMillis() - start)
                .build();
    }

    private List<VectorRecord> rerank(String query, List<VectorRecord> hits) {
        String q = query.toLowerCase(Locale.ROOT);

        return hits.stream()
                .peek(h -> {
                    double boost = 0;
                    String src = h.getSource() != null ? h.getSource().toLowerCase() : "";
                    String text = h.getText() != null ? h.getText().toLowerCase() : "";

                    if (q.contains("return")) {
                        if (src.contains("return")) boost += 0.35;
                        if (text.contains("return")) boost += 0.15;
                    }
                    if (q.contains("shipping")) {
                        if (src.contains("shipping")) boost += 0.35;
                        if (text.contains("shipping")) boost += 0.15;
                    }
                    if (q.contains("billing") || q.contains("refund")) {
                        if (src.contains("billing")) boost += 0.3;
                        if (text.contains("refund")) boost += 0.15;
                    }

                    // add the boost to the score
                    h.setScore(h.getScore() + boost);
                })
                .sorted(Comparator.comparingDouble(VectorRecord::getScore).reversed())
                .toList();
    }

    private String cleanSource(String raw) {
        if (raw == null) return "unknown";
        return raw.replaceAll("\\.md$|\\.txt$|\\.json$", "")
                .replace('-', ' ')
                .trim();
    }

}
