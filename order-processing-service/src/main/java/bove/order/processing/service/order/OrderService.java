package bove.order.processing.service.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
   @Value("${order.API_KEY}")
   private String exchangeAPIkey;

   @Value("${order.API_URL}")
   private String exchangeURL;


   public String placeOrder (Order order){
      WebClient webClient = WebClient.create(exchangeURL);
      try{
         String response = webClient.post()
                 .uri("/" + exchangeAPIkey + "/order")
                 .body(Mono.just(order), Order.class)
                 .retrieve()
                 .bodyToMono(String.class)
                 .block();
         System.out.println(response);
         return response;

      }catch (Exception e){
         // needs refactoring
        return "Error"+e.getMessage();
      }
   };
}
