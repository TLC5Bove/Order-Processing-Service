package bove.order.processing.service.receiver.controller;

import bove.order.processing.service.receiver.dto.StockResponseDto;
import bove.order.processing.service.receiver.entity.MarketDataCache;
import bove.order.processing.service.receiver.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ReceiverController {
//    @Autowired
    @Autowired
    ReceiverService receiverService;
    public void updateCache( String ticker, StockResponseDto stock){
        receiverService.updateCache(ticker, stock);
    }

    public MarketDataCache getMarketDataFromCache(String ticker){
        return receiverService.getMarketDataFromCache(ticker);
    }
}
