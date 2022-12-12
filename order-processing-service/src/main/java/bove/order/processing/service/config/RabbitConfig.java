package bove.order.processing.service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String OBS_QUEUE = "obs_queue";
    public static final String TRACKING_QUEUE = "tracking_queue";
    public static final String COMPLETION_QUEUE = "completion_queue";
    public static final String CLIENT_QUEUE = "client_queue";
    public static final String ORDER_QUEUE = "order_queue";
    public static final String OBS_EXCHANGE = "obs_exchange";
    public static final String TRACKING_EXCHANGE = "tracking_exchange";
    public static final String COMPLETION_EXCHANGE = "completion_exchange";
    public static final String CLIENT_EXCHANGE = "completion_exchange";
    public static final String ORDER_EXCHANGE = "order_exchange";
    public static final String ROUTING_KEY = "message_routingKey";

    @Bean
    public Queue obs_queue() {
        return  new Queue(OBS_QUEUE);
    }

    @Bean
    public Queue tracking_queue() {
        return  new Queue(TRACKING_QUEUE);
    }

    @Bean
    public Queue completion_queue() {
        return  new Queue(COMPLETION_QUEUE);
    }

    @Bean
    public Queue client_queue() { return new Queue(CLIENT_QUEUE); }

    @Bean
    public Queue order_queue() { return new Queue(ORDER_QUEUE); }

    @Bean
    public DirectExchange obs_exchange() {
        return new DirectExchange(OBS_EXCHANGE);
    }

    @Bean
    public DirectExchange tracking_exchange() {
        return new DirectExchange(TRACKING_EXCHANGE);
    }

    @Bean
    public DirectExchange completion_exchange() {
        return new DirectExchange(COMPLETION_EXCHANGE);
    }

    @Bean
    public DirectExchange client_exchange() {
        return new DirectExchange(CLIENT_EXCHANGE);
    }

    @Bean
    public DirectExchange order_exchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding obs_binding() {
        return BindingBuilder
                .bind(obs_queue())
                .to(obs_exchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding tracking_binding() {
        return BindingBuilder
                .bind(tracking_queue())
                .to(tracking_exchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding completion_binding() {
        return BindingBuilder
                .bind(completion_queue())
                .to(completion_exchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding client_binding() {
        return BindingBuilder
                .bind(client_queue())
                .to(client_exchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding order_binding() {
        return BindingBuilder
                .bind(order_queue())
                .to(order_exchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return  new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return  template;
    }
}
