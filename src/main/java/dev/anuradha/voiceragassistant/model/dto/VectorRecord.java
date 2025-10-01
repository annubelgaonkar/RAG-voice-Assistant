package dev.anuradha.voiceragassistant.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VectorRecord {
    private String id;
    private String docId;
    private String source;
    private String text;
    private float[] embedding;
    private double score;   // <-- ADD THIS    // vector

}
