package bove.order.processing.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class OrderProcessingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderProcessingServiceApplication.class, args);
	}

}
