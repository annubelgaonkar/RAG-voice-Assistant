package dev.anuradha.voiceragassistant.controller;

import dev.anuradha.voiceragassistant.ingest.IngestionService;
import dev.anuradha.voiceragassistant.rag.VectorStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ingest")
public class IngestController {

    private final IngestionService ingestionService;
    private final VectorStore store;

    @PostMapping
    public ResponseEntity<Map<String, Object>> ingest() {
        int chunks = ingestionService.ingestClasspathFaqs();
        return ResponseEntity.ok(Map.of(
                "message", "Ingested FAQs",
                "chunks", chunks,
                "vectorCount", store.count()
        ));
    }
}
