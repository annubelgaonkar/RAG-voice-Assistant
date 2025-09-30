package dev.anuradha.voiceragassistant.rag;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VectorRecord {
    private String id;            // unique id for the chunk
    private String docId;         // file/document id
    private String source;        // short source name, e.g., "Returns Policy"
    private String text;          // chunk content
    private float[] embedding;    // vector

}
