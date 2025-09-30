package dev.anuradha.voiceragassistant.controller;

import dev.anuradha.voiceragassistant.model.dto.OrderStatusResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order-status")
public class OrderController {

    // Stubbed order status intent
    @GetMapping
    public ResponseEntity<OrderStatusResponse> getStatus(@RequestParam String orderId) {
        // TODO: replace with stub service that randomizes status
        OrderStatusResponse resp = OrderStatusResponse.builder()
                .orderId(orderId)
                .status("shipped")
                .eta("2025-10-03")
                .build();
        return ResponseEntity.ok(resp);
    }
}
