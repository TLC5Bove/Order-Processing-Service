package bove.order.processing.service.receiver.service;

import bove.order.processing.service.receiver.dto.StockResponseDto;
import bove.order.processing.service.receiver.entity.MarketData;
import bove.order.processing.service.receiver.repository.MarketDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReceiverService {

    @Autowired
    private final MarketDataRepository marketDataRepository;

    public void updateCache(String ticker, StockResponseDto stockResponseDto){
        MarketData dataToBeUpdated = marketDataRepository.getMarketData(ticker);
        System.out.println(dataToBeUpdated);
        System.out.println(stockResponseDto);


    }

    public MarketData getMarketDataFromCache(String ticker){
        return marketDataRepository.getMarketData(ticker);
    }
}