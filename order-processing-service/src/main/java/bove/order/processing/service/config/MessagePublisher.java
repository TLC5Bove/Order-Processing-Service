package bove.order.processing.service.config;

import bove.order.processing.service.receiver.event.MessageEvent;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Resource
public class MessagePublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public MessagePublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(Object obj){
        applicationEventPublisher.publishEvent(new MessageEvent(obj));
    }
}
