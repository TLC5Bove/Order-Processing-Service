package bove.order.processing.service.service;

import bove.order.processing.service.dto.message.IdAndExchange;
import bove.order.processing.service.dto.order.Execution;
import bove.order.processing.service.dto.order.Order;
import bove.order.processing.service.dto.order.OrderRequest;
import bove.order.processing.service.dto.order.OrderStatusResponse;
import bove.order.processing.service.messaging.MQMessagePublisher;
import bove.order.processing.service.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    ExecutionService executionService;
    @Autowired
    OrderValidatorService orderValidatorService;
    @Autowired
    MQMessagePublisher mqMessagePublisher;
    @Value("${order.API_KEY}")
    private String exchangeAPIkey;
    @Value("${order.EXCHANGE_URL}")
    private String exchangeURL;
    @Value("${order.EXCHANGE2_URL}")
    private String exchange2URL;

    public Order findById(String orderId) {
        return orderRepo.findById(orderId).orElse(null);
    }

    public void saveOrder(Order order) {
        orderRepo.save(order);
    }

    // validate the request
    public List<String> validator(OrderRequest orderRequest) {
        String stringResults = "";

        List<String> limit = List.of(orderValidatorService.quantityIsWithinLimit(orderRequest).split(":"));
        List<String> priceRange = List.of(orderValidatorService.orderPriceIsWithinRange(orderRequest).split(":"));

        if (!Objects.equals(limit.get(1), "Failure") && !Objects.equals(priceRange.get(1), "Failure")) {
            stringResults += "Success, " + limit.get(1) + ", " + priceRange.get(1);
        } else {
            stringResults += "Fail, " + limit.get(1) + ", " + priceRange.get(1);
        }

        return List.of(stringResults.split(","));
    }

    public String placeOrder(OrderRequest orderRequest, String exchange) {
        // to validate validation list for success,
        // index 0: the overall Status, success for failure
        // index 1: Status for buy or sell limit
        // index 2: Status for bid or price shift range

//        List<String> validationResults = validator(orderRequest);
//        if (Objects.equals(validationResults.get(0), "Fail")) {
//            String addon = "";
//            if (Objects.equals(validationResults.get(1), "Failure")) {
//                addon += "Quantity of Stock to buy exceeded limit.";
//            }
//            if (Objects.equals(validationResults.get(2), "Failure")) {
//                addon += "";
//            }
//            return "Error: " + addon;
//        }

        // Else use info as you see fit.
        // If it is SuccessBoth, you can exchange from any of the exchanges
        // If it is exchange 1 or 2 then you can only work with the valid one.

        WebClient webClient = WebClient.create("https://exchange.matraining.com");
        try {
            String response = webClient.post()
                    .uri("/" + exchangeAPIkey + "/order")
                    .body(Mono.just(orderRequest), Order.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            assert response != null;

            String orderId = response.substring(1, response.length() - 1);

            IdAndExchange message = new IdAndExchange();
            message.setId(orderId);
            message.setExchange("exchange");

            mqMessagePublisher.publishMessageToODS(message);
            mqMessagePublisher.publishMessageToLORS(message);

            saveOrder(orderRequest, orderId, "exchange");

            return orderId;
        } catch (Exception e) {
            return "Error => " + e;
        }
    }

    public void saveOrder(OrderRequest orderRequest, String orderId, String exchange) {
        saveOrder(new Order(orderId,
                orderRequest.getProduct(),
                orderRequest.getQuantity(),
                orderRequest.getPrice(),
                orderRequest.getSide(),
                orderRequest.getType(),
                new Date(),
                exchange,
                orderRequest.getUserId())
        );
    }

    public OrderStatusResponse getOrderStatus(String orderId, String exchange) {
        WebClient webClient = WebClient.create("https://exchange2.matraining.com");

        OrderStatusResponse response = webClient.get()
                .uri("/" + exchangeAPIkey + "/order/" + orderId)
                .retrieve()
                .bodyToMono(OrderStatusResponse.class)
                .block();

        assert response != null;
        checkOrderExecutionStatus(response, orderId);
        return response;
    }

    private void checkOrderExecutionStatus(OrderStatusResponse response, String orderId) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Starting");
        Order order = findById(orderId);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> passed find order");

        if (order == null) return;

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> passed first check");

        if (response.getExecutions() == order.getExecutions()) return;

        if (Objects.equals(order.getStatus(), "complete")) return;

        if (response.getExecutions() == null) return;

        if (response.getQuantity() >= 1 && response.getQuantity() > response.getCumulatitiveQuantity()) {
            order.setStatus("partial");
        } else {
            order.setStatus("complete");
        }

        for (Execution execution : response.getExecutions()) {
            if (order.getExecutions().contains(execution)) continue;
            execution.setOrder(order);
            executionService.save(execution);
        }
        order.setCumulatitivePrice(response.getCumulatitivePrice());
        order.setCumulatitiveQuantity(response.getCumulatitiveQuantity());
        order.setDateUpdated(new Date());
        orderRepo.save(order);
    }

    public void placeCancelOrder(String orderId, String exchange) {
        Optional<Order> ord = orderRepo.findById(orderId);

        if (ord.isEmpty())
            return;

        Order order = ord.get();
        if (order.getStatus() == "pending" || order.getStatus() == "partial") {
            WebClient webClient = WebClient.create("https://" + exchange + ".matraining.com");

            Boolean response = webClient.delete()
                    .uri("/" + exchangeAPIkey + "/order/" + orderId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            cancelOrder(response, order);

        }

    }

    private void cancelOrder(Boolean response, Order order) {
        if (response) order.setStatus("cancelled");
    }

}