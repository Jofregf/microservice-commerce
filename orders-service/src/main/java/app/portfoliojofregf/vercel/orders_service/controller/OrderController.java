package app.portfoliojofregf.vercel.orders_service.controller;

import app.portfoliojofregf.vercel.orders_service.model.dtos.OrderRequest;
import app.portfoliojofregf.vercel.orders_service.model.dtos.OrderResponse;
import app.portfoliojofregf.vercel.orders_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "orders-service", fallbackMethod = "placeOrderFallback")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest orderRequest) {
        var orders = this.orderService.placeOrder(orderRequest);
        return ResponseEntity.ok(orders);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getAllOrders(){
        return this.orderService.getAllOrders();
    }

    private ResponseEntity<OrderResponse> placeOrderFallback(OrderRequest orderRequest, Throwable throwable) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public OrderResponse findById(@PathVariable("id") Long id) {
        return this.orderService.getById(id);
    }
}