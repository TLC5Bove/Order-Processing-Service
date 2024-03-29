package bove.order.processing.service.messaging;

import bove.order.processing.service.config.RabbitConfig;
import bove.order.processing.service.dto.message.IdAndExchange;
import bove.order.processing.service.dto.message.OsidQuantityPrice;
import bove.order.processing.service.dto.order.CompleteOrder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQMessagePublisher {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void publishMessageToOBS(IdAndExchange message){
        rabbitTemplate.convertAndSend(RabbitConfig.OBS_EXCHANGE, RabbitConfig.ROUTING_KEY, message);
    }

    public void publishMessageToLORS(IdAndExchange message){
        rabbitTemplate.convertAndSend(RabbitConfig.TRACKING_EXCHANGE, RabbitConfig.ROUTING_KEY, message);
    }

    public void publishOrderCompletionMessage(CompleteOrder message){
        rabbitTemplate.convertAndSend(RabbitConfig.CLIENT_EXCHANGE, RabbitConfig.ROUTING_KEY, message);
    }

    public void publishCancelOrderToLogging(String id){
        rabbitTemplate.convertAndSend(RabbitConfig.CANCEL_FROM_OPS_EXCHANGE, RabbitConfig.ROUTING_KEY, id);
    }

    public void publishCancelledOrderToClient(OsidQuantityPrice cancelled){
        rabbitTemplate.convertAndSend(RabbitConfig.CANC_COMP_FROM_OPS_QUEUE, RabbitConfig.ROUTING_KEY, cancelled);
    }
}
