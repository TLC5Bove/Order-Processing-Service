package bove.order.processing.service.service;

import bove.order.processing.service.dto.message.IdAndExchange;
import bove.order.processing.service.dto.order.Execution;
import bove.order.processing.service.dto.order.Order;
import bove.order.processing.service.dto.order.OrderRequest;
import bove.order.processing.service.dto.order.OrderStatusResponse;

import bove.order.processing.service.messaging.MQMessagePublisher;
import bove.order.processing.service.dto.order.enums.Action;
import bove.order.processing.service.dto.order.enums.ValidatorResponse;
import bove.order.processing.service.repository.ExecutionRepo;
import bove.order.processing.service.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

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
    @Autowired
    private ExecutionRepo executionRepo;

    public Order findById(String orderId) {
        return orderRepo.findById(orderId).orElse(null);
    }

    public void saveOrder(Order order) {
        orderRepo.save(order);
    }

    public List<Order> findAllByOsId(String osid){
        return orderRepo.findAllByOsId(osid);
    }

    // validate the request
    public Action validator(OrderRequest orderRequest) {
        String stringResults = "";

        ValidatorResponse quantityLimit = orderValidatorService.quantityIsWithinLimit(orderRequest);
        ValidatorResponse priceRange = orderValidatorService.orderPriceIsWithinRange(orderRequest);

        if (quantityLimit.equals(ValidatorResponse.SUCCESS_BOTH) && priceRange.equals(ValidatorResponse.SUCCESS_BOTH)) {
            return Action.SPLIT;
        }
        if (quantityLimit.equals(ValidatorResponse.SUCCESS_BOTH) && priceRange.equals(ValidatorResponse.SUCCESS_EXCHANGE1) ||
                (quantityLimit.equals(ValidatorResponse.SUCCESS_EXCHANGE1) && priceRange.equals(ValidatorResponse.SUCCESS_BOTH))) {
            return Action.EXCHANGE1;
        }
        if (quantityLimit.equals(ValidatorResponse.SUCCESS_BOTH) && priceRange.equals(ValidatorResponse.SUCCESS_EXCHANGE2) ||
                (quantityLimit.equals(ValidatorResponse.SUCCESS_EXCHANGE2) && priceRange.equals(ValidatorResponse.SUCCESS_BOTH))) {
            return Action.EXCHANGE2;
        }
        if (quantityLimit.equals(ValidatorResponse.SUCCESS_EXCHANGE1) &&
                priceRange.equals(ValidatorResponse.SUCCESS_EXCHANGE1)) {
            return Action.EXCHANGE1;
        }
        if (quantityLimit.equals(ValidatorResponse.SUCCESS_EXCHANGE2) &&
                priceRange.equals(ValidatorResponse.SUCCESS_EXCHANGE2)) {
            return Action.EXCHANGE2;
        }
        if (quantityLimit.equals(ValidatorResponse.SUCCESS_EXCHANGE1) &&
                priceRange.equals(ValidatorResponse.SUCCESS_EXCHANGE2)) {
            return Action.BOTH;
        }
        if (quantityLimit.equals(ValidatorResponse.SUCCESS_EXCHANGE2) &&
                priceRange.equals(ValidatorResponse.SUCCESS_EXCHANGE1)) {
            return Action.BOTH;
        }
        if (quantityLimit.equals(ValidatorResponse.FAIL) && priceRange.equals(ValidatorResponse.FAIL)) {
            return Action.BOTH;
        }
        if (quantityLimit.equals(ValidatorResponse.FAIL)) {
            return Action.INVALID_LIMIT;
        }
        if (priceRange.equals(ValidatorResponse.FAIL)) {
            return Action.RANGE_EXCEEDED;
        } else {
            return Action.BOTH;
        }

    }

    public void placeOrder(OrderRequest orderRequest) {
        String exchange1 = "exchange";
        String exchange2 = "exchange2";

//        Action action = validator(orderRequest);
        // TODO: 1. ACTION = BOTH CALL SPLIT
        // TODO: 2. ACTION = EXCHANGE1 CALL SPLIT FOR EXCHANGE 1
        // TODO 3. ACTION == EXCHANGE2 CALL SPLIT FOR EXCHANGE2
        // TODO 4: ELSE RETURN ERROR WITH ACTION.VALUE();

//        if (action.equals(Action.BOTH)) {
//            splitOrder(orderRequest);
//        } else if (action.equals(Action.EXCHANGE1)) {
//            return decideExchangeToPlaceOrder(orderRequest, exchange1);
//        } else if (action.equals(Action.EXCHANGE2)) {
//            return decideExchangeToPlaceOrder(orderRequest, exchange2);
//        }
//        return action.value();
        decideExchangeToPlaceOrder(orderRequest, "exchange");
    }

    public String decideExchangeToPlaceOrder(OrderRequest orderRequest, String exchange) {

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

            IdAndExchange message = new IdAndExchange();
            message.setId(orderId);
            message.setExchange("exchange");

            mqMessagePublisher.publishMessageToOBS(message);
            mqMessagePublisher.publishMessageToLORS(message);

            saveOrder(orderRequest, orderId, "exchange");

            return orderId;
        } catch (Exception e) {
            return "Error => " + e;
        }
    }

    public void splitOrder(OrderRequest orderRequest) {
        System.out.println("split");
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
                        orderRequest.getUserId(),
                        orderRequest.getOsId()
                )
        );
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
            assert response != null;
            cancelOrder(response, order);

        }

    }

    private void cancelOrder(Boolean response, Order order) {
        if (response) order.setStatus("cancelled");
    }

}