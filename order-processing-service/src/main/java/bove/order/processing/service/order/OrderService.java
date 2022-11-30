package bove.order.processing.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class OrderService {
    @Autowired
    OrderRepo orderRepo;
    @Value("${order.API_KEY}")
    private String exchangeAPIkey;

    @Value("${order.API_URL}")
    private String exchangeURL;

    public String placeOrder(OrderRequest orderRequest) {
        WebClient webClient = WebClient.create(exchangeURL);
        try {
            String response = webClient.post()
                    .uri("/" + exchangeAPIkey + "/order")
                    .body(Mono.just(orderRequest), Order.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            String orderId = response.substring(1, response.length() - 1);
            saveOrder(orderRequest, orderId);
            return orderId;
        } catch (Exception e) {
            return "Error" + e;
        }
    }

    public void saveOrder(OrderRequest orderRequest, String orderId) {
        orderRepo.save(new Order(orderId,
                orderRequest.getProduct(),
                orderRequest.getQuantity(),
                orderRequest.getPrice(),
                orderRequest.getSide(),
                orderRequest.getType(), new Date()));
    }
}
