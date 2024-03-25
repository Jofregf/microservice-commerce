package app.portfoliojofregf.vercel.inventory_service.controller;

import app.portfoliojofregf.vercel.inventory_service.model.dtos.BaseResponse;
import app.portfoliojofregf.vercel.inventory_service.model.dtos.OrderItemRequest;
import app.portfoliojofregf.vercel.inventory_service.model.entity.Inventory;
import app.portfoliojofregf.vercel.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{sku}")
    @ResponseStatus(HttpStatus.FOUND)
    public Boolean isInStock(@PathVariable("sku") String sku) {
        return inventoryService.isInStock(sku);
    }

    @PostMapping("/in-stock")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse areInStock(@RequestBody List<OrderItemRequest> orderItems) {
        return inventoryService.areInStock(orderItems);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Inventory> getAll(){
        return inventoryService.getAll();
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public Inventory findById(@PathVariable("id") Long id){
        return inventoryService.getById(id);
    }
}
