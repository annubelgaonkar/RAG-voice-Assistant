package dev.anuradha.voiceragassistant.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AskResponse {
    private String answer;           // final text to TTS
    private List<CitationDTO> citations;
    private long latencyMs;
}
