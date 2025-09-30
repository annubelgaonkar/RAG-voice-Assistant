package dev.anuradha.voiceragassistant.controller;

import dev.anuradha.voiceragassistant.model.dto.AskRequest;
import dev.anuradha.voiceragassistant.model.dto.AskResponse;
import dev.anuradha.voiceragassistant.rag.RagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ask")
public class AskController {

    private final RagService rag;

    @PostMapping
    public ResponseEntity<AskResponse> ask(@Valid @RequestBody AskRequest req) {
        return ResponseEntity.ok(rag.answer(req.getQuery()));
    }

    @GetMapping("/ping")
    public String ping() { return "ask-ok@" + Instant.now(); }
}
