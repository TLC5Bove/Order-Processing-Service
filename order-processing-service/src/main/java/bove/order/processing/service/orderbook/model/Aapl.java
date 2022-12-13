package bove.order.processing.service.orderbook.model;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "aapl")
public class Aapl extends Stock{
}