package bove.order.processing.service.receiver.repository;

import bove.order.processing.service.receiver.entity.MarketDataCache;

import java.util.Map;

public interface MarketDataDao {
    void saveData(MarketDataCache data);
    void saveAll(Map<String, MarketDataCache> marketDataList);
    void updateMarketData(MarketDataCache data);
    void deleteMarketData(String ticker);
    MarketDataCache getMarketData(String ticker);
    Map<String, MarketDataCache> getAllMarketData();

}
