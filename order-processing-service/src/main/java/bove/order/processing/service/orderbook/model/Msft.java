package bove.order.processing.service.orderbook.model;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "msft")
public class Msft extends Stock {
}