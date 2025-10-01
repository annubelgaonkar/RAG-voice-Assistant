package dev.anuradha.voiceragassistant.rag.embeddings;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;


@Component
@ConditionalOnProperty(name = "app.embeddings.provider", havingValue = "local", matchIfMissing = true)
public class LocalEmbeddingClient implements EmbeddingClient{

    private static final int D = 384;

    @Override public float[] embed(String text) {
        float[] v = new float[D];
        if (text == null) return v;
        StringTokenizer tok = new StringTokenizer(text.toLowerCase(),
                " \t\r\n.,;:!?()[]{}\"'");
        while (tok.hasMoreTokens()) {
            String t = tok.nextToken();
            int idx = Math.abs(murmur3(t)) % D;
            v[idx] += 1.0f;
        }
        double n=0; for (float x: v) n += x*x; n = Math.sqrt(n);
        if (n>0) for (int i=0;i<D;i++) v[i] /= (float)n;
        return v;
    }
    @Override public int dimension() { return D; }

    private static int murmur3(String s) {
        byte[] data = s.getBytes(StandardCharsets.UTF_8);
        int h1 = 0, c1 = 0xcc9e2d51, c2 = 0x1b873593;
        int i = 0, len = data.length;
        while (i + 4 <= len) {
            int k1 = (data[i]&0xff)|((data[i+1]&0xff)<<8)|((data[i+2]&0xff)<<16)|(data[i+3]<<24);
            i += 4; k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2;
            h1 ^= k1; h1 = Integer.rotateLeft(h1, 13); h1 = h1*5 + 0xe6546b64;
        }
        int k1 = 0, rem = len & 3;
        if (rem == 3) k1 = (data[i+2]&0xff) << 16;
        if (rem >= 2) k1 |= (data[i+1]&0xff) << 8;
        if (rem >= 1) { k1 |= (data[i]&0xff); k1 *= c1; k1 = Integer.rotateLeft(k1, 15); k1 *= c2; h1 ^= k1; }
        h1 ^= len; h1 ^= (h1>>>16); h1 *= 0x85ebca6b; h1 ^= (h1>>>13); h1 *= 0xc2b2ae35; h1 ^= (h1>>>16);
        return h1;
    }

}
