package bove.order.processing.service.mqPubSub;

import bove.order.processing.service.config.RabbitConfig;
import bove.order.processing.service.dto.order.Order;
import bove.order.processing.service.dto.order.OrderStatusResponse;
import bove.order.processing.service.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Receiver {
    @Autowired
    OrderService orderService;

    @RabbitListener(queues = RabbitConfig.TRACKING_QUEUE)
    public void listener(OrderStatusResponse message) {
        Order order = orderService.findById(message.getOrderID());
        order.setStatus("complete");
        order.setCumulatitivePrice(message.getCumulatitivePrice());
        order.setCumulatitiveQuantity(message.getCumulatitiveQuantity());
        order.setDateUpdated(new Date());
        orderService.saveOrder(order);
    }
}
