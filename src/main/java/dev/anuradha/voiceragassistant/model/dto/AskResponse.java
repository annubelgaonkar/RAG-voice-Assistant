package dev.anuradha.voiceragassistant.model.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class AskResponse {
    private String answer;           // final text to TTS
    private long latencyMs;          // Tracks the total time taken (from query to answer) in milliseconds.

}
