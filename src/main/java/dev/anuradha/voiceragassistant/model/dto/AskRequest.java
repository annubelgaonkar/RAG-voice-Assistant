package dev.anuradha.voiceragassistant.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AskRequest {
    private String query;      // text transcribed from speech
}
