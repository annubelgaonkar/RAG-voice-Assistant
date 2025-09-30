package dev.anuradha.voiceragassistant.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitationDTO {
    private String source;
    private String chunkId;    // optional
}
