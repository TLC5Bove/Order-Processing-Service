package bove.order.processing.service.receiver.entity;

import bove.order.processing.service.config.MessagePublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class Subscriber implements MessageListener {

    MessagePublisher messagePublisher;
    Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer<>(Object.class);
    @Override
    public void onMessage(Message message, byte[] pattern) {
        Object obj = serializer.deserialize(message.getBody());
        messagePublisher.publish(obj);
        System.out.println(obj);
    }
}
