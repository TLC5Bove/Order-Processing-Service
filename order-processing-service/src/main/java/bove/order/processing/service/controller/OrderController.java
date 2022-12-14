package bove.order.processing.service.controller;


import bove.order.processing.service.dto.order.OrderRequest;
import bove.order.processing.service.dto.order.OrderStatusResponse;
import bove.order.processing.service.orderbook.model.Ibm;
import bove.order.processing.service.orderbook.repo.IbmPageSortRepo;
import bove.order.processing.service.orderbook.service.IbmService;
import bove.order.processing.service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    IbmService ibmService;
    @Autowired
    private IbmPageSortRepo ibmPageSortRepo;


//    @Autowired
//    public OrderController(OrderService orderService) {
//        this.orderService = orderService;
//    }

    @PostMapping
    public void placeOrderOnExchange(@RequestBody OrderRequest order) {
        orderService.placeOrder(order);
    }

//    @DeleteMapping("{orderId}")
//    public void cancelOrderOnExchange(@PathVariable String orderId, String exchange) {
//        orderService.placeCancelOrder(orderId, exchange);
//    }

    @GetMapping
    public List<Ibm> getFromPage(){
        return ibmService.findAllPageAndSortBySide("BUY", 100);
    }
}
