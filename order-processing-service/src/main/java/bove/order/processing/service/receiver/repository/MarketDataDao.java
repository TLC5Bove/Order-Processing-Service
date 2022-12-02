package bove.order.processing.service.receiver.repository;

import bove.order.processing.service.receiver.entity.MarketData;

import java.util.Map;

public interface MarketDataDao {
    void saveData(MarketData data);
    void saveAll(Map<String, MarketData> marketDataList);
    void updateMarketData(MarketData data);
    void deleteMarketData(String ticker);
    MarketData getMarketData(String ticker);
    Map<String, MarketData> getAllMarketData();

}
