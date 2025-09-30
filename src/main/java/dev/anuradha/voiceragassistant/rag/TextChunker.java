package dev.anuradha.voiceragassistant.rag;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TextChunker {

    private static final int MAX_CHARS = 800;
    private static final int OVERLAP = 120;

    public List<String> chunk(String text) {
        if (text == null || text.isBlank()) return List.of();
        String[] paras = text.split("\\n\\s*\\n");
        List<String> chunks = new ArrayList<>();
        StringBuilder cur = new StringBuilder();

        for (String p : paras) {
            String para = p.trim();
            if (para.isEmpty()) continue;

            if (cur.length() + para.length() + 1 <= MAX_CHARS) {
                if (!cur.isEmpty()) cur.append("\n\n");
                cur.append(para);
            } else {
                if (!cur.isEmpty()) {
                    chunks.add(cur.toString());
                    String tail = cur.substring(Math.max(0, cur.length() - OVERLAP));
                    cur = new StringBuilder(tail);
                }
                if (para.length() <= MAX_CHARS) {
                    if (!cur.isEmpty()) cur.append("\n\n");
                    cur.append(para);
                } else {
                    // hard wrap long paragraph
                    for (int i = 0; i < para.length(); i += MAX_CHARS - OVERLAP) {
                        int end = Math.min(para.length(), i + (MAX_CHARS - OVERLAP));
                        String piece = para.substring(i, end);
                        if (cur.length() + piece.length() + 1 > MAX_CHARS && !cur.isEmpty()) {
                            chunks.add(cur.toString());
                            String tail2 = cur.substring(Math.max(0, cur.length() - OVERLAP));
                            cur = new StringBuilder(tail2);
                        }
                        if (!cur.isEmpty()) cur.append("\n\n");
                        cur.append(piece);
                    }
                }
            }
        }
        if (!cur.isEmpty()) chunks.add(cur.toString());
        return chunks;
    }
}
