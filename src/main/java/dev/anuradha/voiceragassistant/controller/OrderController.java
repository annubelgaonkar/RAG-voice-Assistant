package dev.anuradha.voiceragassistant.controller;

import dev.anuradha.voiceragassistant.model.dto.OrderStatusResponse;
import dev.anuradha.voiceragassistant.service.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order-status")
public class OrderController {

    private final OrderStatusService orderStatusService;

    @GetMapping
    public ResponseEntity<OrderStatusResponse> getStatus(@RequestParam String orderId) {
        return ResponseEntity.ok(orderStatusService.getOrderStatus(orderId));
    }
}
