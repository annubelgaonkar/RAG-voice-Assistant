package dev.anuradha.voiceragassistant.service;

import dev.anuradha.voiceragassistant.model.dto.OrderStatusResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class OrderStatusService {

    private static final List<String> STATUSES = List.of("processing", "shipped",
            "out for delivery", "delivered");

    private final Random random = new Random();

    public OrderStatusResponse getOrderStatus(String orderId) {
        // Fake status selection
        String status = STATUSES.get(random.nextInt(STATUSES.size()));

        // Fake ETA: today + 1–5 days if not delivered
        String eta = status.equals("delivered")
                ? LocalDate.now().minusDays(random.nextInt(3) + 1).toString()
                : LocalDate.now().plusDays(random.nextInt(5) + 1).toString();
        return OrderStatusResponse.builder()
                .orderId(orderId)
                .status(status)
                .eta(eta)
                .build();
    }

    }
