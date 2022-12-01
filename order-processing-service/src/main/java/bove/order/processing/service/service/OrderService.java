package bove.order.processing.service.service;

import bove.order.processing.service.dto.order.Order;
import bove.order.processing.service.dto.order.OrderRequest;
import bove.order.processing.service.dto.order.OrderStatusResponse;
import bove.order.processing.service.repository.OrderRepo;
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

    @Value("${order.EXCHANGE_URL}")
    private String exchangeURL;
    @Value("${order.EXCHANGE2_URL}")
    private String exchange2URL;

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
        System.out.println();
    }

    public OrderStatusResponse getOrderStatus(String orderId) {
        WebClient webClient = WebClient.create(exchangeURL);

        OrderStatusResponse response = webClient.get()
                .uri("/" + exchangeAPIkey + "/order/" + orderId)
                .retrieve()
                .bodyToMono(OrderStatusResponse.class)
                .block();

        assert response != null;
        checkOrderExecutionStatus(response, orderId);

        return response;
    }

    public String checkOrderExecutionStatus(OrderStatusResponse response, String orderId) {

        Order order = orderRepo.findById(orderId).get();
        if (order.getStatus() == "complete") return "";

        Integer unexecutedQuantity = response.getQuantity() - response.getCumulatitiveQuantity();
        if (response.getExecutions() == null) {
            order.setStatus("pending");
            order.setDateUpdated(new Date());
            orderRepo.save(order);
            return "This order with ID " + response.getOrderID() + " has not been executed";
        } else if (response.getQuantity() >= 1 && response.getQuantity() > response.getCumulatitiveQuantity()) {
            order.setStatus("partial");
            order.setDateUpdated(new Date());
            orderRepo.save(order);
            return "This order with ID " + response.getOrderID() + " has been partially executed \n " + "" + unexecutedQuantity + "to " + response.getSide();
        } else {
            order.setStatus("complete");
            order.setDateUpdated(new Date());
            orderRepo.save(order);
            return "This order with ID " + response.getOrderID() + " has been fully executed";
        }

    }

}