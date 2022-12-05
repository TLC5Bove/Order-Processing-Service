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
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    OrderValidatorService orderValidatorService;
    @Value("${order.API_KEY}")
    private String exchangeAPIkey;

    @Value("${order.EXCHANGE_URL}")
    private String exchangeURL;
    @Value("${order.EXCHANGE2_URL}")
    private String exchange2URL;

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

        List<String> validationResults = validator(orderRequest);
        if (Objects.equals(validationResults.get(0), "Fail")) {
            String addon = "";
            if (Objects.equals(validationResults.get(1), "Failure")) {
                addon += "Quantity of Stock to buy exceeded limit.";
            }
            if (Objects.equals(validationResults.get(2), "Failure")) {
                addon += "";
            }
            return "Error: " + addon;
        }

        // Else use info as you see fit.
        // If it is SuccessBoth, you can exchange from any of the exchanges
        // If it is exchange 1 or 2 then you can only work with the valid one.

        WebClient webClient = WebClient.create("https://" + exchange + ".matraining.com");
        try {
            String response = webClient.post()
                    .uri("/" + exchangeAPIkey + "/order")
                    .body(Mono.just(orderRequest), Order.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            assert response != null;
            String orderId = response.substring(1, response.length() - 1);
            saveOrder(orderRequest, orderId, "exchange 1");
            return orderId;
        } catch (Exception e) {
            return "Error => " + e;
        }
    }

    public void saveOrder(OrderRequest orderRequest, String orderId, String exchange) {
        orderRepo.save(new Order(orderId,
                orderRequest.getProduct(),
                orderRequest.getQuantity(),
                orderRequest.getPrice(),
                orderRequest.getSide(),
                orderRequest.getType(),
                new Date(),
                exchange,
                orderRequest.getUserId()));

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