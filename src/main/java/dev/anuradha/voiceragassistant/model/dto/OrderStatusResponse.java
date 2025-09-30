package dev.anuradha.voiceragassistant.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderStatusResponse {
    private String orderId;
    private String status;     // e.g., "shipped", "delivered", "processing"
    private String eta;
}
