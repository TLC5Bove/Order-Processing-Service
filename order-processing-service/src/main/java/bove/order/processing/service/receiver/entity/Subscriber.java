package bove.order.processing.service.receiver.entity;

import bove.order.processing.service.config.MessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class Subscriber implements MessageListener {

    @Autowired
    MessagePublisher messagePublisher;
    Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer<>(Object.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        Object obj = serializer.deserialize(message.getBody());
        messagePublisher.publish(obj);
    }
}
