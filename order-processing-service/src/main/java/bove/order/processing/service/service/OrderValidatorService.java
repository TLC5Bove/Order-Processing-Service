package bove.order.processing.service.service;

import bove.order.processing.service.dto.order.OrderRequest;
import bove.order.processing.service.receiver.controller.ReceiverController;
import bove.order.processing.service.receiver.entity.MarketData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OrderValidatorService {

    @Autowired
    private ReceiverController receiverController;

    // Validate that client has sufficient funds to place order
    public boolean clientHasSufficientFunds(String clientId){
        // TODO
        return false;
    }

    // validate that client owns the stock they are selling
    public boolean clientIsOwnerOfStock(OrderRequest orderRequest){
        // TODO
        return false;
    }

    // limit checking - ensure the clients aren't more than a certain number of orders - limit orders
    public String quantityIsWithinLimit(OrderRequest orderRequest){
        // TODO
        String validator = "QuantityWithinLimit: ";

        String product = orderRequest.getProduct();
        String tickerForExchange1 = product+"_exchange 1";
        String tickerForExchange2 = product+"_exchange 2";

        MarketData marketDataExchange1 = receiverController.getMarketDataFromCache(tickerForExchange1);
        MarketData marketDataExchange2 = receiverController.getMarketDataFromCache(tickerForExchange2);

        if (Objects.equals(orderRequest.getSide(), "SELL")){
            if (orderRequest.getQuantity() <= marketDataExchange1.getSellLimit() &&
                    (orderRequest.getQuantity() <= marketDataExchange2.getSellLimit())){
                return validator + "Success_Both";
            } else if (orderRequest.getQuantity() <= marketDataExchange1.getSellLimit()) {
                return validator + "Success_Exchange 1";
            } else if (orderRequest.getQuantity() <= marketDataExchange2.getSellLimit()) {
                return validator + "Success_Exchange 2";
            }
            else {
                return validator + "Failure";
            }
        }
        else {
            if (orderRequest.getQuantity() <= marketDataExchange1.getBuyLimit() &&
                    (orderRequest.getQuantity() <= marketDataExchange2.getBuyLimit())){
                return validator + "Success - valid for both exchanges";
            } else if (orderRequest.getQuantity() <= marketDataExchange1.getBuyLimit()) {
                return validator + "Success - valid for Exchange 1";
            } else if (orderRequest.getQuantity() <= marketDataExchange2.getBuyLimit()) {
                return validator + "Success - valid for Exchange 2";
            }
            else {
                return validator + "Failure - Limit exceeded.";
            }
        }
    }

    // price difference is within price shift range
    public String orderPriceIsWithinRange(OrderRequest orderRequest){
        // TODO
        String validator = "PriceWithinRange: ";

        String product = orderRequest.getProduct();
        String tickerForExchange1 = product+"_exchange 1";
        String tickerForExchange2 = product+"_exchange 2";

        MarketData marketDataExchange1 = receiverController.getMarketDataFromCache(tickerForExchange1);
        MarketData marketDataExchange2 = receiverController.getMarketDataFromCache(tickerForExchange2);

        if (Objects.equals(orderRequest.getSide(), "SELL")){
            if ((Math.abs(orderRequest.getPrice() - marketDataExchange1.getAskPrice()) <= marketDataExchange1.getMaxPriceShift()) &&
                    Math.abs(orderRequest.getPrice() - marketDataExchange2.getAskPrice()) <= marketDataExchange2.getMaxPriceShift()){
                return validator + "Success_Both";
            } else if (Math.abs(orderRequest.getPrice() - marketDataExchange1.getAskPrice()) <= marketDataExchange1.getMaxPriceShift()) {
                return validator + "Success_Exchange 1";
            } else if (Math.abs(orderRequest.getPrice() - marketDataExchange2.getAskPrice()) <= marketDataExchange2.getMaxPriceShift()) {
                return validator + "Success_Exchange 2";
            }
            else {
                return validator + "Failure";
            }
        }
        else {
            if ((Math.abs(orderRequest.getPrice() - marketDataExchange1.getBidPrice()) <= marketDataExchange1.getMaxPriceShift()) &&
                    Math.abs(orderRequest.getPrice() - marketDataExchange2.getBidPrice()) <= marketDataExchange2.getMaxPriceShift()){
                return validator + "Success_Both";
            } else if (Math.abs(orderRequest.getPrice() - marketDataExchange1.getBidPrice()) <= marketDataExchange1.getMaxPriceShift()) {
                return validator + "Success_Exchange 1";
            } else if (Math.abs(orderRequest.getPrice() - marketDataExchange2.getBidPrice()) <= marketDataExchange2.getMaxPriceShift()) {
                return validator + "Success_Exchange 2";
            }
            else {
                return validator + "Failure";
            }
        }
    }



}