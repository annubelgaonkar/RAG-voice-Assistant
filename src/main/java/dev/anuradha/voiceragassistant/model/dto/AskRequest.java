package dev.anuradha.voiceragassistant.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AskRequest {
    @NotBlank
    private String query;      // text transcribed from speech
    private boolean enableBargeIn = true;
}
