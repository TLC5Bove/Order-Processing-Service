package bove.order.processing.service.controller;


import bove.order.processing.service.dto.order.OrderRequest;
import bove.order.processing.service.dto.order.OrderStatusResponse;
import bove.order.processing.service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {
    @Autowired
    OrderService orderService;


//    @Autowired
//    public OrderController(OrderService orderService) {
//        this.orderService = orderService;
//    }

    @PostMapping
    public void placeOrderOnExchange(@RequestBody OrderRequest order, String exchange) {
        orderService.placeOrder(order);
    }

    @DeleteMapping("{orderId}")
    public void cancelOrderOnExchange(@PathVariable String orderId, String exchange) {
        orderService.placeCancelOrder(orderId, exchange);
    }

    @GetMapping("{orderId}")
    public OrderStatusResponse getOrderStatus(@PathVariable String orderId, String exchange) {
        return orderService.getOrderStatus(orderId, exchange);
    }

}
