package dev.anuradha.voiceragassistant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ingest")
public class IngestController {

    @PostMapping
    public ResponseEntity<Map<String, Object>> ingest() {
        return ResponseEntity.accepted().body(Map.of(
                "message", "Ingestion started (stub).",
                "count", 0
        ));
    }
}
