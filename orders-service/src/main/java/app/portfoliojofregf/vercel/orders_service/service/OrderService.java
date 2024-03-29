package app.portfoliojofregf.vercel.orders_service.service;

import app.portfoliojofregf.vercel.orders_service.events.OrderEvent;
import app.portfoliojofregf.vercel.orders_service.model.dtos.*;
import app.portfoliojofregf.vercel.orders_service.model.entity.Order;
import app.portfoliojofregf.vercel.orders_service.model.entity.OrderItems;
import app.portfoliojofregf.vercel.orders_service.model.enums.OrderStatus;
import app.portfoliojofregf.vercel.orders_service.repository.OrderRepository;
import app.portfoliojofregf.vercel.orders_service.utils.JsonUtils;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObservationRegistry observationRegistry;

    public OrderResponse placeOrder(OrderRequest orderRequest){

        Observation inventoryObservation = Observation.createNotStarted("inventory-service", observationRegistry);

        return inventoryObservation.observe(() -> {
            // Se debe chequear si antes hay producto en inventario
            BaseResponse result = this.webClientBuilder.build()
                    .post()
                    .uri("lb://inventory-service/api/inventory/in-stock")
                    .bodyValue(orderRequest.getOrderItems())
                    .retrieve()
                    .bodyToMono(BaseResponse.class)
                    .block();

            if(result != null && !result.hasErrors()){
                Order order = new Order();
                order.setOrderNumber(UUID.randomUUID().toString());
                order.setOrderItems(orderRequest.getOrderItems().stream()
                        .map(orderItemRequest -> mapOrderItemRequestToOrderItem(orderItemRequest, order))
                        .toList());
                var savedOrder = this.orderRepository.save(order);
                // Envía mensaje a orders topic
                this.kafkaTemplate.send("orders-topic", JsonUtils.toJson(
                        new OrderEvent(savedOrder.getOrderNumber(), savedOrder.getOrderItems().size(), OrderStatus.PLACED)));
                return mapToOrderResponse(savedOrder);
            } else {
                throw new IllegalArgumentException("Some of the products are not in stock");
            }
        });
    }

    public List<OrderResponse> getAllOrders(){
        List<Order> orders = this.orderRepository.findAll();
        return orders.stream().map(this::mapToOrderResponse).toList();
    }

    public OrderResponse getById(Long id){
        Optional<Order> orderOpt = orderRepository.findById(id);
        if(orderOpt.isPresent()){
            Order order = orderOpt.get();
            return mapToOrderResponse(order);
        } else {
            return null;
        }
    }

    public boolean existById(Long id){
        return orderRepository.existsById(id);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(order.getId(), order.getOrderNumber(),
                order.getOrderItems().stream().map(this::mapToOrderItemRequest).toList());
    }

    private OrderItemsResponse mapToOrderItemRequest(OrderItems orderItems) {
        return new OrderItemsResponse(orderItems.getId(), orderItems.getSku(), orderItems.getPrice(), orderItems.getQuantity());
    }

    private OrderItems mapOrderItemRequestToOrderItem(OrderItemRequest orderItemRequest, Order order) {
        return OrderItems.builder()
                .id(orderItemRequest.getId())
                .sku(orderItemRequest.getSku())
                .price(orderItemRequest.getPrice())
                .quantity(orderItemRequest.getQuantity())
                .order(order)
                .build();
    }
}