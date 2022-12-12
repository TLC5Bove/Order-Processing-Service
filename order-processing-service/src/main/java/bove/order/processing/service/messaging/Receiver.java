package bove.order.processing.service.messaging;

import bove.order.processing.service.config.RabbitConfig;
import bove.order.processing.service.dto.order.*;
import bove.order.processing.service.service.OrderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Receiver {
    @Autowired
    OrderService orderService;

    @Autowired
    MQMessagePublisher publisher;

    @RabbitListener(queues = RabbitConfig.COMPLETION_QUEUE)
    public void listener(OrderStatusResponse message) {
        Order order = orderService.findById(message.getOrderID());

        if (order.getExecutions() == message.getExecutions()) return;

        if (message.getQuantity() >= 1 && message.getQuantity() > message.getCumulatitiveQuantity()){
            order.setStatus("partial");
        } else {
            order.setStatus("complete");
            CompleteOrder completeOrder = new CompleteOrder();
            completeOrder.setOSID(order.getOsId());
            completeOrder.setCummPrice(calcCummPrice(message));
            publisher.publishOrderCompletionMessage(completeOrder);
        }
        order.setCumulatitivePrice(message.getCumulatitivePrice());
        order.setCumulatitiveQuantity(message.getCumulatitiveQuantity());
        order.setDateUpdated(new Date());
        orderService.saveOrder(order);
        System.out.println("Order with id " + message.getOrderID() + " is partially or fully fulfilled");
    }

    private Double calcCummPrice(OrderStatusResponse order){
        Double price = 0.0;
        for (var execution : order.getExecutions()){
            price += (execution.getPrice() * execution.getQuantity());
        }

        return price;
    }

    @RabbitListener(queues = RabbitConfig.ORDER_QUEUE)
    public void orderListener(OrderDTO message){
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(message.getUserId());
        orderRequest.setProduct(message.getProduct().getProductName());
        orderRequest.setOrderDate(new Date());
        orderRequest.setPrice(message.getPrice());
        orderRequest.setSide(message.getSide().getSide());
        orderRequest.setQuantity(message.getQuantity());
        orderRequest.setType(message.getType().getType());
        orderRequest.setPortfolioId(message.getPortId());
        orderRequest.setOsId(message.getOSID());
        orderService.placeOrder(orderRequest);
        System.out.println("Received order from user " + message.getUserId() + " and order OSID " + message.getOSID() );
    }
}
