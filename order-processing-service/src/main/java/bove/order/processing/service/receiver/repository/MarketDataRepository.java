package bove.order.processing.service.receiver.repository;

import bove.order.processing.service.receiver.entity.MarketDataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import com.google.gson.Gson;
import java.util.Map;

@Repository
public class MarketDataRepository implements MarketDataDao {

    private final String hashReference = "MarketData";

    @Autowired
    private RedisTemplate hashOperations;

    @Override
    public void saveData(MarketDataCache data) {
        hashOperations.opsForHash().putIfAbsent(hashReference, data.getTICKER(), data);
    }

    @Override
    public void saveAll(Map<String, MarketDataCache> marketDataList) {
        hashOperations.opsForHash().putAll(hashReference, marketDataList);
    }

    @Override
    public void updateMarketData(MarketDataCache data) {
        hashOperations.opsForHash().put(hashReference, data.getTICKER(), data);
    }

    @Override
    public void deleteMarketData(String ticker) {
        hashOperations.opsForHash().delete(hashReference, ticker);
    }

    @Override
    public MarketDataCache getMarketData(String ticker) {
        Object data =  hashOperations.opsForHash().get(hashReference, ticker);
        assert data != null;
        return new Gson().fromJson(data.toString(), MarketDataCache.class);
    }

    @Override
    public Map getAllMarketData() {
        return hashOperations.opsForHash().entries(hashReference);
    }
}
