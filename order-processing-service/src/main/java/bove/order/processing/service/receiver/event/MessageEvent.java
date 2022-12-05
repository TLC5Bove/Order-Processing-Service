package bove.order.processing.service.receiver.event;

import org.springframework.context.ApplicationEvent;

public class MessageEvent extends ApplicationEvent {
    public MessageEvent(Object source) {
        super(source);
    }
}
