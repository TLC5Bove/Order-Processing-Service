package bove.order.processing.service.receiver.entity;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class Subscriber implements MessageListener {
    Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer<>(Object.class);
    @Override
    public void onMessage(Message message, byte[] pattern) {
        Object obj = serializer.deserialize(message.getBody());
        System.out.println(obj);
    }
}
