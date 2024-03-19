package app.portfoliojofregf.vercel.notification_service.events;

import app.portfoliojofregf.vercel.notification_service.model.enums.OrderStatus;

public record OrderEvent(String orderNumber, int itemsCount, OrderStatus orderStatus) {
}
