package bove.order.processing.service.mqProducer;

import bove.order.processing.service.config.RabbitConfig;
import bove.order.processing.service.dto.message.IdAndExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQMessagePublisher {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void publishMessage(IdAndExchange message){
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, message);
    }
}
