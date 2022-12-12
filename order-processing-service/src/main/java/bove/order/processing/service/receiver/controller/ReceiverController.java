package bove.order.processing.service.receiver.controller;

import bove.order.processing.service.receiver.dto.StockResponseDto;
import bove.order.processing.service.receiver.entity.MarketData;
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

    public MarketData getMarketDataFromCache(String ticker){
        return receiverService.getMarketDataFromCache(ticker);
    }
}
