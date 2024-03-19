package app.portfoliojofregf.vercel.orders_service.events;

import app.portfoliojofregf.vercel.orders_service.model.enums.OrderStatus;

public record OrderEvent(String orderNumber, int itemsCount, OrderStatus orderStatus) {
}
