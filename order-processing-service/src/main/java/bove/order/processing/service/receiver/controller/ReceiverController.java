package bove.order.processing.service.receiver.controller;

import bove.order.processing.service.receiver.dto.StockResponseDto;
import bove.order.processing.service.receiver.entity.MarketData;
import bove.order.processing.service.receiver.event.MessageEvent;
import bove.order.processing.service.receiver.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@Controller
public class ReceiverController {
//    @Autowired
    @Autowired
    ReceiverService receiverService;
    public void updateCache( String ticker, StockResponseDto stock){
        receiverService.updateCache(ticker, stock);
    }

    @Async
    @EventListener()
    public void updateFromEvent(MessageEvent message){
        String response = message.getSource().toString();

        String refactored = response.replace('{', ' ').replace('}', ' ').strip();
        List<String> res = Stream.of(refactored.split(",")).map(str -> str.split("=")[1]).toList();

        String exchange = res.get(2).replace('\"', ' ').strip();
        String ticker = res.get(1).replace('\"', ' ').strip() + "_" + exchange;
        String side = res.get(4).replace('\"', ' ').strip();
        double price = Double.parseDouble(res.get(3));

        StockResponseDto stock = new StockResponseDto(ticker, price, side, exchange);
        updateCache(ticker, stock);
    }

    public MarketData getMarketDataFromCache(String ticker){
        return receiverService.getMarketDataFromCache(ticker);
    }
}
