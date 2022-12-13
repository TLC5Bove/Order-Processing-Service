package bove.order.processing.service.orderbook.model;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "tsla")
public class Tsla extends Stock{
}
