package bove.order.processing.service;

import bove.order.processing.service.receiver.entity.MarketData;
import bove.order.processing.service.receiver.repository.MarketDataRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Component
public class MarketDataRunner implements CommandLineRunner {
    @Value("${order.EXCHANGE_URL}")
    private String exchangeURL;
    @Value("${order.EXCHANGE2_URL}")
    private String exchange2URL;
    @Autowired
//    private MarketDataDao marketDataDao;
    private MarketDataRepository marketDataDao;

    @Override
    public void run(String... args) throws Exception {

        // data from exchange1
        WebClient webClient = WebClient.create(exchangeURL);
        try {
            Object[] response = webClient.get()
                    .uri("/pd")
                    .retrieve()
                    .bodyToMono(Object[].class)
                    .block();

            assert response != null;
            List<Object> data = Arrays.stream(response).toList();
            data.forEach(res -> saveMarketDataToCache(res, "exchange 1"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // from exchange 2
        WebClient webClient2 = WebClient.create(exchange2URL);
        try {
            Object[] response = webClient2.get()
                    .uri("/pd")
                    .retrieve()
                    .bodyToMono(Object[].class)
                    .block();

            assert response != null;
            List<Object> data = Arrays.stream(response).toList();
            data.forEach(res -> saveMarketDataToCache(res, "exchange 2"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        marketDataDao.getAllMarketData().forEach((k, v) -> System.out.println(k +": "+v));
    }

    private void saveMarketDataToCache(Object object, String exchange) {
        MarketData marketData = new Gson().fromJson(object.toString(), MarketData.class);
        String ticker = marketData.getTICKER()+"_"+exchange;
        marketData.setTICKER(ticker);
    }


}
