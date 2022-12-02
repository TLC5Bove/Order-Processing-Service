package bove.order.processing.service.receiver.repository;

import bove.order.processing.service.receiver.entity.MarketData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public class MarketDataRepository implements MarketDataDao {

//    private MarketDataRepository(){}

    private final String hashReference = "MarketData";

    @Autowired
    private RedisTemplate hashOperations;

    @Override
    public void saveData(MarketData data) {
        hashOperations.opsForHash().putIfAbsent(hashReference, data.getTicker(), data);
    }

    @Override
    public void saveAll(Map<String, MarketData> marketDataList) {
        hashOperations.opsForHash().putAll(hashReference, marketDataList);
        System.out.println("All saved!!");
    }

    @Override
    public void updateMarketData(MarketData data) {
        hashOperations.opsForHash().put(hashReference, data.getTicker(), data);
    }

    @Override
    public void deleteMarketData(String ticker) {
        hashOperations.opsForHash().delete(hashReference, ticker);
    }

    @Override
    public MarketData getMarketData(String ticker) {
        return (MarketData) hashOperations.opsForHash().get(hashReference, ticker);
    }

    @Override
    public Map getAllMarketData() {
        return hashOperations.opsForHash().entries(hashReference);
    }
}
