package dev.anuradha.voiceragassistant.controller;

import dev.anuradha.voiceragassistant.model.dto.AskRequest;
import dev.anuradha.voiceragassistant.model.dto.AskResponse;
import dev.anuradha.voiceragassistant.model.dto.CitationDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/ask")
public class AskController {

    @PostMapping
    public ResponseEntity<AskResponse> ask(@Valid @RequestBody AskRequest req) {
        long start = System.currentTimeMillis();

        // TEMP stub: replace with RAG pipeline in next step
        AskResponse resp = AskResponse.builder()
                .answer("You can return items within 30 days of delivery (from Returns Policy).")
                .citations(List.of(new CitationDTO("Returns Policy", "chunk-001")))
                .latencyMs(System.currentTimeMillis() - start)
                .build();

        return ResponseEntity.ok(resp);
    }
    @GetMapping("/ping")
    public String ping() {
        return "ask-ok@" + Instant.now();
    }
}
