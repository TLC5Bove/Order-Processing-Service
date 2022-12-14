package bove.order.processing.service.service;

import bove.order.processing.service.dto.message.IdAndExchange;
import bove.order.processing.service.dto.order.Execution;
import bove.order.processing.service.dto.order.Order;
import bove.order.processing.service.dto.order.OrderRequest;
import bove.order.processing.service.dto.order.OrderStatusResponse;

import bove.order.processing.service.messaging.MQMessagePublisher;
import bove.order.processing.service.dto.order.enums.Action;
import bove.order.processing.service.dto.order.enums.ValidatorResponse;
import bove.order.processing.service.orderbook.model.*;
import bove.order.processing.service.orderbook.service.*;
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
    @Autowired
    AaplService aaplService;
    @Autowired
    AmznService amznService;
    @Autowired
    TslaService tslaService;
    @Autowired
    IbmService ibmService;
    @Autowired
    GooglService googlService;
    @Autowired
    MsftService msftService;
    @Autowired
    NflxService nflxService;
    @Autowired
    OrclService orclService;

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

    public String placeOrder(OrderRequest orderRequest) {
        String exchange1 = "exchange";
        String exchange2 = "exchange2";

        Action action = validator(orderRequest);
        // TODO: 1. ACTION = BOTH CALL SPLIT
        // TODO: 2. ACTION = EXCHANGE1 CALL SPLIT FOR EXCHANGE 1
        // TODO 3. ACTION == EXCHANGE2 CALL SPLIT FOR EXCHANGE2
        // TODO 4: ELSE RETURN ERROR WITH ACTION.VALUE();

        if (action.equals(Action.SPLIT)) {
            splitAndOrder(orderRequest, Action.SPLIT);
        } else if (action.equals(Action.EXCHANGE1)) {
            return splitAndOrder(orderRequest, Action.EXCHANGE1);
        } else if (action.equals(Action.EXCHANGE2)) {
            return splitAndOrder(orderRequest, Action.EXCHANGE2);
        }
        return action.value();
    }

    public List<Stock> getOpenOrders(String product, String side, int size){

        List<Stock> openOrders = new ArrayList<>();
        List<Ibm> ibmOrders;
        List<Aapl> aaplOrders;
        List<Amzn> amznOrders;
        List<Msft> msftOrders;
        List<Nflx> nflxOrders;
        List<Googl> googlOrders;
        List<Orcl> orclOrders;
        List<Tsla> tslaOrders;

        if (Objects.equals(product, "IBM")) {
            ibmOrders = ibmService.findAllPageAndSortBySide(side, size);
        }
        else {
            ibmOrders = new ArrayList<>();
        }

        if (Objects.equals(product, "AAPL")) {
            aaplOrders = aaplService.findAllPageAndSortBySide(side, size);
        }
        else {
            aaplOrders = new ArrayList<>();
        }

        if (Objects.equals(product, "AMZN")) {
            amznOrders = amznService.findAllPageAndSortBySide(side, size);
        }
        else {
            amznOrders = new ArrayList<>();
        }

        if (Objects.equals(product, "MSFT")) {
            msftOrders = msftService.findAllPageAndSortBySide(side, size);
        }
        else {
            msftOrders = new ArrayList<>();
        }

        if (Objects.equals(product, "NFLX")) {
            nflxOrders = nflxService.findAllPageAndSortBySide(side, size);
        }
        else {
            nflxOrders = new ArrayList<>();
        }

        if (Objects.equals(product, "GOOGL")) {
            googlOrders = googlService.findAllPageAndSortBySide(side, size);
        }
        else {
            googlOrders = new ArrayList<>();
        }

        if (Objects.equals(product, "ORCL")) {
            orclOrders = orclService.findAllPageAndSortBySide(side, size);
        }
        else {
            orclOrders = new ArrayList<>();
        }

        if (Objects.equals(product, "TSLA")) {
            tslaOrders = tslaService.findAllPageAndSortBySide(side, size);
        }else {
            tslaOrders = new ArrayList<>();
        }

        openOrders.addAll(ibmOrders);
        openOrders.addAll(aaplOrders);
        openOrders.addAll(amznOrders);
        openOrders.addAll(googlOrders);
        openOrders.addAll(nflxOrders);
        openOrders.addAll(msftOrders);
        openOrders.addAll(orclOrders);
        openOrders.addAll(tslaOrders);

        return openOrders;
    }

    public String splitAndOrder(OrderRequest orderRequest, Action action){
        String side = orderRequest.getSide();
        String product = orderRequest.getProduct();
        Integer quantity = orderRequest.getQuantity();
        String type = orderRequest.getType();
        Long portId = orderRequest.getPortfolioId();
        Long userId = orderRequest.getUserId();
        String osid = orderRequest.getOsId();

        List<Stock> openOrders = getOpenOrders(product, side, 200);



        if (action == Action.EXCHANGE1){
            openOrders = openOrders.stream().filter(order -> Objects.equals(order.getSide(), "exchange")).toList();

            splitOrders(openOrders, quantity, orderRequest);
            return "Order made on exchange 1";
            // split and order on exchange 1 only
        } else if (action == Action.EXCHANGE2) {
            // split and order on exchange 2 only
            openOrders = openOrders.stream().filter(order -> Objects.equals(order.getSide(), "exchange2")).toList();

            splitOrders(openOrders, quantity, orderRequest);
            return "Order made on exchange 2";
        }else {
            splitOrders(openOrders, quantity, orderRequest);
            // split and order on both exchanges
            return "Order split between exchanges";
        }
    }

    public void splitOrders(List<Stock> openOrders, Integer quantity, OrderRequest orderRequest){
        System.out.println("In actual Split");
        Double price = orderRequest.getPrice();
        if (openOrders.size() == 0){
            openOrders = getOpenOrders(orderRequest.getProduct(), orderRequest.getSide(), 500);
        }
        while (quantity > 0){
            System.out.println("entered while loop");
            Stock order;
            if (openOrders.size() == 0){
                order = null;
                System.out.println("placing my own order");
                orderRequest.setQuantity(quantity);
                decideExchangeToPlaceOrder(orderRequest, "exchange");
                break;
            }
            order = openOrders.remove(0);
            int orQuantity = order.getQuantity() - order.getCumulatitiveQuantity();
            System.out.println(order);

            OrderRequest or;
            if (Objects.equals(orderRequest.getSide(), "BUY")) {
                or = new OrderRequest(
                        orderRequest.getProduct(),
                        (quantity < orQuantity) ? quantity : orQuantity,
                        (price < order.getPrice()) ? price : order.getPrice(),
                        orderRequest.getSide(),
                        order.getOrderType(),
                        orderRequest.getPortfolioId(),
                        orderRequest.getUserId(),
                        orderRequest.getOsId(),
                        new Date()
                );
            }else{
                or = new OrderRequest(
                        orderRequest.getProduct(),
                        (quantity < orQuantity) ? quantity : orQuantity,
                        (price > order.getPrice()) ? price : order.getPrice(),
                        orderRequest.getSide(),
                        order.getOrderType(),
                        orderRequest.getPortfolioId(),
                        orderRequest.getUserId(),
                        orderRequest.getOsId(),
                        new Date()
                );
            }
            decideExchangeToPlaceOrder(or, order.getExchange());
            quantity = quantity - orQuantity;
        }
    }

    public String decideExchangeToPlaceOrder(OrderRequest orderRequest, String exchange) {
        System.out.println("Buying stocks now");
        WebClient webClient = WebClient.create("https://" + exchange + ".matraining.com");
        try {
            String response = webClient.post()
                    .uri("/" + exchangeAPIkey + "/order")
                    .body(Mono.just(orderRequest), Order.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println("Done buying stocks");
            assert response != null;
            String orderId = response.substring(1, response.length() - 1);

            System.out.println(orderId);

            IdAndExchange message = new IdAndExchange();
            message.setId(orderId);
            message.setExchange("exchange");

            mqMessagePublisher.publishMessageToOBS(message);
            mqMessagePublisher.publishMessageToLORS(message);

            saveOrder(orderRequest, orderId, exchange);

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
                        orderRequest.getUserId(),
                        orderRequest.getOsId()
                )
        );
    }

    public void placeCancelOrder(Order order) {
        if (Objects.equals(order.getStatus(), "pending") || Objects.equals(order.getStatus(), "partial")) {
            WebClient webClient = WebClient.create("https://" + order.getExchange() + ".matraining.com");

            Boolean response = webClient.delete()
                    .uri("/" + exchangeAPIkey + "/order/" + order.getOrderID())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            assert response != null;
            cancelOrder(response, order);
        }
    }

    private void cancelOrder(Boolean response, Order order) {
        if (response) order.setStatus("cancelled");
        orderRepo.save(order);
    }

}