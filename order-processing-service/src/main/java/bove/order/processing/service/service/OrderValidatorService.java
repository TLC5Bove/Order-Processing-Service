package bove.order.processing.service.service;

import bove.order.processing.service.dto.order.OrderRequest;
import bove.order.processing.service.dto.order.enums.ValidatorResponse;
import bove.order.processing.service.receiver.controller.ReceiverController;
import bove.order.processing.service.receiver.entity.MarketDataCache;
import bove.order.processing.service.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OrderValidatorService {

    @Autowired
    private ReceiverController receiverController;
    @Autowired
    private OrderRepo orderRepo;

    // limit checking - ensure the clients aren't more than a certain number of orders - limit orders
    public ValidatorResponse quantityIsWithinLimit(OrderRequest orderRequest) {

        String product = orderRequest.getProduct();
        String tickerForExchange1 = product + "_exchange 1";
        String tickerForExchange2 = product + "_exchange 2";

        MarketDataCache marketDataCacheExchange1 = receiverController.getMarketDataFromCache(tickerForExchange1);
        MarketDataCache marketDataCacheExchange2 = receiverController.getMarketDataFromCache(tickerForExchange2);
        if (Objects.equals(orderRequest.getSide(), "SELL")) {
            if (orderRequest.getQuantity() <= marketDataCacheExchange1.getSELL_LIMIT() &&
                    (orderRequest.getQuantity() <= marketDataCacheExchange2.getSELL_LIMIT())) {
                return ValidatorResponse.SUCCESS_BOTH;
            } else if (orderRequest.getQuantity() <= marketDataCacheExchange1.getSELL_LIMIT()) {
                return ValidatorResponse.SUCCESS_EXCHANGE1;
            } else if (orderRequest.getQuantity() <= marketDataCacheExchange2.getSELL_LIMIT()) {
                return ValidatorResponse.SUCCESS_EXCHANGE2;
            } else {
                return ValidatorResponse.FAIL;
            }
        } else {
            if (orderRequest.getQuantity() <= marketDataCacheExchange1.getBUY_LIMIT() &&
                    (orderRequest.getQuantity() <= marketDataCacheExchange2.getBUY_LIMIT())) {
                return ValidatorResponse.SUCCESS_BOTH;
            } else if (orderRequest.getQuantity() <= marketDataCacheExchange1.getBUY_LIMIT()) {
                return ValidatorResponse.SUCCESS_EXCHANGE1;
            } else if (orderRequest.getQuantity() <= marketDataCacheExchange2.getBUY_LIMIT()) {
                return ValidatorResponse.SUCCESS_EXCHANGE2;
            } else {
                return ValidatorResponse.FAIL;
            }
        }
    }

    // price difference is within price shift range
    public ValidatorResponse orderPriceIsWithinRange(OrderRequest orderRequest) {
        String product = orderRequest.getProduct();
        String tickerForExchange1 = product + "_exchange 1";
        String tickerForExchange2 = product + "_exchange 2";

        MarketDataCache marketDataCacheExchange1 = receiverController.getMarketDataFromCache(tickerForExchange1);
        MarketDataCache marketDataCacheExchange2 = receiverController.getMarketDataFromCache(tickerForExchange2);

        if (Objects.equals(orderRequest.getSide(), "SELL")) {
            if ((Math.abs(orderRequest.getPrice() - marketDataCacheExchange1.getASK_PRICE()) <= marketDataCacheExchange1.getMAX_PRICE_SHIFT()) &&
                    Math.abs(orderRequest.getPrice() - marketDataCacheExchange2.getASK_PRICE()) <= marketDataCacheExchange2.getMAX_PRICE_SHIFT()) {
                return ValidatorResponse.SUCCESS_BOTH;
            } else if (Math.abs(orderRequest.getPrice() - marketDataCacheExchange1.getASK_PRICE()) <= marketDataCacheExchange1.getMAX_PRICE_SHIFT()) {
                return ValidatorResponse.SUCCESS_EXCHANGE1;
            } else if (Math.abs(orderRequest.getPrice() - marketDataCacheExchange2.getASK_PRICE()) <= marketDataCacheExchange2.getMAX_PRICE_SHIFT()) {
                return ValidatorResponse.SUCCESS_EXCHANGE2;
            } else {
                return ValidatorResponse.FAIL;
            }
        } else {
            if ((Math.abs(orderRequest.getPrice() - marketDataCacheExchange1.getBID_PRICE()) <= marketDataCacheExchange1.getMAX_PRICE_SHIFT()) &&
                    Math.abs(orderRequest.getPrice() - marketDataCacheExchange2.getBID_PRICE()) <= marketDataCacheExchange2.getMAX_PRICE_SHIFT()) {
                return ValidatorResponse.SUCCESS_BOTH;
            } else if (Math.abs(orderRequest.getPrice() - marketDataCacheExchange1.getBID_PRICE()) <= marketDataCacheExchange1.getMAX_PRICE_SHIFT()) {
                return ValidatorResponse.SUCCESS_EXCHANGE1;
            } else if (Math.abs(orderRequest.getPrice() - marketDataCacheExchange2.getBID_PRICE()) <= marketDataCacheExchange2.getMAX_PRICE_SHIFT()) {
                return ValidatorResponse.SUCCESS_EXCHANGE2;
            } else {
                return ValidatorResponse.FAIL;
            }
        }
    }


}
