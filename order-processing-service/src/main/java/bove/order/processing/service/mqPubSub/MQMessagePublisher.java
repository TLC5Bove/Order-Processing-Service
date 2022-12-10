package bove.order.processing.service.mqPubSub;

import bove.order.processing.service.config.RabbitConfig;
import bove.order.processing.service.dto.message.IdAndExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQMessagePublisher {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void publishMessageToODS(IdAndExchange message){
        rabbitTemplate.convertAndSend(RabbitConfig.OBS_EXCHANGE, RabbitConfig.ROUTING_KEY, message);
    }

    public void publishMessageToLORS(IdAndExchange message){
        rabbitTemplate.convertAndSend(RabbitConfig.TRACKING_EXCHANGE, RabbitConfig.ROUTING_KEY, message);
    }
}
