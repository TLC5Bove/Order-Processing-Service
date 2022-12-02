package bove.order.processing.service.receiver.entity;

import bove.order.processing.service.config.MessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class Subscriber implements MessageListener {
    @Autowired
    private MessagePublisher messagePublisher;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        messagePublisher.publish(message);
    }
}
