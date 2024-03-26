package app.portfoliojofregf.vercel.notification_service.listeners;

import app.portfoliojofregf.vercel.notification_service.events.OrderEvent;
import app.portfoliojofregf.vercel.notification_service.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventListener {

    @KafkaListener(topics = "orders-topic")
    public void handleOrdersNotifications(String message) {
        var orderEvent = JsonUtils.fromJson(message, OrderEvent.class);

        // Enviar email al cliente, sms al cliente, etc.
        // Notificar a otros service...

        log.info("Order {} event received for order : {} with {} items",
                orderEvent.orderStatus(), orderEvent.orderNumber(), orderEvent.itemsCount());
    }

}
