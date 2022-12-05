package bove.order.processing.service.controller;


import bove.order.processing.service.dto.order.OrderRequest;
import bove.order.processing.service.dto.order.OrderStatusResponse;
import bove.order.processing.service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public String placeOrderOnExchange(@RequestBody OrderRequest order, String exchange) {
        return orderService.placeOrder(order, exchange);
    }

    @GetMapping("{orderId}")
    public OrderStatusResponse getOrderStatus(OrderStatusResponse orderStatusResponse, @PathVariable String orderId) {
        return orderService.getOrderStatus(orderId);
    }

}
