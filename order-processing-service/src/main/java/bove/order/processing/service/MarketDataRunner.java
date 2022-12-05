package bove.order.processing.service;

import bove.order.processing.service.receiver.entity.MarketData;
import bove.order.processing.service.receiver.repository.MarketDataDao;
import bove.order.processing.service.receiver.repository.MarketDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MarketDataRunner implements CommandLineRunner {
    @Autowired
//    private MarketDataDao marketDataDao;
    private MarketDataRepository marketDataDao;

    @Override
    public void run(String... args) throws Exception {
        marketDataDao.saveAll(Map.of(
                "IBM_exchange 1", new MarketData("IBM_exchange 1", 5000, 10000, 0.00, 1.25, 1.3, 10.0),
                "IBM_exchange 2", new MarketData("IBM_exchange 2", 5000, 10000, 0.00, 1.25, 1.3, 10.0),
                "GOOGL_exchange 1", new MarketData("GOOGL_exchange 1", 5000, 10000, 0.00, 1.25, 0.0, 10.0),
                "GOOGL_exchange 2", new MarketData("GOOGL_exchange 2", 5000, 10000, 0.00, 1.25, 0.0, 10.0)
    ));

        Map<String, MarketData> data = marketDataDao.getAllMarketData();

        data.forEach((k, v) -> System.out.println(k +": "+v));

        System.out.println(marketDataDao.getMarketData("GOOGL_exchange 1"));
    }


}
