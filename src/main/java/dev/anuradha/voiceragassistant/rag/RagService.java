package dev.anuradha.voiceragassistant.rag;


import dev.anuradha.voiceragassistant.model.dto.*;
import dev.anuradha.voiceragassistant.rag.embeddings.EmbeddingClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<VectorRecord> hits = store.topK(q, 4);


        // 3) LLM answer (stub for now)
        String response = llm.answer(query, hits.stream().map(VectorRecord::getText).toList());

        // 4) Build short citations (unique sources)
        List<CitationDTO> cites = hits.stream()
                .map(h -> new CitationDTO(h.getSource(),
                        h.getId()))
                .distinct()
                .limit(2)
                .toList();

//        return AskResponse.builder()
//                .answer(appendCitationTag(response, cites))
//                .citations(cites)
//                .latencyMs(System.currentTimeMillis() - start)
//                .build();
        String citationTag = " (" + hits.stream()
                .map(VectorRecord::getSource)
                .distinct()
                .limit(2)
                .reduce((a,b) -> a + ", " + b)
                .orElse("") + ")";

        return AskResponse.builder()
                .answer(response + citationTag)
                .latencyMs(System.currentTimeMillis() - start)
                .build();
    }

}
